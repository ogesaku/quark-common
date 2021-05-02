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

public final class Floats {
    private static final float[] EMPTY_FLOAT_ARRAY = new float[0];

    private Floats() {
        throw new UnsupportedOperationException("Do not instantiate utility class");
    }

    public static float[] emptyFloatArray() {
        return EMPTY_FLOAT_ARRAY;
    }

    public static float first(float[] array) {
        checkNotEmpty(array, "array");
        return array[0];
    }

    public static float firstOrDefault(float[] array, float defaultValue) {
        return array == null || array.length == 0
                ? defaultValue
                : array[0];
    }

    public static float last(float[] array) {
        checkNotEmpty(array, "array");
        return array[array.length - 1];
    }

    public static float lastOrDefault(float[] array, float defaultValue) {
        return array == null || array.length == 0
                ? defaultValue
                : array[array.length - 1];
    }

    public static float fuzzyEpsilon(float a, float b) {
        return Math.max(Math.ulp(a), Math.ulp(b)) * 5;
    }

    public static boolean fuzzyEquals(float a, float b) {
        return fuzzyEquals(a, b, fuzzyEpsilon(a, b));
    }

    public static boolean fuzzyEquals(float a, float b, float epsilon) {
        checkPositive(epsilon, "epsilon");
        return Math.abs(a - b) < epsilon;
    }

    public static boolean fuzzyGreaterThanOrEqual(float a, float b) {
        return fuzzyGreaterThanOrEqual(a, b, fuzzyEpsilon(a, b));
    }

    public static boolean fuzzyGreaterThanOrEqual(float a, float b, float epsilon) {
        checkPositive(epsilon, "epsilon");
        return a > b || fuzzyEquals(a, b, epsilon);
    }

    public static boolean fuzzyLowerThanOrEqual(float a, float b) {
        return fuzzyGreaterThanOrEqual(a, b, fuzzyEpsilon(a, b));
    }

    public static boolean fuzzyLowerThanOrEqual(float a, float b, float epsilon) {
        checkPositive(epsilon, "epsilon");
        return a < b || fuzzyEquals(a, b, epsilon);
    }

    public static boolean contains(float[] array, float target) {
        for (float value : array) {
            if (value == target) {
                return true;
            }
        }
        return false;
    }

    public static float indexOf(float[] array, float target) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == target) {
                return i;
            }
        }
        return -1;
    }

    public static float lastIndexOf(float[] array, float target) {
        for (int i = array.length - 1; i >= 0; i--) {
            if (array[i] == target) {
                return i;
            }
        }
        return -1;
    }

    public static float min(float... array) {
        check(array.length > 0, "Expected array.length > 0");
        float min = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] < min) {
                min = array[i];
            }
        }
        return min;
    }

    public static float max(float... array) {
        check(array.length > 0, "Expected array.length > 0");
        float max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        return max;
    }

    public static float[] concat(float[]... arrays) {
        int length = 0;
        for (float[] array : arrays) {
            length += array.length;
        }
        float[] result = new float[length];
        int pos = 0;
        for (float[] array : arrays) {
            System.arraycopy(array, 0, result, pos, array.length);
            pos += array.length;
        }
        return result;
    }

    public static float[] copy(float[] array) {
        checkNotNull(array);
        return Arrays.copyOf(array, array.length);
    }

    public static void reverse(float[] array) {
        checkNotNull(array);
        for (int i = 0, j = array.length - 1; i < j; i++, j--) {
            float tmp = array[i];
            array[i] = array[j];
            array[j] = tmp;
        }
    }

    public static void swap(float[] array, int i, int j) {
        float tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }

    public static float[] ensureCapacity(float[] array, int minLength) {
        return ensureCapacity(array, minLength, 0);
    }

    public static float[] ensureCapacity(float[] array, int minLength, int padding) {
        Args.check(minLength >= 0, "Invalid minLength: %s", minLength);
        Args.check(padding >= 0, "Invalid padding: %s", padding);
        return (array.length < minLength) ? Arrays.copyOf(array, minLength + padding) : array;
    }

    public static float[] toArray(Collection<? extends Number> collection) {
        Object[] boxedArray = collection.toArray();
        int len = boxedArray.length;
        float[] array = new float[len];
        for (int i = 0; i < len; i++) {
            array[i] = ((Number) boxedArray[i]).floatValue();
        }
        return array;
    }

    public static void shuffle(float[] array) {
        shuffle(array, ThreadLocalRandom.current());
    }

    public static void shuffle(float[] array, Random random) {
        for (int i = 0; i < array.length; i++) {
            int randomIndexToSwap = random.nextInt(array.length);
            float temp = array[randomIndexToSwap];
            array[randomIndexToSwap] = array[i];
            array[i] = temp;
        }
    }

    public static float parseFloat(String value) {
        try {
            float result = Float.parseFloat(value);
            if (!Float.isFinite(result)) {
                throw new IllegalArgumentException("Could not parse Float value: '" + value + "'. Got infinite result.");
            }
            return result;
        } catch (Exception e) {
            throw new IllegalArgumentException("Could not parse Float value: '" + value + "'", e);
        }
    }

    @Nullable
    public static Float parseFloatOrNull(@Nullable String value) {
        if (value == null) {
            return null;
        }
        try {
            float result = Float.parseFloat(value);
            return Float.isFinite(result)
                    ? result
                    : null;
        } catch (Exception e) {
            return null;
        }
    }

    public static double parseFloatOrDefault(@Nullable String value, float defaultValue) {
        Float result = parseFloatOrNull(value);
        return result == null ? defaultValue : result;
    }

    public static Optional<Float> parseFloatOrEmpty(@Nullable String value) {
        return Optional.ofNullable(parseFloatOrNull(value));
    }
}
