package com.mastery.aplsql.service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class QueryStringParser {

    public static String parseTableName(String queryString) {
        Pattern p = Pattern.compile("\\b(?:FROM|INSERT INTO|DROP TABLE|CREATE TABLE)\\s(\\w+)\\b", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(queryString);
        if (!m.find()) return null;
        return m.group(1);
    }

    public static List<String> parseWhereCondition(String queryString) {
        Pattern p = Pattern.compile("\\bWHERE\\s(\\w+)\\s([<>=!]{1,2}|LIKE|IN|BETWEEN)\\s(\\w+)\\b", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(queryString);
        if (!m.find()) return null;
        return List.of(m.group(1), m.group(2), m.group(3));
    }
}
