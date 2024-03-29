package com.tikalabs.commons.csv.recordprocessor;

import com.tikalabs.commons.database.servicelayer.IServiceLayer;

import java.util.List;


public abstract class DatabaseRecordProcessor<T> implements RecordProcessor<T> {

    private IServiceLayer<T> service;
    private String tableName;
    private List<String> columnNames;

    public DatabaseRecordProcessor(String tableName, IServiceLayer<T> service) {
        this.setService(service);
        this.setTableName(tableName);
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public void process(T record) {
        // Rufe die abstrakte Methode auf, die in der Unterklasse implementiert werden
        // muss
        saveToEmbeddedDB(record);
    }

    // Abstrakte Methode, die jede Unterklasse implementieren muss
    protected abstract void saveToEmbeddedDB(T record);

    public IServiceLayer<T> getService() {
        return service;
    }

    public void setService(IServiceLayer<T> service) {
        this.service = service;
    }

    public List<String> getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(List<String> columnNames) {
        this.columnNames = columnNames;
    }
}
