package com.coditory.quark.common.util;

import com.coditory.quark.common.check.Args;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;

import static com.coditory.quark.common.check.Args.check;
import static com.coditory.quark.common.check.Args.checkNotEmpty;
import static com.coditory.quark.common.check.Args.checkNotNull;

public final class Longs {
    public static final DecimalFormat HUGE_NUMBER_FORMATTER = new DecimalFormat("###,###.##");
    public static final DecimalFormat HUGE_NUMBER_FORMATTER_WITHOUT_DECIMALS = new DecimalFormat("###,###");
    private static final TreeMap<Long, String> SI_UNITS = new TreeMap<>();
    private static final long[] EMPTY_LONG_ARRAY = new long[0];

    static {
        SI_UNITS.put(1_000L, "k");
        SI_UNITS.put(1_000_000L, "M");
        SI_UNITS.put(1_000_000_000L, "G");
        SI_UNITS.put(1_000_000_000_000L, "T");
        SI_UNITS.put(1_000_000_000_000_000L, "P");
        SI_UNITS.put(1_000_000_000_000_000_000L, "E");
    }

    private Longs() {
        throw new UnsupportedOperationException("Do not instantiate utility class");
    }

    public static long[] emptyLongArray() {
        return EMPTY_LONG_ARRAY;
    }

    public static long first(long[] array) {
        checkNotEmpty(array, "array");
        return array[0];
    }

    public static long firstOrDefault(long[] array, long defaultValue) {
        return array == null || array.length == 0
                ? defaultValue
                : array[0];
    }

    public static long last(long[] array) {
        checkNotEmpty(array, "array");
        return array[array.length - 1];
    }

    public static long lastOrDefault(long[] array, long defaultValue) {
        return array == null || array.length == 0
                ? defaultValue
                : array[array.length - 1];
    }

    public static boolean contains(long[] array, long target) {
        for (long value : array) {
            if (value == target) {
                return true;
            }
        }
        return false;
    }

    public static int indexOf(long[] array, long target) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == target) {
                return i;
            }
        }
        return -1;
    }

    public static int lastIndexOf(long[] array, long target) {
        for (int i = array.length - 1; i >= 0; i--) {
            if (array[i] == target) {
                return i;
            }
        }
        return -1;
    }

    public static long min(long... array) {
        check(array.length > 0, "Expected array.length > 0");
        long min = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] < min) {
                min = array[i];
            }
        }
        return min;
    }

    public static long max(long... array) {
        check(array.length > 0, "Expected array.length > 0");
        long max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        return max;
    }

    public static long[] concat(long[]... arrays) {
        int length = 0;
        for (long[] array : arrays) {
            length += array.length;
        }
        long[] result = new long[length];
        int pos = 0;
        for (long[] array : arrays) {
            System.arraycopy(array, 0, result, pos, array.length);
            pos += array.length;
        }
        return result;
    }

    public static long[] copy(long[] array) {
        checkNotNull(array);
        return Arrays.copyOf(array, array.length);
    }

    public static void reverse(long[] array) {
        checkNotNull(array);
        for (int i = 0, j = array.length - 1; i < j; i++, j--) {
            long tmp = array[i];
            array[i] = array[j];
            array[j] = tmp;
        }
    }

    public static void swap(long[] array, int i, int j) {
        long tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }

    public static long[] ensureCapacity(long[] array, int minLength) {
        return ensureCapacity(array, minLength, 0);
    }

    public static long[] ensureCapacity(long[] array, int minLength, int padding) {
        Args.check(minLength >= 0, "Invalid minLength: %s", minLength);
        Args.check(padding >= 0, "Invalid padding: %s", padding);
        return (array.length < minLength) ? Arrays.copyOf(array, minLength + padding) : array;
    }

    public static long[] toArray(Collection<? extends Number> collection) {
        Object[] boxedArray = collection.toArray();
        int len = boxedArray.length;
        long[] array = new long[len];
        for (int i = 0; i < len; i++) {
            array[i] = ((Number) boxedArray[i]).longValue();
        }
        return array;
    }

    public static void shuffle(long[] array) {
        shuffle(array, ThreadLocalRandom.current());
    }

    public static void shuffle(long[] array, Random random) {
        for (int i = 0; i < array.length; i++) {
            int randomIndexToSwap = random.nextInt(array.length);
            long temp = array[randomIndexToSwap];
            array[randomIndexToSwap] = array[i];
            array[i] = temp;
        }
    }

    public static long parseLong(String value) {
        checkNotNull(value, "value");
        try {
            return Long.parseLong(value);
        } catch (Exception e) {
            throw new IllegalArgumentException("Could not parse Long value: '" + value + "'", e);
        }
    }

    @Nullable
    public static Long parseLongOrNull(@Nullable String value) {
        if (value == null) {
            return null;
        }
        try {
            return Long.parseLong(value);
        } catch (Exception e) {
            return null;
        }
    }

    public static long parseLongOrDefault(@Nullable String value, long defaultValue) {
        Long result = parseLongOrNull(value);
        return result == null ? defaultValue : result;
    }

    public static Optional<Long> parseLongOrEmpty(@Nullable String value) {
        return Optional.ofNullable(parseLongOrNull(value));
    }

    public static String formatToHumanReadable(long number) {
        return formatToHumanReadable(number, HUGE_NUMBER_FORMATTER);
    }

    public static String formatToHumanReadable(long number, DecimalFormat decimalFormat) {
        checkNotNull(decimalFormat, "decimalFormat");
        Map.Entry<Long, String> entry = Long.MIN_VALUE == number
                ? SI_UNITS.floorEntry(Long.MAX_VALUE)
                : SI_UNITS.floorEntry(Math.abs(number));
        if (entry == null) {
            return Long.toString(number);
        }
        String formattedNumber = decimalFormat.getMaximumFractionDigits() > 0
                ? decimalFormat.format(number * 1.0d / entry.getKey())
                : decimalFormat.format(number / entry.getKey());
        return formattedNumber + entry.getValue();
    }
}
