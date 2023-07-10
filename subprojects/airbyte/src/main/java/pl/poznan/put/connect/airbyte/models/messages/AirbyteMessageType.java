package pl.poznan.put.connect.airbyte.models.messages;

/**
 * Class defined in: https://docs.airbyte.com/understanding-airbyte/airbyte-protocol/#airbytemessage
 */
public enum AirbyteMessageType {
    RECORD,
    STATE,
    LOG,
    SPEC,
    CONNECTION_STATUS,
    CATALOG,
    TRACE

}
