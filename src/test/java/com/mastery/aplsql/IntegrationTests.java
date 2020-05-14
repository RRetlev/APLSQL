package com.mastery.aplsql;

import com.mastery.aplsql.Datastorage.Storage;
import com.mastery.aplsql.exceptionhandling.DuplicateEntryException;
import com.mastery.aplsql.exceptionhandling.TypeMismatchException;
import com.mastery.aplsql.model.*;
import com.mastery.aplsql.service.CreateQueryStringParser;
import com.mastery.aplsql.service.InsertQueryStringParser;
import com.mastery.aplsql.service.SelectQueryStringParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

@SpringBootTest
public class IntegrationTests {
    private Storage storage;

    @BeforeEach
    void init() {
        storage = new Storage();
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
        String s = "INSERT INTO test(name) VALUES (Jack)";
        Table table = storage.insertTable(new TableProperties("test"));
        Column column = table.insertColumn(new ColumnProperties("name", Types.STRING));
        table.insertRecords(InsertQueryStringParser.parseInsertValues(s));
        Assertions.assertEquals("Jack", column.getDataAtIndex(0));
    }

    @Test
    void MultipleDataInsertedFromQuery() throws Exception {
        String s = "INSERT INTO test(name, age) VALUES (Jack,6)";
        Table table = storage.insertTable(new TableProperties("test"));
        Column column = table.insertColumn(new ColumnProperties("name", Types.STRING));
        Column columnb = table.insertColumn(new ColumnProperties("age", Types.INTEGER));
        table.insertRecords(InsertQueryStringParser.parseInsertValues(s));
        Assertions.assertEquals("Jack", column.getDataAtIndex(0));
        Assertions.assertEquals(6, columnb.getDataAtIndex(0));
    }

    @Test
    void SelectQueryReturnsAllTheColumnsOnStar() throws DuplicateEntryException, TypeMismatchException {
        List<String> l = List.of("*");
        List<String> names = List.of("testColumn", "alma", "körte");
        Table table = storage.insertTable(new TableProperties("test"));
        table.insertColumn(new ColumnProperties("testColumn", Types.STRING));
        table.insertColumn(new ColumnProperties("alma", Types.STRING));
        table.insertColumn(new ColumnProperties("körte", Types.STRING));
        table.insertRecords(Map.of("testColumn", "first", "alma", "pos", "körte", "fruit"));
        table.insertRecords(Map.of("testColumn", "second", "alma", "trash", "körte", "veggie"));
        Assertions.assertEquals(List.of(names, List.of("first", "pos", "fruit"), List.of("second", "trash", "veggie")), table.selectRecords(l));
    }

}

