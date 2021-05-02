package com.coditory.quark.common.util;

import com.coditory.quark.common.check.Args;
import com.coditory.quark.common.text.CaseConverter;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Enums {
    private Enums() {
        throw new UnsupportedOperationException("Do not instantiate utility class");
    }

    public static <E extends Enum<E>> long generateBitVector(Class<E> enumClass, Iterable<? extends E> values) {
        Args.checkNotNull(enumClass, "enumClass");
        Args.checkNotNull(values, "values");
        checkBitVectorable(enumClass);
        long total = 0;
        for (E constant : values) {
            if (constant != null) {
                total |= 1L << constant.ordinal();
            }
        }
        return total;
    }

    public static <E extends Enum<E>> EnumSet<E> processBitVector(Class<E> enumClass, long value) {
        checkBitVectorable(enumClass).getEnumConstants();
        return processBitVectors(enumClass, value);
    }

    private static <E extends Enum<E>> Class<E> checkBitVectorable(Class<E> enumClass) {
        Args.checkNotNull(enumClass, "enumClass");
        E[] constants = enumClass.getEnumConstants();
        Args.check(
                constants.length <= Long.SIZE,
                "Cannot store %s %s values in %s bit",
                constants.length,
                enumClass.getSimpleName(),
                Long.SIZE
        );
        return enumClass;
    }

    public static <E extends Enum<E>> EnumSet<E> processBitVectors(Class<E> enumClass, long... values) {
        EnumSet<E> results = EnumSet.noneOf(enumClass);
        long[] lvalues = Longs.copy(values);
        Longs.reverse(lvalues);
        for (E constant : enumClass.getEnumConstants()) {
            int block = constant.ordinal() / Long.SIZE;
            if (block < lvalues.length && (lvalues[block] & 1L << (constant.ordinal() % Long.SIZE)) != 0) {
                results.add(constant);
            }
        }
        return results;
    }

    public static <E extends Enum<E>> boolean isValidEnum(Class<E> enumClass, String enumName) {
        return getEnum(enumClass, enumName) != null;
    }

    public static <E extends Enum<E>> boolean isValidEnumIgnoreCase(Class<E> enumClass, String enumName) {
        return getEnumIgnoreCase(enumClass, enumName) != null;
    }

    public static <E extends Enum<E>> Map<String, E> getEnumMap(Class<E> enumClass) {
        Map<String, E> map = new LinkedHashMap<>();
        for (E e : enumClass.getEnumConstants()) {
            map.put(e.name(), e);
        }
        return map;
    }

    public static <E extends Enum<E>> List<E> getEnumList(Class<E> enumClass) {
        return new ArrayList<>(Arrays.asList(enumClass.getEnumConstants()));
    }

    @Nullable
    public static <E extends Enum<E>> E getEnumIgnoreCase(Class<E> enumClass, String enumName) {
        return getEnumIgnoreCase(enumClass, enumName, null);
    }

    public static <E extends Enum<E>> E getEnumIgnoreCase(Class<E> enumClass, String enumName, E defaultEnum) {
        if (enumName == null || !enumClass.isEnum()) {
            return defaultEnum;
        }
        for (E each : enumClass.getEnumConstants()) {
            if (each.name().equalsIgnoreCase(enumName)) {
                return each;
            }
            String snakeCased = CaseConverter.toUpperSnakeCase(enumName);
            if (each.name().equals(snakeCased)) {
                return each;
            }
        }
        return defaultEnum;
    }

    public static <E extends Enum<E>> E getEnum(Class<E> enumClass, String enumName, E defaultEnum) {
        if (enumName == null) {
            return defaultEnum;
        }
        try {
            return Enum.valueOf(enumClass, enumName);
        } catch (IllegalArgumentException ex) {
            return defaultEnum;
        }
    }

    @Nullable
    public static <E extends Enum<E>> E getEnum(Class<E> enumClass, String enumName) {
        return getEnum(enumClass, enumName, null);
    }

}
