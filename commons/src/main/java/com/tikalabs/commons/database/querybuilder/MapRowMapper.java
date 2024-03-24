package com.tikalabs.commons.database.querybuilder;

import com.tikalabs.commons.database.mapper.RowMapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class MapRowMapper implements RowMapper<Map<String, String>> {

    @Override
    public Map<String, String> mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Map<String, String> result = new HashMap<>();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        for (int i = 1; i <= columnCount; i++) {
            String columnName = metaData.getColumnName(i);
            String columnValue = resultSet.getString(i);
            result.put(columnName, columnValue);
        }

        return result;
    }
}
