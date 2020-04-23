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


    public Column insertColumn(String columnName, boolean allowNulls,Types dataType) throws Exception {
        if (!Util.containsName(columnNames, columnName)) {
            ColumnProperties columnProperties = new ColumnProperties(columnName, allowNulls,dataType.dataType);
            var column = Util.createColumn(dataType.dataType);
            columnNames.add(columnName);
            columns.put(columnProperties, column);
            return column;
        }
        throw new DuplicateEntryException();
    }
    public Column insertColumn(String columnName, Types dataType) throws Exception {
        if (!Util.containsName(columnNames, columnName)) {
            ColumnProperties columnProperties = new ColumnProperties(columnName,dataType.dataType);
            var column = Util.createColumn(dataType.dataType);
            columnNames.add(columnName);
            columns.put(columnProperties, column);
            return column;
        }
        throw new DuplicateEntryException();
    }

    public Column getColumnByName(String name) throws EntityNotFoundException {
        for (Map.Entry<ColumnProperties, Column> entry :
                columns.entrySet())
            if (entry.getKey().getName().equals(name)) {
                return entry.getValue();
            }
        throw new EntityNotFoundException();
    }
}
