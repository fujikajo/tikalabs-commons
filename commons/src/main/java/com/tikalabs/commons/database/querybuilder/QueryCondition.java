package com.tikalabs.commons.database.querybuilder;


import java.util.List;

public class QueryCondition {
    private String column;
    private String operator;
    private Object value;
    private List<Object> values; // Liste für den IN-Operator

    // Konstruktor für einfache Bedingungen
    public QueryCondition(String column, String operator, Object value) {
        this.column = column;
        this.operator = operator;
        this.value = value;
        this.values = null; // Nicht genutzt in diesem Fall
    }

    // Konstruktor für den IN-Operator
    public QueryCondition(String column, List<Object> values) {
        this.column = column;
        this.operator = "IN";
        this.values = values;
        this.value = null; // Nicht genutzt in diesem Fall
    }

    public String getColumn() {
        return column;
    }

    public Object getValue() {
        return value;
    }

    public List<Object> getValues() {
        return values;
    }

    public String getOperator() {
        return operator;
    }

    // Getter und eventuell Setter
}
