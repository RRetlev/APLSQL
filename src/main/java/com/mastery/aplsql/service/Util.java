package com.mastery.aplsql.service;

import com.mastery.aplsql.model.Column;
import com.mastery.aplsql.model.OperatorBehaviour;
import com.mastery.aplsql.model.Types;

import java.util.List;

public class Util {
    public static boolean containsName(List<String> set,String name){
        return set.contains(name);
    }

    public static <T> Column<T> createColumn(Class<T> tClass){
        return new Column<T>(tClass);
    }
    public static Types getDataTypeFromString(String s){
        Types type;
        switch (s){
            case "int":
                type = Types.INTEGER;
                break;
            case"double":
                type = Types.DOUBLE;
                break;
            case"boolean":
                type = Types.BOOLEAN;
                break;
            case"date":
                type = Types.DATE;
                break;
            default:
                type = Types.STRING;
                break;
        }
        return type;
    }

    public static OperatorBehaviour decideOperation(String operator){
        OperatorBehaviour behaviour;
        switch (operator){
            case "=":
                behaviour = OperatorBehaviour.EQUAL;
                break;
            case ">":
                behaviour = OperatorBehaviour.GREATERTHAN;
                break;
            case "<":
                behaviour = OperatorBehaviour.LESSTHAN;
                break;
            case ">=":
                behaviour = OperatorBehaviour.GREATEROREQUAL;
                break;
            case "<=":
                behaviour = OperatorBehaviour.LESSOREQUAL;
                break;
            case "<>":
            case "!=":
                behaviour = OperatorBehaviour.NOTEQUAL;
                break;
            case "BETWEEN":
                behaviour = OperatorBehaviour.BETWEEN;
                break;
            case "LIKE":
                behaviour = OperatorBehaviour.LIKE;
                break;
            case "IN":
                behaviour = OperatorBehaviour.IN;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + operator);
        }
        return behaviour;
    }
    //TODO lambda switchcase
}
