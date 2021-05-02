package com.coditory.quark.common.check.base

import com.coditory.quark.commons.check.Asserts
import groovy.transform.CompileStatic
import org.jetbrains.annotations.Nullable

import java.util.function.BiFunction
import java.util.function.Function

@CompileStatic
class AssertsChecker implements Checker {
    @Override
    Class<? extends RuntimeException> exceptionType() {
        return IllegalStateException.class
    }

    @Override
    void check(boolean valid, String message, Object... args) {
        Asserts.assertThat(valid, message, args)
    }

    @Override
    <T> T checkAll(@Nullable T value, String name, BiFunction<T, String, T>... expects) {
        return Asserts.assertAll(value, name, expects)
    }

    @Override
    <T> T checkAll(T value, Function<T, T>... expects) {
        return Asserts.assertAll(value, expects)
    }

    @Override
    <T> T nonNull(@Nullable T value) {
        return Asserts.assertNotNull(value)
    }

    @Override
    <T> T nonNull(@Nullable T value, String name) {
        return Asserts.assertNotNull(value, name)
    }

    @Override
    String nonBlank(@Nullable String value) {
        return Asserts.assertNotBlank(value)
    }

    @Override
    String nonBlank(@Nullable String value, String name) {
        return Asserts.assertNotBlank(value, name)
    }

    @Override
    <E, C extends Collection<E>> C nonEmpty(@Nullable C collection) {
        return Asserts.assertNotEmpty(collection)
    }

    @Override
    <E, C extends Collection<E>> C nonEmpty(@Nullable C value, String name) {
        return Asserts.assertNotEmpty(value, name)
    }

    @Override
    <K, V, M extends Map<K, V>> M nonEmpty(@Nullable M value) {
        return Asserts.assertNotEmpty(value)
    }

    @Override
    <K, V, M extends Map<K, V>> M nonEmpty(@Nullable M value, String name) {
        return Asserts.assertNotEmpty(value, name)
    }


    @Override
    int positive(byte value) {
        return Asserts.assertPositive(value)
    }

    @Override
    int positive(byte value, String name) {
        return Asserts.assertPositive(value, name)
    }

    @Override
    int positive(short value) {
        return Asserts.assertPositive(value)
    }

    @Override
    int positive(short value, String name) {
        return Asserts.assertPositive(value, name)
    }

    @Override
    int positive(long value) {
        return Asserts.assertPositive(value)
    }

    @Override
    int positive(long value, String name) {
        return Asserts.assertPositive(value, name)
    }

    @Override
    int positive(float value) {
        return Asserts.assertPositive(value)
    }

    @Override
    int positive(float value, String name) {
        return Asserts.assertPositive(value, name)
    }

    @Override
    int positive(double value) {
        return Asserts.assertPositive(value)
    }

    @Override
    int positive(double value, String name) {
        return Asserts.assertPositive(value, name)
    }

    @Override
    int positive(int value) {
        return Asserts.assertPositive(value)
    }

    @Override
    int positive(int value, String name) {
        return Asserts.assertPositive(value, name)
    }

    @Override
    int negative(byte value) {
        return Asserts.assertNegative(value)
    }

    @Override
    int negative(byte value, String name) {
        return Asserts.assertNegative(value, name)
    }

    @Override
    int negative(short value) {
        return Asserts.assertNegative(value)
    }

    @Override
    int negative(short value, String name) {
        return Asserts.assertNegative(value, name)
    }

    @Override
    int negative(long value) {
        return Asserts.assertNegative(value)
    }

    @Override
    int negative(long value, String name) {
        return Asserts.assertNegative(value, name)
    }

    @Override
    int negative(float value) {
        return Asserts.assertNegative(value)
    }

    @Override
    int negative(float value, String name) {
        return Asserts.assertNegative(value, name)
    }

    @Override
    int negative(double value) {
        return Asserts.assertNegative(value)
    }

    @Override
    int negative(double value, String name) {
        return Asserts.assertNegative(value, name)
    }

    @Override
    int negative(int value) {
        return Asserts.assertNegative(value)
    }

    @Override
    int negative(int value, String name) {
        return Asserts.assertNegative(value, name)
    }

    @Override
    int nonNegative(byte value) {
        return Asserts.assertNotNegative(value)
    }

    @Override
    int nonNegative(byte value, String name) {
        return Asserts.assertNotNegative(value, name)
    }

    @Override
    int nonNegative(short value) {
        return Asserts.assertNotNegative(value)
    }

    @Override
    int nonNegative(short value, String name) {
        return Asserts.assertNotNegative(value, name)
    }

    @Override
    int nonNegative(long value) {
        return Asserts.assertNotNegative(value)
    }

    @Override
    int nonNegative(long value, String name) {
        return Asserts.assertNotNegative(value, name)
    }

    @Override
    int nonNegative(float value) {
        return Asserts.assertNotNegative(value)
    }

    @Override
    int nonNegative(float value, String name) {
        return Asserts.assertNotNegative(value, name)
    }

    @Override
    int nonNegative(double value) {
        return Asserts.assertNotNegative(value)
    }

    @Override
    int nonNegative(double value, String name) {
        return Asserts.assertNotNegative(value, name)
    }

    @Override
    int nonNegative(int value) {
        return Asserts.assertNotNegative(value)
    }

    @Override
    int nonNegative(int value, String name) {
        return Asserts.assertNotNegative(value, name)
    }

    @Override
    int nonPositive(byte value) {
        return Asserts.assertNotPositive(value)
    }

    @Override
    int nonPositive(byte value, String name) {
        return Asserts.assertNotPositive(value, name)
    }

    @Override
    int nonPositive(short value) {
        return Asserts.assertNotPositive(value)
    }

    @Override
    int nonPositive(short value, String name) {
        return Asserts.assertNotPositive(value, name)
    }

    @Override
    int nonPositive(long value) {
        return Asserts.assertNotPositive(value)
    }

    @Override
    int nonPositive(long value, String name) {
        return Asserts.assertNotPositive(value, name)
    }

    @Override
    int nonPositive(float value) {
        return Asserts.assertNotPositive(value)
    }

    @Override
    int nonPositive(float value, String name) {
        return Asserts.assertNotPositive(value, name)
    }

    @Override
    int nonPositive(double value) {
        return Asserts.assertNotPositive(value)
    }

    @Override
    int nonPositive(double value, String name) {
        return Asserts.assertNotPositive(value, name)
    }

    @Override
    int nonPositive(int value) {
        return Asserts.assertNotPositive(value)
    }

    @Override
    int nonPositive(int value, String name) {
        return Asserts.assertNotPositive(value, name)
    }

    @Override
    boolean[] nonEmpty(@Nullable boolean[] array) {
        return Asserts.assertNotEmpty(array)
    }

    @Override
    boolean[] nonEmpty(@Nullable boolean[] array, String name) {
        return Asserts.assertNotEmpty(array, name)
    }

    @Override
    byte[] nonEmpty(@Nullable byte[] array) {
        return Asserts.assertNotEmpty(array)
    }

    @Override
    byte[] nonEmpty(@Nullable byte[] array, String name) {
        return Asserts.assertNotEmpty(array, name)
    }

    @Override
    char[] nonEmpty(@Nullable char[] array) {
        return Asserts.assertNotEmpty(array)
    }

    @Override
    char[] nonEmpty(@Nullable char[] array, String name) {
        return Asserts.assertNotEmpty(array, name)
    }

    @Override
    double[] nonEmpty(@Nullable double[] array) {
        return Asserts.assertNotEmpty(array)
    }

    @Override
    double[] nonEmpty(@Nullable double[] array, String name) {
        return Asserts.assertNotEmpty(array, name)
    }

    @Override
    float[] nonEmpty(@Nullable float[] array) {
        return Asserts.assertNotEmpty(array)
    }

    @Override
    float[] nonEmpty(@Nullable float[] array, String name) {
        return Asserts.assertNotEmpty(array, name)
    }

    @Override
    int[] nonEmpty(@Nullable int[] array) {
        return Asserts.assertNotEmpty(array)
    }

    @Override
    int[] nonEmpty(@Nullable int[] array, String name) {
        return Asserts.assertNotEmpty(array, name)
    }

    @Override
    long[] nonEmpty(@Nullable long[] array) {
        return Asserts.assertNotEmpty(array)
    }

    @Override
    long[] nonEmpty(@Nullable long[] array, String name) {
        return Asserts.assertNotEmpty(array, name)
    }

    @Override
    short[] nonEmpty(@Nullable short[] array) {
        return Asserts.assertNotEmpty(array)
    }

    @Override
    short[] nonEmpty(@Nullable short[] array, String name) {
        return Asserts.assertNotEmpty(array, name)
    }

    @Override
    def <T> T[] nonEmpty(@Nullable T[] array) {
        return Asserts.assertNotEmpty(array)
    }

    @Override
    def <T> T[] nonEmpty(@Nullable T[] array, String name) {
        return Asserts.assertNotEmpty(array, name)
    }

    @Override
    int checkPositionIndex(int index, int size) {
        return Asserts.assertPositionIndex(index, size)
    }

    @Override
    int checkPositionIndex(int index, int size, String indexName) {
        return Asserts.assertPositionIndex(index, size, indexName)
    }
}
