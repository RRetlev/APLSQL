package com.mastery.aplsql.service.scraper;

import com.mastery.aplsql.exceptionhandling.MalformedQueryException;
import com.mastery.aplsql.model.WhereCondition;
import com.mastery.aplsql.service.Util;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class QueryStringParser {

    public static String parseTableName(String queryString) throws MalformedQueryException {
        Pattern p = Pattern.compile("\\b(?:FROM|INSERT INTO|UPDATE|DROP TABLE|CREATE TABLE)\\s(\\w+)\\b", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(queryString);
        if (!m.find()) throw new MalformedQueryException();
        return m.group(1);
    }

    public static WhereCondition parseWhereCondition(String queryString) {
        Pattern p = Pattern.compile("\\bWHERE\\s(\\w+)\\s([<>=!]{1,2}|LIKE|IN|BETWEEN)\\s(\\w+)\\b", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(queryString);
        if (!m.find()) return new WhereCondition();
        return  new WhereCondition(m.group(1), Util.decideOperation(m.group(2)),m.group(3));
    }
}
