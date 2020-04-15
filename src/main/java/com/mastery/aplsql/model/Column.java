package com.mastery.aplsql.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@Data
@AllArgsConstructor
public class Column {
    private List<Object> data;


    public Column() {
        this.data = new ArrayList<>();
    }
}
