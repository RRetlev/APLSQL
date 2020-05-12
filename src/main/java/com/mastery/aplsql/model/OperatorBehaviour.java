package com.mastery.aplsql.model;

public enum OperatorBehaviour {
    EQUAL(){
        @Override
        public boolean evaluateCondition(String colname, String operand){
            return true;
        }
    },
    GREATERTHAN(){
        @Override
        public boolean evaluateCondition(String colname, String operand){
            return true;
        }
    },
    LESSTHAN(){
        @Override
        public boolean evaluateCondition(String colname, String operand){
            return true;
        }
    },
    GREATEROREQUAL(){
        @Override
        public boolean evaluateCondition(String colname, String operand){
            return true;
        }
    },
    LESSOREQUAL(){
        @Override
        public boolean evaluateCondition(String colname, String operand){
            return true;
        }
    },
    NOTEQUAL(){
        @Override
        public boolean evaluateCondition(String colname, String operand){
            return true;
        }
    },
    BETWEEN(){
        @Override
        public boolean evaluateCondition(String colname, String operand){
            return true;
        }
    },
    LIKE(){
        @Override
        public boolean evaluateCondition(String colname, String operand){
            return true;
        }
    },
    IN(){
        @Override
        public boolean evaluateCondition(String colname, String operand){
            return true;
        }
    };

    public abstract boolean evaluateCondition(String colname, String operand);
}
