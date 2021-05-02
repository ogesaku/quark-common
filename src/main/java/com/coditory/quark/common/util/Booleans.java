package com.coditory.quark.common.util;

import com.coditory.quark.common.check.Args;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static com.coditory.quark.common.check.Args.check;
import static com.coditory.quark.common.check.Args.checkNotEmpty;
import static com.coditory.quark.common.check.Args.checkNotNull;

public final class Booleans {
    private static final boolean[] EMPTY_BOOLEAN_ARRAY = new boolean[0];

    private Booleans() {
        throw new UnsupportedOperationException("Do not instantiate utility class");
    }

    public static boolean[] emptyBooleanArray() {
        return EMPTY_BOOLEAN_ARRAY;
    }

    public static boolean first(boolean[] array) {
        checkNotEmpty(array, "array");
        return array[0];
    }

    public static boolean firstOrDefault(boolean[] array, boolean defaultValue) {
        return array == null || array.length == 0
                ? defaultValue
                : array[0];
    }

    public static boolean last(boolean[] array) {
        checkNotEmpty(array, "array");
        return array[array.length - 1];
    }

    public static boolean lastOrDefault(boolean[] array, boolean defaultValue) {
        return array == null || array.length == 0
                ? defaultValue
                : array[array.length - 1];
    }

    public static boolean contains(boolean[] array, boolean target) {
        for (boolean value : array) {
            if (value == target) {
                return true;
            }
        }
        return false;
    }

    public static int indexOf(boolean[] array, boolean target) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == target) {
                return i;
            }
        }
        return -1;
    }

    public static int lastIndexOf(boolean[] array, boolean target) {
        for (int i = array.length - 1; i >= 0; i--) {
            if (array[i] == target) {
                return i;
            }
        }
        return -1;
    }

    public static boolean anyTrue(boolean... array) {
        check(array.length > 0, "Expected array.length > 0");
        for (int i = 1; i < array.length; i++) {
            if (array[i]) {
                return true;
            }
        }
        return false;
    }

    public static boolean anyFalse(boolean... array) {
        check(array.length > 0, "Expected array.length > 0");
        for (int i = 1; i < array.length; i++) {
            if (!array[i]) {
                return true;
            }
        }
        return false;
    }

    public static boolean[] concat(boolean[]... arrays) {
        int length = 0;
        for (boolean[] array : arrays) {
            length += array.length;
        }
        boolean[] result = new boolean[length];
        int pos = 0;
        for (boolean[] array : arrays) {
            System.arraycopy(array, 0, result, pos, array.length);
            pos += array.length;
        }
        return result;
    }

    public static boolean[] copy(boolean[] array) {
        checkNotNull(array);
        return Arrays.copyOf(array, array.length);
    }

    public static void reverse(boolean[] array) {
        checkNotNull(array);
        for (int i = 0, j = array.length - 1; i < j; i++, j--) {
            boolean tmp = array[i];
            array[i] = array[j];
            array[j] = tmp;
        }
    }

    public static void swap(boolean[] array, int i, int j) {
        boolean tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }

    public static boolean[] ensureCapacity(boolean[] array, int minLength) {
        return ensureCapacity(array, minLength, 0);
    }

    public static boolean[] ensureCapacity(boolean[] array, int minLength, int padding) {
        Args.check(minLength >= 0, "Invalid minLength: %s", minLength);
        Args.check(padding >= 0, "Invalid padding: %s", padding);
        return (array.length < minLength) ? Arrays.copyOf(array, minLength + padding) : array;
    }

    public static boolean[] toArray(Collection<Boolean> collection) {
        Object[] boxedArray = collection.toArray();
        int len = boxedArray.length;
        boolean[] array = new boolean[len];
        for (int i = 0; i < len; i++) {
            array[i] = ((Boolean) boxedArray[i]);
        }
        return array;
    }

    public static void shuffle(boolean[] array) {
        shuffle(array, ThreadLocalRandom.current());
    }

    public static void shuffle(boolean[] array, Random random) {
        for (int i = 0; i < array.length; i++) {
            int randomIndexToSwap = random.nextInt(array.length);
            boolean temp = array[randomIndexToSwap];
            array[randomIndexToSwap] = array[i];
            array[i] = temp;
        }
    }

    @Nullable
    public static Boolean parseBooleanOrNull(@Nullable String value) {
        if (value == null) {
            return null;
        }
        String normalized = value.trim().toLowerCase();
        if ("true".equals(normalized)) {
            return true;
        }
        if ("false".equals(normalized)) {
            return false;
        }
        return null;
    }

    public static boolean parseBoolean(String value) {
        Boolean result = parseBooleanOrNull(value);
        if (result == null) {
            throw new IllegalArgumentException("Could not parse Boolean value: '" + value + "'. Expected true/false.");
        }
        return result;
    }

    public static boolean parseBooleanOrDefault(@Nullable String value, boolean defaultValue) {
        Boolean result = parseBooleanOrNull(value);
        return result == null ? defaultValue : result;
    }

    public static Optional<Boolean> parseBooleanOrEmpty(@Nullable String value) {
        return Optional.ofNullable(parseBooleanOrNull(value));
    }
}
