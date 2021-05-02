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

public final class Shorts {
    private static final short[] EMPTY_SHORT_ARRAY = new short[0];

    private Shorts() {
        throw new UnsupportedOperationException("Do not instantiate utility class");
    }

    public static short[] emptyShortArray() {
        return EMPTY_SHORT_ARRAY;
    }

    public static short first(short[] array) {
        checkNotEmpty(array, "array");
        return array[0];
    }

    public static short firstOrDefault(short[] array, short defaultValue) {
        return array == null || array.length == 0
                ? defaultValue
                : array[0];
    }

    public static short last(short[] array) {
        checkNotEmpty(array, "array");
        return array[array.length - 1];
    }

    public static short lastOrDefault(short[] array, short defaultValue) {
        return array == null || array.length == 0
                ? defaultValue
                : array[array.length - 1];
    }

    public static short checkedCast(int value) {
        short result = (short) value;
        check(result == value, "Out of range: %s", value);
        return result;
    }

    public static short closestCast(int value) {
        if (value > Short.MAX_VALUE) {
            return Short.MAX_VALUE;
        }
        if (value < Short.MIN_VALUE) {
            return Short.MIN_VALUE;
        }
        return (short) value;
    }

    public static boolean contains(short[] array, short target) {
        for (int value : array) {
            if (value == target) {
                return true;
            }
        }
        return false;
    }

    public static int indexOf(short[] array, short target) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == target) {
                return i;
            }
        }
        return -1;
    }

    public static int lastIndexOf(short[] array, short target) {
        for (int i = array.length - 1; i >= 0; i--) {
            if (array[i] == target) {
                return i;
            }
        }
        return -1;
    }

    public static int min(short... array) {
        check(array.length > 0, "Expected array.length > 0");
        short min = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] < min) {
                min = array[i];
            }
        }
        return min;
    }

    public static short max(short... array) {
        check(array.length > 0, "Expected array.length > 0");
        short max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        return max;
    }

    public static short[] concat(short[]... arrays) {
        int length = 0;
        for (short[] array : arrays) {
            length += array.length;
        }
        short[] result = new short[length];
        int pos = 0;
        for (short[] array : arrays) {
            System.arraycopy(array, 0, result, pos, array.length);
            pos += array.length;
        }
        return result;
    }

    public static short[] copy(short[] array) {
        checkNotNull(array);
        return Arrays.copyOf(array, array.length);
    }

    public static void reverse(short[] array) {
        checkNotNull(array);
        for (int i = 0, j = array.length - 1; i < j; i++, j--) {
            short tmp = array[i];
            array[i] = array[j];
            array[j] = tmp;
        }
    }

    public static void swap(short[] array, int i, int j) {
        short tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }

    public static short[] ensureCapacity(short[] array, int minLength) {
        return ensureCapacity(array, minLength, 0);
    }

    public static short[] ensureCapacity(short[] array, int minLength, int padding) {
        Args.check(minLength >= 0, "Invalid minLength: %s", minLength);
        Args.check(padding >= 0, "Invalid padding: %s", padding);
        return (array.length < minLength) ? Arrays.copyOf(array, minLength + padding) : array;
    }

    public static short[] toArray(Collection<? extends Number> collection) {
        Object[] boxedArray = collection.toArray();
        int len = boxedArray.length;
        short[] array = new short[len];
        for (int i = 0; i < len; i++) {
            array[i] = ((Number) boxedArray[i]).shortValue();
        }
        return array;
    }

    public static void shuffle(short[] array) {
        shuffle(array, ThreadLocalRandom.current());
    }

    public static void shuffle(short[] array, Random random) {
        for (int i = 0; i < array.length; i++) {
            int randomIndexToSwap = random.nextInt(array.length);
            short temp = array[randomIndexToSwap];
            array[randomIndexToSwap] = array[i];
            array[i] = temp;
        }
    }

    public static short parseShort(String value) {
        checkNotNull(value, "value");
        try {
            return Short.parseShort(value);
        } catch (Exception e) {
            throw new IllegalArgumentException("Could not parse Short value: '" + value + "'", e);
        }
    }

    @Nullable
    public static Short parseShortOrNull(@Nullable String value) {
        if (value == null) {
            return null;
        }
        try {
            return Short.parseShort(value);
        } catch (Exception e) {
            return null;
        }
    }

    public static short parseShortOrDefault(@Nullable String value, short defaultValue) {
        Short result = parseShortOrNull(value);
        return result == null ? defaultValue : result;
    }

    public static Optional<Short> parseShortOrEmpty(@Nullable String value) {
        return Optional.ofNullable(parseShortOrNull(value));
    }
}
