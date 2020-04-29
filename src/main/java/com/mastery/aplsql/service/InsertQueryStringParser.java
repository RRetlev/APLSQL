package com.mastery.aplsql.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InsertQueryStringParser {
    public static String parseTableName(String queryString){
        Pattern p = Pattern.compile(".*?\\bINTO\\s+(\\w+)\\b.*");
        Matcher m = p.matcher(queryString);
        if (m.find()){
            return m.group(1);
        }
        return null;
    }
    //TODO java regex stream
    public static Map<String,String> parseInsertValues(String queryString){
        Pattern p = Pattern.compile("\\((.*?)\\)");
        Matcher m = p.matcher(queryString);
        List<String> props = new ArrayList<>();
        while (m.find()){
            props.add(m.group(1));
        }
        Map<String,String> map = new HashMap<>();
        String[] arr1 = props.get(0).split("[\\s,]+");
        String[] arr2 = props.get(1).split("[\\s,]+");
        for (int i = 0; i < arr1.length ; i++) {
            map.put(arr1[i],arr2[i]);
        }
        return map;
    }
}
