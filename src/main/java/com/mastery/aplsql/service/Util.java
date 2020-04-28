package com.mastery.aplsql.service;

import com.mastery.aplsql.model.Column;
import com.mastery.aplsql.model.Types;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Util {
    public static boolean containsName(Set<String> set,String name){
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
}
