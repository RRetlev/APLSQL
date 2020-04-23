package com.mastery.aplsql.service;

import com.mastery.aplsql.model.Column;

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
}
