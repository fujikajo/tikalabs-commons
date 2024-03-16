package de.tikalabs.commons.database.embedded;

import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import de.tikalabs.commons.database.jdbc.DBAccessor;
import de.tikalabs.commons.database.jdbc.DataSourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class EmbeddedDB extends DBAccessor {

	private static final Logger logger = LoggerFactory.getLogger(EmbeddedDB.class);

	public EmbeddedDB(String dbName) {
		Properties properties = new Properties();
		properties.setProperty("db-name", dbName);
		properties.setProperty("dbType", "H2");
		try {
			this.setDataSource(DataSourceFactory.createDataSource(properties, DataSourceFactory.DatabaseType.H2));
		} catch (SQLException e) {

			logger.error("EmbeddedDB konnte nicht aufgebaut werden.");
		}

	}

	public boolean insertRecord(String tableName, Map<String, String> columnDataMap) {

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
		return this.insert(sql, values);

	}

}