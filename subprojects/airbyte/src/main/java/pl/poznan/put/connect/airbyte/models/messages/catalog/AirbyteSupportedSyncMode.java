package pl.poznan.put.connect.airbyte.models.messages.catalog;

import com.google.gson.annotations.SerializedName;

/**
 * Class defined in: https://docs.airbyte.com/understanding-airbyte/airbyte-protocol/#configuredairbytestream
 */
public enum AirbyteSupportedSyncMode {
    @SerializedName("full_refresh")
    FULL_REFRESH,
    @SerializedName("incremental")
    INCREMENTAL
}
