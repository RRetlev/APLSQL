package com.mastery.aplsql.service;

import com.mastery.aplsql.exceptionhandling.MalformedQueryException;
import com.mastery.aplsql.model.OperatorBehaviour;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SelectQueryStringParser extends  QueryStringParser{

    public static List<String> parseColumnNames(String s) throws MalformedQueryException {
        Pattern p = Pattern.compile("\\bSELECT\\s+(.{2,}|\\*)\\sFROM\\b", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(s);
        if (!m.find()) throw new MalformedQueryException();
        return Arrays.asList(m.group(1).split("[\\s,]+"));
    }
    @Deprecated
    public static OperatorBehaviour parseWhereConditionOperator(String queryString){
        Pattern p = Pattern.compile(".*?\\bWHERE\\s+\\w+\\s+(.).*");
        Matcher m = p.matcher(queryString.trim());
        if(m.find()){
            return Util.decideOperation(m.group(1));
        }
        return null;
    }
    @Deprecated
    public static String getOperandFromWhereCondition(String queryString){
        Pattern p = Pattern.compile("\\s(\\w+)$");
        Matcher m = p.matcher(queryString.trim());
        if(m.find()){
            return m.group(1);
        }
        return null;
    }
    @Deprecated
    public static String parseColumnNameFromWhereCondition(String queryString){
        Pattern p = Pattern.compile(".*?\\bWHERE\\s+(\\w+)\\b.*");
        Matcher m = p.matcher(queryString);
        if (m.find()){
            return m.group(1);
        }
        return null;
    }
}
