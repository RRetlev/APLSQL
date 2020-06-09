package com.mastery.aplsql;

import com.mastery.aplsql.exceptionhandling.MalformedQueryException;
import com.mastery.aplsql.model.OperatorBehaviour;
import com.mastery.aplsql.model.WhereCondition;
import com.mastery.aplsql.service.QueryStringParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@SpringBootTest
public class StringParserTests {

    @Test
    void oneCreateParameterParsed() throws MalformedQueryException {
        String s = "CREATE TABLE test(col string)";
        Map<String, String> map = Map.of("col", "string");
        Assertions.assertEquals(map, QueryStringParser.getColumnSpecs(s));
    }

    @Test
    void multipleCreateParametersParsed() throws MalformedQueryException {
        String s = "CREATE TABLE test(col string, other string, another int)";
        Map<String, String> map = Map.of("col", "string", "other", "string", "another", "int");
        Assertions.assertEquals(map, QueryStringParser.getColumnSpecs(s));
    }

    @Test
    void oneInsertParameterParsed() throws MalformedQueryException {
        String s = "INSERT INTO test (name) VALUES (Jack)";
        Map<String, String> map = Map.of("name", "Jack");
        Assertions.assertEquals(map, QueryStringParser.parseInsertValues(s));
    }

    @Test
    void multipleInsertParameterParsed() throws MalformedQueryException {
        String s = "INSERT INTO test (name, age, color) VALUES (Jack, 2, blue)";
        Map<String, String> map = Map.of("name", "Jack", "age", "2", "color", "blue");
        Assertions.assertEquals(map, QueryStringParser.parseInsertValues(s));

    }

    @Test
    void oneSelectColumnNameParsed() throws MalformedQueryException {
        String s = "SELECT apple, cider, vinegar FROM testTable";
        List<String> list = List.of("apple", "cider", "vinegar");
        Assertions.assertEquals(list, QueryStringParser.parseColumnNames(s));
    }

    @Test
    void whereConditionParsed() {
        String s = "SELECT * FROM table WHERE col = 5";
        Assertions.assertEquals(new WhereCondition("col", OperatorBehaviour.EQUAL, "5"), QueryStringParser.parseWhereCondition(s));
    }

    @ParameterizedTest
    @MethodSource("provideAllWhereOperators")
    void whereOperatorsParsed(String queryString, WhereCondition expectedCondition) {
        Assertions.assertEquals(expectedCondition, QueryStringParser.parseWhereCondition(queryString));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "CREATE TABLE table (col1 string, col2 int)",
            "INSERT INTO table (col1, col2, col3) VALUES (apple, cider, vinegar)",
            "SELECT * FROM table",
            "DROP TABLE table",
            "UPDATE table SET name = name",
            "DELETE FROM table"
    })
    void parseTableName(String queryString) throws MalformedQueryException {
        Assertions.assertEquals("table", QueryStringParser.parseTableName(queryString));
    }

    @Test
    void tableNameParseThrowsMalformedQueryException() {
        Assertions.assertThrows(MalformedQueryException.class, ()->QueryStringParser.parseTableName("SELECT * FROM"));
    }

    @Test
    void getUpdateValues() throws MalformedQueryException {
        String s = "UPDATE table SET name = Jack, surname = Carl WHERE name = Jack";
        Assertions.assertEquals(Map.of("name", "Jack", "surname", "Carl"), QueryStringParser.getUpdateParameters(s));
    }

    private static Stream<Arguments> provideAllWhereOperators() {
        return Stream.of(
                Arguments.of("WHERE col = 5", new WhereCondition("col", OperatorBehaviour.EQUAL, "5")),
                Arguments.of("WHERE col < 5", new WhereCondition("col", OperatorBehaviour.LESSTHAN, "5")),
                Arguments.of("WHERE col > 5", new WhereCondition("col", OperatorBehaviour.GREATERTHAN, "5")),
                Arguments.of("WHERE col <= 5", new WhereCondition("col", OperatorBehaviour.LESSOREQUAL, "5")),
                Arguments.of("WHERE col >= 5", new WhereCondition("col", OperatorBehaviour.GREATEROREQUAL, "5")),
                Arguments.of("WHERE col <> 5", new WhereCondition("col", OperatorBehaviour.NOTEQUAL, "5")),
                Arguments.of("WHERE col != 5", new WhereCondition("col", OperatorBehaviour.NOTEQUAL, "5")),
                Arguments.of("WHERE col BETWEEN 5", new WhereCondition("col", OperatorBehaviour.BETWEEN, "5")),
                Arguments.of("WHERE col LIKE column", new WhereCondition("col", OperatorBehaviour.LIKE, "column")),
                Arguments.of("WHERE col IN column", new WhereCondition("col", OperatorBehaviour.IN, "column"))
        );
    }
}
