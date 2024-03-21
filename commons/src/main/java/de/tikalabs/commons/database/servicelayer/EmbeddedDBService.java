package de.tikalabs.commons.database.servicelayer;

import de.tikalabs.commons.database.jdbc.DBAccessor;
import de.tikalabs.commons.database.jdbc.DataSourceFactory;
import de.tikalabs.commons.database.mapper.RowMapper;
import de.tikalabs.commons.database.querybuilder.QueryCondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;


public class EmbeddedDBService implements IServiceLayer<Map<String, String>> {

    private static final Logger logger = LoggerFactory.getLogger(EmbeddedDBService.class);
    private EmbeddedDBDAO embeddedDBDAO;

    public EmbeddedDBService(String dbName) {
        Properties properties = new Properties();
        properties.setProperty("db-name", dbName);
        properties.setProperty("dbType", "H2");
        try {
            DBAccessor accessor = new DBAccessor(DataSourceFactory.createDataSource(properties, DataSourceFactory.DatabaseType.H2));
            this.setEmbeddedDBDAO(new EmbeddedDBDAO(accessor));
        } catch (SQLException e) {

            logger.error("EmbeddedDB konnte nicht aufgebaut werden.");
        }

    }

    public EmbeddedDBDAO getEmbeddedDBDAO() {
        return embeddedDBDAO;
    }

    public void setEmbeddedDBDAO(EmbeddedDBDAO embeddedDBDAO) {
        this.embeddedDBDAO = embeddedDBDAO;
    }

    public void createTable(String tableName, List<String> columnNames) {
        getEmbeddedDBDAO().createTable(tableName, columnNames);
    }

    public <T> List<T> queryTable(String tableName, List<QueryCondition> conditions, RowMapper<T> mapper) {
        // Implementierung der Methode, die eine Liste von Datensätzen basierend auf den Kriterien zurückgibt
        return getEmbeddedDBDAO().executeDynamicQuery(tableName, conditions, mapper);
    }


    @Override
    public boolean addRow(Map<String, String> row) {
        return false;
    }

    @Override
    public boolean insert(String tableName, Map<String, String> row) {
        return getEmbeddedDBDAO().insert(tableName, row);
    }

    public int count(String tableName) {
        return this.getEmbeddedDBDAO().countRecords(tableName);
    }
}