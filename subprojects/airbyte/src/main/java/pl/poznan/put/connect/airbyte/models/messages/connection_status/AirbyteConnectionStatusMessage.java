package pl.poznan.put.connect.airbyte.models.messages.connection_status;

import lombok.Data;

/**
 * Class defined in: https://docs.airbyte.com/understanding-airbyte/airbyte-protocol#airbyteconnectionstatus-message
 */
@Data
public class AirbyteConnectionStatusMessage {
    AirbyteConnectionStatusType status;
    String message;
}
