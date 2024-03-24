package com.tikalabs.commons.fixedwidthdata;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FixedWidthFileReader {

    private DataMapper mapper;


    public FixedWidthFileReader(String jsonSchema) throws IOException {
        this.mapper = new DataMapper(jsonSchema);
    }

    public List<Map<String, Object>> readDataFromFile(String filePath) throws IOException {
        List<Map<String, Object>> records = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Map<String, Object> record = mapper.mapLineToData(line);
                records.add(record);
            }
        }
        return records;
    }

    public List<String> getHeadersOrder() {
        return this.mapper.getFieldNames();
    }
}
