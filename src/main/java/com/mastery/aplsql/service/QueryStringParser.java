package com.mastery.aplsql.service;

import com.mastery.aplsql.exceptionhandling.MalformedQueryException;
import com.mastery.aplsql.model.WhereCondition;
import com.mastery.aplsql.service.Util;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryStringParser {

    public static String parseTableName(String queryString) throws MalformedQueryException {
        Pattern p = Pattern.compile("\\b(?:FROM|INSERT INTO|UPDATE|DROP TABLE|CREATE TABLE)\\s(\\w+)\\b", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(queryString);
        if (!m.find()) throw new MalformedQueryException();
        return m.group(1);
    }

    public static HashMap<String, String> getColumnSpecs(String queryString) throws MalformedQueryException {
        Pattern p = Pattern.compile("\\((.*)\\)");
        Matcher m = p.matcher(queryString);
        if (!m.find()) throw new MalformedQueryException();
        String parameters = m.group(1);
        HashMap<String, String> paramMap = new HashMap<>();
        Arrays.stream(parameters.split(",\\s"))
                .map(param -> param.split("\\s"))
                .forEach(paramArr -> paramMap.put(paramArr[0], paramArr[1]));
        return paramMap;
    }

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

    public static List<String> parseColumnNames(String s) throws MalformedQueryException {
        Pattern p = Pattern.compile("\\bSELECT\\s+(.{2,}|\\*)\\sFROM\\b", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(s);
        if (!m.find()) throw new MalformedQueryException();
        return Arrays.asList(m.group(1).split("[\\s,]+"));
    }

    public static WhereCondition parseWhereCondition(String queryString) {
        Pattern p = Pattern.compile("\\bWHERE\\s(\\w+)\\s([<>=!]{1,2}|LIKE|IN|BETWEEN)\\s(\\w+)\\b", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(queryString);
        if (!m.find()) return new WhereCondition();
        return new WhereCondition(m.group(1), Util.decideOperation(m.group(2)), m.group(3));
    }

    public static LinkedHashMap<String, String> getUpdateParameters(String queryString) throws MalformedQueryException {
        queryString = queryString.toLowerCase().contains("where") ?
                queryString.substring(0, queryString.toLowerCase().indexOf("where")) : queryString;
        LinkedHashMap values = new LinkedHashMap();
        Pattern p = Pattern.compile("\\bSET\\s+(.{2,})\\b");
        Matcher m = p.matcher(queryString);
        if (!m.find()) throw new MalformedQueryException();
        String[] arr = m.group(1).split("\\s=|,");
        for (int i = 0; i < arr.length - 1; i += 2) {
            values.put(arr[i].trim(), arr[i + 1].trim());
        }
        return values;
    }
}
