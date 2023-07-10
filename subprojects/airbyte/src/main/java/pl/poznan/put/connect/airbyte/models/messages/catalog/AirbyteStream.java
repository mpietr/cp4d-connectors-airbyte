package pl.poznan.put.connect.airbyte.models.messages.catalog;

import pl.poznan.put.connect.airbyte.models.schema.JsonSchema;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

/**
 * Class defined in: https://docs.airbyte.com/understanding-airbyte/airbyte-protocol#airbytestream
 */
@Data
public class AirbyteStream {
    @SerializedName("json_schema")
    JsonSchema jsonSchema;
    String name;
    @SerializedName("supported_sync_modes")
    List<AirbyteSupportedSyncMode> supportedSyncModes;
    @SerializedName("source_defined_cursor")
    Boolean sourceDefinedCursor;
    @SerializedName("default_cursor_field")
    List<String> defaultCursorField;
    @SerializedName("source_defined_primary_key")
    List<String> sourceDefinedPrimaryKey;
    String namespace;
}
