/* *************************************************** */

/* (C) Copyright IBM Corp. 2022                        */

/* *************************************************** */
package pl.poznan.put.connect.airbyte;

import com.ibm.wdp.connect.common.sdk.api.models.*;
import com.ibm.wdp.connect.common.sdk.api.models.CustomDatasourceTypeProperty.TypeEnum;
import pl.poznan.put.connect.airbyte.models.schema.JsonSchema;
import pl.poznan.put.connect.airbyte.models.schema.JsonSchemaTypes;

import java.util.Collections;
import java.util.stream.Collectors;

@SuppressWarnings({ "PMD.AvoidDollarSigns", "PMD.ClassNamingConventions" })
public class AirbyteDatasourceType extends CustomFlightDatasourceType
{

    /**
     * The unique identifier name of the data source type.
     */
    public static final String DATASOURCE_TYPE_NAME_FORMAT = "%s";
    private static final String DATASOURCE_TYPE_LABEL_FORMAT = "%s datasource";
    private static final String DATASOURCE_TYPE_DESCRIPTION_FORMAT = "%s (SDK)";

    public AirbyteDatasourceType(String datasourceName, String imageName)
    {
        super();

        // Set the data source type attributes.
        setName(String.format(DATASOURCE_TYPE_NAME_FORMAT, datasourceName));
        setLabel(String.format(DATASOURCE_TYPE_LABEL_FORMAT, datasourceName));
        setDescription(String.format(DATASOURCE_TYPE_DESCRIPTION_FORMAT, datasourceName));
        setAllowedAsSource(true);
        setAllowedAsTarget(false);
        setStatus(CustomFlightDatasourceType.StatusEnum.PENDING);
        setTags(Collections.emptyList());
        final CustomFlightDatasourceTypeProperties properties = new CustomFlightDatasourceTypeProperties();
        setProperties(properties);

        ConnectionActionResponse response = AirbyteProcessRunner.spec(imageName);
        JsonSchema connectionSpecification = (JsonSchema) response.get("connectionSpecification");

        properties.setConnection(
            connectionSpecification.getProperties().entrySet().stream().map(
                    namePropertiesEntry -> new CustomDatasourceTypeProperty()
                            .name(namePropertiesEntry.getKey())
                            .type(parseTypeFromAirbyteToTypeEnum(AirbyteStreamProcessor.getType(namePropertiesEntry.getValue().getType())))
                            .label(namePropertiesEntry.getKey())
                            .description(namePropertiesEntry.getValue().getDescription())
                            .required(connectionSpecification.getRequired().contains(namePropertiesEntry.getKey()))
            ).collect(Collectors.toList())
        );

        // Define the source interaction properties.
        properties.addSourceItem(new CustomDatasourceTypeProperty()
                .name("stream_name")
                .label("Stream name")
                .description("The name of the stream (table eg.) for the airbyte.")
                .type(TypeEnum.STRING)
                .required(true));
        properties.addSourceItem(new CustomDatasourceTypeProperty()
                .name("namespace")
                .label("Stream namespace")
                .description("The namespace of the stream.")
                .type(TypeEnum.STRING)
                .required(false));

        // Define custom actions.
        CustomDatasourceTypeActionProperties actionProperties = new CustomDatasourceTypeActionProperties();

        CustomDatasourceTypeAction specAction = new CustomDatasourceTypeAction()
                .name(AirbyteCustomActions.SPEC_ACTION)
                .description("Get a detailed specification of the connection.")
                .properties(actionProperties);

        CustomDatasourceTypeProperty schemaProperty = new CustomDatasourceTypeProperty()
                .label("Airbyte schema")
                .name("connectionSpecification")
                .required(true);

        actionProperties.addOutputItem(schemaProperty);
        addActionsItem(specAction);

        CustomDatasourceTypeActionProperties checkProperties = new CustomDatasourceTypeActionProperties();

        CustomDatasourceTypeAction checkAction = new CustomDatasourceTypeAction()
                .name(AirbyteCustomActions.CHECK_ACTION)
                .description("Check a connection with the specified configuration.")
                .properties(checkProperties);

        CustomDatasourceTypeProperty checkStatusProperty = new CustomDatasourceTypeProperty()
                .label("Airbyte connection status")
                .name("status")
                .required(true);

        CustomDatasourceTypeProperty checkErrorProperty = new CustomDatasourceTypeProperty()
                .label("Airbyte connection error")
                .name("error")
                .required(false);

        checkProperties.addOutputItem(checkStatusProperty);
        checkProperties.addOutputItem(checkErrorProperty);

        addActionsItem(checkAction);

    }

    private TypeEnum parseTypeFromAirbyteToTypeEnum(String type) {
        switch (type) {
            case "number":
            case "integer":
                return TypeEnum.INTEGER;
            case "boolean":
                return TypeEnum.BOOLEAN;
            case "string":
                return TypeEnum.STRING;
            default:
                return null;
        }
    }
}
