package ipleiria.estg.dei.ei.pi.utils;

import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.InputStream;

public class JSONValidator {

    public static void validateJSON(String contentString, InputStream inputStreamSchema) {
            JSONObject rawSchema = new JSONObject(new JSONTokener(inputStreamSchema));
            Schema schema = SchemaLoader.load(rawSchema);
            schema.validate(new JSONObject(contentString)); // throws a ValidationException if this object is invalid
    }
}
