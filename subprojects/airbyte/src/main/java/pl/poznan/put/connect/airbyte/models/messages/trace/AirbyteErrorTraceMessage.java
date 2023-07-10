package pl.poznan.put.connect.airbyte.models.messages.trace;


import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * Class defined in: https://docs.airbyte.com/understanding-airbyte/airbyte-protocol/#airbytetracemessage
 */
@Data
public class AirbyteErrorTraceMessage {
    String message;
    @SerializedName("internal_message")
    String internalMessage;
    @SerializedName("stack_trace")
    String stackTrace;
    @SerializedName("failure_type")
    AirbyteFailureType failureType;
}
