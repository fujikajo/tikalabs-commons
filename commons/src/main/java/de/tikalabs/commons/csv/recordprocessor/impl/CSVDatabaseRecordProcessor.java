package de.tikalabs.commons.csv.recordprocessor.impl;

import de.tikalabs.commons.csv.recordprocessor.DatabaseRecordProcessor;
import de.tikalabs.commons.database.servicelayer.IServiceLayer;

import java.util.Map;


public class CSVDatabaseRecordProcessor extends DatabaseRecordProcessor<Map<String, String>> {


    public CSVDatabaseRecordProcessor(String tableName, IServiceLayer<Map<String, String>> embeddedDBService) {
        super(tableName, embeddedDBService);

    }

    @Override
    protected void saveToEmbeddedDB(Map<String, String> record) {
        final boolean b = this.getService().insert(this.getTableName(), record);
        if (b) System.out.println("Datensatz in die Datenbank geschrieben: " + record.get("VERS_NR"));
    }

}
