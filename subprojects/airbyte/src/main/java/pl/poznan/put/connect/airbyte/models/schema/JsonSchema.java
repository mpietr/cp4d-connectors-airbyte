package pl.poznan.put.connect.airbyte.models.schema;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class JsonSchema {
    String $schema;
    String title;
    String description;
    String type;
    Map<String, JsonSchemaProperty> properties;
    List<String> required;
}
