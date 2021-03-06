package com.mastery.aplsql.exceptionhandling;

import java.util.function.IntPredicate;

public interface ThrowingPredicate<T, E extends Throwable > {
    boolean test(int t) throws E;

    static <T,E extends Throwable> IntPredicate isEqual(ThrowingPredicate<T,E> predicate) {
        return (t)-> {
            try {
                return predicate.test(t) ;
            }catch (Throwable e){
                throw new RuntimeException(e);
            }

        };
    }
}
