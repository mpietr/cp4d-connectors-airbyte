package pl.poznan.put.connect.airbyte.models.messages.log;

/**
 * Class defined in: https://docs.airbyte.com/understanding-airbyte/airbyte-protocol/#airbytelogmessage
 */
public enum AirbyteLogType {
    FATAL,
    ERROR,
    WARN,
    INFO,
    DEBUG,
    TRACE
}
