package com.mastery.aplsql.model;

import com.mastery.aplsql.exceptions.DuplicateEntryException;
import com.mastery.aplsql.exceptions.EntityNotFoundException;
import com.mastery.aplsql.service.Util;
import lombok.Data;

import java.util.*;

@Data
public class Table {
    private HashMap<ColumnProperties, Column> columns;
    private Set<String> columnNames;

    public Table() {
        this.columns = new HashMap<>();
        this.columnNames = new HashSet<>();
    }


    public Column insertColumn(ColumnProperties columnProperties) throws Exception {
        if (!Util.containsName(columnNames, columnProperties.getName())) {
            var column = Util.createColumn(columnProperties.getDataType().dataType);
            columnNames.add(columnProperties.getName());
            columns.put(columnProperties, column);
            return column;
        }
        throw new DuplicateEntryException("Column name:"+ columnProperties.getName()+"is already present in the Table");
    }

    public Column getColumnByName(String name) throws EntityNotFoundException {
        for (Map.Entry<ColumnProperties, Column> entry :
                columns.entrySet())
            if (entry.getKey().getName().equals(name)) {
                return entry.getValue();
            }
        throw new EntityNotFoundException();
    }

    public void insertColumns(HashMap<String, String> columnSpecs) {
        for (Map.Entry<String,String> entry: columnSpecs.entrySet()
             ) {
            ColumnProperties columnProperties = new ColumnProperties(entry.getKey(),Util.getDataTypeFromString(entry.getValue()));
            try {
                insertColumn(columnProperties);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
