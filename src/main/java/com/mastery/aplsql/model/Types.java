package com.mastery.aplsql.model;

import java.time.LocalDate;

public enum Types {
    STRING(String.class) {
        @Override
        public String convert(String s) {
            return s;
        }

        @Override
        public String getDefault() {
            return "";
        }
    },
    INTEGER(Integer.class) {
        @Override
        public Integer convert(String s) {
            return Integer.parseInt(s);
        }

        @Override
        public Integer getDefault() {
            return 0;
        }
    },
    BOOLEAN(Boolean.class) {
        @Override
        public Boolean convert(String s) {
            return s.equals("true");
        }

        @Override
        public Boolean getDefault() {
            return false;
        }
    },
    DATE(LocalDate.class) {
        @Override
        public LocalDate convert(String s) {
            return LocalDate.parse(s);
        }

        @Override
        public LocalDate getDefault() {
            return LocalDate.now();
        }
    },
    DOUBLE(Double.class) {
        @Override
        public Double convert(String s) {
            return Double.parseDouble(s);
        }

        @Override
        public Double getDefault() {
            return 0.0;
        }
    };

    public final Class<?> dataType;

    Types(Class<?> o) {
        this.dataType = o;
    }


    public abstract Object convert(String s);
    public abstract Object getDefault();
}
