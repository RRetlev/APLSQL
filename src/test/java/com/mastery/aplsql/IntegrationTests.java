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

    @BeforeEach
    void init() throws DuplicateEntryException, TypeMismatchException, EntityNotFoundException {
        storage = new Storage();
        Table table = storage.insertTable(new TableProperties("test"));
        table.insertColumn(new ColumnProperties("name", Types.STRING));
        table.insertColumn(new ColumnProperties("email", Types.STRING));
        table.insertColumn(new ColumnProperties("age", Types.INTEGER));
        table.insertRecords(Map.of("name", "Joe", "email", "joe@joe.joe", "age", "5"));
        table.insertRecords(Map.of("name", "Bill", "email", "bill@bill.bill", "age", "9"));
        table.insertRecords(Map.of("name", "Dick", "email", "dick@dick.dick", "age", "47"));
    }

    @Test
    void TableCreatedFromQueryString() throws MalformedQueryException, DuplicateEntryException {
        String s = "CREATE TABLE test2 (isActive boolean)";
        storage.insertTable(new TableProperties(CreateQueryStringParser.parseTableName(s)));
        Assertions.assertDoesNotThrow(() -> storage.getTableByName("test2"));
    }


    @Test
    void DataInsertedFromQuery() throws Exception {
        String s = "INSERT INTO test (name) VALUES (Jack)";
        Table table = storage.getTableByName(QueryStringParser.parseTableName(s));
        storage.getTableByName(QueryStringParser.parseTableName(s)).insertRecords(InsertQueryStringParser.parseInsertValues(s));
        Assertions.assertEquals("Jack", table.getColumnByName("name").getDataAtIndex(3));

    }

    @Test
    void MultipleDataInsertedFromQuery() throws Exception {
        String s = "INSERT INTO test (name, age) VALUES (Philip,6)";
        Table table = storage.getTableByName("test");
        Column nameColumn = table.getColumnByName("name");
        Column ageColumn = table.getColumnByName("age");
        table.insertRecords(InsertQueryStringParser.parseInsertValues(s));
        Assertions.assertEquals("Philip", nameColumn.getDataAtIndex(3));
        Assertions.assertEquals(6, ageColumn.getDataAtIndex(3));
    }

    @Test
    void SelectAllQueryWithoutWhere() throws EntityNotFoundException, MalformedQueryException {
        String s = "SELECT * FROM table";
        List<String> headers = List.of("id", "name", "email", "age");
        List<String> col1 = List.of("0", "Joe", "joe@joe.joe", "5");
        List<String> col2 = List.of("1", "Bill", "bill@bill.bill", "9");
        List<String> col3 = List.of("2", "Dick", "dick@dick.dick", "47");
        Table table = storage.getTableByName("test");
        Assertions.assertEquals(List.of(headers, col1, col2, col3),
                table.selectRecords(SelectQueryStringParser.parseColumnNames(s), QueryStringParser.parseWhereCondition(s)));
    }

    @Test
    void SelectQueryWithWereCondition() throws EntityNotFoundException, MalformedQueryException {
        String s = "SELECT * FROM test WHERE name = Dick";
        List<String> headers = List.of("id", "name", "email", "age");
        List<String> colToSelect = List.of("2", "Dick", "dick@dick.dick", "47");
        Table table = storage.getTableByName("test");
        Assertions.assertEquals(List.of(headers, colToSelect), table.selectRecords(SelectQueryStringParser.parseColumnNames(s), QueryStringParser.parseWhereCondition(s)));

    }

    @Test
    void UpdateWithoutWhere() throws MalformedQueryException, EntityNotFoundException {
        String s = "UPDATE test SET name = Emily";
        Table table = storage.getTableByName(QueryStringParser.parseTableName(s));
        table.updateRecord(UpdateQueryStringParser.getUpdateParameters(s), QueryStringParser.parseWhereCondition(s));
        Assertions.assertEquals("Emily", table.getColumnByName("name").getDataAtIndex(0));
        Assertions.assertEquals("Emily", table.getColumnByName("name").getDataAtIndex(1));
        Assertions.assertEquals("Emily", table.getColumnByName("name").getDataAtIndex(2));
    }

    @Test
    void UpdateQueryWithWhere() throws EntityNotFoundException, MalformedQueryException {
        String s = "UPDATE test SET name = Emily WHERE name = Dick";
        Table table = storage.getTableByName(QueryStringParser.parseTableName(s));
        table.updateRecord(UpdateQueryStringParser.getUpdateParameters(s), QueryStringParser.parseWhereCondition(s));
        Assertions.assertEquals("Emily", table.getColumnByName("name").getDataAtIndex(2));
    }

    @Test
    void UpdateQueryWithWhereMultipleData() throws EntityNotFoundException, MalformedQueryException {
        String s = "UPDATE test SET name = Emily, age = 10 WHERE name = Dick";
        Table table = storage.getTableByName(QueryStringParser.parseTableName(s));
        table.updateRecord(UpdateQueryStringParser.getUpdateParameters(s), QueryStringParser.parseWhereCondition(s));
        Assertions.assertEquals("Emily", table.getColumnByName("name").getDataAtIndex(2));
        Assertions.assertEquals("10", table.getColumnByName("age").getDataAtIndex(2));

    }

    @Test
    void DeleteQueryWithoutWhere() throws MalformedQueryException, EntityNotFoundException {
        String s = "DELETE FROM test";
        List<String> headers = List.of("id", "name", "email", "age");
        Table table = storage.getTableByName(QueryStringParser.parseTableName(s));
        table.deleteRecords(QueryStringParser.parseWhereCondition(s));
        Assertions.assertEquals(List.of(headers), table.selectRecords(List.of("*"), new WhereCondition()));
    }

    @Test
    void DeleteQueryWithWhere() throws EntityNotFoundException, MalformedQueryException {
        String s = "DELETE FROM test WHERE age > 10";
        List<String> headers = List.of("id", "name", "email", "age");
        Table table = storage.getTableByName(QueryStringParser.parseTableName(s));
        table.deleteRecords(QueryStringParser.parseWhereCondition(s));
        Assertions.assertEquals(List.of(headers),
                table.selectRecords(List.of("*"), QueryStringParser.parseWhereCondition("WHERE age > 10")));
    }

}

