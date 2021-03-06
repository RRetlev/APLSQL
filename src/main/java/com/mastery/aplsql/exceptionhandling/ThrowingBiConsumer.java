package com.mastery.aplsql.exceptionhandling;

import java.util.function.BiConsumer;

public interface ThrowingBiConsumer<T, U, E extends Throwable> {
    void accept(T t, U u) throws E;

    static <T, U, E extends Throwable> BiConsumer<T, U> unchecked(ThrowingBiConsumer<T, U, E> consumer) {
        return (t, u) -> {
            try {
                consumer.accept(t, u);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        };
    }
}
