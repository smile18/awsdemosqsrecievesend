package org.rogs.model.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class RogsOffsetDateTimeDeserializer extends JsonDeserializer<String> {
    @Override
    public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        if(jsonParser.getText() != null && !jsonParser.getText().isEmpty())
            OffsetDateTime.parse(jsonParser.getText(),DateTimeFormatter.ISO_DATE_TIME);
        return jsonParser.getText();
    }
}
