package pl.poznan.put.connect.airbyte.models.messages.spec;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import pl.poznan.put.connect.airbyte.models.schema.JsonSchema;

import java.util.List;

/**
 * Class defined in: https://docs.airbyte.com/understanding-airbyte/airbyte-protocol/#actor-specification
 */
@Data
public class AirbyteSpecMessage {
    @SerializedName("protocol_version")
    String protocolVersion;
    String documentationUrl;
    String changelogUrl;
    JsonSchema connectionSpecification;
    Boolean supportsIncremental;
    Boolean supportsNormalization = false;
    Boolean supportsDBT = false;
    @SerializedName("supported_destination_sync_modes")
    List<AirbyteDestinationSyncMode> supportedDestinationSyncModes;
}
