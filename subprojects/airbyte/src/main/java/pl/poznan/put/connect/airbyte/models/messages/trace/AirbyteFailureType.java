package pl.poznan.put.connect.airbyte.models.messages.trace;

import com.google.gson.annotations.SerializedName;

/**
 * Class defined in: https://docs.airbyte.com/understanding-airbyte/airbyte-protocol/#airbytetracemessage
 */
public enum AirbyteFailureType {
    @SerializedName("system_error")
    SYSTEM_ERROR,
    @SerializedName("config_error")
    CONFIG_ERROR
}
