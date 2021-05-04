package com.coditory.quark.common.check


import com.coditory.quark.common.check.base.Checker
import spock.lang.Specification
import spock.lang.Unroll

import java.util.function.BiFunction
import java.util.function.Function

abstract class CheckSpec extends Specification {
    abstract Checker getChecker()

    @Unroll
    def "should throw error on non-met expectation"() {
        when:
            checker.check(1 > 2, "Expected %d to be more than %d", 1, 2)
        then:
            RuntimeException e = thrown(RuntimeException)
            e.class == checker.exceptionType()
            e.message == "Expected 1 to be more than 2"
    }

    @Unroll
    def "should non-throw error on met expectation"() {
        expect:
            checker.check(2 > 1, "Expected %d to be more than %d", 2, 1)
    }

    @Unroll
    def "should throw error if any expectation fails"() {
        given:
            String value = "   "
        when:
            checker.checkAll(value,
                    checker::nonNull as Function<String, String>,
                    checker::nonBlank as Function<String, String>)
        then:
            RuntimeException e = thrown(RuntimeException)
            e.class == checker.exceptionType()
            e.message == "Expected non-blank string. Got: \"$value\""
    }

    @Unroll
    def "should throw error if any expectation fails for a field"() {
        given:
            String value = "   "
        when:
            checker.checkAll(value, "someField",
                    checker::nonNull as BiFunction<String, String, String>,
                    checker::nonBlank as BiFunction<String, String, String>)
        then:
            RuntimeException e = thrown(RuntimeException)
            e.class == checker.exceptionType()
            e.message == "Expected non-blank someField. Got: \"$value\""
    }

    @Unroll
    def "should not throw an error if all expectations passes"() {
        when:
            checker.checkAll("abc",
                    checker::nonNull as Function<String, String>,
                    checker::nonBlank as Function<String, String>)
        then:
            notThrown()
    }

    def "should fail non-null check: #value"() {
        when:
            checker.nonNull(null)
        then:
            RuntimeException e = thrown(RuntimeException)
            e.class == checker.exceptionType()
            e.message == "Expected value != null. Got: null"
    }

    @Unroll
    def "should pass non-null check: #value"() {
        expect:
            checker.nonNull(value) == value
        where:
            value << ["abc", " a ", "", " "]
    }

    @Unroll
    def "should fail non-blank check: #value"() {
        when:
            checker.nonBlank(value)
        then:
            RuntimeException e = thrown(RuntimeException)
            e.class == checker.exceptionType()
            e.message == "Expected non-blank string. Got: ${value == null ? null : "\"$value\""}"

        where:
            value << [null, "", " ", "\n", "\t", " \n\t "]
    }

    @Unroll
    def "should pass non-blank check: #value"() {
        expect:
            checker.nonNull(value) == value
        where:
            value << ["abc", " a "]
    }

    @Unroll
    def "should fail non-empty list check: #value"() {
        when:
            checker.nonEmpty(value as Collection<Object>)
        then:
            RuntimeException e = thrown(RuntimeException)
            e.class == checker.exceptionType()
            e.message == "Expected non-empty collection. Got: ${value}"

        where:
            value << [null, List.of(), Set.of()]
    }

    @Unroll
    def "should pass non-empty list check: #value"() {
        expect:
            checker.nonNull(value as Collection<Object>) == value
        where:
            value << [List.of("a"), Set.of("a")]
    }

    @Unroll
    def "should fail non-empty map check: #value"() {
        when:
            checker.nonEmpty(value as Map<Object, Object>)
        then:
            RuntimeException e = thrown(RuntimeException)
            e.class == checker.exceptionType()
            e.message == "Expected non-empty map. Got: ${value != null ? "{}" : null}"

        where:
            value << [null, Map.of()]
    }

    @Unroll
    def "should pass non-empty map check: #value"() {
        expect:
            checker.nonNull(value) == value
        where:
            value << [Map.of("a", "b")]
    }

    @Unroll
    def "should fail positive check with: #message"() {
        when:
            check(this)
        then:
            RuntimeException e = thrown(RuntimeException)
            e.class == checker.exceptionType()
            e.message == message
        where:
            message                                | check
            "Expected byte value > 0. Got: -1"     | { it.checker.positive(-1 as Byte) }
            "Expected short value > 0. Got: -1"    | { it.checker.positive(-1 as Short) }
            "Expected float value > 0. Got: -1.0"  | { it.checker.positive(-1 as Float) }
            "Expected double value > 0. Got: -1.0" | { it.checker.positive(-1 as Double) }
            "Expected int value > 0. Got: -1"      | { it.checker.positive(-1) }
            "Expected long value > 0. Got: -1"     | { it.checker.positive(-1 as Long) }
    }

    @Unroll
    def "should pass positive check"() {
        when:
            check(this)
        then:
            noExceptionThrown()
        where:
            check << [
                    { it.checker.positive(1 as Byte) },
                    { it.checker.positive(1 as Short) },
                    { it.checker.positive(1 as Float) },
                    { it.checker.positive(1 as Double) },
                    { it.checker.positive(1) },
                    { it.checker.positive(1 as Long) }
            ]
    }

    @Unroll
    def "should fail negative check with: #message"() {
        when:
            check(this)
        then:
            RuntimeException e = thrown(RuntimeException)
            e.class == checker.exceptionType()
            e.message == message
        where:
            message                               | check
            "Expected byte value < 0. Got: 1"     | { it.checker.negative(1 as Byte) }
            "Expected short value < 0. Got: 1"    | { it.checker.negative(1 as Short) }
            "Expected float value < 0. Got: 1.0"  | { it.checker.negative(1 as Float) }
            "Expected double value < 0. Got: 1.0" | { it.checker.negative(1 as Double) }
            "Expected int value < 0. Got: 1"      | { it.checker.negative(1) }
            "Expected long value < 0. Got: 1"     | { it.checker.negative(1 as Long) }
    }

    @Unroll
    def "should pass negative check"() {
        when:
            check(this)
        then:
            noExceptionThrown()
        where:
            check << [
                    { it.checker.negative(-1 as Byte) },
                    { it.checker.negative(-1 as Short) },
                    { it.checker.negative(-1 as Float) },
                    { it.checker.negative(-1 as Double) },
                    { it.checker.negative(-1) },
                    { it.checker.negative(-1 as Long) }
            ]
    }

    @Unroll
    def "should fail not negative check with: #message"() {
        when:
            check(this)
        then:
            RuntimeException e = thrown(RuntimeException)
            e.class == checker.exceptionType()
            e.message == message
        where:
            message                                 | check
            "Expected byte value >= 0. Got: -1"     | { it.checker.nonNegative(-1 as Byte) }
            "Expected short value >= 0. Got: -1"    | { it.checker.nonNegative(-1 as Short) }
            "Expected float value >= 0. Got: -1.0"  | { it.checker.nonNegative(-1 as Float) }
            "Expected double value >= 0. Got: -1.0" | { it.checker.nonNegative(-1 as Double) }
            "Expected int value >= 0. Got: -1"      | { it.checker.nonNegative(-1) }
            "Expected long value >= 0. Got: -1"     | { it.checker.nonNegative(-1 as Long) }
    }

    @Unroll
    def "should pass non-negative check"() {
        when:
            check(this)
        then:
            noExceptionThrown()
        where:
            check << [
                    { it.checker.nonNegative(0 as Byte) },
                    { it.checker.nonNegative(0 as Short) },
                    { it.checker.nonNegative(0 as Float) },
                    { it.checker.nonNegative(0 as Double) },
                    { it.checker.nonNegative(0) },
                    { it.checker.nonNegative(0 as Long) }
            ]
    }

    @Unroll
    def "should fail not positive check with: #message"() {
        when:
            check(this)
        then:
            RuntimeException e = thrown(RuntimeException)
            e.class == checker.exceptionType()
            e.message == message
        where:
            message                                | check
            "Expected byte value <= 0. Got: 1"     | { it.checker.nonPositive(1 as Byte) }
            "Expected short value <= 0. Got: 1"    | { it.checker.nonPositive(1 as Short) }
            "Expected float value <= 0. Got: 1.0"  | { it.checker.nonPositive(1 as Float) }
            "Expected double value <= 0. Got: 1.0" | { it.checker.nonPositive(1 as Double) }
            "Expected int value <= 0. Got: 1"      | { it.checker.nonPositive(1) }
            "Expected long value <= 0. Got: 1"     | { it.checker.nonPositive(1 as Long) }
    }

    @Unroll
    def "should pass not positive check"() {
        when:
            check(this)
        then:
            noExceptionThrown()
        where:
            check << [
                    { it.checker.nonPositive(0 as Byte) },
                    { it.checker.nonPositive(0 as Short) },
                    { it.checker.nonPositive(0 as Float) },
                    { it.checker.nonPositive(0 as Double) },
                    { it.checker.nonPositive(0) },
                    { it.checker.nonPositive(0 as Long) }
            ]
    }

    @Unroll
    def "should check array boolean[]"() {
        when:
            checker.nonEmpty([true] as boolean[])
        then:
            noExceptionThrown()

        when:
            checker.nonEmpty([] as boolean[])
        then:
            RuntimeException e = thrown(RuntimeException)
            e.class == checker.exceptionType()
    }

    @Unroll
    def "should check array byte[]"() {
        when:
            checker.nonEmpty([1] as byte[])
        then:
            noExceptionThrown()

        when:
            checker.nonEmpty([] as byte[])
        then:
            RuntimeException e = thrown(RuntimeException)
            e.class == checker.exceptionType()
    }

    @Unroll
    def "should check array char[]"() {
        when:
            checker.nonEmpty([1] as char[])
        then:
            noExceptionThrown()

        when:
            checker.nonEmpty([] as char[])
        then:
            RuntimeException e = thrown(RuntimeException)
            e.class == checker.exceptionType()
    }

    @Unroll
    def "should check array double[]"() {
        when:
            checker.nonEmpty([1] as double[])
        then:
            noExceptionThrown()

        when:
            checker.nonEmpty([] as double[])
        then:
            RuntimeException e = thrown(RuntimeException)
            e.class == checker.exceptionType()
    }

    @Unroll
    def "should check array float[]"() {
        when:
            checker.nonEmpty([1] as float[])
        then:
            noExceptionThrown()

        when:
            checker.nonEmpty([] as float[])
        then:
            RuntimeException e = thrown(RuntimeException)
            e.class == checker.exceptionType()
    }

    @Unroll
    def "should check array int[]"() {
        when:
            checker.nonEmpty([1] as int[])
        then:
            noExceptionThrown()

        when:
            checker.nonEmpty([] as int[])
        then:
            RuntimeException e = thrown(RuntimeException)
            e.class == checker.exceptionType()
    }

    @Unroll
    def "should check array long[]"() {
        when:
            checker.nonEmpty([1] as long[])
        then:
            noExceptionThrown()

        when:
            checker.nonEmpty([] as long[])
        then:
            RuntimeException e = thrown(RuntimeException)
            e.class == checker.exceptionType()
    }

    @Unroll
    def "should check array short[]"() {
        when:
            checker.nonEmpty([1] as short[])
        then:
            noExceptionThrown()

        when:
            checker.nonEmpty([] as short[])
        then:
            RuntimeException e = thrown(RuntimeException)
            e.class == checker.exceptionType()
    }

    @Unroll
    def "should check array Object[]"() {
        when:
            checker.nonEmpty(["abc"] as Object[])
        then:
            noExceptionThrown()

        when:
            checker.nonEmpty([] as Object[])
        then:
            RuntimeException e = thrown(RuntimeException)
            e.class == checker.exceptionType()
    }

    @Unroll
    def "checkPositionIndex(#index, #size) throws error"() {
        when:
            checker.checkPositionIndex(index, size)
        then:
            RuntimeException e = thrown(RuntimeException)
            e.class == checker.exceptionType()
        where:
            index | size
            1     | 1
            -1    | 2
            100   | 10
    }

    @Unroll
    def "checkPositionIndex(#index, #size) throws no error"() {
        when:
            checker.checkPositionIndex(index, size)
        then:
            noExceptionThrown()
        where:
            index | size
            0     | 1
            1     | 2
            10    | 100
    }

    @Unroll
    def "checkNotContains(#value, #chunk) throws no error"() {
        when:
            checker.checkNotContains(value, chunk)
        then:
            noExceptionThrown()
        where:
            value | chunk
            "abc" | "ac"
            "abc" | "A"
    }

    @Unroll
    def "checkNotContains(#value, #chunk) throws error"() {
        when:
            checker.checkNotContains(value, chunk)
        then:
            RuntimeException e = thrown(RuntimeException)
            e.class == checker.exceptionType()
        where:
            value | chunk
            "abc" | "abc"
            "abc" | "ab"
            "abc" | "bc"
            "abc" | "c"
    }

    @Unroll
    def "checkNotContains(#value, #c) throws no error"() {
        when:
            checker.checkNotContains(value, c as char)
        then:
            noExceptionThrown()
        where:
            value | c
            "abc" | "A"
            "abc" | "d"
    }

    @Unroll
    def "checkNotContains(#value, #c) throws error"() {
        when:
            checker.checkNotContains(value, c as char)
        then:
            RuntimeException e = thrown(RuntimeException)
            e.class == checker.exceptionType()
        where:
            value | c
            "abc" | "a"
            "abc" | "b"
    }

    @Unroll
    def "checkNotContainsAnyChar(#value, #chars) throws no error"() {
        when:
            checker.checkNotContainsAnyChar(value, chars)
        then:
            noExceptionThrown()
        where:
            value | chars
            "abc" | "Ad"
            "abc" | "AAA"
    }

    @Unroll
    def "checkNotContainsAnyChar(#value, #chars) throws error"() {
        when:
            checker.checkNotContainsAnyChar(value, chars)
        then:
            RuntimeException e = thrown(RuntimeException)
            e.class == checker.exceptionType()
        where:
            value | chars
            "abc" | "Aa"
            "abc" | "cX"
            "abc" | "AaA"
    }
}
