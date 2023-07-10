/* *************************************************** */

/* (C) Copyright IBM Corp. 2022                        */

/* *************************************************** */
package pl.poznan.put.connect.airbyte;

import com.ibm.connect.sdk.api.Record;
import com.ibm.connect.sdk.api.RowBasedSourceInteraction;
import com.ibm.wdp.connect.common.sdk.api.models.CustomFlightAssetDescriptor;
import com.ibm.wdp.connect.common.sdk.api.models.CustomFlightAssetField;
import com.ibm.wdp.connect.common.sdk.api.models.DiscoveredAssetDetails;
import org.apache.arrow.flight.Ticket;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.SequenceInputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.slf4j.LoggerFactory.getLogger;

@SuppressWarnings({ "PMD.AvoidDollarSigns", "PMD.ClassNamingConventions" })
public class AirbyteSourceInteraction extends RowBasedSourceInteraction<AirbyteConnector>
{
    BufferedReader reader = null;
    Process podProcess = null;
    List<CustomFlightAssetDescriptor> descriptors = List.of();

    protected AirbyteSourceInteraction(AirbyteConnector connector, CustomFlightAssetDescriptor asset, List<CustomFlightAssetDescriptor> descriptors) {
        super();
        setConnector(connector);
        setAsset(asset);
        this.descriptors = descriptors;
    }

    protected AirbyteSourceInteraction(AirbyteConnector connector, CustomFlightAssetDescriptor asset, Process podProcess)
    {
        super();
        setConnector(connector);
        setAsset(asset);
        this.reader = new BufferedReader(new InputStreamReader(new SequenceInputStream(podProcess.getInputStream(), podProcess.getErrorStream()), StandardCharsets.UTF_8));
        this.podProcess = podProcess;
    }

    @Override
    public Record getRecord()
    {
        try {
            return AirbyteStreamProcessor.processReadAction(reader, getAsset().getFields());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Ticket> getTickets() {
        Ticket ticket = new Ticket(String.format("{\"request_id\": \"%s\"}", UUID.randomUUID()).getBytes());
        return List.of(ticket);
    }

    @Override
    public List<CustomFlightAssetField> getFields()
    {
        if (getAsset().getFields() == null) {
            String streamName = (String) getAsset().getInteractionProperties().get("stream_name");
            String namespace = (String) getAsset().getInteractionProperties().get("namespace");
            Optional<CustomFlightAssetDescriptor> descriptor = descriptors.stream()
                    .filter(assetDescriptor -> Objects.equals(assetDescriptor.getName(), streamName) &&
                            Objects.equals(assetDescriptor.getDetails().get("namespace"), namespace))
                    .findFirst();

            if (descriptor.isEmpty()) {
                throw new IllegalArgumentException("Cannot find a given parameter");
            }
            getAsset().setDetails(new DiscoveredAssetDetails());
            getAsset().getDetails().put("configuredCatalog", descriptor.get().getDetails().get("configuredCatalog"));
            return descriptor.get().getFields();
        }
        return getAsset().getFields();
    }

    @Override
    public void close() throws Exception {
        super.close();
        if(podProcess != null){
            podProcess.destroy();
            reader.close();
        }
    }
}
