package de.tikalabs.commons.fixedwidthdata;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class FixedWidthDataWriter {

    private JsonNode fieldsNode;

    public FixedWidthDataWriter(String jsonSchema) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(jsonSchema);
        this.fieldsNode = rootNode.path("fields");
    }

    public void writeDataToFile(List<Map<String, Object>> records, String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Map<String, Object> record : records) {
                StringBuilder line = new StringBuilder();
                for (JsonNode fieldNode : fieldsNode) {
                    String name = fieldNode.path("name").asText();
                    int length = fieldNode.path("length").asInt();
                    String padding = fieldNode.path("padding").asText(" ");
                    String alignment = fieldNode.path("alignment").asText("left");
                    String defaultValue = fieldNode.path("defaultValue").asText(" ");
                    String value = record.getOrDefault(name, "").toString();
                    String paddedValue;
                    // Anonymisierung für das Feld "Identifikationsnummer"
                    if ("Identifikationsnummer".equals(name)) {
                        String anonymValue = anonymizeValue(value, value.length());
                        paddedValue = padValue(anonymValue, length, padding, alignment, defaultValue);
                    } else {
                        // Padding-Anpassung für normale Felder
                        paddedValue = padValue(value, length, padding, alignment, defaultValue);
                    }

                    // String paddedValue = padValue(value, length, padding, alignment,
                    // defaultValue);
                    line.append(paddedValue);
                }
                writer.write(line.toString());
                writer.newLine();
            }
        }
    }

    private String padValue(String value, int length, String padding, String alignment, String defaultValue) {
        // Prüfen, ob der Wert null oder leer ist und dementsprechend das Feld mit
        // defaultValue auffüllen
        if (value == null || value.isEmpty()) {
            StringBuilder defaultValuePad = new StringBuilder(length);
            while (defaultValuePad.length() < length) {
                defaultValuePad.append(defaultValue);
            }
            // Kürzen Sie das defaultValuePad, falls es länger als die vorgesehene Länge ist
            return defaultValuePad.substring(0, length);
        }

        int valueLength = value.length();
        if (valueLength > length) {
            return value.substring(0, length);
        }

        int padLength = length - valueLength;
        StringBuilder pad = new StringBuilder(padLength);
        for (int i = 0; i < padLength; i++) {
            pad.append(padding);
        }

        switch (alignment) {
            case "right":
                return pad + value;
            case "centre":
            case "center":
                int halfPadLength = pad.length() / 2;
                return pad.substring(0, halfPadLength) + value + pad.substring(halfPadLength);
            default:
                return value + pad.toString();
        }
    }

    private String anonymizeValue(String value, int length) {
        // Ersetzen Sie den Wert durch Sternchen (*) oder eine andere Logik zur
        // Anonymisierung
        return "X".repeat(Math.max(0, length));
    }

//	public static void main(String[] args) {
//		String jsonSchema = "{ \"fields\": [ /* Ihr JSON Schema */ ] }";
//		String filePath = "path/to/your/output/fixedwidthfile.txt";
//
//		// Hier Ihre Liste von Datensätzen definieren
//		List<Map<String, Object>> records = List.of(/* Ihre Daten */);
//
//		try {
//			FixedWidthDataWriter writer = new FixedWidthDataWriter(jsonSchema);
//			writer.writeDataToFile(records, filePath);
//			System.out.println("Datei wurde erfolgreich geschrieben.");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
}
