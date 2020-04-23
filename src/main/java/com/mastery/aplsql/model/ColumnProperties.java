package com.mastery.aplsql.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Type;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColumnProperties {
    private String name;
    private boolean AllowNulls;
    private Class dataType;

    public ColumnProperties(String name, Class datatype) {
        this.name = name;
        this.AllowNulls = true;
        dataType = datatype;
    }
//    private String DefaultValue;
//    private int length;



}
