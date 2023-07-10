package pl.poznan.put.connect.airbyte.models.messages.log;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * Class defined in: https://docs.airbyte.com/understanding-airbyte/airbyte-protocol/#airbytelogmessage
 */
@Data
public class AirbyteLogMessage {
    AirbyteLogType level;
    String message;
    @SerializedName("stack_trace")
    String stackTrace;
}
