package de.tikalabs.commons.database.querybuilder;

public class QueryCondition {
    private String column;
    private String operator;
    private Object value;

    public QueryCondition(String column, String operator, Object value) {
        this.column = column;
        this.operator = operator;
        this.value = value;
    }

    // Getter und Setter
    public String getColumn() {
        return column;
    }

    public String getOperator() {
        return operator;
    }

    public Object getValue() {
        return value;
    }
}

