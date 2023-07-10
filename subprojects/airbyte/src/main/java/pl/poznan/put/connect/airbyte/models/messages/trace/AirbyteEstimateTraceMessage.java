package pl.poznan.put.connect.airbyte.models.messages.trace;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * Class defined in: https://docs.airbyte.com/understanding-airbyte/airbyte-protocol/#airbytetracemessage
 */
@Data
public class AirbyteEstimateTraceMessage {
    String name;
    AirbyteEstimateType type;
    String namespace;
    @SerializedName("row_estimate")
    Integer rowEstimate;
    @SerializedName("byte_estimate")
    Integer byteEstimate;
}
