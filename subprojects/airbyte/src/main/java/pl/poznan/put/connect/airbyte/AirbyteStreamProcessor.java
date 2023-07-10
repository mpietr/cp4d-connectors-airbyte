package pl.poznan.put.connect.airbyte;

import com.google.gson.Gson;
import com.ibm.connect.sdk.api.Record;
import com.ibm.wdp.connect.common.sdk.api.models.CustomFlightAssetDescriptor;
import com.ibm.wdp.connect.common.sdk.api.models.CustomFlightAssetField;
import com.ibm.wdp.connect.common.sdk.api.models.DiscoveredAssetDetails;
import com.ibm.wdp.connect.common.sdk.api.models.DiscoveredAssetType;
import org.slf4j.Logger;
import pl.poznan.put.connect.airbyte.models.messages.AirbyteMessage;
import pl.poznan.put.connect.airbyte.models.messages.catalog.AirbyteStream;
import pl.poznan.put.connect.airbyte.models.messages.catalog.ConfiguredAirbyteStream;
import pl.poznan.put.connect.airbyte.models.messages.catalog.ConfiguredAirbyteStreams;
import pl.poznan.put.connect.airbyte.models.messages.connection_status.AirbyteConnectionStatusType;
import pl.poznan.put.connect.airbyte.models.messages.trace.AirbyteTraceType;
import pl.poznan.put.connect.airbyte.models.schema.JsonSchemaProperty;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class AirbyteStreamProcessor {
    private static final Logger LOGGER = getLogger(AirbyteStreamProcessor.class);

    private static final Gson gson = new Gson();

    public static HashMap<String,Object> processCustomAction(BufferedReader bufferedReader) throws IOException {
        HashMap<String,Object> response = new HashMap<>();
        for (String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
            LOGGER.debug("Reading line: {}", line);
            AirbyteMessage airbyteMessage = gson.fromJson(line, AirbyteMessage.class);
            switch (airbyteMessage.getType()) {
                case SPEC:
                    response.put("connectionSpecification", airbyteMessage.getSpec().getConnectionSpecification());
                    break;
                case TRACE:
                    if (AirbyteTraceType.ERROR.equals(airbyteMessage.getTrace().getType())) {
                        response.put("error", airbyteMessage.getTrace().getError().getMessage());
                    }
                    break;
                case CONNECTION_STATUS:
                    response.put("status", airbyteMessage.getConnectionStatus().getStatus());
                    if (AirbyteConnectionStatusType.FAILED.equals(airbyteMessage.getConnectionStatus().getStatus())) {
                        response.put("error", airbyteMessage.getConnectionStatus().getMessage());
                    }
                    break;
                default:
                    break;
            }
        }
        return response;
    }

    public static List<CustomFlightAssetDescriptor> processDiscoverAction(BufferedReader bufferedReader) throws IOException {
        List<CustomFlightAssetDescriptor> assets  = new ArrayList<>();
        for (String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
            LOGGER.debug("Reading line: {}", line);
            AirbyteMessage airbyteMessage = gson.fromJson(line, AirbyteMessage.class);
            switch (airbyteMessage.getType()) {
                case CATALOG:
                    for(AirbyteStream stream: airbyteMessage.getCatalog().getStreams()) {
                        CustomFlightAssetDescriptor asset = new CustomFlightAssetDescriptor();
                        if(stream.getNamespace() == null){
                            asset.setId(stream.getName());
                        } else{
                            asset.setId(stream.getNamespace() + "." + stream.getName());
                        }
                        asset.setName(stream.getName());
                        DiscoveredAssetType type = new DiscoveredAssetType();
                        type.setType(stream.getJsonSchema().getType());
                        asset.setAssetType(type);
                        List<CustomFlightAssetField> fields = new ArrayList<>();
                        for (String property: stream.getJsonSchema().getProperties().keySet()) {
                            JsonSchemaProperty schema = stream.getJsonSchema().getProperties().get(property);
                            CustomFlightAssetField field = new CustomFlightAssetField();
                            field.setName(property);
                            field.setType(parseTypeName(getType(schema.getType())));
                            fields.add(field);
                        }
                        asset.setFields(fields);
                        DiscoveredAssetDetails details = new DiscoveredAssetDetails();
                        details.put("namespace", stream.getNamespace());
                        details.put("primaryKeys", stream.getSourceDefinedPrimaryKey());
                        ConfiguredAirbyteStreams configuredAirbyteStreams = new ConfiguredAirbyteStreams();
                        ConfiguredAirbyteStream configuredAirbyteStream = new ConfiguredAirbyteStream();
                        configuredAirbyteStream.setStream(stream);
                        configuredAirbyteStreams.setStreams(List.of(configuredAirbyteStream));
                        details.put("configuredCatalog", gson.toJson(configuredAirbyteStreams));
                        asset.setDetails(details);
                        assets.add(asset);
                    }
                    break;
                case TRACE:
                    if (AirbyteTraceType.ERROR.equals(airbyteMessage.getTrace().getType())) {
                        return List.of();
                    }
                    break;
                case CONNECTION_STATUS:
                    if (AirbyteConnectionStatusType.FAILED.equals(airbyteMessage.getConnectionStatus().getStatus())) {
                        return List.of();
                    }
                    break;
                default:
                    break;
            }
        }
        return assets;
    }

    public static Record processReadAction(BufferedReader bufferedReader, List<CustomFlightAssetField> fields) throws IOException {
        for (String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
            LOGGER.debug("Reading line: {}", line);
            AirbyteMessage airbyteMessage = gson.fromJson(line, AirbyteMessage.class);
            switch (airbyteMessage.getType()) {
                case RECORD:
                    Record record = new Record();
                    for(CustomFlightAssetField field : fields){
                        record.appendValue(airbyteMessage.getRecord().getData().get(field.getName()).toString());
                    }
                    return record;
                case TRACE:
                    if (AirbyteTraceType.ERROR.equals(airbyteMessage.getTrace().getType())) {
                        return null;
                    }
                    break;
                case CONNECTION_STATUS:
                    if (AirbyteConnectionStatusType.FAILED.equals(airbyteMessage.getConnectionStatus().getStatus())) {
                        return null;
                    }
                default:
                    break;
            }
        }
        return null;
    }
    public static String parseTypeName(String type){
        switch(type.toUpperCase()){
            case "INTEGER":
                return "INT";
            case "STRING":
                return "VARCHAR";
            default:
                return type.toUpperCase();
        }
    }

    public static String getType(Object type){
        if(type instanceof String){
            return ((String) type).toLowerCase();
        }
        else if(type instanceof List){
            List<String> l = (List) type;
            return l.get(0).toLowerCase();
        }
        else if(type instanceof String[]){
            String[] l = (String[]) type;
            return l[0].toLowerCase();
        }
        else{
            throw new IllegalArgumentException("Illegal type type");
        }
    }

}
