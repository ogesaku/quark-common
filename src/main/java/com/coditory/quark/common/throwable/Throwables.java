package com.coditory.quark.common.throwable;

import org.jetbrains.annotations.Nullable;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.coditory.quark.common.check.Args.checkNotNull;

public final class Throwables {
    private Throwables() {
        throw new UnsupportedOperationException("Do not instantiate utility class");
    }

    public static void sneakyThrow(ThrowingRunnable action) {
        checkNotNull(action, "action");
        try {
            action.run();
        } catch (Throwable e) {
            sneakyThrow(e);
        }
    }

    public static void sneakyThrow(ThrowingRunnable action, Function<Throwable, RuntimeException> throwableMapper) {
        checkNotNull(action, "action");
        try {
            action.run();
        } catch (Throwable e) {
            throw throwableMapper.apply(e);
        }
    }

    public static <T> T sneakyThrow(ThrowingSupplier<T> action) {
        checkNotNull(action, "action");
        try {
            return action.get();
        } catch (Throwable e) {
            sneakyThrow(e);
            return null;
        }
    }

    public static <T> T sneakyThrow(ThrowingSupplier<T> action, Function<Throwable, RuntimeException> throwableMapper) {
        checkNotNull(action, "action");
        try {
            return action.get();
        } catch (Throwable e) {
            throw throwableMapper.apply(e);
        }
    }

    public static void sneakyThrow(Throwable throwable) {
        checkNotNull(throwable, "throwable");
        if (throwable instanceof RuntimeException) {
            throw (RuntimeException) throwable;
        }
        throw new RuntimeException(throwable);
    }

    @Nullable
    public static <T> T onErrorNull(ThrowingSupplier<T> action) {
        checkNotNull(action, "action");
        try {
            return action.get();
        } catch (Throwable e) {
            return null;
        }
    }

    public static <T> Optional<T> onErrorEmpty(ThrowingSupplier<T> action) {
        checkNotNull(action, "action");
        return Optional.ofNullable(onErrorNull(action));
    }

    public static <T> T onErrorDefault(ThrowingSupplier<T> action, T defaultValue) {
        checkNotNull(action, "action");
        return onErrorEmpty(action)
                .orElse(defaultValue);
    }

    public static <T> T onErrorGet(ThrowingSupplier<T> action, ThrowingSupplier<T> defaultValue) {
        checkNotNull(action, "action");
        return onErrorEmpty(action)
                .orElseGet(defaultValue.sneakyThrowing());
    }


    public static void throwCause(Throwable throwable) {
        Throwable cause = throwable.getCause();
        if (cause instanceof RuntimeException) {
            throw (RuntimeException) cause;
        }
        if (cause instanceof Error) {
            throw (Error) cause;
        }
    }

    public static Throwable getRootCause(Throwable throwable) {
        checkNotNull(throwable, "throwable");
        List<Throwable> list = getCauses(throwable);
        return list.get(list.size() - 1);
    }

    @Nullable
    @SuppressWarnings("unchecked")
    public static <T extends Throwable> T getRootCauseOfType(Throwable throwable, Class<T> type) {
        checkNotNull(throwable, "throwable");
        checkNotNull(type, "type");
        List<Throwable> list = getCauses(throwable);
        Collections.reverse(list);
        Throwable result = list.stream()
                .filter(type::isInstance)
                .findFirst()
                .orElse(null);
        return (T) result;
    }

    public static List<Throwable> getCauses(Throwable throwable) {
        checkNotNull(throwable, "throwable");
        List<Throwable> list = new ArrayList<>();
        Set<Throwable> visited = new HashSet<>();
        while (throwable != null && !visited.contains(throwable)) {
            list.add(throwable);
            visited.add(throwable);
            throwable = throwable.getCause();
        }
        return list;
    }

    public static String getStackTrace(Throwable throwable) {
        checkNotNull(throwable, "throwable");
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        return sw.getBuffer().toString();
    }
}
