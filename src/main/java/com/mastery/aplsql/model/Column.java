package com.mastery.aplsql.model;

import com.mastery.aplsql.exceptionhandling.TypeMismatchException;
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

    public Column(Column that){
        this(new ArrayList<>(that.data),that.type);
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

    public T getDataAtIndex(int i){
        return data.get(i);
    }

    public void setDataAtIndex(int i, T value) {
            data.set(i,value);
    }

    public void removeDataAtIndex(int i){
        data.remove(i);
    }
}
