package pl.poznan.put.connect.airbyte.models.messages.record;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.math.BigInteger;

/**
 * Class defined in: https://docs.airbyte.com/understanding-airbyte/airbyte-protocol#airbyterecordmessage
 */
@Data
public class AirbyteRecordMessage {
    String namespace;
    String stream;
    JsonObject data;
    @SerializedName("emitted_at")
    BigInteger emittedAt;
}
