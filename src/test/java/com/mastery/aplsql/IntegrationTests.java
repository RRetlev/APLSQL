package com.mastery.aplsql;

import com.mastery.aplsql.Datastorage.Storage;
import com.mastery.aplsql.model.*;
import com.mastery.aplsql.service.CreateQueryStringParser;
import com.mastery.aplsql.service.InsertQueryStringParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class IntegrationTests {
    private Storage storage;

    @BeforeEach
    void init(){
        storage = new Storage();
    }

    @Test
    void TableCreatedFromQueryString() throws Exception {
        String s = "CREATE table pipacs(ID int)";
        Table table = storage.insertTable(new TableProperties(CreateQueryStringParser.parseTableName(s)));
        table.insertColumns(CreateQueryStringParser.getColumnSpecs(s));
        Assertions.assertEquals(new TableProperties("pipacs"),storage.getTablePropertiesByName("pipacs"));
    }

    @Test
    void DataInsertedFromQuery() throws Exception {
        String s = "Insert INTO test(name) VALUES (Jack)";
        Table table = storage.insertTable(new TableProperties("test"));
        Column column =table.insertColumn(new ColumnProperties("name", Types.STRING));
        table.insertRecords(InsertQueryStringParser.parseInsertValues(s));
        Assertions.assertEquals("Jack",column.getDataAtIndex(0));
    }
    @Test
    void MultipleDataInsertedFromQuery() throws Exception {
        String s = "Insert INTO test(name, age) VALUES (Jack,6)";
        Table table = storage.insertTable(new TableProperties("test"));
        Column column =table.insertColumn(new ColumnProperties("name", Types.STRING));
        Column columnb =table.insertColumn(new ColumnProperties("age", Types.INTEGER));
        table.insertRecords(InsertQueryStringParser.parseInsertValues(s));
        Assertions.assertEquals("Jack",column.getDataAtIndex(0));
        Assertions.assertEquals(6,columnb.getDataAtIndex(0));
    }


}

