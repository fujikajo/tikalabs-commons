package com.tikalabs.commons.database.servicelayer;

import com.tikalabs.commons.database.jdbc.DBAccessor;
import com.tikalabs.commons.database.mapper.RowMapper;
import com.tikalabs.commons.database.querybuilder.QueryCondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EmbeddedDBDAO {

    private static final Logger logger = LoggerFactory.getLogger(EmbeddedDBDAO.class);

    private final DBAccessor accessor;

    public EmbeddedDBDAO(DBAccessor accessor) {
        this.accessor = accessor;
    }

    public DBAccessor getAccessor() {
        return accessor;
    }

    /**
     * Legt eine neue Datenbanktabelle an.
     *
     * @param tableName   Tabellenname
     * @param columnNames Spaltennamen
     */
    public void createTable(final String tableName, final List<String> columnNames) {

        this.dropTableIfExists(tableName);
        StringBuilder query = new StringBuilder().append("CREATE TABLE ").append(tableName).append(" (");
        String prefix = "";
        for (String columnName : columnNames) {
            query.append(prefix);
            query.append(columnName).append(" varchar(255)");
            prefix = ",";
        }
        query.append(")");

        this.accessor.update(query.toString());
    }

    /**
     * Löscht eine Tabelle aus der Datenbank, falls sie existiert.
     *
     * @param tableName Der Name der Tabelle, die gelöscht werden soll.
     */
    public void dropTableIfExists(String tableName) {
        String sql = "DROP TABLE IF EXISTS " + tableName;
        try {
            this.accessor.update(sql);
            System.out.println("Tabelle " + tableName + " wurde erfolgreich gelöscht, falls sie existierte.");
        } catch (Exception e) {
            // Hier solltest du geeignete Fehlerbehandlung einfügen
            System.err.println("Fehler beim Löschen der Tabelle " + tableName + ": " + e.getMessage());
        }
    }

    public boolean insert(String tableName, Map<String, String> columnDataMap) {

        // Generiere den Teil des SQL-Statements für die Spaltennamen
        String columns = String.join(", ", columnDataMap.keySet());
        // createTable(tableName, columnDataMap.keySet().toArray());

        // Generiere den Teil des SQL-Statements für die Platzhalter
        String placeholders = columnDataMap.keySet().stream().map(key -> "?").collect(Collectors.joining(", "));

        // Füge alles zu einem vollständigen SQL-Insert-Statement zusammen
        String sql = "INSERT INTO " + tableName + " (" + columns + ") VALUES (" + placeholders + ")";

        // Extrahiere die Werte in der Reihenfolge der Spaltennamen
        Object[] values = columnDataMap.values().toArray();

        // Führe das Insert mit dynamischen Parametern aus
        return this.accessor.insert(sql, values);

    }

    public List<Map<String, String>> fetchDataAsMap(String tableName, List<QueryCondition> conditions) {
        return this.accessor.fetchDataAsMap(tableName, conditions);
    }

    public <T> List<T> executeDynamicQuery(String tableName, List<QueryCondition> conditions, RowMapper<T> mapper) {
        return this.accessor.executeDynamicQuery(tableName, conditions, mapper);
    }

    /**
     * Ermittelt die Anzahl der Datensätze in einer spezifischen Tabelle.
     *
     * @param tableName Der Name der Tabelle, für die die Anzahl der Datensätze
     *                  ermittelt werden soll.
     * @return Die Anzahl der Datensätze in der angegebenen Tabelle.
     * @throws SQLException Falls ein Datenbankzugriffsfehler auftritt.
     */
    public int countRecords(String tableName) {
        String query = "SELECT COUNT(*) AS count FROM " + tableName;
        // Nutze den DBAccessor, um die Abfrage auszuführen und das Ergebnis zu erhalten
        return accessor.queryForInt(query);
    }
}
