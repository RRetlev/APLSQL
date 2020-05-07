package com.mastery.aplsql.model;

import java.time.LocalDate;
import java.util.Date;

public enum Types {
    STRING(String.class) {
        @Override
        public String convert(String s) {
            return s;
        }
    },
    INTEGER(Integer.class) {
        @Override
        public Integer convert(String s) {
            return Integer.parseInt(s);
        }
    },
    BOOLEAN(Boolean.class) {
        @Override
        public Boolean convert(String s) {
            return s.equals("true");
        }
    },
    DATE(LocalDate.class) {
        @Override
        public LocalDate convert(String s) {
            return LocalDate.parse(s);
        }
    },
    DOUBLE(Double.class) {
        @Override
        public Double convert(String s) {
            return Double.parseDouble(s);
        }
    };

    public final Class<?> dataType;

    Types(Class<?> o) {
        this.dataType = o;
    }


    public abstract Object convert(String s);
}
