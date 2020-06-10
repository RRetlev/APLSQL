package com.mastery.aplsql.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Data
@AllArgsConstructor
public class Table {
    private LinkedHashMap<ColumnProperties, Column> columns;
    private List<String> columnNames;
    private int idPointer;

    public Table() {
        this.columns = new LinkedHashMap<>();
        this.columnNames = new ArrayList<>();
        this.idPointer = 0;
    }

    public Table(Table that){
        this.columns = this.copyOfColumns(that.columns);
        this.columnNames = that.columnNames;
        this.idPointer = that.idPointer;
    }

    private LinkedHashMap<ColumnProperties, Column> copyOfColumns(LinkedHashMap<ColumnProperties, Column> columns){
        LinkedHashMap<ColumnProperties, Column> copied = new LinkedHashMap<>();
        columns.forEach((key, value) -> copied.put(new ColumnProperties(key), new Column(value)));
        return copied;
    }

















}
