package com.mastery.aplsql.model;

import lombok.Data;
@Data
public class TableProperties {
    private String name;
    private String primaryKey;

    public TableProperties(String name) {
        this.name = name;
        this.primaryKey = "id";
    }

    public TableProperties(String name, String primaryKey) {
        this.name = name;
        this.primaryKey = primaryKey;
    }

    public TableProperties(TableProperties that){
        this(that.getName(),that.primaryKey);
    }
}
