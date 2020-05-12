package com.mastery.aplsql.service;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SelectQueryStringParser {

    public static String parseTableName(String queryString){
        Pattern p = Pattern.compile(".*?\\bfrom\\s+(\\w+)\\b.*");
        Matcher m = p.matcher(queryString);
        if (m.find()){
            return m.group(1);
        }
        return null;
    }

    public static List<String> parseColumnNames(String s){
        Pattern p = Pattern.compile(".*?\\bSELECT\\s+(\\S+)\\s.*");
        Matcher m = p.matcher(s);
        String columnNameString = null;
        if (m.find()){
             columnNameString = m.group(1);
        }
        return Arrays.asList(columnNameString.split(","));

    }
}
