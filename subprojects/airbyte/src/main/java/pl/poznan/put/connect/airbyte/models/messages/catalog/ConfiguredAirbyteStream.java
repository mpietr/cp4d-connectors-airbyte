package pl.poznan.put.connect.airbyte.models.messages.catalog;

import pl.poznan.put.connect.airbyte.models.messages.spec.AirbyteDestinationSyncMode;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

/**
 * Class defined- in: https://docs.airbyte.com/understanding-airbyte/airbyte-protocol#configuredairbytestream
 */
@Data
public class ConfiguredAirbyteStream {
    AirbyteStream stream;
    @SerializedName("sync_mode")
    AirbyteSupportedSyncMode syncMode = AirbyteSupportedSyncMode.FULL_REFRESH;
    @SerializedName("cursor_field")
    List<String> cursorField;
    @SerializedName("destination_sync_mode")
    AirbyteDestinationSyncMode destinationSyncMode = AirbyteDestinationSyncMode.OVERWRITE;
    @SerializedName("primary_key")
    List<List<String>> primaryKey;
}
