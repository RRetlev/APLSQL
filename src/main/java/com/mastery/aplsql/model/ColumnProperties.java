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
    private Types dataType;

    public ColumnProperties(String name, Types datatype) {
        this.name = name;
        this.AllowNulls = true;
        dataType = datatype;
    }
//    private String DefaultValue;
//    private int length;



}
