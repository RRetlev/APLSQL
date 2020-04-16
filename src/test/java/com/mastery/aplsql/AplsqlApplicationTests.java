package com.mastery.aplsql;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.mastery.aplsql.Datastorage.Storage;
import com.mastery.aplsql.exceptions.DuplicateEntryException;
import com.mastery.aplsql.exceptions.EntityNotFound;
import com.mastery.aplsql.exceptions.TypeMismatchException;
import com.mastery.aplsql.model.Column;
import com.mastery.aplsql.model.Table;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Type;

@SpringBootTest
class AplsqlApplicationTests {

    private Storage storage;
    private Table table;
    private Column column;

    @BeforeEach
    void init() {
        storage = new Storage();
        table = storage.insertTable("testTable");
        column = table.insertColumn("testColumn",true);

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
    void tableCreatedWithoutPrimaryKeyGetsID() {
        storage.insertTable("test");
        Assertions.assertEquals("id", storage.getTablePropertiesByName("test").getPrimaryKey());
    }

    @Test
    void tableCreatedWithPrimaryKeyGetsIt() {
        storage.insertTable("test", "kiskutya");
        Assertions.assertEquals("kiskutya", storage.getTablePropertiesByName("test").getPrimaryKey());
    }

    @Test
    void DuplicateNameOnTableCreationThrowsException() {
        storage.insertTable("test");
        Assertions.assertThrows(DuplicateEntryException.class, () -> storage.insertTable("test"));
    }

    @Test
    void getTableByName() {
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
        Assertions.assertThrows(EntityNotFound.class,()->storage.getTableByName("habakukk"));
    }

    @Test
    void columnCreated() {
        table.insertColumn("kutya", true);
        table.insertColumn("cica", true);
        Assertions.assertEquals(3, table.getColumns().size());
    }

    @Test
    void GetColumnByName(){
        table.insertColumn("test2",true);
        table.insertColumn("test3",true);
        Column column = table.insertColumn("test",true);
        table.insertColumn("test5",true);
        table.insertColumn("test9",true);
        table.insertColumn("test76",true);
        Assertions.assertEquals(column,table.getColumnByName("test"));
    }

    @Test
    void DuplicateNameOnColumnCreationThrowsException() {
        table.insertColumn("test", true);
        Assertions.assertThrows(DuplicateEntryException.class,() -> table.insertColumn("test", true));
    }

    @Test
    void ColumnNotFoundThrowsException(){
        Assertions.assertThrows(EntityNotFound.class,()-> table.getColumnByName("pinokkió"));
    }

    @Test
    void saveDataInColumn(){
        column.addDataToColumn("a");
        column.addDataToColumn("b");
        column.addDataToColumn("c");
        column.addDataToColumn("d");
        Assertions.assertEquals("a",column.getData().get(0));
    }

    @Test
    void TypeMismatchThrowsException(){
        column.addDataToColumn("a");
        Assertions.assertThrows(TypeMismatchException.class,()-> column.addDataToColumn(5));
    }

}
