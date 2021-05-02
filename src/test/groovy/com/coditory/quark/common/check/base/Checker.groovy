package com.coditory.quark.common.check.base


import groovy.transform.CompileStatic
import org.jetbrains.annotations.Nullable

import java.util.function.BiFunction
import java.util.function.Function

@CompileStatic
interface Checker {
    Class<? extends RuntimeException> exceptionType()

    void check(boolean valid, String message, Object... args)

    public <T> T checkAll(@Nullable T value, String name, BiFunction<T, String, T>... expects)

    public <T> T checkAll(@Nullable T value, Function<T, T>... expects)

    public <T> T nonNull(@Nullable T value)

    public <T> T nonNull(@Nullable T value, String name)

    String nonBlank(@Nullable String value)

    String nonBlank(@Nullable String value, String name)

    public <E, C extends Collection<E>> C nonEmpty(@Nullable C collection)

    public <E, C extends Collection<E>> C nonEmpty(@Nullable C value, String name)

    public <K, V, M extends Map<K, V>> M nonEmpty(@Nullable M value)

    public <K, V, M extends Map<K, V>> M nonEmpty(@Nullable M value, String name)

    boolean[] nonEmpty(@Nullable boolean[] array)

    boolean[] nonEmpty(@Nullable boolean[] array, String name)

    byte[] nonEmpty(@Nullable byte[] array)

    byte[] nonEmpty(@Nullable byte[] array, String name)

    char[] nonEmpty(@Nullable char[] array)

    char[] nonEmpty(@Nullable char[] array, String name)

    double[] nonEmpty(@Nullable double[] array)

    double[] nonEmpty(@Nullable double[] array, String name)

    float[] nonEmpty(@Nullable float[] array)

    float[] nonEmpty(@Nullable float[] array, String name)

    int[] nonEmpty(@Nullable int[] array)

    int[] nonEmpty(@Nullable int[] array, String name)

    long[] nonEmpty(@Nullable long[] array)

    long[] nonEmpty(@Nullable long[] array, String name)

    short[] nonEmpty(@Nullable short[] array)

    short[] nonEmpty(@Nullable short[] array, String name)

    public <T> T[] nonEmpty(@Nullable T[] array)

    public <T> T[] nonEmpty(@Nullable T[] array, String name)

    int positive(byte value)

    int positive(byte value, String name)

    int positive(short value)

    int positive(short value, String name)

    int positive(long value)

    int positive(long value, String name)

    int positive(float value)

    int positive(float value, String name)

    int positive(double value)

    int positive(double value, String name)

    int positive(int value)

    int positive(int value, String name)

    int negative(byte value)

    int negative(byte value, String name)

    int negative(short value)

    int negative(short value, String name)

    int negative(long value)

    int negative(long value, String name)

    int negative(float value)

    int negative(float value, String name)

    int negative(double value)

    int negative(double value, String name)

    int negative(int value)

    int negative(int value, String name)

    int nonNegative(byte value)

    int nonNegative(byte value, String name)

    int nonNegative(short value)

    int nonNegative(short value, String name)

    int nonNegative(long value)

    int nonNegative(long value, String name)

    int nonNegative(float value)

    int nonNegative(float value, String name)

    int nonNegative(double value)

    int nonNegative(double value, String name)

    int nonNegative(int value)

    int nonNegative(int value, String name)

    int nonPositive(byte value)

    int nonPositive(byte value, String name)

    int nonPositive(short value)

    int nonPositive(short value, String name)

    int nonPositive(long value)

    int nonPositive(long value, String name)

    int nonPositive(float value)

    int nonPositive(float value, String name)

    int nonPositive(double value)

    int nonPositive(double value, String name)

    int nonPositive(int value)

    int nonPositive(int value, String name)

    int checkPositionIndex(int index, int size);

    int checkPositionIndex(int index, int size, String indexName);
}
