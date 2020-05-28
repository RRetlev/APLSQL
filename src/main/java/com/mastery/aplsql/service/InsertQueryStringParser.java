package com.mastery.aplsql.service;

import com.mastery.aplsql.exceptionhandling.MalformedQueryException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InsertQueryStringParser extends QueryStringParser {

    //TODO java regex stream
    public static Map<String, String> parseInsertValues(String queryString) throws MalformedQueryException {
        Pattern p = Pattern.compile("\\((.*)\\)\\s+VALUES\\s+\\((.*)\\)", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(queryString);
        if (!m.find()) throw new MalformedQueryException();
        List<String> insertProps = List.of(m.group(1), m.group(2));
        String[] columns = insertProps.get(0).split("[\\s,]+");
        String[] values = insertProps.get(1).split("[\\s,]+");
        if (columns.length != values.length) throw new MalformedQueryException();
        Map<String, String> insertMap = new HashMap<>();
        for (int i = 0; i < columns.length; i++) {
            insertMap.put(columns[i], values[i]);
        }
        return insertMap;
    }
}
