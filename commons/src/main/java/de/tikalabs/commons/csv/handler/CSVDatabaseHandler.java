package de.tikalabs.commons.csv.handler;


import de.tikalabs.commons.csv.recordprocessor.impl.CSVDatabaseRecordProcessor;
import de.tikalabs.commons.csv.utils.CustomCSVParser;
import de.tikalabs.commons.database.servicelayer.EmbeddedDBService;

public class CSVDatabaseHandler {

    private final EmbeddedDBService service;
    private CSVDatabaseRecordProcessor recordProcessor;
    private CustomCSVParser csvParser;
    private String tableName;

    public CSVDatabaseHandler(String tableName, EmbeddedDBService service, String filePath, int headerIndex) {
        this.service = service;
        this.setTableName(tableName);
        this.setRecordProcessor(new CSVDatabaseRecordProcessor(tableName, this.service));
        this.setCsvParser(new CustomCSVParser(filePath, this.recordProcessor, headerIndex));

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
