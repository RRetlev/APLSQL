package com.mastery.aplsql.service;

import com.mastery.aplsql.exceptionhandling.MalformedQueryException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateQueryStringParser extends QueryStringParser {

    public static HashMap<String, String> getColumnSpecs(String queryString) throws MalformedQueryException {
        Pattern p = Pattern.compile("\\((.*)\\)");
        Matcher m = p.matcher(queryString);
        if (!m.find())throw new MalformedQueryException();
        String parameters = m.group(1);
        HashMap<String, String> paramMap = new HashMap<>();
        Arrays.stream(parameters.split(",\\s"))
                .map(param -> param.split("\\s"))
                .forEach(paramArr -> paramMap.put(paramArr[0], paramArr[1]));
        return paramMap;
    }

}
