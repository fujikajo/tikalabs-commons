package com.tikalabs.commons.csv.handler;


import com.tikalabs.commons.csv.recordprocessor.impl.CSVDatabaseRecordProcessor;
import com.tikalabs.commons.csv.utils.CustomCSVParser;
import com.tikalabs.commons.database.servicelayer.EmbeddedDBService;

public class CSVDatabaseHandler {

    private final EmbeddedDBService service;
    private CSVDatabaseRecordProcessor recordProcessor;
    private CustomCSVParser csvParser;
    private String tableName;

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
        this.setCsvParser(new CustomCSVParser(csvFilePath, this.recordProcessor, headerIndex)); // Angenommen, der headerIndex ist immer 8, anpassen falls nötig

        // Erstelle die Tabelle in der Datenbank und lade die CSV-Daten
        this.service.createTable(getTableName(), getCsvParser().getHeaderNames());
        this.getCsvParser().parseCSV();
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
