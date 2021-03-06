package com.mastery.aplsql;

import com.mastery.aplsql.Datastorage.Storage;
import com.mastery.aplsql.exceptionhandling.DuplicateEntryException;
import com.mastery.aplsql.exceptionhandling.EntityNotFoundException;
import com.mastery.aplsql.exceptionhandling.MalformedQueryException;
import com.mastery.aplsql.exceptionhandling.TypeMismatchException;
import com.mastery.aplsql.model.*;
import com.mastery.aplsql.service.DataBaseService;
import com.mastery.aplsql.service.QueryStringParser;
import com.mastery.aplsql.service.TableService;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

@SpringBootTest
public class IntegrationTests {


    private static TableService tableService;
    private static DataBaseService dataBaseService;

    private static Storage storage;
    private static Storage snapShot;

    @BeforeAll
    static void init() throws DuplicateEntryException, TypeMismatchException, EntityNotFoundException, MalformedQueryException {
        tableService = new TableService();
        dataBaseService = new DataBaseService();
        storage = new Storage();
        Table table = dataBaseService.insertTable(storage,new TableProperties("test"));
        tableService.insertColumn(table,new ColumnProperties("name", Types.STRING));
        tableService.insertColumn(table,new ColumnProperties("email", Types.STRING));
        tableService.insertColumn(table,new ColumnProperties("age", Types.INTEGER));
        tableService.insertRecords(table,Map.of("name", "Joe", "email", "joe@joe.joe", "age", "5"));
        tableService.insertRecords(table,Map.of("name", "Bill", "email", "bill@bill.bill", "age", "9"));
        tableService.insertRecords(table,Map.of("name", "Dick", "email", "dick@dick.dick", "age", "47"));
        snapShot = new Storage(storage);
        System.out.println(snapShot.equals(storage));
    }

    @AfterEach
    void rollback(){
        storage = new Storage(snapShot);
    }

    @Test
    void TableCreatedFromQueryString() throws MalformedQueryException, DuplicateEntryException {
        String s = "CREATE TABLE test2 (isActive boolean)";
        dataBaseService.insertTable(storage,new TableProperties(QueryStringParser.parseTableName(s)));
        Assertions.assertDoesNotThrow(() -> dataBaseService.getTableByName(storage,"test2"));
    }



    @Test
    void WrongNumberOfDataInsertedFromQueryThrows() throws Exception {
        String s = "INSERT INTO test (name) VALUES (Jack)";
        dataBaseService.getTableByName(storage,QueryStringParser.parseTableName(s));
        Assertions.assertThrows(MalformedQueryException.class,() -> tableService.insertRecords(dataBaseService.getTableByName(storage,QueryStringParser.parseTableName(s)), QueryStringParser.parseInsertValues(s)));
    }

    @Test
    void MultipleDataInsertedFromQuery() throws Exception {
        String s = "INSERT INTO test (name, age, email) VALUES (Philip,6,philip@philip.philip)";
        Table table = dataBaseService.getTableByName(storage,"test");
        Column nameColumn = tableService.getColumnByName(table,"name");
        Column ageColumn = tableService.getColumnByName(table,"age");
        Column emailColumn = tableService.getColumnByName(table,"email");
        tableService.insertRecords(table, QueryStringParser.parseInsertValues(s));
        Assertions.assertEquals("Philip", nameColumn.getDataAtIndex(3));
        Assertions.assertEquals(6, ageColumn.getDataAtIndex(3));
        Assertions.assertEquals("philip@philip.philip", emailColumn.getDataAtIndex(3));
    }

    @Test
    void SelectAllQueryWithoutWhere() throws EntityNotFoundException, MalformedQueryException {
        String s = "SELECT * FROM table";
        List<String> headers = List.of("id", "name", "email", "age");
        List<String> col1 = List.of("0", "Joe", "joe@joe.joe", "5");
        List<String> col2 = List.of("1", "Bill", "bill@bill.bill", "9");
        List<String> col3 = List.of("2", "Dick", "dick@dick.dick", "47");
        Table table = dataBaseService.getTableByName(storage,"test");
        Assertions.assertEquals(List.of(headers, col1, col2, col3),
                tableService.selectRecords(table, QueryStringParser.parseColumnNames(s), QueryStringParser.parseWhereCondition(s)));
    }

    @Test
    void SelectQueryWithWereCondition() throws EntityNotFoundException, MalformedQueryException {
        String s = "SELECT * FROM test WHERE name = Dick";
        List<String> headers = List.of("id", "name", "email", "age");
        List<String> colToSelect = List.of("2", "Dick", "dick@dick.dick", "47");
        Table table = dataBaseService.getTableByName(storage,"test");
        Assertions.assertEquals(List.of(headers, colToSelect), tableService.selectRecords(table, QueryStringParser.parseColumnNames(s), QueryStringParser.parseWhereCondition(s)));

    }

    @Test
    void UpdateWithoutWhere() throws MalformedQueryException, EntityNotFoundException, TypeMismatchException {
        String s = "UPDATE test SET name = Emily";
        Table table = dataBaseService.getTableByName(storage,QueryStringParser.parseTableName(s));
        tableService.updateRecord(table , QueryStringParser.getUpdateParameters(s), QueryStringParser.parseWhereCondition(s));
        Assertions.assertEquals("Emily", tableService.getColumnByName(table,"name").getDataAtIndex(0));
        Assertions.assertEquals("Emily", tableService.getColumnByName(table,"name").getDataAtIndex(1));
        Assertions.assertEquals("Emily", tableService.getColumnByName(table,"name").getDataAtIndex(2));
    }

    @Test
    void UpdateQueryWithWhere() throws EntityNotFoundException, MalformedQueryException, TypeMismatchException {
        String s = "UPDATE test SET name = Emily WHERE name = Dick";
        Table table = dataBaseService.getTableByName(storage,QueryStringParser.parseTableName(s));
        tableService.updateRecord(table, QueryStringParser.getUpdateParameters(s), QueryStringParser.parseWhereCondition(s));
        Assertions.assertEquals("Emily", tableService.getColumnByName(table,"name").getDataAtIndex(2));
    }

    @Test
    void UpdateQueryWithWhereMultipleData() throws EntityNotFoundException, MalformedQueryException, TypeMismatchException {
        String s = "UPDATE test SET name = Emily, age = 10 WHERE name = Dick";
        Table table = dataBaseService.getTableByName(storage,QueryStringParser.parseTableName(s));
        tableService.updateRecord(table, QueryStringParser.getUpdateParameters(s), QueryStringParser.parseWhereCondition(s));
        Assertions.assertEquals("Emily", tableService.getColumnByName(table,"name").getDataAtIndex(2));
        Assertions.assertEquals(10, tableService.getColumnByName(table,"age").getDataAtIndex(2));

    }

    @Test
    void DeleteQueryWithoutWhere() throws MalformedQueryException, EntityNotFoundException {
        String s = "DELETE FROM test";
        List<String> headers = List.of("id", "name", "email", "age");
        Table table = dataBaseService.getTableByName(storage,QueryStringParser.parseTableName(s));
        tableService.deleteRecords(table,QueryStringParser.parseWhereCondition(s));
        Assertions.assertEquals(List.of(headers), tableService.selectRecords(table,List.of("*"), new WhereCondition()));
    }

    @Test
    void DeleteQueryWithWhere() throws EntityNotFoundException, MalformedQueryException {
        String s = "DELETE FROM test WHERE age > 10";
        List<String> headers = List.of("id", "name", "email", "age");
        Table table = dataBaseService.getTableByName(storage,QueryStringParser.parseTableName(s));
        tableService.deleteRecords(table,QueryStringParser.parseWhereCondition(s));
        Assertions.assertEquals(List.of(headers),
                tableService.selectRecords(table,List.of("*"), QueryStringParser.parseWhereCondition("WHERE age > 10")));
    }

    @Test
    void InWhereConditionWorking() throws MalformedQueryException, EntityNotFoundException {
        String s = "SELECT * FROM test WHERE age IN (5, 9)";
        List<String> headers = List.of("id", "name", "email", "age");
        Table table = dataBaseService.getTableByName(storage,QueryStringParser.parseTableName(s));
        Assertions.assertEquals(List.of(headers,List.of("0","Joe","joe@joe.joe","5"),List.of("1","Bill","bill@bill.bill","9")),
                tableService.selectRecords(table,QueryStringParser.parseColumnNames(s),QueryStringParser.parseWhereCondition(s))
                );
    }

    @Test
    void UpdateThrowsTypeMismatch() throws MalformedQueryException, EntityNotFoundException, TypeMismatchException {
        String s = "UPDATE test SET age = text WHERE name = Bill";
        Table table = dataBaseService.getTableByName(storage,QueryStringParser.parseTableName(s));
        Assertions.assertThrows(RuntimeException.class, ()->tableService.updateRecord(table,
                QueryStringParser.getUpdateParameters(s), QueryStringParser.parseWhereCondition(s)));

    }

}

