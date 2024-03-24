package com.tikalabs.commons.database.jdbc;


import com.tikalabs.commons.database.mapper.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface DBOperations {

    public ResultSet query(String sql) throws SQLException;

    <T> List<T> query(String sql, RowMapper<T> rowMapper);

}
