package pl.poznan.put.connect.airbyte.models.schema;

import com.google.gson.annotations.SerializedName;

public enum JsonSchemaTypes {
    @SerializedName("integer")
    INTEGER,
    @SerializedName("string")
    STRING,
    @SerializedName("number")
    NUMBER,
    @SerializedName("boolean") 
    BOOLEAN,
    @SerializedName("array")
    ARRAY,
    @SerializedName("object")
    OBJECT
}
