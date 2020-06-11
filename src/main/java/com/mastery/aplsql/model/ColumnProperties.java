package com.mastery.aplsql.model;

import lombok.Data;
@Data
public class ColumnProperties {
    private String name;
    private boolean AllowNulls;
    private Types dataType;

    public ColumnProperties(String name, Types datatype) {
        this.name = name;
        this.AllowNulls = true;
        dataType = datatype;
    }

    public ColumnProperties(ColumnProperties that){
        this(that.getName(),that.dataType);
        this.AllowNulls = that.AllowNulls;
    }
//    private String DefaultValue;
//    private int length;



}
