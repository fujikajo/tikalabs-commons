package com.tikalabs.commons.csv.handler;


import com.tikalabs.commons.csv.recordprocessor.impl.CSVDatabaseRecordProcessor;
import com.tikalabs.commons.csv.utils.CustomCSVParser;
import com.tikalabs.commons.database.servicelayer.EmbeddedDBService;

public class CSVDatabaseHandler {

	private final EmbeddedDBService service;
	private CSVDatabaseRecordProcessor recordProcessor;
	private CustomCSVParser csvParser;
	private String tableName;
	// Eine Map, um die Reihenfolge der Header basierend auf dem Tabellennamen zu
	// speichern
	private Map<String, List<String>> headerOrderMap = new HashMap<>();

	public CSVDatabaseHandler(EmbeddedDBService service) {
		this.service = service;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public void execute() {
		this.service.createTable(getTableName(), getCsvParser().getHeaderNames());
		this.getCsvParser().parseCSV();

	}

	public void load(String tableName, String csvFilePath, int headerIndex) {

		// Setze den Tabellennamen und den Dateipfad für den aktuellen Ladevorgang
		this.setTableName(tableName);
		this.setRecordProcessor(new CSVDatabaseRecordProcessor(tableName, this.service));
		this.setCsvParser(new CustomCSVParser(csvFilePath, this.recordProcessor, headerIndex));

		// Speichere die Reihenfolge der Header für die aktuelle Tabelle
		this.headerOrderMap.put(tableName, this.csvParser.getHeaderNames());

		// Erstelle die Tabelle in der Datenbank und lade die CSV-Daten
		this.service.createTable(getTableName(), getCsvParser().getHeaderNames());
		this.service.createIndex(getTableName(), "idx_vers_nr_" + tableName);
		this.getCsvParser().parseCSV();
	}

	public List<String> getHeaderOrder(String tableName) {
		return headerOrderMap.get(tableName);
	}

	public CSVDatabaseRecordProcessor getRecordProcessor() {
		return recordProcessor;
	}

	public void setRecordProcessor(CSVDatabaseRecordProcessor recordProcessor) {
		this.recordProcessor = recordProcessor;
	}

	public CustomCSVParser getCsvParser() {
		return csvParser;
	}

	public void setCsvParser(CustomCSVParser csvParser) {
		this.csvParser = csvParser;
	}

}
