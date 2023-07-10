/* *************************************************** */

/* (C) Copyright IBM Corp. 2022                        */

/* *************************************************** */
package pl.poznan.put.connect.airbyte;

import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import org.apache.arrow.flight.Ticket;

import com.ibm.connect.sdk.api.RowBasedConnector;
import com.ibm.wdp.connect.common.sdk.api.models.*;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

@SuppressWarnings({ "PMD.AvoidDollarSigns", "PMD.ClassNamingConventions" })
public class AirbyteConnector
        extends RowBasedConnector<AirbyteSourceInteraction, AirbyteTargetInteraction>
{
    private static final Logger LOGGER = getLogger(AirbyteConnector.class);
    private final String imageName;
    private final HashMap<String, Object> connectionProperties;
    private final Gson gson = new Gson();

    /**
     * Creates a row-based connector.
     *
     * @param properties
     *            connection properties
     */
    public AirbyteConnector(ConnectionProperties properties)
    {
        super(properties);
        connectionProperties = properties;
        imageName = null;
    }

    public AirbyteConnector(ConnectionProperties properties, String imageName) {
        super(properties);
        connectionProperties = properties;
        this.imageName = imageName;
    }

    @Override
    public void close()
    {
        // Do nothing
    }

    @Override
    public void connect()
    {
        // Do nothing
    }

    @Override
    public List<CustomFlightAssetDescriptor> discoverAssets(CustomFlightAssetsCriteria criteria)
    {
        String configuration = gson.toJson(connectionProperties);
        List<CustomFlightAssetDescriptor> discoveryResults = AirbyteProcessRunner.discover(imageName, configuration);
        String path = criteria.getPath();
        return AirbyteAssetParser.parseAssets(path, discoveryResults);
    }

    @Override
    public AirbyteSourceInteraction getSourceInteraction(CustomFlightAssetDescriptor asset, Ticket ticket)
    {
        if (ticket == null) {
            String configuration = gson.toJson(connectionProperties);
            return new AirbyteSourceInteraction(this, asset, AirbyteProcessRunner.discover(imageName, configuration));
        } else {
            String configuration = gson.toJson(connectionProperties);
            String catalog = asset.getDetails().get("configuredCatalog").toString();
            Process podProcess = AirbyteProcessRunner.read(imageName, configuration, catalog);
            if (podProcess == null) {
                throw new NullPointerException("Cannot setup pod");
            }
            return new AirbyteSourceInteraction(this, asset, podProcess);
        }
    }

    @Override
    public AirbyteTargetInteraction getTargetInteraction(CustomFlightAssetDescriptor asset)
    {
        return new AirbyteTargetInteraction(this, asset);
    }

    @Override
    public ConnectionActionResponse performAction(String action, ConnectionActionConfiguration config) throws Exception {
        switch (action) {
            case AirbyteCustomActions.SPEC_ACTION:
                return AirbyteProcessRunner.spec(imageName);
            case AirbyteCustomActions.CHECK_ACTION:
                String configuration = gson.toJson(connectionProperties);
                return AirbyteProcessRunner.check(imageName, configuration);
            default:
                return null;
        }
    }

    @Override
    public void commit()
    {
        // Do nothing
    }

}
