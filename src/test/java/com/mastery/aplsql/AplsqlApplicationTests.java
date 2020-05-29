package com.mastery.aplsql;

import com.mastery.aplsql.Datastorage.Storage;
import com.mastery.aplsql.exceptionhandling.DuplicateEntryException;
import com.mastery.aplsql.exceptionhandling.EntityNotFoundException;
import com.mastery.aplsql.exceptionhandling.TypeMismatchException;
import com.mastery.aplsql.model.*;
import com.mastery.aplsql.service.DataBaseService;
import com.mastery.aplsql.service.TableService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;


@SpringBootTest
class AplsqlApplicationTests {

    private Storage storage;
    private Table table;
    private Column column;

    @Autowired
    TableService tableService;

    @Autowired
    DataBaseService dataBaseService;

    @BeforeEach
    void init() throws Exception {
        storage = new Storage();
        table = dataBaseService.insertTable(storage,new TableProperties("testTable"));
        column = tableService.insertColumn(table,new ColumnProperties("testColumn", Types.STRING));
    }

    @Test
    void contextLoads() {
        Assertions.assertEquals("test", "test");
    }

    @Test
    void DBCreated() {
        Assertions.assertNotNull(storage);
    }


    @Test
    void tableCreatedWithoutPrimaryKeyGetsID() throws DuplicateEntryException, EntityNotFoundException {
        dataBaseService.insertTable(storage,new TableProperties("test"));
        Assertions.assertEquals("id", dataBaseService.getTablePropertiesByName(storage,"test").getPrimaryKey());
    }

    @Test
    void tableCreatedWithPrimaryKeyGetsIt() throws DuplicateEntryException, EntityNotFoundException {
        dataBaseService.insertTable(storage,new TableProperties("test", "kiskutya"));
        Assertions.assertEquals("kiskutya", dataBaseService.getTablePropertiesByName(storage,"test").getPrimaryKey());
    }

    @Test
    void DuplicateNameOnTableCreationThrowsException() throws DuplicateEntryException {
        dataBaseService.insertTable(storage,new TableProperties("test"));
        Assertions.assertThrows(DuplicateEntryException.class, () -> dataBaseService.insertTable(storage,new TableProperties("test")));
    }

    @Test
    void getTableByName() throws DuplicateEntryException, EntityNotFoundException {
        dataBaseService.insertTable(storage,new TableProperties("test1"));
        dataBaseService.insertTable(storage,new TableProperties("test2"));
        Table table = dataBaseService.insertTable(storage,new TableProperties("test3"));
        dataBaseService.insertTable(storage,new TableProperties("test4"));
        dataBaseService.insertTable(storage,new TableProperties("test5"));
        dataBaseService.insertTable(storage,new TableProperties("test6"));
        Assertions.assertEquals(table, dataBaseService.getTableByName(storage,"test3"));
    }

    @Test
    void TableNotFoundThrowsException() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> dataBaseService.getTableByName(storage,"habakukk"));
    }

    @Test
    void columnCreated() throws Exception {
        tableService.insertColumn(table,new ColumnProperties("kutya", Types.STRING));
        tableService.insertColumn(table,new ColumnProperties("cica", Types.STRING));
        Assertions.assertEquals(4, table.getColumns().size());
    }

    @Test
    void GetColumnByName() throws Exception {
        tableService.insertColumn(table,new ColumnProperties("test2", Types.STRING));
        tableService.insertColumn(table,new ColumnProperties("test3", Types.STRING));
        Column column = tableService.insertColumn(table,new ColumnProperties("test", Types.STRING));
        tableService.insertColumn(table,new ColumnProperties("test5", Types.STRING));
        tableService.insertColumn(table,new ColumnProperties("test9", Types.STRING));
        tableService.insertColumn(table,new ColumnProperties("test76", Types.STRING));
        Assertions.assertEquals(column, tableService.getColumnByName(table,"test"));
    }

    @Test
    void DuplicateNameOnColumnCreationThrowsException() throws Exception {
        tableService.insertColumn(table,new ColumnProperties("test", Types.STRING));
        Assertions.assertThrows(DuplicateEntryException.class, () -> tableService.insertColumn(table,new ColumnProperties("test", Types.STRING)));
    }

    @Test
    void ColumnNotFoundThrowsException() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> tableService.getColumnByName(table,"pinokkió"));
    }

    @Test
    void saveDataInColumn() throws TypeMismatchException {
        column.addDataToColumn("a");
        column.addDataToColumn("b");
        column.addDataToColumn("c");
        column.addDataToColumn("d");
        Assertions.assertEquals("a", column.getData().get(0));
    }

    @Test
    void TypeMismatchThrowsException() throws TypeMismatchException {
        column.addDataToColumn("a");
        Assertions.assertThrows(TypeMismatchException.class, () -> column.addDataToColumn(5));
    }

    @Test
    void SelectQueryResultCorrectOrder() throws Exception {
        List<String> names = List.of("testColumn", "alma", "körte");
        tableService.insertColumn(table,new ColumnProperties("alma", Types.STRING));
        tableService.insertColumn(table,new ColumnProperties("körte", Types.STRING));
        tableService.insertRecords(table,Map.of("testColumn", "first", "alma", "pos", "körte", "fruit"));
        tableService.insertRecords(table,Map.of("testColumn", "second", "alma", "trash", "körte", "veggie"));
        Assertions.assertEquals(List.of(names, List.of("first", "pos", "fruit"), List.of("second", "trash", "veggie")), tableService.selectRecords(table,names,new WhereCondition()));
    }

    @Test
    void tableDropped() throws EntityNotFoundException {
        dataBaseService.dropTable(storage,"testTable");
        Assertions.assertThrows(EntityNotFoundException.class, () -> dataBaseService.getTablePropertiesByName(storage,"testTable"));
    }

    @Test
    void SelectQueryWithINConditionWorking() throws DuplicateEntryException, TypeMismatchException, EntityNotFoundException {
        List<String> names = List.of("testColumn", "alma", "körte");
        tableService.insertColumn(table,new ColumnProperties("alma", Types.STRING));
        tableService.insertColumn(table,new ColumnProperties("körte", Types.STRING));
        tableService.insertRecords(table,Map.of("testColumn", "first", "alma", "pos", "körte", "fruit"));
        tableService.insertRecords(table,Map.of("testColumn", "second", "alma", "trash", "körte", "veggie"));
        tableService.insertRecords(table,Map.of("testColumn", "third", "alma", "fos", "körte", "mittomen"));
        Assertions.assertEquals(List.of(names, List.of("first", "pos", "fruit"), List.of("third", "fos", "mittomen")), tableService.selectRecords(table,names,new WhereCondition("körte",OperatorBehaviour.IN,"(fruit,mittomen)")));


    }

}
