package com.mastery.aplsql.service;

import com.mastery.aplsql.model.Column;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateQueryStringParser {
    public static String parseTableName(String queryString){
        Pattern p = Pattern.compile(".*?\\bTABLE\\s+(\\w+)\\b.*");
        Matcher m = p.matcher(queryString);
        if (m.find()){
            return m.group(1);
        }
        return null;
    }

    public static HashMap<String, String> getColumnSpecs(String queryString){
        String paramString = queryString.substring(queryString.indexOf("(")+1,queryString.lastIndexOf(")"));
        String[] paramArr = paramString.split("[\\s,]+");
        HashMap<String,String> paramMap = new HashMap<>();
        for (int i = 0; i < paramArr.length; i+=2) {
            paramMap.put(paramArr[i],paramArr[i+1]);
        }
        return paramMap;
    }

}
