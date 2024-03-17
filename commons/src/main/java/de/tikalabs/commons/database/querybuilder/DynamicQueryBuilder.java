package de.tikalabs.commons.database.querybuilder;

import de.tikalabs.commons.database.jdbc.DBAccessor;
import de.tikalabs.commons.database.mapper.RowMapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DynamicQueryBuilder<T> {

    private final DBAccessor dbAccessor;

    public DynamicQueryBuilder(DBAccessor dbAccessor) {
        this.dbAccessor = dbAccessor;
    }

    public <T> List<T> executeDynamicQuery(String tableName, List<QueryCondition> conditions, RowMapper<T> rowMapper) throws SQLException {

        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM " + tableName + " WHERE ");
        List<Object> parameters = new ArrayList<>();

        for (int i = 0; i < conditions.size(); i++) {
            QueryCondition condition = conditions.get(i);
            queryBuilder.append(condition.getColumn()).append(" ").append(condition.getOperator()).append(" ?");
            parameters.add(condition.getValue());

            if (i < conditions.size() - 1) {
                queryBuilder.append(" AND ");
            }
        }


        try (PreparedStatement statement = dbAccessor.createPreparedStatement(queryBuilder.toString(), parameters.toArray())) {
            try (ResultSet resultSet = statement.executeQuery()) {
                List<T> results = new ArrayList<>();
                int rowNum = 0;
                while (resultSet.next()) {
                    results.add(rowMapper.mapRow(resultSet, rowNum++));
                }
                return results;
            }
        }
    }
}

