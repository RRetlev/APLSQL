package com.mastery.aplsql.model;

import java.util.Date;

public enum Types{
    STRING(String.class),
    INTEGER(Integer.class),
    BOOLEAN(Boolean.class),
    DATE(Date.class),
    DOUBLE(Double.class);

    public final Class<?> dataType;

    Types(Class<?> o) {
        this.dataType=o;
    }
}
