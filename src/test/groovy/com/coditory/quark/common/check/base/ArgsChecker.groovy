package com.coditory.quark.common.check.base

import com.coditory.quark.common.check.Args
import groovy.transform.CompileStatic
import org.jetbrains.annotations.Nullable

import java.util.function.BiFunction
import java.util.function.Function

@CompileStatic
class ArgsChecker implements Checker {
    @Override
    Class<? extends RuntimeException> exceptionType() {
        return IllegalArgumentException.class
    }

    @Override
    void check(boolean valid, String message, Object... args) {
        Args.check(valid, message, args)
    }

    @Override
    public <T> T checkAll(@Nullable T value, String name, BiFunction<T, String, T>... expects) {
        return Args.checkAll(value, name, expects)
    }

    @Override
    public <T> T checkAll(T value, Function<T, T>... expects) {
        return Args.checkAll(value, expects)
    }

    @Override
    public <T> T nonNull(@Nullable T value) {
        return Args.checkNotNull(value)
    }

    @Override
    public <T> T nonNull(@Nullable T value, String name) {
        return Args.checkNotNull(value, name)
    }

    @Override
    String nonBlank(@Nullable String value) {
        return Args.checkNotBlank(value)
    }

    @Override
    String nonBlank(@Nullable String value, String name) {
        return Args.checkNotBlank(value, name)
    }

    @Override
    public <E, C extends Collection<E>> C nonEmpty(@Nullable C collection) {
        return Args.checkNotEmpty(collection)
    }

    @Override
    public <E, C extends Collection<E>> C nonEmpty(@Nullable C value, String name) {
        return Args.checkNotEmpty(value, name)
    }

    @Override
    public <K, V, M extends Map<K, V>> M nonEmpty(@Nullable M value) {
        return Args.checkNotEmpty(value)
    }

    @Override
    public <K, V, M extends Map<K, V>> M nonEmpty(@Nullable M value, String name) {
        return Args.checkNotEmpty(value, name)
    }

    @Override
    boolean[] nonEmpty(@Nullable boolean[] array) {
        return Args.checkNotEmpty(array)
    }

    @Override
    boolean[] nonEmpty(@Nullable boolean[] array, String name) {
        return Args.checkNotEmpty(array, name)
    }

    @Override
    byte[] nonEmpty(@Nullable byte[] array) {
        return Args.checkNotEmpty(array)
    }

    @Override
    byte[] nonEmpty(@Nullable byte[] array, String name) {
        return Args.checkNotEmpty(array, name)
    }

    @Override
    char[] nonEmpty(@Nullable char[] array) {
        return Args.checkNotEmpty(array)
    }

    @Override
    char[] nonEmpty(@Nullable char[] array, String name) {
        return Args.checkNotEmpty(array, name)
    }

    @Override
    double[] nonEmpty(@Nullable double[] array) {
        return Args.checkNotEmpty(array)
    }

    @Override
    double[] nonEmpty(@Nullable double[] array, String name) {
        return Args.checkNotEmpty(array, name)
    }

    @Override
    float[] nonEmpty(@Nullable float[] array) {
        return Args.checkNotEmpty(array)
    }

    @Override
    float[] nonEmpty(@Nullable float[] array, String name) {
        return Args.checkNotEmpty(array, name)
    }

    @Override
    int[] nonEmpty(@Nullable int[] array) {
        return Args.checkNotEmpty(array)
    }

    @Override
    int[] nonEmpty(@Nullable int[] array, String name) {
        return Args.checkNotEmpty(array, name)
    }

    @Override
    long[] nonEmpty(@Nullable long[] array) {
        return Args.checkNotEmpty(array)
    }

    @Override
    long[] nonEmpty(@Nullable long[] array, String name) {
        return Args.checkNotEmpty(array, name)
    }

    @Override
    short[] nonEmpty(@Nullable short[] array) {
        return Args.checkNotEmpty(array)
    }

    @Override
    short[] nonEmpty(@Nullable short[] array, String name) {
        return Args.checkNotEmpty(array, name)
    }

    @Override
    def <T> T[] nonEmpty(@Nullable T[] array) {
        return Args.checkNotEmpty(array)
    }

    @Override
    def <T> T[] nonEmpty(@Nullable T[] array, String name) {
        return Args.checkNotEmpty(array, name)
    }

    @Override
    int positive(byte value) {
        return Args.checkPositive(value)
    }

    @Override
    int positive(byte value, String name) {
        return Args.checkPositive(value, name)
    }

    @Override
    int positive(short value) {
        return Args.checkPositive(value)
    }

    @Override
    int positive(short value, String name) {
        return Args.checkPositive(value, name)
    }

    @Override
    int positive(long value) {
        return Args.checkPositive(value)
    }

    @Override
    int positive(long value, String name) {
        return Args.checkPositive(value, name)
    }

    @Override
    int positive(float value) {
        return Args.checkPositive(value)
    }

    @Override
    int positive(float value, String name) {
        return Args.checkPositive(value, name)
    }

    @Override
    int positive(double value) {
        return Args.checkPositive(value)
    }

    @Override
    int positive(double value, String name) {
        return Args.checkPositive(value, name)
    }

    @Override
    int positive(int value) {
        return Args.checkPositive(value)
    }

    @Override
    int positive(int value, String name) {
        return Args.checkPositive(value, name)
    }

    @Override
    int negative(byte value) {
        return Args.checkNegative(value)
    }

    @Override
    int negative(byte value, String name) {
        return Args.checkNegative(value, name)
    }

    @Override
    int negative(short value) {
        return Args.checkNegative(value)
    }

    @Override
    int negative(short value, String name) {
        return Args.checkNegative(value, name)
    }

    @Override
    int negative(long value) {
        return Args.checkNegative(value)
    }

    @Override
    int negative(long value, String name) {
        return Args.checkNegative(value, name)
    }

    @Override
    int negative(float value) {
        return Args.checkNegative(value)
    }

    @Override
    int negative(float value, String name) {
        return Args.checkNegative(value, name)
    }

    @Override
    int negative(double value) {
        return Args.checkNegative(value)
    }

    @Override
    int negative(double value, String name) {
        return Args.checkNegative(value, name)
    }

    @Override
    int negative(int value) {
        return Args.checkNegative(value)
    }

    @Override
    int negative(int value, String name) {
        return Args.checkNegative(value, name)
    }

    @Override
    int nonNegative(byte value) {
        return Args.checkNotNegative(value)
    }

    @Override
    int nonNegative(byte value, String name) {
        return Args.checkNotNegative(value, name)
    }

    @Override
    int nonNegative(short value) {
        return Args.checkNotNegative(value)
    }

    @Override
    int nonNegative(short value, String name) {
        return Args.checkNotNegative(value, name)
    }

    @Override
    int nonNegative(long value) {
        return Args.checkNotNegative(value)
    }

    @Override
    int nonNegative(long value, String name) {
        return Args.checkNotNegative(value, name)
    }

    @Override
    int nonNegative(float value) {
        return Args.checkNotNegative(value)
    }

    @Override
    int nonNegative(float value, String name) {
        return Args.checkNotNegative(value, name)
    }

    @Override
    int nonNegative(double value) {
        return Args.checkNotNegative(value)
    }

    @Override
    int nonNegative(double value, String name) {
        return Args.checkNotNegative(value, name)
    }

    @Override
    int nonNegative(int value) {
        return Args.checkNotNegative(value)
    }

    @Override
    int nonNegative(int value, String name) {
        return Args.checkNotNegative(value, name)
    }

    @Override
    int nonPositive(byte value) {
        return Args.checkNotPositive(value)
    }

    @Override
    int nonPositive(byte value, String name) {
        return Args.checkNotPositive(value, name)
    }

    @Override
    int nonPositive(short value) {
        return Args.checkNotPositive(value)
    }

    @Override
    int nonPositive(short value, String name) {
        return Args.checkNotPositive(value, name)
    }

    @Override
    int nonPositive(long value) {
        return Args.checkNotPositive(value)
    }

    @Override
    int nonPositive(long value, String name) {
        return Args.checkNotPositive(value, name)
    }

    @Override
    int nonPositive(float value) {
        return Args.checkNotPositive(value)
    }

    @Override
    int nonPositive(float value, String name) {
        return Args.checkNotPositive(value, name)
    }

    @Override
    int nonPositive(double value) {
        return Args.checkNotPositive(value)
    }

    @Override
    int nonPositive(double value, String name) {
        return Args.checkNotPositive(value, name)
    }

    @Override
    int nonPositive(int value) {
        return Args.checkNotPositive(value)
    }

    @Override
    int nonPositive(int value, String name) {
        return Args.checkNotPositive(value, name)
    }

    @Override
    int checkPositionIndex(int index, int size) {
        return Args.checkPositionIndex(index, size)
    }

    @Override
    int checkPositionIndex(int index, int size, String indexName) {
        return Args.checkPositionIndex(index, size, indexName)
    }

    @Override
    String checkNotContains(@Nullable String value, char c) {
        return Args.checkNotContains(value, c)
    }

    @Override
    String checkNotContains(@Nullable String value, char c, String name) {
        return Args.checkNotContains(value, c, name)
    }

    @Override
    String checkNotContains(@Nullable String value, String c) {
        return Args.checkNotContains(value, c)
    }

    @Override
    String checkNotContains(@Nullable String value, String chunk, String name) {
        return Args.checkNotContains(value, chunk, name)
    }

    @Override
    String checkNotContainsAnyChar(@Nullable String value, String chars) {
        return Args.checkNotContainsAnyChar(value, chars)
    }

    @Override
    String checkNotContainsAnyChar(String value, String chars, String name) {
        return Args.checkNotContainsAnyChar(value, chars, name)
    }
}
