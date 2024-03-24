package com.tikalabs.commons.csv.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CSVWriter {

    private static final Logger logger = LoggerFactory.getLogger(CSVWriter.class);
    private List<String> headers;

    public CSVWriter(String jsonSchema) throws IOException {
        this.headers = parseHeadersFromSchema(jsonSchema);
    }

    private List<String> parseHeadersFromSchema(String jsonSchema) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(jsonSchema);
        JsonNode fieldsNode = rootNode.path("fields");
        List<String> headers = new ArrayList<>();
        fieldsNode.forEach(field -> headers.add(field.path("name").asText()));
        return headers;
    }

    public void writeDataToCsv(List<Map<String, Object>> records, List<String> headers,String csvFilePath) throws IOException {
        this.headers = headers;
        this.writeDataToCsv(records,csvFilePath);
    }

    public void writeDataToCsv(List<Map<String, Object>> records, String csvFilePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFilePath))) {
            writer.write(String.join(";", headers));
            writer.newLine();

            for (Map<String, Object> record : records) {
                for (String header : headers) {
                    Object value = record.getOrDefault(header, "");
                    writer.write(value.toString());
                    writer.write(";");
                }
                writer.newLine();
            }
        }
    }
}
