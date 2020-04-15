package com.mastery.aplsql.model;

import lombok.Data;

import java.util.*;

@Data
public class Table {
    private HashMap<ColumnProperties, Column> columns;

    public Table() {
      this.columns = new HashMap<>();
    }
}
