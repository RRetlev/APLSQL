package com.mastery.aplsql.service;

import com.mastery.aplsql.exceptionhandling.*;
import com.mastery.aplsql.model.Column;
import com.mastery.aplsql.model.ColumnProperties;
import com.mastery.aplsql.model.Table;
import com.mastery.aplsql.model.WhereCondition;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class TableService {


    public Column insertColumn(Table table, ColumnProperties columnProperties) throws DuplicateEntryException {
        if (!Util.containsName(table.getColumnNames(), columnProperties.getName())) {
            var column = Util.createColumn(columnProperties.getDataType().dataType);
            table.getColumnNames().add(columnProperties.getName());
            table.getColumns().put(columnProperties, column);
            return column;
        }
        throw new DuplicateEntryException();
    }

    public Column getColumnByName(Table table, String name) throws EntityNotFoundException {
        return table.getColumns().entrySet()
                .stream()
                .filter(columnEntry -> columnEntry.getKey().getName().equals(name))
                .findFirst()
                .orElseThrow(EntityNotFoundException::new).getValue();
    }

    public void insertColumns(Table table, HashMap<String, String> columnSpecs) {
        columnSpecs.forEach(ThrowingBiConsumer.unchecked((key, value) -> insertColumn(table, new ColumnProperties(key, Util.getDataTypeFromString(value)))));
    }

    public ColumnProperties getColumnPropertiesByName(Table table, String name) throws EntityNotFoundException {
        return table.getColumns().keySet()
                .stream()
                .filter(columnProperties -> columnProperties.getName().equals(name))
                .findFirst()
                .orElseThrow(EntityNotFoundException::new);
    }

    public void insertRecords(Table table, Map<String, String> map) throws EntityNotFoundException, TypeMismatchException, MalformedQueryException {
        if (map.entrySet().size() == table.getColumnNames().size() - 1) {
            map.forEach(ThrowingBiConsumer.unchecked((key, value) -> getColumnByName(table, key).addDataToColumn(getColumnPropertiesByName(table, key).getDataType().convert(value))));
            getColumnByName(table, "id").addDataToColumn(table.getIdPointer());
            table.setIdPointer(table.getIdPointer() + 1);
        } else throw new MalformedQueryException();
    }

    public List<List<String>> selectRecords(Table table, List<String> columnNames, WhereCondition condition) throws EntityNotFoundException {
        List<List<String>> queryResult = new ArrayList<>();

        if (columnNames.size() == 1 && columnNames.get(0).equals("*")) {
            columnNames = new ArrayList<>(table.getColumnNames());
        } else {
            for (String colName : columnNames) {
                if (!Util.containsName(table.getColumnNames(), colName)) throw new EntityNotFoundException();
            }
        }
        queryResult.add(columnNames);
        List<String> finalColumnNamesFormQuery = columnNames;
        List<Column> columnsFromSelect = table.getColumns().entrySet()
                .stream()
                .filter(columnPropertiesColumnEntry -> finalColumnNamesFormQuery.contains(columnPropertiesColumnEntry.getKey().getName()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());

        List<Integer> correctRecordIndeces = getCorrectIndeces(table, condition);
        correctRecordIndeces
                .forEach(i -> queryResult.add(columnsFromSelect.stream()
                        .map(col -> col.getDataAtIndex(i).toString())
                        .collect(Collectors.toList())));
        return queryResult;
    }

    public void thisShitDoesNothing() throws TypeMismatchException{
        if (true){
            return;
        }else {
            throw new TypeMismatchException();
        }
    }

    public List<List<String>> updateRecord(Table table, LinkedHashMap<String, String> values, WhereCondition condition) throws TypeMismatchException, EntityNotFoundException {
        List<Integer> correctRecordIndeces = getCorrectIndeces(table, condition);
        List<List<String>> originalRecord = selectRecords(table, new ArrayList<>(table.getColumnNames()), condition);
        List<String> columnNames = new ArrayList<>(values.keySet());

        correctRecordIndeces
                .forEach( i -> columnNames
                        .forEach(ThrowingConsumer.unchecked(name -> getColumnByName(table, name).setDataAtIndex(i,
                                getColumnPropertiesByName(table, name).getDataType().convert(values.get(name))))));
        return originalRecord;
    }

    private List<Integer> getCorrectIndeces(Table table, WhereCondition condition) throws EntityNotFoundException {
        return IntStream.range(0, getColumnByName(table, "id").getData().size()).filter(
                ThrowingPredicate.isEqual(i ->
                        condition.getOperation().evaluateCondition(getColumnByName(table, condition.getColumnName()).getDataAtIndex(i).toString(), condition.getValue())))
                .boxed()
                .collect(Collectors.toList());
    }

    public void deleteRecords(Table table, WhereCondition condition) throws EntityNotFoundException {
        List<Integer> correctIndeces = getCorrectIndeces(table, condition);
        Collections.reverse(correctIndeces);
        correctIndeces.forEach(i -> table.getColumns().values().forEach(ThrowingConsumer.unchecked(record -> record.removeDataAtIndex(i))));
    }
}
