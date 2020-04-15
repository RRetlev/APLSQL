package com.mastery.aplsql.model;

import lombok.Data;

import java.util.*;

@Data
public class Table {
    private HashMap<ColumnProperties, Column> columns;

    public Table() {
      this.columns = new HashMap<>();
    }


    public void insertColumn(String columnName, boolean allowNulls){
        ColumnProperties columnProperties = new ColumnProperties(columnName,allowNulls);
        Column column = new Column();
        columns.put(columnProperties,column);
    }

    public Column getColumnByName(String name){
        for (Map.Entry<ColumnProperties, Column> entry :
                columns.entrySet())
            if (entry.getKey().getName().equals(name)) {
                return entry.getValue();
            }
        return null;
    }
}
