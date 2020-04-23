package com.mastery.aplsql;

import com.mastery.aplsql.Datastorage.Storage;
import com.mastery.aplsql.exceptions.DuplicateEntryException;
import com.mastery.aplsql.exceptions.EntityNotFoundException;
import com.mastery.aplsql.exceptions.TypeMismatchException;
import com.mastery.aplsql.model.Column;
import com.mastery.aplsql.model.Table;
import com.mastery.aplsql.model.Types;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class AplsqlApplicationTests {

    private Storage storage;
    private Table table;
    private Column column;

    @BeforeEach
    void init() throws Exception {
        storage = new Storage();
        table = storage.insertTable("testTable");
        column = table.insertColumn("testColumn",Types.STRING);

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
        storage.insertTable("test");
        Assertions.assertEquals("id", storage.getTablePropertiesByName("test").getPrimaryKey());
    }

    @Test
    void tableCreatedWithPrimaryKeyGetsIt() throws DuplicateEntryException, EntityNotFoundException {
        storage.insertTable("test", "kiskutya");
        Assertions.assertEquals("kiskutya", storage.getTablePropertiesByName("test").getPrimaryKey());
    }

    @Test
    void DuplicateNameOnTableCreationThrowsException() throws DuplicateEntryException {
        storage.insertTable("test");
        Assertions.assertThrows(DuplicateEntryException.class, () -> storage.insertTable("test"));
    }

    @Test
    void getTableByName() throws DuplicateEntryException, EntityNotFoundException {
        storage.insertTable("test1");
        storage.insertTable("test2");
        Table table = storage.insertTable("test3");
        storage.insertTable("test4");
        storage.insertTable("test5");
        storage.insertTable("test6");
        Assertions.assertEquals(table, storage.getTableByName("test3"));
    }

    @Test
    void TableNotFoundThrowsException(){
        Assertions.assertThrows(EntityNotFoundException.class,()->storage.getTableByName("habakukk"));
    }

    @Test
    void columnCreated() throws Exception {
        table.insertColumn("kutya", Types.STRING);
        table.insertColumn("cica", Types.STRING);
        Assertions.assertEquals(3, table.getColumns().size());
    }

    @Test
    void GetColumnByName() throws Exception {
        table.insertColumn("test2",Types.STRING);
        table.insertColumn("test3",Types.STRING);
        Column column = table.insertColumn("test",Types.STRING);
        table.insertColumn("test5",Types.STRING);
        table.insertColumn("test9",Types.STRING);
        table.insertColumn("test76",Types.STRING);
        Assertions.assertEquals(column,table.getColumnByName("test"));
    }

    @Test
    void DuplicateNameOnColumnCreationThrowsException() throws Exception {
        table.insertColumn("test", Types.STRING);
        Assertions.assertThrows(DuplicateEntryException.class,() -> table.insertColumn("test", Types.STRING));
    }

    @Test
    void ColumnNotFoundThrowsException(){
        Assertions.assertThrows(EntityNotFoundException.class,()-> table.getColumnByName("pinokkió"));
    }

    @Test
    void saveDataInColumn() throws TypeMismatchException {
        column.addDataToColumn("a");
        column.addDataToColumn("b");
        column.addDataToColumn("c");
        column.addDataToColumn("d");
        Assertions.assertEquals("a",column.getData().get(0));
    }

    @Test
    void TypeMismatchThrowsException() throws TypeMismatchException {
        column.addDataToColumn("a");
        Assertions.assertThrows(TypeMismatchException.class,()-> column.addDataToColumn(5));
    }

}
