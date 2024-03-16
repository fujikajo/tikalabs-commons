package de.tikalabs.commons.database.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class DBUtils {
	

	public static Object getResultSetValue(ResultSet rs, int index, Class requiredType) throws SQLException {

		Object value = null;
		if (int.class.equals(requiredType) || Integer.class.equals(requiredType)) {
			value = rs.getInt(index);

		}
		
		if (String.class.equals(requiredType)) {
			value = rs.getString(index);
		}
		
		return value;

	}
	
	public static void closeResultSet(ResultSet rs) {
		try {
			rs.close();
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}
	
	public static void closeStatement(Statement statement) {
		
		if (statement == null) {
			return;
		}
		try {
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}



}
