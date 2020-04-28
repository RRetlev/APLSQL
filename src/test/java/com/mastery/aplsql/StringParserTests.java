package com.mastery.aplsql;

import com.mastery.aplsql.service.CreateQueryStringParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class StringParserTests {

    @Test
    void isTableNameRetrieved(){
        String s= "CREATE table dirrdurr";
        Assertions.assertEquals("dirrdurr", CreateQueryStringParser.parseTableName(s));
    }

    @Test
    void oneParameterParsed(){
        String s= "CREATE table dirrdurr(col string)";
        Map<String,String>map = new HashMap<>();
        map.put("col","string");
        Assertions.assertEquals(map,CreateQueryStringParser.getColumnSpecs(s));
    }

    @Test
    void multipleParametersParsed(){
        String s= "CREATE table dirrdurr(col string , other string , another int)";
        Map<String,String>map = new HashMap<>();
        map.put("col","string");
        map.put("other","string");
        map.put("another","int");
        Assertions.assertEquals(map,CreateQueryStringParser.getColumnSpecs(s));
    }


}
