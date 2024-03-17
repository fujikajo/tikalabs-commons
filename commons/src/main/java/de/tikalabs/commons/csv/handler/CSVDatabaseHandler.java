package de.tikalabs.commons.csv.handler;


import de.tikalabs.commons.csv.recordprocessor.impl.CSVDatabaseRecordProcessor;
import de.tikalabs.commons.csv.utils.CustomCSVParser;
import de.tikalabs.commons.database.servicelayer.EmbeddedDBService;

public class CSVDatabaseHandler {

    private final EmbeddedDBService service;
    private CSVDatabaseRecordProcessor recordProcessor;
    private CustomCSVParser csvParser;

    public CSVDatabaseHandler(EmbeddedDBService service, String filePath, int headerIndex) {
        this.service = service;
        this.setRecordProcessor(new CSVDatabaseRecordProcessor(this.service));
        this.setCsvParser(new CustomCSVParser(filePath, this.recordProcessor, headerIndex));

    }

    public void execute() {
        this.service.createTable(getCsvParser().getHeaderNames());
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
