package ru.yandex.practicum.filmorate.formatter;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.Duration;

public class DurationDeserializeFormatter extends JsonDeserializer<Duration> {

    @Override
    public Duration deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        return Duration.ofSeconds(jsonParser.getLongValue());
    }
}
