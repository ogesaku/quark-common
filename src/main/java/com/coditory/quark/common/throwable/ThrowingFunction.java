package com.coditory.quark.common.throwable;

import java.util.function.Function;

import static com.coditory.quark.common.check.Args.checkNotNull;

@FunctionalInterface
public interface ThrowingFunction<T, R> {
    R apply(T value) throws Exception;

    default R applyWithSneakyThrow(T value) {
        return Throwables.sneakyThrow(() -> apply(value));
    }

    default Function<T, R> toFunction() {
        return (T value) -> Throwables.sneakyThrow(() -> this.apply(value));
    }

    default <V> ThrowingFunction<T, V> andThen(ThrowingFunction<? super R, ? extends V> after) {
        checkNotNull(after, "after");
        return (T t) -> {
            R result = apply(t);
            return after.apply(result);
        };
    }

    static <T, R> Function<T, R> sneakyFunction(ThrowingFunction<T, R> function) {
        checkNotNull(function, "function");
        return function.toFunction();
    }

    static <T, R> ThrowingFunction<T, R> throwingFunction(Function<T, R> function) {
        checkNotNull(function, "function");
        return function::apply;
    }

    static <T> ThrowingFunction<T, T> identity() {
        return (t) -> t;
    }

    static <T, R> ThrowingFunction<T, R> nop() {
        return t -> null;
    }
}
