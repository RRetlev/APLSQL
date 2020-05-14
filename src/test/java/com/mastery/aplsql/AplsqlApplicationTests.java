package com.mastery.aplsql;

import com.mastery.aplsql.Datastorage.Storage;
import com.mastery.aplsql.exceptionhandling.DuplicateEntryException;
import com.mastery.aplsql.exceptionhandling.EntityNotFoundException;
import com.mastery.aplsql.exceptionhandling.TypeMismatchException;
import com.mastery.aplsql.model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;


@SpringBootTest
class AplsqlApplicationTests {

    private Storage storage;
    private Table table;
    private Column column;

    @BeforeEach
    void init() throws Exception {
        storage = new Storage();
        table = storage.insertTable(new TableProperties("testTable"));
        column = table.insertColumn(new ColumnProperties("testColumn", Types.STRING));
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
        storage.insertTable(new TableProperties("test"));
        Assertions.assertEquals("id", storage.getTablePropertiesByName("test").getPrimaryKey());
    }

    @Test
    void tableCreatedWithPrimaryKeyGetsIt() throws DuplicateEntryException, EntityNotFoundException {
        storage.insertTable(new TableProperties("test", "kiskutya"));
        Assertions.assertEquals("kiskutya", storage.getTablePropertiesByName("test").getPrimaryKey());
    }

    @Test
    void DuplicateNameOnTableCreationThrowsException() throws DuplicateEntryException {
        storage.insertTable(new TableProperties("test"));
        Assertions.assertThrows(DuplicateEntryException.class, () -> storage.insertTable(new TableProperties("test")));
    }

    @Test
    void getTableByName() throws DuplicateEntryException, EntityNotFoundException {
        storage.insertTable(new TableProperties("test1"));
        storage.insertTable(new TableProperties("test2"));
        Table table = storage.insertTable(new TableProperties("test3"));
        storage.insertTable(new TableProperties("test4"));
        storage.insertTable(new TableProperties("test5"));
        storage.insertTable(new TableProperties("test6"));
        Assertions.assertEquals(table, storage.getTableByName("test3"));
    }

    @Test
    void TableNotFoundThrowsException() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> storage.getTableByName("habakukk"));
    }

    @Test
    void columnCreated() throws Exception {
        table.insertColumn(new ColumnProperties("kutya", Types.STRING));
        table.insertColumn(new ColumnProperties("cica", Types.STRING));
        Assertions.assertEquals(3, table.getColumns().size());
    }

    @Test
    void GetColumnByName() throws Exception {
        table.insertColumn(new ColumnProperties("test2", Types.STRING));
        table.insertColumn(new ColumnProperties("test3", Types.STRING));
        Column column = table.insertColumn(new ColumnProperties("test", Types.STRING));
        table.insertColumn(new ColumnProperties("test5", Types.STRING));
        table.insertColumn(new ColumnProperties("test9", Types.STRING));
        table.insertColumn(new ColumnProperties("test76", Types.STRING));
        Assertions.assertEquals(column, table.getColumnByName("test"));
    }

    @Test
    void DuplicateNameOnColumnCreationThrowsException() throws Exception {
        table.insertColumn(new ColumnProperties("test", Types.STRING));
        Assertions.assertThrows(DuplicateEntryException.class, () -> table.insertColumn(new ColumnProperties("test", Types.STRING)));
    }

    @Test
    void ColumnNotFoundThrowsException() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> table.getColumnByName("pinokkió"));
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
        table.insertColumn(new ColumnProperties("alma", Types.STRING));
        table.insertColumn(new ColumnProperties("körte", Types.STRING));
        table.insertRecords(Map.of("testColumn", "first", "alma", "pos", "körte", "fruit"));
        table.insertRecords(Map.of("testColumn", "second", "alma", "trash", "körte", "veggie"));
        Assertions.assertEquals(List.of(names, List.of("first", "pos", "fruit"), List.of("second", "trash", "veggie")), table.selectRecords(names));
    }

    @Test
    void tableDropped() throws EntityNotFoundException {
        storage.dropTable("testTable");
        Assertions.assertThrows(EntityNotFoundException.class, () -> storage.getTablePropertiesByName("testTable"));
    }

}
