package com.coditory.quark.common.util;

import com.coditory.quark.common.check.Args;

import java.util.Arrays;
import java.util.Collection;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static com.coditory.quark.common.check.Args.check;
import static com.coditory.quark.common.check.Args.checkNotEmpty;
import static com.coditory.quark.common.check.Args.checkNotNull;

public final class Chars {
    private static final char[] EMPTY_CHAR_ARRAY = new char[0];

    private Chars() {
        throw new UnsupportedOperationException("Do not instantiate utility class");
    }

    public static char[] emptyCharArray() {
        return EMPTY_CHAR_ARRAY;
    }

    public static char first(char[] array) {
        checkNotEmpty(array, "array");
        return array[0];
    }

    public static char firstOrDefault(char[] array, char defaultValue) {
        return array == null || array.length == 0
                ? defaultValue
                : array[0];
    }

    public static char last(char[] array) {
        checkNotEmpty(array, "array");
        return array[array.length - 1];
    }

    public static char lastOrDefault(char[] array, char defaultValue) {
        return array == null || array.length == 0
                ? defaultValue
                : array[array.length - 1];
    }

    public static char checkedCast(int value) {
        char result = (char) value;
        check(result == value, "Out of range: %s", value);
        return result;
    }

    public static char closestCast(int value) {
        if (value > Character.MAX_VALUE) {
            return Character.MAX_VALUE;
        }
        if (value < Character.MIN_VALUE) {
            return Character.MIN_VALUE;
        }
        return (char) value;
    }

    public static boolean contains(char[] array, char target) {
        for (int value : array) {
            if (value == target) {
                return true;
            }
        }
        return false;
    }

    public static int indexOf(char[] array, char target) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == target) {
                return i;
            }
        }
        return -1;
    }

    public static int lastIndexOf(char[] array, char target) {
        for (int i = array.length - 1; i >= 0; i--) {
            if (array[i] == target) {
                return i;
            }
        }
        return -1;
    }

    public static char min(char... array) {
        check(array.length > 0, "Expected array.length > 0");
        char min = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] < min) {
                min = array[i];
            }
        }
        return min;
    }

    public static char max(char... array) {
        check(array.length > 0, "Expected array.length > 0");
        char max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        return max;
    }

    public static char[] concat(char[]... arrays) {
        int length = 0;
        for (char[] array : arrays) {
            length += array.length;
        }
        char[] result = new char[length];
        int pos = 0;
        for (char[] array : arrays) {
            System.arraycopy(array, 0, result, pos, array.length);
            pos += array.length;
        }
        return result;
    }

    public static char[] copy(char[] array) {
        checkNotNull(array);
        return Arrays.copyOf(array, array.length);
    }

    public static void reverse(char[] array) {
        checkNotNull(array);
        for (int i = 0, j = array.length - 1; i < j; i++, j--) {
            char tmp = array[i];
            array[i] = array[j];
            array[j] = tmp;
        }
    }

    public static void swap(char[] array, int i, int j) {
        char tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }

    public static char[] ensureCapacity(char[] array, int minLength) {
        return ensureCapacity(array, minLength, 0);
    }

    public static char[] ensureCapacity(char[] array, int minLength, int padding) {
        Args.check(minLength >= 0, "Invalid minLength: %s", minLength);
        Args.check(padding >= 0, "Invalid padding: %s", padding);
        return (array.length < minLength) ? Arrays.copyOf(array, minLength + padding) : array;
    }

    public static char[] toArray(Collection<Character> collection) {
        Object[] boxedArray = collection.toArray();
        int len = boxedArray.length;
        char[] array = new char[len];
        for (int i = 0; i < len; i++) {
            array[i] = ((Character) boxedArray[i]);
        }
        return array;
    }

    public static void shuffle(char[] array) {
        shuffle(array, ThreadLocalRandom.current());
    }

    public static void shuffle(char[] array, Random random) {
        for (int i = 0; i < array.length; i++) {
            int randomIndexToSwap = random.nextInt(array.length);
            char temp = array[randomIndexToSwap];
            array[randomIndexToSwap] = array[i];
            array[i] = temp;
        }
    }
}
