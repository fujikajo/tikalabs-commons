package de.tikalabs.commons.database.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RowMapperResultSetExtractor<T> implements ResultSetExtractor<List<T>> {
	
	private final RowMapper<T> rowMapper;
	private final int rowsExpected;
	
	public RowMapperResultSetExtractor(RowMapper<T> rowMapper) {
		this(rowMapper,0);
	}
	
	public RowMapperResultSetExtractor(RowMapper<T> rowMapper, int rowsExpected) {
		this.rowMapper = rowMapper;
		this.rowsExpected = rowsExpected;

	}
	
	public List<T> extractData(ResultSet rs) throws SQLException {
		List<T> results = (this.rowsExpected > 0 ? new ArrayList<T>(this.rowsExpected) : new ArrayList<T>());
		int rowNum = 0;
		while (rs.next()) {
			results.add(this.rowMapper.mapRow(rs, rowNum++));
			
		}
		return results;
	}

}
