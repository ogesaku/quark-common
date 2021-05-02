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

public final class Integers {
    private static final int[] EMPTY_INT_ARRAY = new int[0];

    private Integers() {
        throw new UnsupportedOperationException("Do not instantiate utility class");
    }

    public static int[] emptyIntArray() {
        return EMPTY_INT_ARRAY;
    }

    public static int first(int[] array) {
        checkNotEmpty(array, "array");
        return array[0];
    }

    public static int firstOrDefault(int[] array, int defaultValue) {
        return array == null || array.length == 0
                ? defaultValue
                : array[0];
    }

    public static int last(int[] array) {
        checkNotEmpty(array, "array");
        return array[array.length - 1];
    }

    public static int lastOrDefault(int[] array, int defaultValue) {
        return array == null || array.length == 0
                ? defaultValue
                : array[array.length - 1];
    }

    public static int checkedCast(long value) {
        int result = (int) value;
        check(result == value, "Out of range: %s", value);
        return result;
    }

    public static int closestCast(long value) {
        if (value > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        if (value < Integer.MIN_VALUE) {
            return Integer.MIN_VALUE;
        }
        return (int) value;
    }

    public static boolean contains(int[] array, int target) {
        for (int value : array) {
            if (value == target) {
                return true;
            }
        }
        return false;
    }

    public static int indexOf(int[] array, int target) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == target) {
                return i;
            }
        }
        return -1;
    }

    public static int lastIndexOf(int[] array, int target) {
        for (int i = array.length - 1; i >= 0; i--) {
            if (array[i] == target) {
                return i;
            }
        }
        return -1;
    }

    public static int min(int... array) {
        check(array.length > 0, "Expected array.length > 0");
        int min = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] < min) {
                min = array[i];
            }
        }
        return min;
    }

    public static int max(int... array) {
        check(array.length > 0, "Expected array.length > 0");
        int max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        return max;
    }

    public static int[] concat(int[]... arrays) {
        int length = 0;
        for (int[] array : arrays) {
            length += array.length;
        }
        int[] result = new int[length];
        int pos = 0;
        for (int[] array : arrays) {
            System.arraycopy(array, 0, result, pos, array.length);
            pos += array.length;
        }
        return result;
    }

    public static int[] copy(int[] array) {
        checkNotNull(array);
        return Arrays.copyOf(array, array.length);
    }

    public static void reverse(int[] array) {
        checkNotNull(array);
        for (int i = 0, j = array.length - 1; i < j; i++, j--) {
            int tmp = array[i];
            array[i] = array[j];
            array[j] = tmp;
        }
    }

    public static void swap(int[] array, int i, int j) {
        int tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }

    public static int[] ensureCapacity(int[] array, int minLength) {
        return ensureCapacity(array, minLength, 0);
    }

    public static int[] ensureCapacity(int[] array, int minLength, int padding) {
        Args.check(minLength >= 0, "Invalid minLength: %s", minLength);
        Args.check(padding >= 0, "Invalid padding: %s", padding);
        return (array.length < minLength) ? Arrays.copyOf(array, minLength + padding) : array;
    }

    public static int[] toArray(Collection<? extends Number> collection) {
        Object[] boxedArray = collection.toArray();
        int len = boxedArray.length;
        int[] array = new int[len];
        for (int i = 0; i < len; i++) {
            array[i] = ((Number) boxedArray[i]).intValue();
        }
        return array;
    }

    public static void shuffle(int[] array) {
        shuffle(array, ThreadLocalRandom.current());
    }

    public static void shuffle(int[] array, Random random) {
        for (int i = 0; i < array.length; i++) {
            int randomIndexToSwap = random.nextInt(array.length);
            int temp = array[randomIndexToSwap];
            array[randomIndexToSwap] = array[i];
            array[i] = temp;
        }
    }

    public static int parseInteger(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            throw new IllegalArgumentException("Could not parse Integer value: '" + value + "'", e);
        }
    }

    @Nullable
    public static Integer parseIntegerOrNull(@Nullable String value) {
        if (value == null) {
            return null;
        }
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return null;
        }
    }

    public static int parseIntegerOrDefault(@Nullable String value, int defaultValue) {
        Integer result = parseIntegerOrNull(value);
        return result == null ? defaultValue : result;
    }

    public static Optional<Integer> parseIntegerOrEmpty(@Nullable String value) {
        return Optional.ofNullable(parseIntegerOrNull(value));
    }
}
