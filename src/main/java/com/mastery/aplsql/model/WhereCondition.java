package com.mastery.aplsql.model;

import lombok.Data;

@Data
public class WhereCondition {
    private String columnName;
    private OperatorBehaviour operation;
    private String value;

    public WhereCondition(String columnName, OperatorBehaviour operation, String value) {
        this.columnName = columnName;
        this.operation = operation;
        this.value = value;
    }

    public WhereCondition() {
        this.columnName = "id";
        this.operation = OperatorBehaviour.GREATEROREQUAL;
        this.value ="0";
    }
}
