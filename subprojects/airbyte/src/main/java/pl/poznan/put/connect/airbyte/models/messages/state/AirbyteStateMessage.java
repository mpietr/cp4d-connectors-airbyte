package pl.poznan.put.connect.airbyte.models.messages.state;

import com.google.gson.JsonObject;
import lombok.Data;

/**
 * Class defined in: https://docs.airbyte.com/understanding-airbyte/airbyte-protocol#airbytestatemessage-v1
 */
@Data
public class AirbyteStateMessage {
    JsonObject data;
}
