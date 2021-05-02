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
import static com.coditory.quark.common.check.Args.checkPositive;

public final class Doubles {
    private static final double[] EMPTY_DOUBLE_ARRAY = new double[0];

    private Doubles() {
        throw new UnsupportedOperationException("Do not instantiate utility class");
    }

    public static double[] emptyDoubleArray() {
        return EMPTY_DOUBLE_ARRAY;
    }

    public static double first(double[] array) {
        checkNotEmpty(array, "array");
        return array[0];
    }

    public static double firstOrDefault(double[] array, double defaultValue) {
        return array == null || array.length == 0
                ? defaultValue
                : array[0];
    }

    public static double last(double[] array) {
        checkNotEmpty(array, "array");
        return array[array.length - 1];
    }

    public static double lastOrDefault(double[] array, double defaultValue) {
        return array == null || array.length == 0
                ? defaultValue
                : array[array.length - 1];
    }

    public static double fuzzyEpsilon(double a, double b) {
        return Math.max(Math.ulp(a), Math.ulp(b)) * 5;
    }

    public static boolean fuzzyEquals(double a, double b) {
        return fuzzyEquals(a, b, fuzzyEpsilon(a, b));
    }

    public static boolean fuzzyEquals(double a, double b, double epsilon) {
        checkPositive(epsilon, "epsilon");
        return Math.abs(a - b) < epsilon;
    }

    public static boolean fuzzyGreaterThanOrEqual(double a, double b) {
        return fuzzyGreaterThanOrEqual(a, b, fuzzyEpsilon(a, b));
    }

    public static boolean fuzzyGreaterThanOrEqual(double a, double b, double epsilon) {
        checkPositive(epsilon, "epsilon");
        return a > b || fuzzyEquals(a, b, epsilon);
    }

    public static boolean fuzzyLowerThanOrEqual(double a, double b) {
        return fuzzyGreaterThanOrEqual(a, b, fuzzyEpsilon(a, b));
    }

    public static boolean fuzzyLowerThanOrEqual(double a, double b, double epsilon) {
        checkPositive(epsilon, "epsilon");
        return a < b || fuzzyEquals(a, b, epsilon);
    }


    public static boolean contains(double[] array, double target) {
        for (double value : array) {
            if (value == target) {
                return true;
            }
        }
        return false;
    }

    public static int indexOf(double[] array, double target) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == target) {
                return i;
            }
        }
        return -1;
    }

    public static int lastIndexOf(double[] array, double target) {
        for (int i = array.length - 1; i >= 0; i--) {
            if (array[i] == target) {
                return i;
            }
        }
        return -1;
    }

    public static double min(double... array) {
        check(array.length > 0, "Expected array.length > 0");
        double min = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] < min) {
                min = array[i];
            }
        }
        return min;
    }

    public static double max(double... array) {
        check(array.length > 0, "Expected array.length > 0");
        double max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        return max;
    }

    public static double[] concat(double[]... arrays) {
        int length = 0;
        for (double[] array : arrays) {
            length += array.length;
        }
        double[] result = new double[length];
        int pos = 0;
        for (double[] array : arrays) {
            System.arraycopy(array, 0, result, pos, array.length);
            pos += array.length;
        }
        return result;
    }

    public static double[] copy(double[] array) {
        checkNotNull(array);
        return Arrays.copyOf(array, array.length);
    }

    public static void reverse(double[] array) {
        checkNotNull(array);
        for (int i = 0, j = array.length - 1; i < j; i++, j--) {
            double tmp = array[i];
            array[i] = array[j];
            array[j] = tmp;
        }
    }

    public static void swap(double[] array, int i, int j) {
        double tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }

    public static double[] ensureCapacity(double[] array, int minLength) {
        return ensureCapacity(array, minLength, 0);
    }

    public static double[] ensureCapacity(double[] array, int minLength, int padding) {
        Args.check(minLength >= 0, "Invalid minLength: %s", minLength);
        Args.check(padding >= 0, "Invalid padding: %s", padding);
        return (array.length < minLength) ? Arrays.copyOf(array, minLength + padding) : array;
    }

    public static double[] toArray(Collection<? extends Number> collection) {
        Object[] boxedArray = collection.toArray();
        int len = boxedArray.length;
        double[] array = new double[len];
        for (int i = 0; i < len; i++) {
            array[i] = ((Number) boxedArray[i]).doubleValue();
        }
        return array;
    }

    public static void shuffle(double[] array) {
        shuffle(array, ThreadLocalRandom.current());
    }

    public static void shuffle(double[] array, Random random) {
        for (int i = 0; i < array.length; i++) {
            int randomIndexToSwap = random.nextInt(array.length);
            double temp = array[randomIndexToSwap];
            array[randomIndexToSwap] = array[i];
            array[i] = temp;
        }
    }

    public static double parseDouble(String value) {
        try {
            double result = Double.parseDouble(value);
            if (!Double.isFinite(result)) {
                throw new IllegalArgumentException("Could not parse Double value: '" + value + "'. Got: " + result);
            }
            return result;
        } catch (Exception e) {
            throw new IllegalArgumentException("Could not parse Double value: '" + value + "'", e);
        }
    }

    @Nullable
    public static Double parseDoubleOrNull(@Nullable String value) {
        if (value == null) {
            return null;
        }
        try {
            double result = Double.parseDouble(value);
            return Double.isFinite(result)
                    ? result
                    : null;
        } catch (Exception e) {
            return null;
        }
    }

    public static double parseDoubleOrDefault(@Nullable String value, double defaultValue) {
        Double result = parseDoubleOrNull(value);
        return result == null ? defaultValue : result;
    }

    public static Optional<Double> parseDoubleOrEmpty(@Nullable String value) {
        return Optional.ofNullable(parseDoubleOrNull(value));
    }
}
