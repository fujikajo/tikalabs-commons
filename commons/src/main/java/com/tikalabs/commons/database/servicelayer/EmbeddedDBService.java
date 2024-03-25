package com.tikalabs.commons.database.servicelayer;

import com.tikalabs.commons.database.jdbc.DBAccessor;
import com.tikalabs.commons.database.jdbc.DataSourceFactory;
import com.tikalabs.commons.database.mapper.RowMapper;
import com.tikalabs.commons.database.querybuilder.QueryCondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;


public class EmbeddedDBService implements IServiceLayer<Map<String, String>> {

    private static final Logger logger = LoggerFactory.getLogger(EmbeddedDBService.class);
    private EmbeddedDBDAO embeddedDBDAO;

    /**
     * Konstruktor für den EmbeddedDBService.
     *
     * @param dbName Der Name der zu erstellenden oder zu verbindenden Datenbank.
     */

    public EmbeddedDBService(String dbName) {
        Properties properties = new Properties();
        properties.setProperty("db-name", dbName);
        properties.setProperty("dbType", "H2");
        try {
            DBAccessor accessor = new DBAccessor(
                    DataSourceFactory.createDataSource(properties, DataSourceFactory.DatabaseType.H2));
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

    /**
     * Erstellt eine neue Tabelle in der Embedded-Datenbank.
     *
     * @param tableName   Der Name der zu erstellenden Tabelle.
     * @param columnNames Eine Liste der Spaltennamen für die Tabelle.
     */

    public void createTable(String tableName, List<String> columnNames) {
        getEmbeddedDBDAO().createTable(tableName, columnNames);
    }

    /**
     * Führt eine Abfrage auf einer Tabelle aus und gibt die Ergebnisse zurück.
     *
     * @param tableName  Der Name der Tabelle, auf der die Abfrage ausgeführt wird.
     * @param conditions Eine Liste von Bedingungen für die Abfrage.
     * @param mapper     Ein RowMapper, der ResultSet-Zeilen in Objekte umwandelt.
     * @return Eine Liste von Objekten des Typs T, die den Ergebnissen der Abfrage
     * entsprechen.
     */

    public <T> List<T> queryTable(String tableName, List<QueryCondition> conditions, RowMapper<T> mapper) {
        // Implementierung der Methode, die eine Liste von Datensätzen basierend auf den
        // Kriterien zurückgibt
        return getEmbeddedDBDAO().executeDynamicQuery(tableName, conditions, mapper);
    }

    /**
     * Holt Daten aus einer Tabelle und gibt sie als Liste von Maps zurück.
     *
     * @param tableName  Der Name der Tabelle, aus der Daten abgefragt werden
     *                   sollen.
     * @param conditions Eine Liste von Bedingungen für die Abfrage.
     * @return Eine Liste von Maps, wobei jede Map einen Datensatz repräsentiert.
     */

    public List<Map<String, String>> fetchDataAsMap(String tableName, List<QueryCondition> conditions) {
        return getEmbeddedDBDAO().fetchDataAsMap(tableName, conditions);

    }

    @Override
    public boolean addRow(Map<String, String> row) {
        return false;
    }

    /**
     * Fügt eine neue Zeile in eine spezifizierte Tabelle ein.
     *
     * @param tableName Der Name der Tabelle, in die eingefügt wird.
     * @param row       Die Daten der neuen Zeile als Map von Spaltennamen zu
     *                  Werten.
     * @return true, wenn das Einfügen erfolgreich war, sonst false.
     */

    @Override
    public boolean insert(String tableName, Map<String, String> row) {
        return getEmbeddedDBDAO().insert(tableName, row);
    }

    /**
     * Zählt die Anzahl der Datensätze in einer spezifischen Tabelle.
     *
     * @param tableName Der Name der Tabelle, deren Datensätze gezählt werden
     *                  sollen.
     * @return Die Anzahl der Datensätze in der angegebenen Tabelle.
     */

    public int count(String tableName) {
        return this.getEmbeddedDBDAO().countRecords(tableName);
    }

    public boolean createIndex(String tableName, String indexName) {
		return this.getEmbeddedDBDAO().createIndex(tableName, indexName);
	}
}
