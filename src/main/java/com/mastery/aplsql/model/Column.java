package com.mastery.aplsql.model;

import com.mastery.aplsql.exceptions.TypeMismatchException;
import com.mastery.aplsql.service.Util;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class Column<T> {
    private ArrayList<T> data = new ArrayList<>();
    private final Class<T> type;

    public Column(Class type) {
        this.type = type;
    }

    public void addDataToColumn(T t) throws TypeMismatchException {
        if (t.getClass() == type) {
            data.add(t);
        } else {
            throw new TypeMismatchException();
        }
    }

    public List<T> getData() {
        return data;
    }
}
