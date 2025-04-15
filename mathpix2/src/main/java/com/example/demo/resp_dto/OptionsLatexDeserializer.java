package com.example.demo.resp_dto;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class OptionsLatexDeserializer extends JsonDeserializer<List<String>> {
    @Override
    public List<String> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = p.getCodec().readTree(p);

        // Case 1: If it's a text node (String), check for "NA" or return as a single-item list
        if (node.isTextual()) {
            String textValue = node.asText();
            return ("NA".equalsIgnoreCase(textValue) || "N/A".equalsIgnoreCase(textValue))
                    ? Collections.emptyList()  // Convert "NA" to an empty list
                    : Collections.singletonList(textValue);  // Otherwise, return as a single-item list
        }

        // Case 2: If it's an array, extract values normally
        if (node.isArray()) {
            return StreamSupport.stream(node.spliterator(), false)
                    .map(JsonNode::asText)
                    .collect(Collectors.toList());
        }

        // Default case: return an empty list if format is unexpected
        return Collections.emptyList();
    }
}

