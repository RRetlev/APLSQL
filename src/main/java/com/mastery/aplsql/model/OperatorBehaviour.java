package com.mastery.aplsql.model;

import com.mastery.aplsql.exceptionhandling.MalformedQueryException;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum OperatorBehaviour {
    EQUAL(){
        @Override
        public boolean evaluateCondition(String data, String operand){
            return data.equals(operand);
        }
    },
    GREATERTHAN(){
        @Override
        public boolean evaluateCondition(String data, String operand){
            return Integer.parseInt(data) > Integer.parseInt(operand);
        }
    },
    LESSTHAN(){
        @Override
        public boolean evaluateCondition(String data, String operand){
            return Integer.parseInt(data) < Integer.parseInt(operand);
        }
    },
    GREATEROREQUAL(){
        @Override
        public boolean evaluateCondition(String data, String operand){
            return Integer.parseInt(data) >= Integer.parseInt(operand);
        }
    },
    LESSOREQUAL(){
        @Override
        public boolean evaluateCondition(String data, String operand){
            return Integer.parseInt(data) <= Integer.parseInt(operand);
        }
    },
    NOTEQUAL(){
        @Override
        public boolean evaluateCondition(String data, String operand){
            return !data.equals( operand);
        }
    },
    BETWEEN(){
        @Override
        public boolean evaluateCondition(String data, String operand){
            //TODO Parsing the operand splitting it into two integer value
            return true;
        }
    },
    LIKE(){
        @Override
        public boolean evaluateCondition(String data, String operand){
            //TODO search for regex matcher
            return true;
        }
    },
    IN(){
        @Override
        public boolean evaluateCondition(String data, String operand) throws MalformedQueryException {
            Pattern p = Pattern.compile("\\((.*)\\)");
            Matcher m = p.matcher(operand);
            if (!m.find())throw new MalformedQueryException();
            String values = m.group(0);
            List<String> list = Arrays.asList( values.substring(1,values.length()-1).split(","));
            //TODO search a valid Regex cause this is a piece of shit
            return list.contains(data);
        }
    };

    public abstract boolean evaluateCondition(String data, String operand) throws MalformedQueryException;
}
