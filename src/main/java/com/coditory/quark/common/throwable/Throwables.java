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

    /**
     * Executes an action and wraps checked exception in RuntimeException.
     *
     * @param action the action to to be executed
     */
    public static void sneakyThrow(ThrowingRunnable action) {
        checkNotNull(action, "action");
        try {
            action.run();
        } catch (Throwable e) {
            sneakyThrow(e);
        }
    }

    /**
     * Executes an action and maps exception using {@code throwableMapper}.
     *
     * @param action          the action to to be executed
     * @param throwableMapper maps any throwable
     */
    public static void sneakyThrow(ThrowingRunnable action, Function<Throwable, RuntimeException> throwableMapper) {
        checkNotNull(action, "action");
        try {
            action.run();
        } catch (Throwable e) {
            throw throwableMapper.apply(e);
        }
    }

    /**
     * Executes an action and wraps checked exception in RuntimeException.
     *
     * @param action the action to to be executed
     * @return the result of the action
     */
    public static <T> T sneakyThrow(ThrowingSupplier<T> action) {
        checkNotNull(action, "action");
        try {
            return action.get();
        } catch (Throwable e) {
            sneakyThrow(e);
            return null;
        }
    }

    /**
     * Executes an action and maps exception using {@code throwableMapper}.
     *
     * @param action          the action to to be executed
     * @param throwableMapper maps any throwable
     * @return the result of the action
     */
    public static <T> T sneakyThrow(ThrowingSupplier<T> action, Function<Throwable, RuntimeException> throwableMapper) {
        checkNotNull(action, "action");
        try {
            return action.get();
        } catch (Throwable e) {
            throw throwableMapper.apply(e);
        }
    }

    /**
     * Throws checked exception wrapped in RuntimeException.
     * RuntimeException are rethrows without wrapping.
     *
     * @param throwable the exception to be rethrown
     */
    public static void sneakyThrow(Throwable throwable) {
        checkNotNull(throwable, "throwable");
        if (throwable instanceof RuntimeException) {
            throw (RuntimeException) throwable;
        }
        throw new RuntimeException(throwable);
    }

    /**
     * Returns a result of an action or default value in case of an exception.
     *
     * @param action the action to to be executed
     * @return the optional with a result of the action or {@code null} in case of an exception
     */
    public static <T> T onErrorNull(Supplier<T> action) {
        checkNotNull(action, "action");
        try {
            return action.get();
        } catch (Throwable e) {
            return null;
        }
    }

    /**
     * Returns a result of an action or default value in case of an exception.
     *
     * @param action the action to to be executed
     * @return the optional with a result of the action or empty optional in case of an exception
     */
    public static <T> Optional<T> onErrorEmpty(Supplier<T> action) {
        checkNotNull(action, "action");
        return Optional.ofNullable(onErrorNull(action));
    }

    /**
     * Returns a result of an action or default value in case of an exception.
     *
     * @param action       the action to to be executed
     * @param defaultValue the value returned when action throws an exception
     * @return the result of the action or default value in case of an exception
     */
    public static <T> T onErrorDefault(Supplier<T> action, T defaultValue) {
        checkNotNull(action, "action");
        return onErrorEmpty(action)
                .orElse(defaultValue);
    }

    /**
     * Returns a result of an action or default value in case of an exception.
     *
     * @param action       the action to to be executed
     * @param defaultValue the supplier of a value returned when action throws an exception
     * @return the result of the action or default value in case of an exception
     */
    public static <T> T onErrorGet(Supplier<T> action, Supplier<T> defaultValue) {
        checkNotNull(action, "action");
        return onErrorEmpty(action)
                .orElseGet(defaultValue);
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

    /**
     * Introspects the {@code Throwable} to obtain the root cause.
     *
     * @param throwable the throwable to get the root cause for
     * @return the root cause of the {@code Throwable}
     */
    public static Throwable getRootCause(Throwable throwable) {
        checkNotNull(throwable, "throwable");
        List<Throwable> list = getCauses(throwable);
        return list.get(list.size() - 1);
    }

    /**
     * Introspects the {@code Throwable} to obtain the root cause of specific type.
     *
     * @param throwable the throwable to get the root cause for
     * @param type      the throwable type of the root cause
     * @return the root cause of the {@code Throwable}, {@code null} if cause of given type was not found
     */
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

    /**
     * Returns the list of {@code Throwable} objects in the
     * exception chain.
     *
     * @param throwable the throwable to inspect
     * @return the list of throwables, never null
     */
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

    /**
     * Gets the stack trace from a Throwable as a String.
     *
     * @param throwable the {@code Throwable} to be examined
     * @return the stack trace as generated by the exception's
     * {@code printStackTrace(PrintWriter)} method
     */
    public static String getStackTrace(Throwable throwable) {
        checkNotNull(throwable, "throwable");
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        return sw.getBuffer().toString();
    }
}
