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

public final class Bytes {
    private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

    private Bytes() {
        throw new UnsupportedOperationException("Do not instantiate utility class");
    }

    public static byte[] emptyByteArray() {
        return EMPTY_BYTE_ARRAY;
    }

    public static byte first(byte[] array) {
        checkNotEmpty(array, "array");
        return array[0];
    }

    public static byte firstOrDefault(byte[] array, byte defaultValue) {
        return array == null || array.length == 0
                ? defaultValue
                : array[0];
    }

    public static byte last(byte[] array) {
        checkNotEmpty(array, "array");
        return array[array.length - 1];
    }

    public static byte lastOrDefault(byte[] array, byte defaultValue) {
        return array == null || array.length == 0
                ? defaultValue
                : array[array.length - 1];
    }

    public static byte checkedCast(int value) {
        byte result = (byte) value;
        check(result == value, "Out of range: %s", value);
        return result;
    }

    public static byte closestCast(int value) {
        if (value > Byte.MAX_VALUE) {
            return Byte.MAX_VALUE;
        }
        if (value < Byte.MIN_VALUE) {
            return Byte.MIN_VALUE;
        }
        return (byte) value;
    }

    public static boolean contains(byte[] array, byte target) {
        for (int value : array) {
            if (value == target) {
                return true;
            }
        }
        return false;
    }

    public static int indexOf(byte[] array, byte target) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == target) {
                return i;
            }
        }
        return -1;
    }

    public static int lastIndexOf(byte[] array, byte target) {
        for (int i = array.length - 1; i >= 0; i--) {
            if (array[i] == target) {
                return i;
            }
        }
        return -1;
    }

    public static byte min(byte... array) {
        check(array.length > 0, "Expected array.length > 0");
        byte min = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] < min) {
                min = array[i];
            }
        }
        return min;
    }

    public static byte max(byte... array) {
        check(array.length > 0, "Expected array.length > 0");
        byte max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        return max;
    }

    public static byte[] concat(byte[]... arrays) {
        int length = 0;
        for (byte[] array : arrays) {
            length += array.length;
        }
        byte[] result = new byte[length];
        int pos = 0;
        for (byte[] array : arrays) {
            System.arraycopy(array, 0, result, pos, array.length);
            pos += array.length;
        }
        return result;
    }

    public static byte[] copy(byte[] array) {
        checkNotNull(array);
        return Arrays.copyOf(array, array.length);
    }

    public static void reverse(byte[] array) {
        checkNotNull(array);
        for (int i = 0, j = array.length - 1; i < j; i++, j--) {
            byte tmp = array[i];
            array[i] = array[j];
            array[j] = tmp;
        }
    }

    public static void swap(byte[] array, int i, int j) {
        byte tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }

    public static byte[] ensureCapacity(byte[] array, int minLength) {
        return ensureCapacity(array, minLength, 0);
    }

    public static byte[] ensureCapacity(byte[] array, int minLength, int padding) {
        Args.check(minLength >= 0, "Invalid minLength: %s", minLength);
        Args.check(padding >= 0, "Invalid padding: %s", padding);
        return (array.length < minLength) ? Arrays.copyOf(array, minLength + padding) : array;
    }

    public static byte[] toArray(Collection<? extends Number> collection) {
        Object[] boxedArray = collection.toArray();
        int len = boxedArray.length;
        byte[] array = new byte[len];
        for (int i = 0; i < len; i++) {
            array[i] = ((Number) boxedArray[i]).byteValue();
        }
        return array;
    }

    public static void shuffle(byte[] array) {
        shuffle(array, ThreadLocalRandom.current());
    }

    public static void shuffle(byte[] array, Random random) {
        for (int i = 0; i < array.length; i++) {
            int randomIndexToSwap = random.nextInt(array.length);
            byte temp = array[randomIndexToSwap];
            array[randomIndexToSwap] = array[i];
            array[i] = temp;
        }
    }

    public static byte parseByte(String value) {
        try {
            return Byte.parseByte(value);
        } catch (Exception e) {
            throw new IllegalArgumentException("Could not parse Byte value: '" + value + "'", e);
        }
    }

    @Nullable
    public static Byte parseByteOrNull(@Nullable String value) {
        if (value == null) {
            return null;
        }
        try {
            return Byte.parseByte(value);
        } catch (Exception e) {
            return null;
        }
    }

    public static byte parseByteOrDefault(@Nullable String value, byte defaultValue) {
        Byte result = parseByteOrNull(value);
        return result == null ? defaultValue : result;
    }

    public static Optional<Byte> parseByteOrEmpty(@Nullable String value) {
        return Optional.ofNullable(parseByteOrNull(value));
    }
}
