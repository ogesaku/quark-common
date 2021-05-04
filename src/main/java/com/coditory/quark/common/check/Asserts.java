package com.coditory.quark.common.check;

import com.coditory.quark.common.util.Strings;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.coditory.quark.common.check.Args.checkNotNegative;
import static com.coditory.quark.common.check.Args.checkNotNull;
import static com.coditory.quark.common.util.Strings.quote;
import static java.lang.String.format;

public final class Asserts {
    private Asserts() {
        throw new UnsupportedOperationException("Do not instantiate utility class");
    }

    public static void assertThat(boolean valid, String message, Object... args) {
        if (!valid) {
            throw new IllegalStateException(format(message, args));
        }
    }

    public static void assertThat(boolean valid, Supplier<String> messageSupplier) {
        if (!valid) {
            String message = messageSupplier.get();
            throw new IllegalStateException(message);
        }
    }

    public static void assertFailure(String message, Object... args) {
        throw new IllegalStateException(format(message, args));
    }

    @SafeVarargs
    public static <T> T assertAll(@Nullable T value, String name, BiFunction<T, String, T>... checks) {
        for (BiFunction<T, String, T> check : checks) {
            check.apply(value, name);
        }
        return value;
    }

    @SafeVarargs
    public static <T> T assertAll(@Nullable T value, Function<T, T>... checks) {
        for (Function<T, T> check : checks) {
            check.apply(value);
        }
        return value;
    }

    public static void assertTrue(boolean value, String name) {
        if (!value) {
            String field = name == null ? "value" : name;
            String message = message(field + " == true", false);
            throw new IllegalStateException(message);
        }
    }

    public static void assertFalse(boolean value, String name) {
        if (value) {
            String field = name == null ? "value" : name;
            String message = message(field + " == false", true);
            throw new IllegalStateException(message);
        }
    }

    public static int assertPositionIndex(int index, int size) {
        return assertPositionIndex(index, size, null);
    }

    public static int assertPositionIndex(int index, int size, String indexName) {
        checkNotNegative(size, "size");
        if (index < 0 || index >= size) {
            throw new IllegalStateException(badPositionIndex(index, size, indexName));
        }
        return index;
    }

    private static String badPositionIndex(int index, int size, String indexName) {
        String name = indexName == null ? "index" : indexName;
        if (index < 0) {
            return String.format("%s (%s) must not be negative", name, index);
        } else {
            return String.format("%s (%s) must not be greater than size (%s)", name, index, size);
        }
    }

    public static <T> T assertNotNull(@Nullable T value) {
        return assertNotNull(value, null);
    }

    public static <T> T assertNotNull(@Nullable T value, String name) {
        if (value == null) {
            String field = name == null ? "value" : name;
            String message = message(field + " != null", null);
            throw new IllegalStateException(message);
        }
        return value;
    }

    public static String assertNotBlank(@Nullable String value) {
        return assertNotBlank(value, null);
    }

    public static String assertNotBlank(@Nullable String value, String name) {
        if (value == null || value.trim().isEmpty()) {
            String field = name == null ? "string" : name;
            String message = message("non-blank " + field, value);
            throw new IllegalStateException(message);
        }
        return value;
    }

    public static String assertNotContains(@Nullable String value, char c) {
        return assertNotContains(value, c, null);
    }

    public static String assertNotContains(@Nullable String value, char c, String name) {
        return assertNotContains(value, "" + c, null);
    }

    public static String assertNotContains(@Nullable String value, String c) {
        return assertNotContains(value, "" + c, null);
    }

    public static String assertNotContains(@Nullable String value, String chunk, String name) {
        checkNotNull(value, "value");
        checkNotNull(chunk, "chunk");
        if (value.contains(chunk)) {
            String field = name == null ? "string" : name;
            String message = message(field + " to not contain: " + quote(chunk), value);
            throw new IllegalStateException(message);
        }
        return value;
    }

    public static String assertNotContainsAnyChar(@Nullable String value, String chars) {
        return assertNotContainsAnyChar(value, chars, null);
    }

    public static String assertNotContainsAnyChar(@Nullable String value, String chars, String name) {
        checkNotNull(value, "value");
        checkNotNull(chars, "chars");
        if (Strings.containsAnyChar(value, chars)) {
            String field = name == null ? "string" : name;
            String message = message(field + " to not contain any character from: " + quote(chars), value);
            throw new IllegalStateException(message);
        }
        return value;
    }

    public static <E, C extends Collection<E>> C assertNotEmpty(@Nullable C collection) {
        return assertNotEmpty(collection, null);
    }

    public static <E, C extends Collection<E>> C assertNotEmpty(@Nullable C value, String name) {
        if (value == null || value.isEmpty()) {
            String field = name == null ? "collection" : name;
            String message = message("non-empty " + field, value);
            throw new IllegalStateException(message);
        }
        return value;
    }

    public static <K, V, M extends Map<K, V>> M assertNotEmpty(@Nullable M value) {
        return assertNotEmpty(value, null);
    }

    public static <K, V, M extends Map<K, V>> M assertNotEmpty(@Nullable M value, String name) {
        if (value == null || value.isEmpty()) {
            String field = name == null ? "map" : name;
            String message = message("non-empty " + field, value);
            throw new IllegalStateException(message);
        }
        return value;
    }

    public static boolean[] assertNotEmpty(boolean[] array) {
        return assertNotEmpty(array, null);
    }

    public static boolean[] assertNotEmpty(boolean[] array, String name) {
        if (array == null || array.length == 0) {
            String field = name == null ? "array" : name;
            String message = message("non-empty " + field, array);
            throw new IllegalStateException(message);
        }
        return array;
    }

    public static byte[] assertNotEmpty(byte[] array) {
        return assertNotEmpty(array, null);
    }

    public static byte[] assertNotEmpty(byte[] array, String name) {
        if (array == null || array.length == 0) {
            String field = name == null ? "array" : name;
            String message = message("non-empty " + field, array);
            throw new IllegalStateException(message);
        }
        return array;
    }

    public static char[] assertNotEmpty(char[] array) {
        return assertNotEmpty(array, null);
    }

    public static char[] assertNotEmpty(char[] array, String name) {
        if (array == null || array.length == 0) {
            String field = name == null ? "array" : name;
            String message = message("non-empty " + field, array);
            throw new IllegalStateException(message);
        }
        return array;
    }

    public static double[] assertNotEmpty(double[] array) {
        return assertNotEmpty(array, null);
    }

    public static double[] assertNotEmpty(double[] array, String name) {
        if (array == null || array.length == 0) {
            String field = name == null ? "array" : name;
            String message = message("non-empty " + field, array);
            throw new IllegalStateException(message);
        }
        return array;
    }

    public static float[] assertNotEmpty(float[] array) {
        return assertNotEmpty(array, null);
    }

    public static float[] assertNotEmpty(float[] array, String name) {
        if (array == null || array.length == 0) {
            String field = name == null ? "array" : name;
            String message = message("non-empty " + field, array);
            throw new IllegalStateException(message);
        }
        return array;
    }

    public static int[] assertNotEmpty(int[] array) {
        return assertNotEmpty(array, null);
    }

    public static int[] assertNotEmpty(int[] array, String name) {
        if (array == null || array.length == 0) {
            String field = name == null ? "array" : name;
            String message = message("non-empty " + field, array);
            throw new IllegalStateException(message);
        }
        return array;
    }

    public static long[] assertNotEmpty(long[] array) {
        return assertNotEmpty(array, null);
    }

    public static long[] assertNotEmpty(long[] array, String name) {
        if (array == null || array.length == 0) {
            String field = name == null ? "array" : name;
            String message = message("non-empty " + field, array);
            throw new IllegalStateException(message);
        }
        return array;
    }

    public static short[] assertNotEmpty(short[] array) {
        return assertNotEmpty(array, null);
    }

    public static short[] assertNotEmpty(short[] array, String name) {
        if (array == null || array.length == 0) {
            String field = name == null ? "array" : name;
            String message = message("non-empty " + field, array);
            throw new IllegalStateException(message);
        }
        return array;
    }

    public static <T> T[] assertNotEmpty(@Nullable T[] array) {
        return assertNotEmpty(array, null);
    }

    public static <T> T[] assertNotEmpty(@Nullable T[] array, String name) {
        if (array == null || array.length == 0) {
            String field = name == null ? "array" : name;
            String message = message("non-empty " + field, array);
            throw new IllegalStateException(message);
        }
        return array;
    }

    public static byte assertPositive(byte value) {
        return assertPositive(value, null);
    }

    public static short assertPositive(short value) {
        return assertPositive(value, null);
    }

    public static long assertPositive(long value) {
        return assertPositive(value, null);
    }

    public static float assertPositive(float value) {
        return assertPositive(value, null);
    }

    public static double assertPositive(double value) {
        return assertPositive(value, null);
    }

    public static int assertPositive(int value) {
        return assertPositive(value, null);
    }

    public static byte assertPositive(byte value, String name) {
        if (value <= 0) {
            String field = name == null ? "byte value" : name;
            String message = message(field + " > 0", value);
            throw new IllegalStateException(message);
        }
        return value;
    }

    public static short assertPositive(short value, String name) {
        if (value <= 0) {
            String field = name == null ? "short value" : name;
            String message = message(field + " > 0", value);
            throw new IllegalStateException(message);
        }
        return value;
    }

    public static float assertPositive(float value, String name) {
        if (value <= 0) {
            String field = name == null ? "float value" : name;
            String message = message(field + " > 0", value);
            throw new IllegalStateException(message);
        }
        return value;
    }

    public static double assertPositive(double value, String name) {
        if (value <= 0) {
            String field = name == null ? "double value" : name;
            String message = message(field + " > 0", value);
            throw new IllegalStateException(message);
        }
        return value;
    }

    public static int assertPositive(int value, String name) {
        if (value <= 0) {
            String field = name == null ? "int value" : name;
            String message = message(field + " > 0", value);
            throw new IllegalStateException(message);
        }
        return value;
    }

    public static long assertPositive(long value, String name) {
        if (value <= 0) {
            String field = name == null ? "long value" : name;
            String message = message(field + " > 0", value);
            throw new IllegalStateException(message);
        }
        return value;
    }

    public static byte assertNegative(byte value) {
        return assertNegative(value, null);
    }

    public static short assertNegative(short value) {
        return assertNegative(value, null);
    }

    public static float assertNegative(float value) {
        return assertNegative(value, null);
    }

    public static double assertNegative(double value) {
        return assertNegative(value, null);
    }

    public static int assertNegative(int value) {
        return assertNegative(value, null);
    }

    public static long assertNegative(long value) {
        return assertNegative(value, null);
    }

    public static byte assertNegative(byte value, String name) {
        if (value >= 0) {
            String field = name == null ? "byte value" : name;
            String message = message(field + " < 0", value);
            throw new IllegalStateException(message);
        }
        return value;
    }

    public static short assertNegative(short value, String name) {
        if (value >= 0) {
            String field = name == null ? "short value" : name;
            String message = message(field + " < 0", value);
            throw new IllegalStateException(message);
        }
        return value;
    }

    public static float assertNegative(float value, String name) {
        if (value >= 0) {
            String field = name == null ? "float value" : name;
            String message = message(field + " < 0", value);
            throw new IllegalStateException(message);
        }
        return value;
    }

    public static double assertNegative(double value, String name) {
        if (value >= 0) {
            String field = name == null ? "double value" : name;
            String message = message(field + " < 0", value);
            throw new IllegalStateException(message);
        }
        return value;
    }

    public static int assertNegative(int value, String name) {
        if (value >= 0) {
            String field = name == null ? "int value" : name;
            String message = message(field + " < 0", value);
            throw new IllegalStateException(message);
        }
        return value;
    }

    public static long assertNegative(long value, String name) {
        if (value >= 0) {
            String field = name == null ? "long value" : name;
            String message = message(field + " < 0", value);
            throw new IllegalStateException(message);
        }
        return value;
    }

    public static byte assertNotNegative(byte value) {
        return assertNotNegative(value, null);
    }

    public static short assertNotNegative(short value) {
        return assertNotNegative(value, null);
    }

    public static float assertNotNegative(float value) {
        return assertNotNegative(value, null);
    }

    public static double assertNotNegative(double value) {
        return assertNotNegative(value, null);
    }

    public static int assertNotNegative(int value) {
        return assertNotNegative(value, null);
    }

    public static long assertNotNegative(long value) {
        return assertNotNegative(value, null);
    }

    public static int assertNotNegative(int value, String name) {
        if (value < 0) {
            String field = name == null ? "int value" : name;
            String message = message(field + " >= 0", value);
            throw new IllegalStateException(message);
        }
        return value;
    }

    public static byte assertNotNegative(byte value, String name) {
        if (value < 0) {
            String field = name == null ? "byte value" : name;
            String message = message(field + " >= 0", value);
            throw new IllegalStateException(message);
        }
        return value;
    }

    public static short assertNotNegative(short value, String name) {
        if (value < 0) {
            String field = name == null ? "short value" : name;
            String message = message(field + " >= 0", value);
            throw new IllegalStateException(message);
        }
        return value;
    }

    public static float assertNotNegative(float value, String name) {
        if (value < 0) {
            String field = name == null ? "float value" : name;
            String message = message(field + " >= 0", value);
            throw new IllegalStateException(message);
        }
        return value;
    }

    public static double assertNotNegative(double value, String name) {
        if (value < 0) {
            String field = name == null ? "double value" : name;
            String message = message(field + " >= 0", value);
            throw new IllegalStateException(message);
        }
        return value;
    }

    public static long assertNotNegative(long value, String name) {
        if (value < 0) {
            String field = name == null ? "long value" : name;
            String message = message(field + " >= 0", value);
            throw new IllegalStateException(message);
        }
        return value;
    }

    public static byte assertNotPositive(byte value) {
        return assertNotPositive(value, null);
    }

    public static short assertNotPositive(short value) {
        return assertNotPositive(value, null);
    }

    public static float assertNotPositive(float value) {
        return assertNotPositive(value, null);
    }

    public static double assertNotPositive(double value) {
        return assertNotPositive(value, null);
    }

    public static int assertNotPositive(int value) {
        return assertNotPositive(value, null);
    }

    public static long assertNotPositive(long value) {
        return assertNotPositive(value, null);
    }

    public static int assertNotPositive(int value, String name) {
        if (value > 0) {
            String field = name == null ? "int value" : name;
            String message = message(field + " <= 0", value);
            throw new IllegalStateException(message);
        }
        return value;
    }

    public static byte assertNotPositive(byte value, String name) {
        if (value > 0) {
            String field = name == null ? "byte value" : name;
            String message = message(field + " <= 0", value);
            throw new IllegalStateException(message);
        }
        return value;
    }

    public static short assertNotPositive(short value, String name) {
        if (value > 0) {
            String field = name == null ? "short value" : name;
            String message = message(field + " <= 0", value);
            throw new IllegalStateException(message);
        }
        return value;
    }

    public static float assertNotPositive(float value, String name) {
        if (value > 0) {
            String field = name == null ? "float value" : name;
            String message = message(field + " <= 0", value);
            throw new IllegalStateException(message);
        }
        return value;
    }

    public static double assertNotPositive(double value, String name) {
        if (value > 0) {
            String field = name == null ? "double value" : name;
            String message = message(field + " <= 0", value);
            throw new IllegalStateException(message);
        }
        return value;
    }

    public static long assertNotPositive(long value, String name) {
        if (value > 0) {
            String field = name == null ? "long value" : name;
            String message = message(field + " <= 0", value);
            throw new IllegalStateException(message);
        }
        return value;
    }

    private static String message(String expectation, Object value) {
        String stringValue = value instanceof String
                ? ("\"" + value + "\"")
                : Objects.toString(value);
        return "Expected " + expectation + ". Got: " + stringValue;
    }
}
