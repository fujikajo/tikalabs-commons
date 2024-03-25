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

package de.mv.mathdev.commons.csv;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CSVWriter {

	private static final Logger logger = LoggerFactory.getLogger(CSVWriter.class);
	private List<String> headers;

	public CSVWriter(String jsonSchema) throws IOException {
		this.headers = parseHeadersFromSchema(jsonSchema);
	}

	public CSVWriter() {

	}

	private List<String> parseHeadersFromSchema(String jsonSchema) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode rootNode = objectMapper.readTree(jsonSchema);
		JsonNode fieldsNode = rootNode.path("fields");
		List<String> headers = new ArrayList<>();
		fieldsNode.forEach(field -> headers.add(field.path("name").asText()));
		return headers;
	}

//	public void writeDataToCsv(List<Map<String, String>> records, List<String> headers, String csvFilePath)
//			throws IOException {
//		this.headers = headers;
//		try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFilePath))) {
//			writer.write(String.join(";", headers));
//			writer.newLine();
//
//			for (Map<String, String> record : records) {
//				for (String header : headers) {
//					Object value = record.getOrDefault(header, "");
//					writer.write(value.toString());
//					writer.write(";");
//				}
//				writer.newLine();
//			}
//		}
//	}

	public void writeDataToCsv(List<Map<String, String>> records, List<String> headers, String csvFilePath) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFilePath))) {
			// Schreibe die Headerzeile in die CSV-Datei
			writer.write(String.join(";", headers));
			writer.newLine();

			// Schreibe die Datenzeilen in die CSV-Datei
			for (Map<String, String> record : records) {
				String dataLine = headers.stream().map(header -> record.getOrDefault(header, ""))
						.collect(Collectors.joining(";"));
				writer.write(dataLine);
				writer.newLine();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

	public void writeDataToCsvWithDynamicHeaders(List<Map<String, String>> records, String csvFilePath)
			throws IOException {
		// Überprüfe, ob die Liste leer ist; wenn ja, beende die Methode frühzeitig
		if (records.isEmpty()) {
			System.out.println("Keine Daten zum Schreiben in die CSV-Datei.");
			return;
		}

		// Extrahiere die Headernamen aus dem ersten Datensatz
		Set<String> headers = records.get(0).keySet();

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFilePath))) {
			// Schreibe die Headerzeile in die CSV-Datei
			String headerLine = String.join(";", headers);
			writer.write(headerLine);
			writer.newLine();

			// Schreibe die Datenzeilen in die CSV-Datei
			for (Map<String, String> record : records) {
				String dataLine = headers.stream().map(header -> record.getOrDefault(header, ""))
						.collect(Collectors.joining(";"));
				writer.write(dataLine);
				writer.newLine();
			}
		}
	}
}
