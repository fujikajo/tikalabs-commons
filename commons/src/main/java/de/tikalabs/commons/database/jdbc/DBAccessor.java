package de.tikalabs.commons.database.jdbc;


import de.tikalabs.commons.database.mapper.ResultSetExtractor;
import de.tikalabs.commons.database.mapper.RowMapper;
import de.tikalabs.commons.database.mapper.RowMapperResultSetExtractor;
import de.tikalabs.commons.database.mapper.SingleColumnRowMapper;
import de.tikalabs.commons.database.utils.DBUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public class DBAccessor implements DBOperations {

    // Erstellen Sie ein Logger-Objekt für die Klasse MyClass
    private static final Logger logger = LoggerFactory.getLogger(DBAccessor.class);

    private DataSource dataSource = null;
    private Connection con = null;
    private String message = null;

    // private final int nullType;

    public DBAccessor() {
        this(DataSourceFactory.createDataSource());
    }

    public DBAccessor(DataSource datasource) {
        this.setDataSource(datasource);

    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.setConnection(dataSource);
    }

    @Override
    public ResultSet query(String sql) throws SQLException {

        Statement stmt = null;
        ResultSet rs = null;

        stmt = this.createStatement();

        try {
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw e;
        }

        return rs;
    }

    public Connection getConnection() {
        if (con == null) {
            this.setConnection(this.getDataSource());
        }

        return con;
    }

    public void setConnection(DataSource ds) {
        try {
            this.con = ds.getConnection();

        } catch (SQLException e) {
            logger.error("Could not connect to database.");

        }
    }

    public String getURL() {

        if (con != null) {
            try {
                return con.getMetaData().getURL();

            } catch (SQLException e) {

                logger.error(e.getMessage());
            }
        }
        return "Disconnected.";

    }

    public String getDatabaseProductName() {

        if (con != null) {
            try {
                return con.getMetaData().getDriverName() + " " + con.getMetaData().getDriverVersion();

            } catch (SQLException e) {

                logger.error(e.getMessage());
            }
        }
        return "Disconnected.";

    }

    public boolean hasConnection() {
        return (this.con != null);
    }

    @Override
    public <T> List<T> query(String sql, RowMapper<T> rowMapper) {
        return query(sql, new RowMapperResultSetExtractor<T>(rowMapper));
    }

    public <T> T query(final String sql, final ResultSetExtractor<T> rse) {

        ResultSet rs = null;
        Statement stmt = this.createStatement();
        try {
            rs = stmt.executeQuery(sql);
            return rse.extractData(rs);
        } catch (SQLException e) {
            logger.error(e.getMessage());
        } finally {

            DBUtils.closeResultSet(rs);

        }
        return null;

    }

    ;

    public int queryForInt(String sql) {

        Number number = queryForObject(sql, Integer.class);
        return (number != null ? number.intValue() : 0);

    }

    public String queryForString(String sql) {
        String string = queryForObject(sql, String.class);
        return string;
    }

    public <T> T queryForObject(String sql, Class<T> requiredType) {
        return queryForObject(sql, getSingleColumnRowMapper(requiredType));

    }

    public <T> T queryForObject(String sql, RowMapper<T> rowMapper) {

        List<T> results = query(sql, rowMapper);
        return requiredSingleResult(results);

    }

    public Statement createStatement() {
        try {
            return this.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    private PreparedStatement createPreparedStatement(String sql, Object[] params) {
        PreparedStatement statement;
        try {
            statement = this.getConnection().prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                if (params[i] == null) {
                    statement.setNull(i + 1, Types.NULL);
                } else if (params[i] instanceof Integer) {
                    statement.setInt(i + 1, (Integer) params[i]);
                } else if (params[i] instanceof Boolean) {
                    statement.setBoolean(i + 1, (Boolean) params[i]);
                    // Hinzufügen der Behandlung von LocalDate
                } else if (params[i] instanceof LocalDate) {
                    LocalDate localDate = (LocalDate) params[i];
                    statement.setDate(i + 1, Date.valueOf(localDate));
                } else if (params[i] instanceof Double) {
                    statement.setDouble(i + 1, (Double) params[i]);
                } else if (params[i] instanceof String) {
                    statement.setString(i + 1, (String) params[i]);
                    // Sie können weitere Typen hier nach Bedarf behandeln
                } else {
                    // Für den Fall, dass ein unerwarteter Typ übergeben wird
                    throw new SQLException("Unhandhabbarer Parametertyp: " + params[i].getClass().getSimpleName());
                }
            }
            return statement;
        } catch (SQLException e) {
            // Logging und Fehlerbehandlung
            logger.error("Error creating PreparedStatement: ", e);
        }
        return null;
    }

//	public void update(String sql, Object... params) {
//		PreparedStatement statement = null;
//		statement = createPreparedStatement(sql, params);
//		try {
//			statement.executeUpdate();
//		} catch (SQLException e) {
//
//			logger.error(e.getMessage());
//		} finally {
//			DBUtils.closeStatement(statement);
//		}
//	}

    public boolean update(String sql, Object... params) {
        PreparedStatement statement = null;
        try {
            statement = createPreparedStatement(sql, params);
            int affectedRows = statement.executeUpdate();
            return affectedRows > 0; // true, wenn Zeilen betroffen sind, also Update erfolgreich
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                return false; // Update fehlgeschlagen aufgrund von einem Duplikat
            } else {
                // Update fehlgeschlagen aufgrund eines anderen Fehlers
                logger.error(e.getMessage());
                return false;
            }
        } finally {
            DBUtils.closeStatement(statement);
        }

    }

    public boolean insert(String sql, Object... params) {
        PreparedStatement statement = null;
        try {
            statement = createPreparedStatement(sql, params);
            if (statement == null) {
                logger.error("Failed to insert the record: PreparedStatement cannot be null." );
                return false;
            }
            // executeUpdate() gibt die Anzahl der geänderten Zeilen zurück
            int affectedRows = statement.executeUpdate();
            // Wenn affectedRows größer als 0 ist, war die Einfügeoperation erfolgreich
            return affectedRows > 0;
        } catch (Exception e) {
            logger.error("Failed to insert the record: " + e.getMessage());
            return false;
        } finally {
            DBUtils.closeStatement(statement);
        }
    }

    /**
     * Legt eine neue Datenbanktabelle an.
     *
     * @param tableName   Tabellenname
     * @param columnNames Spaltennamen
     */
    public void createTable(final String tableName, final List<String> columnNames) {

        StringBuilder query = new StringBuilder().append("CREATE TABLE ").append(tableName).append(" (");
        String prefix = "";
        for (String columnName : columnNames) {
            query.append(prefix);
            query.append(columnName).append(" varchar(255)");
            prefix = ",";
        }
        query.append(")");

        update(query.toString());
    }

    protected <T> RowMapper<T> getSingleColumnRowMapper(Class<T> requiredType) {
        return new SingleColumnRowMapper<T>(requiredType);

    }

    public <T> T requiredSingleResult(Collection<T> results) {

        int size = (results != null ? results.size() : 0);
        if (size == 0) {

        }

        if (results.size() > 1) {

        }

        return results.iterator().next();

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
