package com.coditory.quark.common.check;

import com.coditory.quark.common.util.Strings;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.coditory.quark.common.util.Strings.quote;
import static java.lang.String.format;

public final class Args {
    private Args() {
        throw new UnsupportedOperationException("Do not instantiate utility class");
    }

    public static void check(boolean valid) {
        if (!valid) {
            throw new IllegalArgumentException();
        }
    }

    public static void check(boolean valid, String message, Object... args) {
        if (!valid) {
            throw new IllegalArgumentException(format(message, args));
        }
    }

    public static void check(boolean valid, Supplier<String> messageSupplier) {
        if (!valid) {
            String message = messageSupplier.get();
            throw new IllegalArgumentException(message);
        }
    }

    public static int checkPositionIndex(int index, int size) {
        return checkPositionIndex(index, size, null);
    }

    public static int checkPositionIndex(int index, int size, String indexName) {
        Args.checkNotNegative(size, "size");
        if (index < 0 || index >= size) {
            throw new IllegalArgumentException(badPositionIndex(index, size, indexName));
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

    @SafeVarargs
    public static <T> T checkAll(@Nullable T value, String name, BiFunction<T, String, T>... checks) {
        for (BiFunction<T, String, T> check : checks) {
            check.apply(value, name);
        }
        return value;
    }

    @SafeVarargs
    public static <T> T checkAll(@Nullable T value, Function<T, T>... checks) {
        for (Function<T, T> expect : checks) {
            expect.apply(value);
        }
        return value;
    }

    public static <T> T checkNotNull(@Nullable T value) {
        return checkNotNull(value, null);
    }

    public static <T> T checkNotNull(@Nullable T value, String name) {
        if (value == null) {
            String field = name == null ? "value" : name;
            String message = message(field + " != null", null);
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    public static String checkNotBlank(@Nullable String value) {
        return checkNotBlank(value, null);
    }

    public static String checkNotBlank(@Nullable String value, String name) {
        if (value == null || value.trim().isEmpty()) {
            String field = name == null ? "string" : name;
            String message = message("non-blank " + field, value);
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    public static String checkNoBlanks(@Nullable String value) {
        return checkNoBlanks(value, null);
    }

    public static String checkNoBlanks(@Nullable String value, String name) {
        checkNotEmpty(value, name);
        if (Strings.containsWhitespaces(value)) {
            String field = name == null ? "string" : name;
            String message = message(field + " without whitespaces", value);
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    public static String checkNotContains(@Nullable String value, char c) {
        return checkNotContains(value, c, null);
    }

    public static String checkNotContains(@Nullable String value, char c, String name) {
        return checkNotContains(value, "" + c, null);
    }

    public static String checkNotContains(@Nullable String value, String c) {
        return checkNotContains(value, "" + c, null);
    }

    public static String checkNotContains(@Nullable String value, String chunk, String name) {
        checkNotNull(value, "value");
        checkNotNull(chunk, "chunk");
        if (value.contains(chunk)) {
            String field = name == null ? "string" : name;
            String message = message(field + " to not contain: " + quote(chunk), value);
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    public static String checkNotContainsAnyChar(@Nullable String value, String chars) {
        return checkNotContainsAnyChar(value, chars, null);
    }

    public static String checkNotContainsAnyChar(@Nullable String value, String chars, String name) {
        checkNotNull(value, "value");
        checkNotNull(chars, "chars");
        if (Strings.containsAnyChar(value, chars)) {
            String field = name == null ? "string" : name;
            String message = message(field + " to not contain any character from: " + quote(chars), value);
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    public static String checkNotEmpty(@Nullable String value) {
        return checkNotEmpty(value, null);
    }

    public static String checkNotEmpty(@Nullable String value, String name) {
        if (value == null || value.isEmpty()) {
            String field = name == null ? "string" : name;
            String message = message("non-empty " + field, value);
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    public static <E, C extends Collection<E>> C checkNotEmpty(@Nullable C collection) {
        return checkNotEmpty(collection, null);
    }

    public static <E, C extends Collection<E>> C checkNotEmpty(@Nullable C value, String name) {
        if (value == null || value.isEmpty()) {
            String field = name == null ? "collection" : name;
            String message = message("non-empty " + field, value);
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    public static <K, V, M extends Map<K, V>> M checkNotEmpty(@Nullable M value) {
        return checkNotEmpty(value, null);
    }

    public static <K, V, M extends Map<K, V>> M checkNotEmpty(@Nullable M value, String name) {
        if (value == null || value.isEmpty()) {
            String field = name == null ? "map" : name;
            String message = message("non-empty " + field, value);
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    public static boolean[] checkNotEmpty(boolean[] array) {
        return checkNotEmpty(array, null);
    }

    public static boolean[] checkNotEmpty(boolean[] array, String name) {
        if (array == null || array.length == 0) {
            String field = name == null ? "array" : name;
            String message = message("non-empty " + field, array);
            throw new IllegalArgumentException(message);
        }
        return array;
    }

    public static byte[] checkNotEmpty(byte[] array) {
        return checkNotEmpty(array, null);
    }

    public static byte[] checkNotEmpty(byte[] array, String name) {
        if (array == null || array.length == 0) {
            String field = name == null ? "array" : name;
            String message = message("non-empty " + field, array);
            throw new IllegalArgumentException(message);
        }
        return array;
    }

    public static char[] checkNotEmpty(char[] array) {
        return checkNotEmpty(array, null);
    }

    public static char[] checkNotEmpty(char[] array, String name) {
        if (array == null || array.length == 0) {
            String field = name == null ? "array" : name;
            String message = message("non-empty " + field, array);
            throw new IllegalArgumentException(message);
        }
        return array;
    }

    public static double[] checkNotEmpty(double[] array) {
        return checkNotEmpty(array, null);
    }

    public static double[] checkNotEmpty(double[] array, String name) {
        if (array == null || array.length == 0) {
            String field = name == null ? "array" : name;
            String message = message("non-empty " + field, array);
            throw new IllegalArgumentException(message);
        }
        return array;
    }

    public static float[] checkNotEmpty(float[] array) {
        return checkNotEmpty(array, null);
    }

    public static float[] checkNotEmpty(float[] array, String name) {
        if (array == null || array.length == 0) {
            String field = name == null ? "array" : name;
            String message = message("non-empty " + field, array);
            throw new IllegalArgumentException(message);
        }
        return array;
    }

    public static int[] checkNotEmpty(int[] array) {
        return checkNotEmpty(array, null);
    }

    public static int[] checkNotEmpty(int[] array, String name) {
        if (array == null || array.length == 0) {
            String field = name == null ? "array" : name;
            String message = message("non-empty " + field, array);
            throw new IllegalArgumentException(message);
        }
        return array;
    }

    public static long[] checkNotEmpty(long[] array) {
        return checkNotEmpty(array, null);
    }

    public static long[] checkNotEmpty(long[] array, String name) {
        if (array == null || array.length == 0) {
            String field = name == null ? "array" : name;
            String message = message("non-empty " + field, array);
            throw new IllegalArgumentException(message);
        }
        return array;
    }

    public static short[] checkNotEmpty(short[] array) {
        return checkNotEmpty(array, null);
    }

    public static short[] checkNotEmpty(short[] array, String name) {
        if (array == null || array.length == 0) {
            String field = name == null ? "array" : name;
            String message = message("non-empty " + field, array);
            throw new IllegalArgumentException(message);
        }
        return array;
    }

    public static <T> T[] checkNotEmpty(@Nullable T[] array) {
        return checkNotEmpty(array, null);
    }

    public static <T> T[] checkNotEmpty(@Nullable T[] array, String name) {
        if (array == null || array.length == 0) {
            String field = name == null ? "array" : name;
            String message = message("non-empty " + field, array);
            throw new IllegalArgumentException(message);
        }
        return array;
    }

    public static byte checkPositive(byte value) {
        return checkPositive(value, null);
    }

    public static short checkPositive(short value) {
        return checkPositive(value, null);
    }

    public static float checkPositive(float value) {
        return checkPositive(value, null);
    }

    public static double checkPositive(double value) {
        return checkPositive(value, null);
    }

    public static int checkPositive(int value) {
        return checkPositive(value, null);
    }

    public static long checkPositive(long value) {
        return checkPositive(value, null);
    }

    public static byte checkPositive(byte value, String name) {
        if (value <= 0) {
            String field = name == null ? "byte value" : name;
            String message = message(field + " > 0", value);
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    public static short checkPositive(short value, String name) {
        if (value <= 0) {
            String field = name == null ? "short value" : name;
            String message = message(field + " > 0", value);
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    public static float checkPositive(float value, String name) {
        if (value <= 0) {
            String field = name == null ? "float value" : name;
            String message = message(field + " > 0", value);
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    public static double checkPositive(double value, String name) {
        if (value <= 0) {
            String field = name == null ? "double value" : name;
            String message = message(field + " > 0", value);
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    public static int checkPositive(int value, String name) {
        if (value <= 0) {
            String field = name == null ? "int value" : name;
            String message = message(field + " > 0", value);
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    public static long checkPositive(long value, String name) {
        if (value <= 0) {
            String field = name == null ? "long value" : name;
            String message = message(field + " > 0", value);
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    public static byte checkNegative(byte value) {
        return checkNegative(value, null);
    }

    public static short checkNegative(short value) {
        return checkNegative(value, null);
    }

    public static float checkNegative(float value) {
        return checkNegative(value, null);
    }

    public static double checkNegative(double value) {
        return checkNegative(value, null);
    }

    public static int checkNegative(int value) {
        return checkNegative(value, null);
    }

    public static long checkNegative(long value) {
        return checkNegative(value, null);
    }

    public static byte checkNegative(byte value, String name) {
        if (value >= 0) {
            String field = name == null ? "byte value" : name;
            String message = message(field + " < 0", value);
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    public static short checkNegative(short value, String name) {
        if (value >= 0) {
            String field = name == null ? "short value" : name;
            String message = message(field + " < 0", value);
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    public static float checkNegative(float value, String name) {
        if (value >= 0) {
            String field = name == null ? "float value" : name;
            String message = message(field + " < 0", value);
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    public static double checkNegative(double value, String name) {
        if (value >= 0) {
            String field = name == null ? "double value" : name;
            String message = message(field + " < 0", value);
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    public static int checkNegative(int value, String name) {
        if (value >= 0) {
            String field = name == null ? "int value" : name;
            String message = message(field + " < 0", value);
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    public static long checkNegative(long value, String name) {
        if (value >= 0) {
            String field = name == null ? "long value" : name;
            String message = message(field + " < 0", value);
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    public static byte checkNotNegative(byte value) {
        return checkNotNegative(value, null);
    }

    public static short checkNotNegative(short value) {
        return checkNotNegative(value, null);
    }

    public static float checkNotNegative(float value) {
        return checkNotNegative(value, null);
    }

    public static double checkNotNegative(double value) {
        return checkNotNegative(value, null);
    }

    public static int checkNotNegative(int value) {
        return checkNotNegative(value, null);
    }

    public static long checkNotNegative(long value) {
        return checkNotNegative(value, null);
    }

    public static int checkNotNegative(int value, String name) {
        if (value < 0) {
            String field = name == null ? "int value" : name;
            String message = message(field + " >= 0", value);
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    public static byte checkNotNegative(byte value, String name) {
        if (value < 0) {
            String field = name == null ? "byte value" : name;
            String message = message(field + " >= 0", value);
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    public static short checkNotNegative(short value, String name) {
        if (value < 0) {
            String field = name == null ? "short value" : name;
            String message = message(field + " >= 0", value);
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    public static float checkNotNegative(float value, String name) {
        if (value < 0) {
            String field = name == null ? "float value" : name;
            String message = message(field + " >= 0", value);
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    public static double checkNotNegative(double value, String name) {
        if (value < 0) {
            String field = name == null ? "double value" : name;
            String message = message(field + " >= 0", value);
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    public static long checkNotNegative(long value, String name) {
        if (value < 0) {
            String field = name == null ? "long value" : name;
            String message = message(field + " >= 0", value);
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    public static byte checkNotPositive(byte value) {
        return checkNotPositive(value, null);
    }

    public static short checkNotPositive(short value) {
        return checkNotPositive(value, null);
    }

    public static float checkNotPositive(float value) {
        return checkNotPositive(value, null);
    }

    public static double checkNotPositive(double value) {
        return checkNotPositive(value, null);
    }

    public static int checkNotPositive(int value) {
        return checkNotPositive(value, null);
    }

    public static long checkNotPositive(long value) {
        return checkNotPositive(value, null);
    }

    public static int checkNotPositive(int value, String name) {
        if (value > 0) {
            String field = name == null ? "int value" : name;
            String message = message(field + " <= 0", value);
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    public static byte checkNotPositive(byte value, String name) {
        if (value > 0) {
            String field = name == null ? "byte value" : name;
            String message = message(field + " <= 0", value);
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    public static short checkNotPositive(short value, String name) {
        if (value > 0) {
            String field = name == null ? "short value" : name;
            String message = message(field + " <= 0", value);
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    public static float checkNotPositive(float value, String name) {
        if (value > 0) {
            String field = name == null ? "float value" : name;
            String message = message(field + " <= 0", value);
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    public static double checkNotPositive(double value, String name) {
        if (value > 0) {
            String field = name == null ? "double value" : name;
            String message = message(field + " <= 0", value);
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    public static long checkNotPositive(long value, String name) {
        if (value > 0) {
            String field = name == null ? "long value" : name;
            String message = message(field + " <= 0", value);
            throw new IllegalArgumentException(message);
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
