package com.mastery.aplsql;

import com.mastery.aplsql.service.CreateQueryStringParser;
import com.mastery.aplsql.service.DropTableQueryStringParser;
import com.mastery.aplsql.service.InsertQueryStringParser;
import com.mastery.aplsql.service.SelectQueryStringParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

@SpringBootTest
public class StringParserTests {

    @Test
    void isCrateTableNameRetrieved(){
        String s= "CREATE TABLE dirrdurr";
        Assertions.assertEquals("dirrdurr", CreateQueryStringParser.parseTableName(s));
    }

    @Test
    void oneCreateParameterParsed(){
        String s= "CREATE TABLE dirrdurr(col string)";
        Map<String,String>map = Map.of("col","string");
        Assertions.assertEquals(map,CreateQueryStringParser.getColumnSpecs(s));
    }

    @Test
    void multipleCreateParametersParsed(){
        String s= "CREATE TABLE dirrdurr(col string , other string , another int)";
        Map<String,String>map = Map.of("col","string","other","string","another","int");
        Assertions.assertEquals(map,CreateQueryStringParser.getColumnSpecs(s));
    }

    @Test
    void isInsertTableNameRetrieved(){
        String s = "INSERT INTO Customers (CustomerName, ContactName, Address, City, PostalCode, Country)\n" +
                "VALUES ('Cardinal', 'Tom B. Erichsen', 'Skagen 21', 'Stavanger', '4006', 'Norway');";
        Assertions.assertEquals("Customers", InsertQueryStringParser.parseTableName(s));
    }

    @Test
    void oneInsertParameterParsed(){
        String s = "INSERT INTO test (name)" +
                "VALUES (Jack)";
        Map<String,String> map = Map.of("name","Jack");
        Assertions.assertEquals(map,InsertQueryStringParser.parseInsertValues(s));
    }

    @Test
    void multipleInsertParameterParsed(){
        String s = "INSERT INTO test (name, age, color)" +
                "VALUES (Jack, 2, blue)";
        Map<String,String> map = Map.of("name","Jack","age","2","color","blue");
        Assertions.assertEquals(map,InsertQueryStringParser.parseInsertValues(s));

    }

    @Test
    void oneSelectColumnNameParsed(){
        String s = "SELECT alma,körte,szilva from testTable";
        List<String> list = List.of("alma","körte","szilva");
        Assertions.assertEquals(list, SelectQueryStringParser.parseColumnNames(s));
    }

    @Test
    void droppedTableNameParsed() {
        String s = "DROP TABLE students";
        Assertions.assertEquals("students", DropTableQueryStringParser.parseTableName(s));
    }


}
