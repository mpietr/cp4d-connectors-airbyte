package pl.poznan.put.connect.airbyte.models.messages.catalog;

import lombok.Data;

import java.util.List;

/**
 * Class defined in: https://docs.airbyte.com/understanding-airbyte/airbyte-protocol/#overview
 */
@Data
public class AirbyteCatalogMessage {
    List<AirbyteStream> streams;
}
