package com.example.demo.resp_dto;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class OptionsDeserializer extends JsonDeserializer<Map<String, String>> {
    @Override
    public Map<String, String> deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException {
        String value = p.getValueAsString();
        if (value == null || value.isEmpty()) {
            return new LinkedHashMap<>(); // Return an empty map instead of throwing an error
        }
        Map<String, String> options = p.readValueAs(new TypeReference<Map<String, String>>() {});
        // Ensure it's not null; return an empty LinkedHashMap if needed
        return (options != null) ? options : new LinkedHashMap<>();
    }
}
