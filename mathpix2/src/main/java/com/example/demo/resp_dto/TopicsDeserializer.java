package com.example.demo.resp_dto;

import com.fasterxml.jackson.core.JsonParser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class TopicsDeserializer extends JsonDeserializer<List<String>> {
//    @Override
//    public List<String> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
//        if (p.isExpectedStartArrayToken()) {
//            // Deserialize JSON array as List<String>
//            return p.readValueAs(new TypeReference<List<String>>() {});
//        } else if (p.getCurrentToken().isScalarValue()) {
//            // Convert single string into a List with one element
//            return Collections.singletonList(p.getValueAsString());
//        } else {
//            // Return an empty list if JSON is null or unexpected
//            return Collections.emptyList();
//        }
//    }
//}
//public class TopicsDeserializer extends JsonDeserializer<List<String>> {
    @Override
    public List<String> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        if (p.isExpectedStartArrayToken()) {
            // If JSON contains an array, parse it as a List
            return p.readValueAs(List.class);
        } else {
            // If JSON contains a single String, convert it into a List with one element
            return Collections.singletonList(p.getValueAsString());
        }
    }
}
