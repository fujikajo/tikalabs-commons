package com.tikalabs.commons.database.querybuilder;

import com.tikalabs.commons.database.jdbc.DBAccessor;
import com.tikalabs.commons.database.mapper.RowMapper;
import org.apache.commons.lang3.tuple.Pair;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DynamicQueryBuilder<T> {

    private final DBAccessor dbAccessor;

    public DynamicQueryBuilder(DBAccessor dbAccessor) {
        this.dbAccessor = dbAccessor;
    }

    public <T> List<T> executeDynamicQuery(String tableName, List<QueryCondition> conditions, RowMapper<T> rowMapper)
            throws SQLException {

        // Nutze die neue Methode, um den Query-String und die Parameter zu bauen
        Pair<String, List<Object>> conditionsResult = buildConditions(conditions);
        String conditionString = conditionsResult.getLeft();
        List<Object> parameters = conditionsResult.getRight();

        // Baue den vollständigen Query
        String query = "SELECT * FROM " + tableName + (conditionString.isEmpty() ? "" : " WHERE " + conditionString);

        try (PreparedStatement statement = dbAccessor.createPreparedStatement(query, parameters.toArray())) {
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

    public List<Map<String, String>> fetchDataAsMap(String tableName, List<QueryCondition> conditions)
            throws SQLException {

        // Nutze die neue Methode, um den Query-String und die Parameter zu bauen
        Pair<String, List<Object>> conditionsResult = buildConditions(conditions);
        String conditionString = conditionsResult.getLeft();
        List<Object> parameters = conditionsResult.getRight();

        // Baue den vollständigen Query
        String query = "SELECT * FROM " + tableName + (conditionString.isEmpty() ? "" : " WHERE " + conditionString);

        // Bereite Statement vor und führe Query aus
        try (PreparedStatement statement = dbAccessor.createPreparedStatement(query, parameters.toArray())) {
            try (ResultSet resultSet = statement.executeQuery()) {
                List<Map<String, String>> results = new ArrayList<>();
                MapRowMapper rowMapper = new MapRowMapper();
                int rowNum = 0;
                while (resultSet.next()) {
                    results.add(rowMapper.mapRow(resultSet, rowNum++));
                }
                return results;
            }
        }
    }

    private Pair<String, List<Object>> buildConditions(List<QueryCondition> conditions) {
        StringBuilder queryBuilder = new StringBuilder();
        List<Object> parameters = new ArrayList<>();

        for (int i = 0; i < conditions.size(); i++) {
            QueryCondition condition = conditions.get(i);

            if (condition.getOperator().equals("IN") && condition.getValues() != null) {
                queryBuilder.append(condition.getColumn()).append(" IN (");
                for (int j = 0; j < condition.getValues().size(); j++) {
                    queryBuilder.append("?");
                    parameters.add(condition.getValues().get(j));
                    if (j < condition.getValues().size() - 1) {
                        queryBuilder.append(", ");
                    }
                }
                queryBuilder.append(")");
            } else {
                queryBuilder.append(condition.getColumn()).append(" ").append(condition.getOperator()).append(" ?");
                parameters.add(condition.getValue());
            }

            if (i < conditions.size() - 1) {
                queryBuilder.append(" AND ");
            }

        }

        return Pair.of(queryBuilder.toString(), parameters);
    }

}
