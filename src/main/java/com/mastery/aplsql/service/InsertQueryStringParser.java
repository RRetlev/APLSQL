package com.mastery.aplsql.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InsertQueryStringParser extends QueryStringParser {

    //TODO java regex stream
    public static Map<String, String> parseInsertValues(String queryString) {
        Pattern p = Pattern.compile("\\((.*)\\)\\s+VALUES\\s+\\((.*)\\)", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(queryString);
        if (!m.find()) return null;

        List<String> insertProps = List.of(m.group(1), m.group(2));

        String[] columns = insertProps.get(0).split("[\\s,]+");
        String[] values = insertProps.get(1).split("[\\s,]+");
        if (columns.length != values.length) return null;

        Map<String, String> insertMap = new HashMap<>();
        for (int i = 0; i < columns.length; i++) {
            insertMap.put(columns[i], values[i]);
        }
        return insertMap;
    }
}
