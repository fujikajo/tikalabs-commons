package com.tikalabs.commons.json.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonSchemaProcessor {

    /**
     * JSON key string for record length, alignment
     */

    public static final String FIELD_NAME = "name";
    public static final String FIELD_TYPE = "type";
    public static final String FIELD_LENGTH = "length";
    public static final String FIELD_ALIGNMENT = "alignment";
    public static final String FIELD_PADDING_CHARACTER = "padding";
    private static final Logger logger = LoggerFactory.getLogger(JsonSchemaProcessor.class);

    public static void processSchema(String jsonSchema) {

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode;
        try {
            rootNode = objectMapper.readTree(jsonSchema);

            JsonNode fieldsNode = rootNode.path("fields");
            if (fieldsNode.isArray()) {
                for (JsonNode fieldNode : fieldsNode) {
                    String name = fieldNode.path("name").asText();
                    String type = fieldNode.path("type").asText();
                    int length = fieldNode.path("length").asInt();
                    String alignment = fieldNode.path("alignment").asText();
                    System.out.println(
                            "Name: " + name + ", Type: " + type + ", Length: " + length + ", Alignment: " + alignment);
                    // Führen Sie hier die gewünschte Verarbeitung durch
                }
            }

        } catch (JsonMappingException e) {
            logger.error("JSON mapping error while processing the schema", e);
        } catch (JsonProcessingException e) {
            logger.error("JSON processing error while reading the schema", e);
        }

    }

}
