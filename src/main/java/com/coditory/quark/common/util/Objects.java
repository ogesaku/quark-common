package com.coditory.quark.common.util;

import com.coditory.quark.common.throwable.ThrowingFunction;
import com.coditory.quark.common.throwable.ThrowingSupplier;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

import static com.coditory.quark.common.check.Args.check;
import static com.coditory.quark.common.check.Args.checkNotEmpty;
import static com.coditory.quark.common.check.Args.checkNotNull;

public final class Objects {
    private static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];

    private Objects() {
        throw new UnsupportedOperationException("Do not instantiate utility class");
    }

    public static Object[] emptyObjectArray() {
        return EMPTY_OBJECT_ARRAY;
    }

    public static String join(Object[] array) {
        return join(array, ", ");
    }

    public static String join(Object[] array, String separator) {
        checkNotNull(array, "array");
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < array.length; ++i) {
            if (i > 0) {
                builder.append(separator);
            }
            Object object = array[i];
            builder.append(Objects.toString(object));
        }
        return builder.toString();
    }

    public static <T> T first(T[] array) {
        checkNotEmpty(array, "array");
        return array[0];
    }

    @Nullable
    public static <T> T firstOrNull(T[] array) {
        return firstOrDefault(array, null);
    }

    @Nullable
    public static <T> T firstOrDefault(T[] array, @Nullable T defaultValue) {
        return array == null || array.length == 0
                ? defaultValue
                : array[0];
    }

    public static <T> T last(T[] array) {
        checkNotEmpty(array, "array");
        return array[array.length - 1];
    }

    @Nullable
    public static <T> T lastOrNull(T[] array) {
        return lastOrDefault(array, null);
    }

    @Nullable
    public static <T> T lastOrDefault(T[] array, @Nullable T defaultValue) {
        return array != null && array.length > 0
                ? array[array.length - 1]
                : defaultValue;
    }

    public static boolean isEmpty(Object[] objects) {
        return objects == null || objects.length == 0;
    }

    public static boolean isNotEmpty(Object[] objects) {
        return objects != null && objects.length > 0;
    }

    public static String toString(Object object) {
        return java.util.Objects.toString(object);
    }

    @Nullable
    public static String toStringOrNull(Object object) {
        return object == null ? null : java.util.Objects.toString(object);
    }

    public static String toStringOrEmpty(Object object) {
        return toStringOrDefault(object, "");
    }

    @Nullable
    public static String toStringOrDefault(Object object, @Nullable String defaultResult) {
        return object == null ? defaultResult : java.util.Objects.toString(object);
    }

    @Nullable
    public static String toStringOrDefault(Object object, ThrowingSupplier<String> defaultSupplier) {
        checkNotNull(defaultSupplier, "defaultSupplier");
        return object == null ? defaultSupplier.getWithSneakyThrow() : java.util.Objects.toString(object);
    }

    @Nullable
    public static <T, R> R mapNotNull(T object, ThrowingFunction<T, R> mapper) {
        checkNotNull(mapper, "mapper");
        return object != null
                ? mapper.applyWithSneakyThrow(object)
                : null;
    }

    public static <T, R> R mapNotNullOrDefault(T object, ThrowingFunction<T, R> mapper, R defaultValue) {
        checkNotNull(mapper, "mapper");
        return object != null
                ? mapper.applyWithSneakyThrow(object)
                : defaultValue;
    }

    public static <T, R> R mapNotNullOrDefault(T object, ThrowingFunction<T, R> mapper, ThrowingSupplier<R> defaultValueSupplier) {
        checkNotNull(mapper, "mapper");
        checkNotNull(defaultValueSupplier, "defaultValueSupplier");
        return object != null
                ? mapper.applyWithSneakyThrow(object)
                : defaultValueSupplier.getWithSneakyThrow();
    }

    public static <T> void onNotNull(T object, Consumer<T> consumer) {
        checkNotNull(consumer, "consumer");
        if (object != null) {
            consumer.accept(object);
        }
    }

    public static <T> T defaultIfNull(T object, T defaultValue) {
        return object != null ? object : defaultValue;
    }

    public static <T> T defaultIfNull(T object, ThrowingSupplier<T> defaultSupplier) {
        checkNotNull(defaultSupplier, "defaultSupplier");
        return object != null ? object : defaultSupplier.getWithSneakyThrow();
    }

    @SafeVarargs
    @SuppressWarnings("varargs")
    public static <T> T firstNonNull(T... values) {
        T result = firstNonNullOrNull(values);
        if (result == null) {
            throw new IllegalArgumentException("Expected at least one non null element");
        }
        return result;
    }

    @SafeVarargs
    @Nullable
    public static <T> T firstNonNullOrNull(T... values) {
        if (values != null) {
            for (T val : values) {
                if (val != null) {
                    return val;
                }
            }
        }
        return null;
    }

    @SafeVarargs
    @SuppressWarnings("varargs")
    public static <T> T getFirstNonNull(ThrowingSupplier<T>... suppliers) {
        T result = getFirstNonNullOrNull(suppliers);
        if (result == null) {
            throw new IllegalArgumentException("Expected at least one non null element");
        }
        return result;
    }

    @SafeVarargs
    @Nullable
    public static <T> T getFirstNonNullOrNull(ThrowingSupplier<T>... suppliers) {
        if (suppliers != null) {
            for (ThrowingSupplier<T> supplier : suppliers) {
                if (supplier != null) {
                    T value = supplier.getWithSneakyThrow();
                    if (value != null) {
                        return value;
                    }
                }
            }
        }
        return null;
    }

    public static boolean equals(@Nullable Object a, @Nullable Object b) {
        return java.util.Objects.equals(a, b);
    }

    public static int hashCode(@Nullable Object o) {
        return java.util.Objects.hashCode(o);
    }

    public static int hash(@Nullable Object... values) {
        return java.util.Objects.hash(values);
    }

    public static boolean nonNull(@Nullable Object value) {
        return value != null;
    }

    public static boolean contains(Object[] array, Object target) {
        for (Object value : array) {
            if (equals(value, target)) {
                return true;
            }
        }
        return false;
    }

    public static int indexOf(Object[] array, Object target) {
        for (int i = 0; i < array.length; i++) {
            if (equals(array[i], target)) {
                return i;
            }
        }
        return -1;
    }

    public static int lastIndexOf(Object[] array, Object target) {
        for (int i = array.length - 1; i >= 0; i--) {
            if (equals(array[i], target)) {
                return i;
            }
        }
        return -1;
    }

    @SuppressWarnings("unchecked")
    public static <T> T min(Comparator<T> comparator, T... array) {
        check(array.length > 0, "Expected array.length > 0");
        T min = array[0];
        for (int i = 1; i < array.length; i++) {
            if (comparator.compare(array[i], min) < 0) {
                min = array[i];
            }
        }
        return min;
    }

    @SuppressWarnings("unchecked")
    public static <T> T max(Comparator<T> comparator, T... array) {
        check(array.length > 0, "Expected array.length > 0");
        T max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (comparator.compare(array[i], max) > 0) {
                max = array[i];
            }
        }
        return max;
    }

    public static Object[] concat(Object[]... arrays) {
        int length = 0;
        for (Object[] array : arrays) {
            length += array.length;
        }
        Object[] result = new Object[length];
        int pos = 0;
        for (Object[] array : arrays) {
            System.arraycopy(array, 0, result, pos, array.length);
            pos += array.length;
        }
        return result;
    }

    public static Object[] copy(Object[] array) {
        checkNotNull(array);
        return Arrays.copyOf(array, array.length);
    }

    public static void reverse(Object[] array) {
        checkNotNull(array);
        for (int i = 0, j = array.length - 1; i < j; i++, j--) {
            Object tmp = array[i];
            array[i] = array[j];
            array[j] = tmp;
        }
    }

    public static void swap(Object[] array, int i, int j) {
        Object tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }

    public static Object[] ensureCapacity(Object[] array, int minLength) {
        return ensureCapacity(array, minLength, 0);
    }

    public static Object[] ensureCapacity(Object[] array, int minLength, int padding) {
        check(minLength >= 0, "Invalid minLength: %s", minLength);
        check(padding >= 0, "Invalid padding: %s", padding);
        return (array.length < minLength) ? Arrays.copyOf(array, minLength + padding) : array;
    }

    public static void shuffle(Object[] array) {
        shuffle(array, ThreadLocalRandom.current());
    }

    public static void shuffle(Object[] array, Random random) {
        for (int i = 0; i < array.length; i++) {
            int randomIndexToSwap = random.nextInt(array.length);
            Object temp = array[randomIndexToSwap];
            array[randomIndexToSwap] = array[i];
            array[i] = temp;
        }
    }
}
