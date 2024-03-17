package de.tikalabs.commons.database.servicelayer;

import de.tikalabs.commons.database.embedded.EmbeddedDB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;


public class EmbeddedDBService implements IServiceLayer<Map<String, String>> {

    private static final Logger logger = LoggerFactory.getLogger(EmbeddedDBService.class);

    private EmbeddedDB embeddedDB;
    private String tableName;

    public EmbeddedDBService(String dbName, String tblName) {
        this.setEmbeddedDB(new EmbeddedDB(dbName));
        this.setTableName(tblName);

    }

    public EmbeddedDB getEmbeddedDB() {
        return embeddedDB;
    }

    public void setEmbeddedDB(EmbeddedDB embeddedDB) {
        this.embeddedDB = embeddedDB;
    }

    @Override
    public boolean addRow(Map<String, String> row) {

        return getEmbeddedDB().insertRecord(this.getTableName(), row);

    }

    public void createTable(List<String> columnNames) {
        getEmbeddedDB().createTable(this.tableName, columnNames);
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

}