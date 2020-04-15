package com.mastery.aplsql.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class Column {
    private List<Object> data;


    public Column() {
        this.data = new ArrayList<>();
    }

    public void addDataToColumn(Object d){
        data.add(d);
    }
}
