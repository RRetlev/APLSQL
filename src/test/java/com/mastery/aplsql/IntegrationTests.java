package com.mastery.aplsql;

import com.mastery.aplsql.Datastorage.Storage;
import com.mastery.aplsql.exceptionhandling.DuplicateEntryException;
import com.mastery.aplsql.exceptionhandling.EntityNotFoundException;
import com.mastery.aplsql.exceptionhandling.MalformedQueryException;
import com.mastery.aplsql.exceptionhandling.TypeMismatchException;
import com.mastery.aplsql.model.*;
import com.mastery.aplsql.service.*;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

@SpringBootTest
public class IntegrationTests {
    private static Storage storage;
    private static Storage snapShot;

    @BeforeEach
     void init() throws DuplicateEntryException, TypeMismatchException, EntityNotFoundException {
        storage = new Storage();
        Table table = storage.insertTable(new TableProperties("test"));
        table.insertColumn(new ColumnProperties("name",Types.STRING));
        table.insertColumn(new ColumnProperties("email",Types.STRING));
        table.insertColumn(new ColumnProperties("age",Types.INTEGER));
        table.insertRecords(Map.of("name","Joe","email","joe@joe.joe","age", "5"));
        table.insertRecords(Map.of("name","Bill","email","bill@bill.bill","age", "9"));
        table.insertRecords(Map.of("name","Dick","email","dick@dick.dick","age", "47"));
        snapShot = new Storage(storage);
    }

//    @AfterEach
//    void rollback(){
//        storage = new Storage(snapShot);
//    }
    @Test
    void testIfSnapShotIsEqual(){
        Assertions.assertEquals(storage, snapShot);
        Assertions.assertEquals(storage.getDB(), snapShot.getDB());
        Assertions.assertEquals(storage.getTableNames(), snapShot.getTableNames());
    }

    @Test
    void TableCreatedFromQueryString() throws Exception {
        String s = "CREATE TABLE pipacs(ID int)";
        Table table = storage.insertTable(new TableProperties(CreateQueryStringParser.parseTableName(s)));
        table.insertColumns(CreateQueryStringParser.getColumnSpecs(s));
        Assertions.assertEquals(new TableProperties("pipacs"), storage.getTablePropertiesByName("pipacs"));
    }

    @Test
    void DataInsertedFromQuery() throws Exception {
//        Assertions.assertEquals(snapShot,storage);
        String s = "INSERT INTO test(name) VALUES (Jack)";
        Table table = storage.getTableByName(QueryStringParser.parseTableName(s));
        storage.getTableByName(QueryStringParser.parseTableName(s)).insertRecords(InsertQueryStringParser.parseInsertValues(s));
        Assertions.assertEquals("Jack", table.getColumnByName("name").getDataAtIndex(3));
//        Assertions.assertEquals(snapShot,storage);

    }

    @Test
    void MultipleDataInsertedFromQuery() throws Exception {
        String s = "INSERT INTO test(name, age) VALUES (Philip,6)";
        Table table = storage.getTableByName("test");
        Column nameColumn = table.getColumnByName("name");
        Column ageColumn = table.getColumnByName("age");
        table.insertRecords(InsertQueryStringParser.parseInsertValues(s));
        Assertions.assertEquals("Philip", nameColumn.getDataAtIndex(3));
        Assertions.assertEquals(6, ageColumn.getDataAtIndex(3));
    }

    @Test
    void SelectQueryReturnsAllTheColumnsOnStar() throws DuplicateEntryException, TypeMismatchException, EntityNotFoundException {
        List<String> l = List.of("*");
        List<String> names = List.of("id","testColumn", "alma", "körte");
        Table table = storage.insertTable(new TableProperties("test"));
        table.insertColumn(new ColumnProperties("testColumn", Types.STRING));
        table.insertColumn(new ColumnProperties("alma", Types.STRING));
        table.insertColumn(new ColumnProperties("körte", Types.STRING));
        table.insertRecords(Map.of("testColumn", "first", "alma", "pos", "körte", "fruit"));
        table.insertRecords(Map.of("testColumn", "second", "alma", "trash", "körte", "veggie"));
        Assertions.assertEquals(List.of(names, List.of("0","first", "pos", "fruit"), List.of("1","second", "trash", "veggie")), table.selectRecords(l,new WhereCondition()));
    }

    @Test
    void SelectQueryWithWereCondition() throws DuplicateEntryException, TypeMismatchException, EntityNotFoundException, MalformedQueryException {
        String s = "SELECT * FROM table WHERE name = alma";
        List<String> names = List.of("id","testColumn", "name", "age");
        Table table = storage.insertTable(new TableProperties("table"));
        table.insertColumn(new ColumnProperties("testColumn", Types.STRING));
        table.insertColumn(new ColumnProperties("name", Types.STRING));
        table.insertColumn(new ColumnProperties("age", Types.STRING));
        table.insertRecords(Map.of("testColumn", "first", "name", "alma", "age", "fruit"));
        table.insertRecords(Map.of("testColumn", "second", "name", "JAck", "age", "veggie"));
        table.insertRecords(Map.of("testColumn", "third", "name", "alma", "age", "trash"));
        Assertions.assertEquals(List.of(names,List.of("0","first","alma","fruit"),List.of("2","third","alma","trash")),table.selectRecords(SelectQueryStringParser.parseColumnNames(s), QueryStringParser.parseWhereCondition(s)));

    }
    @Test
    void SelectQueryWithoutWhere() throws DuplicateEntryException, TypeMismatchException, EntityNotFoundException, MalformedQueryException {
        String s = "SELECT * FROM table";
        List<String> names = List.of("id","testColumn", "name", "age");
        Table table = storage.insertTable(new TableProperties("table"));
        table.insertColumn(new ColumnProperties("testColumn", Types.STRING));
        table.insertColumn(new ColumnProperties("name", Types.STRING));
        table.insertColumn(new ColumnProperties("age", Types.STRING));
        table.insertRecords(Map.of("testColumn", "first", "name", "alma", "age", "fruit"));
        table.insertRecords(Map.of("testColumn", "second", "name", "JAck", "age", "veggie"));
        table.insertRecords(Map.of("testColumn", "third", "name", "alma", "age", "trash"));
        Assertions.assertEquals(List.of
                (names,
                        List.of("0","first","alma","fruit"),
                        List.of("1","second","JAck","veggie"),
                        List.of("2","third","alma","trash")),
                table.selectRecords(SelectQueryStringParser.parseColumnNames(s), QueryStringParser.parseWhereCondition(s)));
    }

    @Test
    void UpdateQueryWithWhere() throws DuplicateEntryException, TypeMismatchException, EntityNotFoundException, MalformedQueryException {
        String s = "UPDATE table SET name = Joe WHERE name = Jack";
        Table table = storage.insertTable(new TableProperties("table"));
        table.insertColumn(new ColumnProperties("testColumn", Types.STRING));
        Column column =table.insertColumn(new ColumnProperties("name", Types.STRING));
        table.insertRecords(Map.of("testColumn", "first", "name", "Jack"));
        table.insertRecords(Map.of("testColumn", "second", "name", "alma"));
        table.updateRecord(UpdateQueryStringParser.getUpdateParameters(s),QueryStringParser.parseWhereCondition(s));
        Assertions.assertEquals(column.getDataAtIndex(0),"Joe");

    }

}

