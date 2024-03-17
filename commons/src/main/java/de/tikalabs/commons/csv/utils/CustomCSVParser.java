package de.tikalabs.commons.csv.utils;

import java.util.HashMap;
import java.util.Map;

import de.tikalabs.commons.csv.recordprocessor.RecordProcessor;
import org.apache.commons.csv.CSVRecord;



public class CustomCSVParser extends AbstractCSVParser<Map<String, String>> {

	public CustomCSVParser(String filePath) {
		// ein Kommentar
		super(filePath);
	}

	public CustomCSVParser(String filePath, int headerIndex) {
		// ein Kommentar
		super(filePath, headerIndex);
	}

	public CustomCSVParser(String filePath, RecordProcessor<Map<String, String>> recordProcessor, int headerIndex) {
		// ein Kommentar
		super(filePath, recordProcessor, headerIndex);
	}

	@Override
	protected Map<String, String> parseRecord(CSVRecord csvRecord) {
		Map<String, String> columnDataMap = new HashMap<>();
		for (String header : getHeaderNames()) {
			String value = csvRecord.get(header);
			columnDataMap.put(header, value);
		}
		return columnDataMap;
	}

}
