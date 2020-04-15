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

//    private Type Datatype;
//    private String DefaultValue;
//    private int length;



}
