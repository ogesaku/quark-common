package com.coditory.quark.common.throwable;

import java.util.function.Consumer;

import static com.coditory.quark.common.check.Args.checkNotNull;

@FunctionalInterface
public interface ThrowingConsumer<T> {
    void accept(T value) throws Exception;

    default void acceptWithSneakyThrow(T value) {
        Throwables.sneakyThrow(() -> accept(value));
    }

    default Consumer<T> sneakyThrowing() {
        return (value) -> Throwables.sneakyThrow(() -> accept(value));
    }

    static <T> Consumer<T> sneakyThrowing(ThrowingConsumer<T> consumer) {
        checkNotNull(consumer, "consumer");
        return consumer.sneakyThrowing();
    }

    static <T> ThrowingConsumer<T> nop() {
        return t -> { /* nop */ };
    }
}
