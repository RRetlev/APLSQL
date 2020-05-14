package com.mastery.aplsql.model;

import com.mastery.aplsql.exceptionhandling.*;
import com.mastery.aplsql.service.SelectQueryStringParser;
import com.mastery.aplsql.service.Util;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class Table {
    private LinkedHashMap<ColumnProperties, Column> columns;
    private List<String> columnNames;
    private int idPointer;

    public Table() {
        this.columns = new LinkedHashMap<>();
        this.columnNames = new ArrayList<>();
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

    public void insertRecords(Map<String, String> map) {
        map.forEach(ThrowingBiConsumer.unchecked((key, value) -> getColumnByName(key).addDataToColumn(getColumnPropertiesByName(key).getDataType().convert(value))));
        idPointer++;
    }

    public List<List<String>> selectRecords(List<String> columnNames) {
        List<List<String>> queryResult = new ArrayList<>();

        if (columnNames.size() == 1 && columnNames.get(0).equals("*")){
            columnNames = new ArrayList<>(this.columnNames);
        }
        queryResult.add(columnNames);

        List<String> finalColumnNamesFormQuery = columnNames;
        List<Column> columnsFromSelect = columns.entrySet()
                .stream()
                .filter(columnPropertiesColumnEntry -> finalColumnNamesFormQuery.contains(columnPropertiesColumnEntry.getKey().getName()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());

//        List<Integer> correctRecordIndices = IntStream.range(0,idPointer).filter(i -> {
//            try {
//                return SelectQueryStringParser.parseWhereCondition(queryString).evaluateCondition(getColumnByName(SelectQueryStringParser.parseColumnNameFromWhereCondition(queryString)).getDataAtIndex(i).toString(),SelectQueryStringParser.getOperandFromWhereCondition(queryString));
//            } catch (EntityNotFoundException e) {
//                e.printStackTrace();
//            }
//            return false;
//        }).boxed().collect(Collectors.toList()) ;

        IntStream.range(0,idPointer)
                .forEach(i -> queryResult.add(columnsFromSelect.stream()
                        .map(col -> col.getDataAtIndex(i).toString())
                        .collect(Collectors.toList())));

        return queryResult;
    }
}
