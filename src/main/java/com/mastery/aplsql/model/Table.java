package com.mastery.aplsql.model;

import com.mastery.aplsql.exceptionhandling.*;
import com.mastery.aplsql.service.SelectQueryStringParser;
import com.mastery.aplsql.service.Util;
import lombok.Data;
import org.springframework.http.ResponseEntity;

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
        columnSpecs.forEach(ThrowingBiConsumer.unchecked((key, value) -> insertColumn(new ColumnProperties(key, Util.getDataTypeFromString(value)))));
    }

    public ColumnProperties getColumnPropertiesByName(String name) throws EntityNotFoundException {
        return columns.keySet()
                .stream()
                .filter(columnProperties -> columnProperties.getName().equals(name))
                .findFirst()
                .orElseThrow(EntityNotFoundException::new);
    }

    public void insertRecords(Map<String, String> map) throws EntityNotFoundException, TypeMismatchException {
        map.forEach(ThrowingBiConsumer.unchecked((key, value) -> getColumnByName(key).addDataToColumn(getColumnPropertiesByName(key).getDataType().convert(value))));
        getColumnByName("id").addDataToColumn(idPointer);
        idPointer++;
    }

    public List<List<String>> selectRecords(List<String> columnNames, WhereCondition condition) throws EntityNotFoundException {
        List<List<String>> queryResult = new ArrayList<>();

        if (columnNames.size() == 1 && columnNames.get(0).equals("*")) {
            columnNames = new ArrayList<>(this.columnNames);
        } else {
            for (String colName : columnNames) {
                if (!Util.containsName(this.columnNames, colName)) throw new EntityNotFoundException();
            }
        }
        queryResult.add(columnNames);
        List<String> finalColumnNamesFormQuery = columnNames;
        List<Column> columnsFromSelect = columns.entrySet()
                .stream()
                .filter(columnPropertiesColumnEntry -> finalColumnNamesFormQuery.contains(columnPropertiesColumnEntry.getKey().getName()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());

        List<Integer> correctRecordIndeces = getCorrectIndeces(condition);
        correctRecordIndeces
                .forEach(i -> queryResult.add(columnsFromSelect.stream()
                        .map(col -> col.getDataAtIndex(i).toString())
                        .collect(Collectors.toList())));
        return queryResult;
    }

    public List<List<String>> updateRecord(LinkedHashMap<String, String> values, WhereCondition condition) throws EntityNotFoundException {
        List<Integer> correctRecordIndeces = getCorrectIndeces(condition);
        List<String> columnNames = new ArrayList<>(values.keySet());
        //TODO ternary to return correct value
        correctRecordIndeces
                .forEach(i -> columnNames
                        .forEach(ThrowingConsumer.unchecked(name -> getColumnByName(name).setDataAtIndex(i, values.get(name)))));
        return selectRecords(new ArrayList<>(this.columnNames), condition);
    }

    private List<Integer> getCorrectIndeces(WhereCondition condition) throws EntityNotFoundException {
        return IntStream.range(0, getColumnByName("id").getData().size()).filter(
                ThrowingPredicate.isEqual(i ->
                        condition.getOperation().evaluateCondition(getColumnByName(condition.getColumnName()).getDataAtIndex(i).toString(), condition.getValue())))
                .boxed()
                .collect(Collectors.toList());
    }

    public void deleteRecords(WhereCondition condition) throws EntityNotFoundException {
        List<Integer> correctIndeces = getCorrectIndeces(condition);
        Collections.reverse(correctIndeces);
        correctIndeces.forEach(i -> columns.values().forEach(ThrowingConsumer.unchecked(record -> record.removeDataAtIndex(i))));
    }
}
