package com.mastery.aplsql.service.scraper;

import com.mastery.aplsql.exceptionhandling.MalformedQueryException;
import com.mastery.aplsql.service.scraper.QueryStringParser;

import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateQueryStringParser extends QueryStringParser {


    public static LinkedHashMap<String,String> getUpdateParameters(String queryString) throws MalformedQueryException {
        queryString = queryString.toLowerCase().contains("where") ?
                queryString.substring(0, queryString.toLowerCase().indexOf("where")) : queryString;
        LinkedHashMap values = new LinkedHashMap();
        Pattern p = Pattern.compile("\\bSET\\s+(.{2,})\\b");
        Matcher m = p.matcher(queryString);
        if (!m.find()) throw new MalformedQueryException() ;
        String[] arr = m.group(1).split("\\s=|,");
        for (int i = 0; i < arr.length-1 ; i+=2) {
            values.put(arr[i].trim(),arr[i+1].trim());
        }
        return values;
    }
}
