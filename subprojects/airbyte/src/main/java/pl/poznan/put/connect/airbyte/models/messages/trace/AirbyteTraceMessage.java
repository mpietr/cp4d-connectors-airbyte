package pl.poznan.put.connect.airbyte.models.messages.trace;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * Class defined in: https://docs.airbyte.com/understanding-airbyte/airbyte-protocol/#airbytetracemessage
 */
@Data
public class AirbyteTraceMessage {
    AirbyteTraceType type;
    @SerializedName("emitted_at")
    Number emittedAt;
    AirbyteErrorTraceMessage error;
    AirbyteEstimateTraceMessage estimate;
}
