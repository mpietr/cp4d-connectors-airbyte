/* *************************************************** */

/* (C) Copyright IBM Corp. 2022                        */

/* *************************************************** */
package pl.poznan.put.connect.airbyte;

import com.ibm.connect.sdk.api.ConnectorFactory;
import com.ibm.wdp.connect.common.sdk.api.models.ConnectionProperties;
import com.ibm.wdp.connect.common.sdk.api.models.CustomFlightDatasourceTypes;

import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings({ "PMD.AvoidDollarSigns", "PMD.ClassNamingConventions" })
public class AirbyteConnectorFactory implements ConnectorFactory
{
    private static final Map<String, String> typesImageMap = Map.of(
            "airbyte-postgres", "airbyte/source-postgres",
            "airbyte-mongodb","airbyte/source-mongodb",
            "airbyte-file", "airbyte/source-file"
    );
    private static final AirbyteConnectorFactory INSTANCE = new AirbyteConnectorFactory();

    /**
     * A connector factory instance.
     *
     * @return a connector factory instance
     */
    public static AirbyteConnectorFactory getInstance()
    {
        return INSTANCE;
    }

    /**
     * Creates a connector for the given data source type.
     *
     * @param datasourceTypeName
     *            the name of the data source type
     * @param properties
     *            connection properties
     * @return a connector for the given data source type
     */
    @Override
    public AirbyteConnector createConnector(String datasourceTypeName, ConnectionProperties properties)
    {
        if ("airbyte".equals(datasourceTypeName)) {
            return new AirbyteConnector(properties);
        } else if (typesImageMap.containsKey(datasourceTypeName)) {
            return new AirbyteConnector(properties, typesImageMap.get(datasourceTypeName));
        }
        throw new UnsupportedOperationException(datasourceTypeName + " is not supported!");
    }

    @Override
    public CustomFlightDatasourceTypes getDatasourceTypes()
    {
        return new CustomFlightDatasourceTypes().datasourceTypes(
                typesImageMap.entrySet().stream()
                        .map(typesImageEntry -> new AirbyteDatasourceType(typesImageEntry.getKey(), typesImageEntry.getValue()))
                        .collect(Collectors.toList()));
    }
}
