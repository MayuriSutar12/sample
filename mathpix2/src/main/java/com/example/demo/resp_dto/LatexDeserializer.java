package com.example.demo.resp_dto;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;

public class LatexDeserializer extends JsonDeserializer<String> {

        @Override
        public String deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {
            String value = jsonParser.getText();
            System.out.println("Debug Deserializer - passageLatex: " + value);
            return value;
        }
    }


