package com.coditory.quark.common.throwable;

import static com.coditory.quark.common.check.Args.checkNotNull;

@FunctionalInterface
public interface ThrowingRunnable {
    void run() throws Exception;

    default void runWithSneakyThrow() {
        Throwables.sneakyThrow(this);
    }

    default Runnable sneakyThrowing() {
        return () -> Throwables.sneakyThrow(this);
    }

    default ThrowingRunnable andThen(ThrowingRunnable after) {
        checkNotNull(after, "after");
        return () -> {
            run();
            after.run();
        };
    }

    static Runnable sneakyThrowing(ThrowingRunnable runnable) {
        checkNotNull(runnable, "runnable");
        return runnable.sneakyThrowing();
    }

    static <E extends Throwable> ThrowingRunnable nop() {
        return () -> { /* nop */ };
    }
}
