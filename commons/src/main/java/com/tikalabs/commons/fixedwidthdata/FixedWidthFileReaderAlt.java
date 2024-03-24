package com.tikalabs.commons.fixedwidthdata;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FixedWidthFileReaderAlt {

    public static void processSchema(String jsonSchema) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(jsonSchema);
        JsonNode fieldsNode = rootNode.path("fields");

        List<Map<String, Object>> records = new ArrayList<>();
        DataMapper mapper = new DataMapper(jsonSchema);

        try (BufferedReader reader = new BufferedReader(
                new FileReader("C:\\Projekte\\MathLV\\workspace-lv\\bestandsmonitoring\\2023-Rente-MV.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Map<String, Object> record = new HashMap<>();
                int currentPosition = 0;

                record = mapper.mapLineToData(line);

//				for (JsonNode fieldNode : fieldsNode) {
//					String name = fieldNode.path("name").asText();
//					String type = fieldNode.path("type").asText();
//					int length = fieldNode.path("length").asInt();
//					String valueString = line.substring(currentPosition, currentPosition + length).trim();
//
//					// Umwandlung basierend auf Typ
//					Object value;
//					switch (type) {
//					case "Integer":
//						value = Integer.parseInt(valueString);
//						break;
//					case "String":
//					default:
//						value = valueString;
//						break;
//					}
//
//					record.put(name, value);
//					currentPosition += length;
//				}
                records.add(record);
            }
        }

    }

}
