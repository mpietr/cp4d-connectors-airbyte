package pl.poznan.put.connect.airbyte.models.messages;

import pl.poznan.put.connect.airbyte.models.messages.catalog.AirbyteCatalogMessage;
import pl.poznan.put.connect.airbyte.models.messages.connection_status.AirbyteConnectionStatusMessage;
import pl.poznan.put.connect.airbyte.models.messages.log.AirbyteLogMessage;
import pl.poznan.put.connect.airbyte.models.messages.record.AirbyteRecordMessage;
import pl.poznan.put.connect.airbyte.models.messages.spec.AirbyteSpecMessage;
import pl.poznan.put.connect.airbyte.models.messages.state.AirbyteStateMessage;
import pl.poznan.put.connect.airbyte.models.messages.trace.AirbyteTraceMessage;
import lombok.Data;

/**
 * Class defined in: https://docs.airbyte.com/understanding-airbyte/airbyte-protocol/#airbytemessage
 */
@Data
public class AirbyteMessage {
    AirbyteMessageType type;
    AirbyteLogMessage log;
    AirbyteSpecMessage spec;
    AirbyteConnectionStatusMessage connectionStatus;
    AirbyteCatalogMessage catalog;
    AirbyteRecordMessage record;
    AirbyteStateMessage state;
    AirbyteTraceMessage trace;
}
