package com.tikalabs.commons.database.jdbc;

import oracle.ucp.jdbc.PoolDataSource;
import oracle.ucp.jdbc.PoolDataSourceFactory;
import org.h2.jdbcx.JdbcDataSource;
import org.mariadb.jdbc.MariaDbDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;

public class DataSourceFactory {

    // Erstellen Sie ein Logger-Objekt für die Klasse MyClass
    private static final Logger logger = LoggerFactory.getLogger(DataSourceFactory.class);

    public static DataSource createDataSource() {

        MariaDbDataSource dataSource = null;

        try {
            dataSource = new MariaDbDataSource();
            dataSource.setUser("root");
            dataSource.setPassword("");
            dataSource.setServerName("localhost");
            dataSource.setPortNumber(3306);
            dataSource.setDatabaseName("portfolio");
        } catch (SQLException e) {
            logger.error("An error occurred: " + e.getMessage(), e);
        }

        return dataSource;

    }

    public static DataSource createDataSource(Properties properties) throws SQLException {

        String dbTypeString = properties.getProperty("dbType").toUpperCase();
        DatabaseType dbType = DatabaseType.valueOf(dbTypeString);
        return createDataSource(properties, dbType);

    }

    public static DataSource createDataSource(Properties properties, DatabaseType dbType) throws SQLException {

        switch (dbType) {
            case MARIADB:
                MariaDbDataSource dataSource = null;
                dataSource = new MariaDbDataSource();
                dataSource.setUser(properties.getProperty("db-user"));
                dataSource.setPassword(properties.getProperty("db-password"));
                dataSource.setServerName(properties.getProperty("db-server"));
                dataSource.setPortNumber(Integer.parseInt(properties.getProperty("db-port")));
                dataSource.setDatabaseName(properties.getProperty("db-name"));
                return dataSource;
            case ORACLE:
                PoolDataSource oracleDs = PoolDataSourceFactory.getPoolDataSource();
                oracleDs.setUser(properties.getProperty("user"));
                oracleDs.setPassword(properties.getProperty("password"));
                oracleDs.setURL("jdbc:oracle:thin:@" + properties.getProperty("db-server") + ":"
                        + properties.getProperty("db-port") + ":" + properties.getProperty("db-name"));
                return oracleDs;
            case H2:
                JdbcDataSource ds = new JdbcDataSource();
                ds.setURL("jdbc:h2:~/" + properties.getProperty("db-name")); // Pfad zur Datenbank
                ds.setUser("user");
                ds.setPassword("password");
                return ds;
            default:
                logger.error("Nicht unterstützter Datenbanktyp: " + dbType);
                throw new IllegalArgumentException("Nicht unterstützter Datenbanktyp: " + dbType);

        }

    }

    public static DataSource createDataSource(DatabaseType dbType) {

        switch (dbType) {
            case H2:
                JdbcDataSource ds = new JdbcDataSource();
                ds.setURL("jdbc:h2:~/test"); // Pfad zur Datenbank
                ds.setUser("user");
                ds.setPassword("password");
            default:
                logger.error("Nicht unterstützter Datenbanktyp: " + dbType);
                throw new IllegalArgumentException("Nicht unterstützter Datenbanktyp: " + dbType);
        }
    }

    public enum DatabaseType {
        MARIADB, ORACLE, H2
        // Definiere hier weitere Datenbanktypen
    }

}
