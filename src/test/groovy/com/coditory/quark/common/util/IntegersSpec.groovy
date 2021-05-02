package com.coditory.quark.common.util

import spock.lang.Specification
import spock.lang.Unroll

import static Integers.*

class IntegersSpec extends Specification {
    def "emptyIntArray() == []"() {
        expect:
            emptyIntArray() == [] as int[]
    }


    @Unroll
    def "first(#input) == #expected"() {
        expect:
            first(input as int[]) == expected
        where:
            input  || expected
            [1, 2] || 1
            [2, 1] || 2
    }

    @Unroll
    def "firstOrDefault(#input, #defaultValue) == #expected"() {
        expect:
            firstOrDefault(input as int[], defaultValue) == expected
        where:
            input  | defaultValue || expected
            [1, 2] | 3            || 1
            [2, 1] | 3            || 2
            []     | 3            || 3
            null   | 3            || 3
    }

    @Unroll
    def "first(#input) - should throw error"() {
        when:
            first(input as int[])
        then:
            IllegalArgumentException e = thrown(IllegalArgumentException)
            e.message.startsWith("Expected non-empty array")
        where:
            input << [null, []]
    }

    @Unroll
    def "last(#input) == #expected"() {
        expect:
            last(input as int[]) == expected
        where:
            input  || expected
            [1, 2] || 2
            [2, 1] || 1
    }

    @Unroll
    def "lastOrDefault(#input, #defaultValue) == #expected"() {
        expect:
            lastOrDefault(input as int[], defaultValue) == expected
        where:
            input  | defaultValue || expected
            [1, 2] | 3            || 2
            [2, 1] | 3            || 1
            []     | 3            || 3
            null   | 3            || 3
    }

    @Unroll
    def "last(#input) - should throw error"() {
        when:
            last(input as int[])
        then:
            IllegalArgumentException e = thrown(IllegalArgumentException)
            e.message.startsWith("Expected non-empty array")
        where:
            input << [null, []]
    }

    @Unroll
    def "should parse int value: #value"() {
        expect:
            parseIntegerOrNull(value) == expected
        where:
            value        || expected
            "0"          || 0
            "-1"         || -1
            "1"          || 1
            "123"        || 123
            "1.1"        || null
            "abc"        || null
            "2147483647" || Integer.MAX_VALUE
            "2147483648" || null
            null         || null
    }

    @Unroll
    def "should parse optional int"() {
        expect:
            parseIntegerOrEmpty(value) == expected
        where:
            value || expected
            "1"   || Optional.of(1)
            "a"   || Optional.empty()
    }

    @Unroll
    def "should parse int or return default value"() {
        expect:
            parseIntegerOrDefault(value, defaultValue) == expected
        where:
            value | defaultValue || expected
            "1"   | 2            || 1
            "2"   | 1            || 2
            "abc" | 2            || 2
    }

    def "should parse int or throw error"() {
        when:
            parseInteger("1") == 1
        then:
            noExceptionThrown()

        when:
            parseInteger("abc")
        then:
            IllegalArgumentException e = thrown(IllegalArgumentException)
            e.message == "Could not parse Integer value: 'abc'"
    }

    def "closestCast(#input) == #expected"() {
        expect:
            closestCast(input) == expected
        where:
            input                   || expected
            123L                    || 123
            Integer.MAX_VALUE + 10L || Integer.MAX_VALUE
            Integer.MIN_VALUE - 10L || Integer.MIN_VALUE
    }

    def "checkedCast(#input)"() {
        expect:
            checkedCast(input) == expected
        where:
            input             || expected
            123L              || 123
            Integer.MAX_VALUE || Integer.MAX_VALUE
            Integer.MIN_VALUE || Integer.MIN_VALUE
    }

    def "checkedCast(#input) throws error when input overflows int"() {
        when:
            checkedCast(input as long)
        then:
            thrown(IllegalArgumentException)
        where:
            input << [
                    Integer.MAX_VALUE + 10L,
                    Integer.MIN_VALUE - 10L
            ]
    }

    @Unroll
    def "contains(#array, #target) == #expected"() {
        expect:
            contains(array as int[], target) == expected
        where:
            array  | target || expected
            [1, 2] | 1      || true
            []     | 1      || false
            [1, 2] | 3      || false
    }

    def "shuffle(#array)"() {
        given:
            int[] input = [1, 2, 3]
        expect:
            shuffle(input, new Random(123)) != input
    }

    def "swap(#array, a, b)"() {
        given:
            int[] array = [1, 2, 3]
        when:
            swap(array, 1, 2)
        then:
            array == [1, 3, 2] as int[]
    }

    def "concat(#a, #b)"() {
        expect:
            concat([1, 2] as int[], [3] as int[]) == [1, 2, 3] as int[]
        and:
            concat([1, 2] as int[], [] as int[], [3] as int[]) == [1, 2, 3] as int[]
    }

    def "ensureCapacity(#array, #minLength, #padding)"() {
        expect:
            ensureCapacity([1, 2] as int[], 3, 2) == [1, 2, 0, 0, 0] as int[]
        and:
            ensureCapacity([1, 2] as int[], 2, 2) == [1, 2] as int[]
    }

    def "ensureCapacity(#array, #minLength)"() {
        expect:
            ensureCapacity([1, 2] as int[], 3) == [1, 2, 0] as int[]
        and:
            ensureCapacity([1, 2] as int[], 2) == [1, 2] as int[]
    }

    def "reverse(#array)"() {
        given:
            int[] array = [1, 2]
        when:
            reverse(array)
        then:
            array == [2, 1] as int[]
    }

    def "min(#array)"() {
        expect:
            min([1, -1, 2] as int[]) == -1 as byte
    }

    def "min([]) should throw error on empty array"() {
        when:
            min([] as int[])
        then:
            thrown(IllegalArgumentException)
    }

    def "max(#array)"() {
        expect:
            max([1, 2, -1] as int[]) == 2 as byte
    }

    def "max([]) should throw error on empty array"() {
        when:
            max([] as int[])
        then:
            thrown(IllegalArgumentException)
    }

    @Unroll
    def "contains(#array, #item) == #expected"() {
        expect:
            contains(array as int[], item) == expected
        where:
            array      | item || expected
            [1, 2, -1] | 2    || true
            []         | 0    || false
            [1, 2, -1] | -2   || false
    }

    @Unroll
    def "indexOf(#array, #item) == #expected"() {
        expect:
            indexOf(array as int[], item) == expected
        where:
            array         | item || expected
            [1, 2, -1, 2] | 2    || 1
            []            | 0    || -1
            [1, 2, -1]    | -2   || -1
    }

    @Unroll
    def "lastIndexOf(#array, #item) == #expected"() {
        expect:
            lastIndexOf(array as int[], item) == expected
        where:
            array         | item || expected
            [1, 2, -1, 2] | 2    || 3
            []            | 0    || -1
            [1, 2, -1]    | -2   || -1
    }

    def "toArray(#list)"() {
        when:
            int[] result = toArray([1, 2, -1])
        then:
            result == [1, 2, -1] as int[]
    }
}
