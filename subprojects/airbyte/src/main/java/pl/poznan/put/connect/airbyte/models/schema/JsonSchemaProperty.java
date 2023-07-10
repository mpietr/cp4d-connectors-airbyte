package pl.poznan.put.connect.airbyte.models.schema;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class JsonSchemaProperty{
    String description;
    Object type;
    String format;
    @SerializedName("airbyte_type")
    String airbyteType;
}
