package com.coditory.quark.common.throwable;

import java.util.function.Consumer;

import static com.coditory.quark.common.check.Args.checkNotNull;

@FunctionalInterface
public interface ThrowingConsumer<T> {
    void accept(T value) throws Exception;

    default void acceptWithSneakyThrow(T value) {
        Throwables.sneakyThrow(() -> accept(value));
    }

    default Consumer<T> toConsumer() {
        return (value) -> Throwables.sneakyThrow(() -> accept(value));
    }

    static <T> Consumer<T> sneakyConsumer(ThrowingConsumer<T> consumer) {
        checkNotNull(consumer, "consumer");
        return consumer.toConsumer();
    }

    static <T> ThrowingConsumer<T> throwingConsumer(Consumer<T> consumer) {
        checkNotNull(consumer, "consumer");
        return consumer::accept;
    }

    static <T> ThrowingConsumer<T> nop() {
        return t -> { /* nop */ };
    }
}
