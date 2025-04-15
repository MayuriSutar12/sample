package com.example.demo.resp_dto;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AnswerKeyDeserializer extends JsonDeserializer<String> {
    //    @Override
//    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
//        if (p.isExpectedStartArrayToken()) {
//            // Convert List to comma-separated string (e.g., ["B", "C"] → "B,C")
//            List<String> answers = p.readValueAs(List.class);
//            return answers.stream().collect(Collectors.joining(","));
//        } else {
//            // Read as a single string
//            return p.getValueAsString();
//        }
//    }
    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        if (p.isExpectedStartObjectToken()) {
            // Read JSON object as a Map (preserving order)
            Map<String, String> answers = p.readValueAs(LinkedHashMap.class);

            // Convert Map entries to "A-r, B-q, C-p, D-q"
            return answers.entrySet().stream()
                    .map(entry -> entry.getKey() + "-" + entry.getValue()) // Replace "=" with "-"
                    .collect(Collectors.joining(", ")); // Ensure proper formatting
        } else if (p.isExpectedStartArrayToken()) {
            // Handle JSON Array (e.g., ["A", "B"])
            return String.join(", ", p.readValueAs(List.class));
        } else {
            // Handle Single String (e.g., "A" or "A,B")
            return p.getValueAsString();
        }
    }
}