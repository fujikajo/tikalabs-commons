package com.tikalabs.commons.fixedwidthdata;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 *  Mapper-Klasse, die eine Zeile verarbeitet und ein Daten-Map zurückgibt
 */

public class DataMapper {

    private JsonNode fieldsNode;

    public DataMapper(String jsonSchema) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(jsonSchema);
        this.fieldsNode = rootNode.path("fields");
    }

    public List<String> getFieldNames() {
        List<String> fieldNames = new ArrayList<>();
        fieldsNode.forEach(field -> fieldNames.add(field.path("name").asText()));
        return fieldNames;
    }

    public Map<String, Object> mapLineToData(String line) {
        Map<String, Object> record = new HashMap<>();
        int currentPosition = 0;
        ObjectMapper objectMapper = new ObjectMapper();
        for (JsonNode fieldNode : fieldsNode) {

            String name = fieldNode.path("name").asText();
            String type = fieldNode.path("type").asText();
            String defaultValue = fieldNode.path("defaultValue").asText();

            int length = fieldNode.path("length").asInt();
            String valueString = line.substring(currentPosition, Math.min(currentPosition + length, line.length()))
                    .trim();

            StringBuilder defaultValuePad = new StringBuilder(length);
            if ((defaultValue != null) && (!defaultValue.isEmpty())) {
                while (defaultValuePad.length() < length) {
                    defaultValuePad.append(defaultValue);
                }
            }

            // Umwandlung basierend auf Typ
            Object value;
            switch (type) {
                case "Integer":
                    value = Integer.parseInt(valueString);
                    break;
                case "String":
                default:
                    value = valueString;
                    break;
            }

            if (valueString.equals(defaultValuePad.toString())) {
                value = "";
            }

            record.put(name, value);
            currentPosition += length;
        }

        return record;
    }
}
