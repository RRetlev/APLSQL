package com.mastery.aplsql.model;

public enum OperatorBehaviour {
    EQUAL(){
        @Override
        public boolean evaluateCondition(String data, String operand){
            return data.equals(operand);
        }
    },
    GREATERTHAN(){
        @Override
        public boolean evaluateCondition(String data, String operand){
            return Integer.parseInt(data) > Integer.parseInt(operand);
        }
    },
    LESSTHAN(){
        @Override
        public boolean evaluateCondition(String data, String operand){
            return Integer.parseInt(data) < Integer.parseInt(operand);
        }
    },
    GREATEROREQUAL(){
        @Override
        public boolean evaluateCondition(String data, String operand){
            return Integer.parseInt(data) >= Integer.parseInt(operand);
        }
    },
    LESSOREQUAL(){
        @Override
        public boolean evaluateCondition(String data, String operand){
            return Integer.parseInt(data) <= Integer.parseInt(operand);
        }
    },
    NOTEQUAL(){
        @Override
        public boolean evaluateCondition(String data, String operand){
            return !data.equals( operand);
        }
    },
    BETWEEN(){
        @Override
        public boolean evaluateCondition(String data, String operand){

            return true;
        }
    },
    LIKE(){
        @Override
        public boolean evaluateCondition(String data, String operand){
            return true;
        }
    },
    IN(){
        @Override
        public boolean evaluateCondition(String data, String operand){
            return true;
        }
    };

    public abstract boolean evaluateCondition(String data, String operand);
}
