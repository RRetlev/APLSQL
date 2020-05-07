package com.mastery.aplsql.model;

import com.mastery.aplsql.exceptions.DuplicateEntryException;
import com.mastery.aplsql.exceptions.EntityNotFoundException;
import com.mastery.aplsql.exceptions.TypeMismatchException;
import com.mastery.aplsql.service.Util;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

@Data
public class Table {
    private LinkedHashMap<ColumnProperties, Column> columns;
    private Set<String> columnNames;
    private int idPointer;

    public Table() {
        this.columns = new LinkedHashMap<>();
        this.columnNames = new HashSet<>();
        this.idPointer = 0;
    }


    public Column insertColumn(ColumnProperties columnProperties) throws Exception {
        if (!Util.containsName(columnNames, columnProperties.getName())) {
            var column = Util.createColumn(columnProperties.getDataType().dataType);
            columnNames.add(columnProperties.getName());
            columns.put(columnProperties, column);
            return column;
        }
        throw new DuplicateEntryException("Column name: " + columnProperties.getName() + " is already present in the Table");
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
        for (Map.Entry<String, String> entry : columnSpecs.entrySet()
        ) {
            ColumnProperties columnProperties = new ColumnProperties(entry.getKey(), Util.getDataTypeFromString(entry.getValue()));
            try {
                insertColumn(columnProperties);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public ColumnProperties getColumnPropertiesByName(String name) throws EntityNotFoundException {
        for (ColumnProperties cp : columns.keySet()) {
            if (cp.getName().equals(name)) {
                return cp;
            }
        }
        throw new EntityNotFoundException();
    }

    public void insertRecords(Map<String, String> map) {
        for (Map.Entry<String, String> entry : map.entrySet()
        ) {
            try {
                ColumnProperties cp = getColumnPropertiesByName(entry.getKey());
                Class cl = cp.getDataType().dataType;
                getColumnByName(entry.getKey()).addDataToColumn(cp.getDataType().convert(entry.getValue()));

            } catch (TypeMismatchException e) {
                e.printStackTrace();
            } catch (EntityNotFoundException e) {
                e.printStackTrace();
            }
        }
        idPointer++;
    }

    public List<List<String>> selectRecords(List<String> columnNames) {
        List<List<String>> queryResult = new ArrayList<>();
        queryResult.add(columnNames);
        List<Column> columnsFromSelect = new ArrayList<>();
        for (Map.Entry<ColumnProperties, Column> entry : columns.entrySet()
        ) {
            if (columnNames.contains(entry.getKey().getName())) {
                columnsFromSelect.add(entry.getValue());
            }
        }
        for (int i = 0; i < idPointer; i++) {
            if (true) {
                int finalI = i;
                queryResult.add(columnsFromSelect.stream().map(col -> col.getDataAtIndex(finalI).toString()).collect(Collectors.toList()));
            }
        }
        return queryResult;
    }
}
