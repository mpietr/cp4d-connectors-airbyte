package pl.poznan.put.connect.airbyte.models.messages.spec;

import com.google.gson.annotations.SerializedName;

/**
 * Class defined in: https://docs.airbyte.com/understanding-airbyte/airbyte-protocol/#configuredairbytestream
 */
public enum AirbyteDestinationSyncMode {
    @SerializedName("append")
    APPEND,
    @SerializedName("overwrite")
    OVERWRITE,
    @SerializedName("append_dedup")
    APPEND_DEDUP
}
