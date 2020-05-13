package com.mastery.aplsql.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DropTableQueryStringParser {
    public static String parseTableName(String s) {
        Pattern p = Pattern.compile("DROP TABLE\\s+(\\w+)\\b.*");
        Matcher m = p.matcher(s);
        if (m.find()){
            return m.group(1);
        }
        return null;
    }
}
