package com.mastery.aplsql.model;

import com.mastery.aplsql.exceptionhandling.*;
import com.mastery.aplsql.service.Util;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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


    public Column insertColumn(ColumnProperties columnProperties) throws DuplicateEntryException {
        if (!Util.containsName(columnNames, columnProperties.getName())) {
            var column = Util.createColumn(columnProperties.getDataType().dataType);
            columnNames.add(columnProperties.getName());
            columns.put(columnProperties, column);
            return column;
        }
        throw new DuplicateEntryException();
    }

    public Column getColumnByName(String name) throws EntityNotFoundException {
        return columns.entrySet()
                .stream()
                .filter(columnEntry -> columnEntry.getKey().getName().equals(name))
                .findFirst()
                .orElseThrow(EntityNotFoundException::new).getValue();
    }

    public void insertColumns(HashMap<String, String> columnSpecs) {
        columnSpecs.forEach( ThrowingBiConsumer.unchecked((key,value) -> insertColumn(new ColumnProperties(key, Util.getDataTypeFromString(value)))));
    }

    public ColumnProperties getColumnPropertiesByName(String name) throws EntityNotFoundException {
        return columns.keySet()
                .stream()
                .filter(columnProperties -> columnProperties.getName().equals(name))
                .findFirst()
                .orElseThrow(EntityNotFoundException::new);
    }

    public void insertRecords(Map<String, String> map){
        map.forEach(ThrowingBiConsumer.unchecked((key, value) -> getColumnByName(key).addDataToColumn(getColumnPropertiesByName(key).getDataType().convert(value))));
        idPointer++;
    }

    public List<List<String>> selectRecords(List<String> columnNames) {
        List<List<String>> queryResult = new ArrayList<>();
        queryResult.add(columnNames);
        List<Column> columnsFromSelect = columns.entrySet()
                .stream()
                .filter(columnPropertiesColumnEntry -> columnNames.contains(columnPropertiesColumnEntry.getKey().getName()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
        // TODO IntStream
        IntStream.range(0,idPointer)
                .forEach(i -> queryResult.add(columnsFromSelect.stream().map(col -> col.getDataAtIndex(i).toString()).collect(Collectors.toList())));
        return queryResult;
    }
}
