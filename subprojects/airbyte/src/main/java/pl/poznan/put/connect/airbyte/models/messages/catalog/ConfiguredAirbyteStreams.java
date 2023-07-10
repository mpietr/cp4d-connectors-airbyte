package pl.poznan.put.connect.airbyte.models.messages.catalog;

import lombok.Data;

import java.util.List;

@Data
public class ConfiguredAirbyteStreams {
    List<ConfiguredAirbyteStream> streams;
}
