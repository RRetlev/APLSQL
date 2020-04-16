package com.mastery.aplsql.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
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
}
