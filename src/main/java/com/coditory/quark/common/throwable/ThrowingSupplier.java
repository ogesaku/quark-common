package com.coditory.quark.common.throwable;

import java.util.function.Supplier;

import static com.coditory.quark.common.check.Args.checkNotNull;

@FunctionalInterface
public interface ThrowingSupplier<T> {
    T get() throws Exception;

    default T getWithSneakyThrow() {
        return Throwables.sneakyThrow(this);
    }

    default Supplier<T> sneakyThrowing() {
        return () -> Throwables.sneakyThrow(this);
    }

    static <T> Supplier<T> sneakyThrowing(ThrowingSupplier<T> supplier) {
        checkNotNull(supplier, "supplier");
        return supplier.sneakyThrowing();
    }
}
