package com.mastery.aplsql;

import com.mastery.aplsql.model.OperatorBehaviour;
import com.mastery.aplsql.model.WhereCondition;
import com.mastery.aplsql.service.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

@SpringBootTest
public class StringParserTests {

    @Test
    void isCrateTableNameRetrieved() {
        String s = "CREATE TABLE dirrdurr";
        Assertions.assertEquals("dirrdurr", CreateQueryStringParser.parseTableName(s));
    }

    @Test
    void oneCreateParameterParsed() {
        String s = "CREATE TABLE dirrdurr(col string)";
        Map<String, String> map = Map.of("col", "string");
        Assertions.assertEquals(map, CreateQueryStringParser.getColumnSpecs(s));
    }

    @Test
    void multipleCreateParametersParsed() {
        String s = "CREATE TABLE dirrdurr(col string , other string , another int)";
        Map<String, String> map = Map.of("col", "string", "other", "string", "another", "int");
        Assertions.assertEquals(map, CreateQueryStringParser.getColumnSpecs(s));
    }

    @Test
    void isInsertTableNameRetrieved() {
        String s = "INSERT INTO Customers (CustomerName, ContactName, Address, City, PostalCode, Country)\n" +
                "VALUES ('Cardinal', 'Tom B. Erichsen', 'Skagen 21', 'Stavanger', '4006', 'Norway');";
        Assertions.assertEquals("Customers", InsertQueryStringParser.parseTableName(s));
    }

    @Test
    void oneInsertParameterParsed() {
        String s = "INSERT INTO test (name) VALUES (Jack)";
        Map<String, String> map = Map.of("name", "Jack");
        Assertions.assertEquals(map, InsertQueryStringParser.parseInsertValues(s));
    }

    @Test
    void multipleInsertParameterParsed() {
        String s = "INSERT INTO test (name, age, color) VALUES (Jack, 2, blue)";
        Map<String, String> map = Map.of("name", "Jack", "age", "2", "color", "blue");
        Assertions.assertEquals(map, InsertQueryStringParser.parseInsertValues(s));

    }

    @Test
    void oneSelectColumnNameParsed() {
        String s = "SELECT alma, körte, szilva FROM testTable";
        List<String> list = List.of("alma", "körte", "szilva");
        Assertions.assertEquals(list, SelectQueryStringParser.parseColumnNames(s));
    }

    @Test
    void extractWhereOperator() {
        String s = "SELECT alma FROM test WHERE col = 2";
        Assertions.assertEquals(OperatorBehaviour.EQUAL, SelectQueryStringParser.parseWhereConditionOperator(s));
    }

    @Test
    void extractOperandFromWhere() {
        String s = "SELECT alma FROM test WHERE col = 2";
        Assertions.assertEquals("2", SelectQueryStringParser.getOperandFromWhereCondition(s));
    }

    @Test
    void extractColumnNameFromWhere() {
        String s = "SELECT alma FROM test WHERE col = 2";
        Assertions.assertEquals("col", SelectQueryStringParser.parseColumnNameFromWhereCondition(s));
    }

    @Test
    void testNewParserWhere() {
        String s = "SELECT * FROM table WHERE col = 5";
        Assertions.assertEquals(new WhereCondition("col",OperatorBehaviour.EQUAL, "5"), QueryStringParser.parseWhereCondition(s));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "CREATE TABLE table (col1 string, col2 int)",
            "INSERT INTO table (col1, col2, col3) VALUES (apple, cider, vinegar)",
            "SELECT * FROM table",
            "DROP TABLE table"
    })
    void testNewParserTableName(String queryString) {
        Assertions.assertEquals("table", QueryStringParser.parseTableName(queryString));
    }

}
