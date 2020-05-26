package com.mastery.aplsql.exceptionhandling;

import java.util.function.Predicate;

public interface ThrowingPredicate<T, E extends Throwable > {
    boolean test(T var1);
}
