package com.mastery.aplsql;

import com.mastery.aplsql.exceptionhandling.TypeMismatchException;
import com.mastery.aplsql.model.OperatorBehaviour;
import com.mastery.aplsql.model.Types;
import com.mastery.aplsql.service.Util;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.stream.Stream;

@SpringBootTest
public class UtilTests {

    @ParameterizedTest
    @MethodSource("provideStringsAndDataTypes")
    void correctDataTypeIsReturned(String s, Types expectedType) {
        Assertions.assertEquals(expectedType, Util.getDataTypeFromString(s));
    }

    @ParameterizedTest
    @MethodSource("provideStringsAndOperators")
    void correctOperationSelected(String s, OperatorBehaviour expectedOperation) {
        Assertions.assertEquals(expectedOperation, Util.decideOperation(s));
    }

    @Test
    void decideOperationThrows() {
        Assertions.assertThrows(IllegalStateException.class, ()-> Util.decideOperation("no-op"));
    }

    @ParameterizedTest
    @MethodSource("provideStringsTypesAndValues")
    void typesConvertProperly(String s, Types type, Object expectedValue) throws TypeMismatchException {
        Assertions.assertEquals(expectedValue, type.convert(s));
    }

    private static Stream<Arguments> provideStringsAndDataTypes() {
        return Stream.of(
          Arguments.of("int", Types.INTEGER),
          Arguments.of("double", Types.DOUBLE),
          Arguments.of("boolean", Types.BOOLEAN),
          Arguments.of("date", Types.DATE),
          Arguments.of("string", Types.STRING)
        );
    }

    private static Stream<Arguments> provideStringsAndOperators() {
        return Stream.of(
                Arguments.of("=", OperatorBehaviour.EQUAL),
                Arguments.of(">", OperatorBehaviour.GREATERTHAN),
                Arguments.of("<", OperatorBehaviour.LESSTHAN),
                Arguments.of(">=", OperatorBehaviour.GREATEROREQUAL),
                Arguments.of("<=", OperatorBehaviour.LESSOREQUAL),
                Arguments.of("<>", OperatorBehaviour.NOTEQUAL),
                Arguments.of("!=", OperatorBehaviour.NOTEQUAL),
                Arguments.of("IN", OperatorBehaviour.IN),
                Arguments.of("BETWEEN", OperatorBehaviour.BETWEEN),
                Arguments.of("LIKE", OperatorBehaviour.LIKE)
        );
    }

    private static Stream<Arguments> provideStringsTypesAndValues() {
        return Stream.of(
                Arguments.of("5", Types.INTEGER, 5),
                Arguments.of("3.14", Types.DOUBLE, 3.14),
                Arguments.of("true", Types.BOOLEAN, true),
                Arguments.of("2020-05-01", Types.DATE, LocalDate.of(2020,5,1)),
                Arguments.of("text", Types.STRING, "text")
        );
    }

}
