package com.coditory.quark.common.util

import spock.lang.Specification
import spock.lang.Unroll

import static Shorts.*

class ShortsSpec extends Specification {
    def "emptyShortArray() == []"() {
        expect:
            emptyShortArray() == [] as short[]
    }

    @Unroll
    def "should parse short value: #value"() {
        expect:
            parseShortOrNull(value) == expected as Short
        where:
            value   || expected
            "0"     || 0
            "-1"    || -1
            "1"     || 1
            "123"   || 123
            "1.1"   || null
            "abc"   || null
            "32767" || Short.MAX_VALUE
            "32768" || null
            null    || null
    }

    @Unroll
    def "should parse optional short"() {
        expect:
            parseShortOrEmpty(value) == expected
        where:
            value || expected
            "1"   || Optional.of(1 as Short)
            "a"   || Optional.empty()
    }

    @Unroll
    def "should parse short or return default value"() {
        expect:
            parseShortOrDefault(value, defaultValue as Short) == expected as Short
        where:
            value | defaultValue || expected
            "1"   | 2            || 1
            "2"   | 1            || 2
            "abc" | 2            || 2
    }

    def "should parse short or throw error"() {
        when:
            parseShort("1") == 1 as Short
        then:
            noExceptionThrown()

        when:
            parseShort("abc")
        then:
            IllegalArgumentException e = thrown(IllegalArgumentException)
            e.message == "Could not parse Short value: 'abc'"
    }

    def "closestCast(#input) == #expected"() {
        expect:
            closestCast(input) == expected as short
        where:
            input                || expected
            123                  || 123
            Short.MAX_VALUE + 10 || Short.MAX_VALUE
            Short.MIN_VALUE - 10 || Short.MIN_VALUE
    }

    def "checkedCast(#input)"() {
        expect:
            checkedCast(input) == expected as short
        where:
            input           || expected
            123             || 123
            Short.MAX_VALUE || Short.MAX_VALUE
            Short.MIN_VALUE || Short.MIN_VALUE
    }

    def "checkedCast(#input) throws error when input overflows int"() {
        when:
            checkedCast(input as int)
        then:
            thrown(IllegalArgumentException)
        where:
            input << [
                    Short.MAX_VALUE + 10L,
                    Short.MIN_VALUE - 10L
            ]
    }

    @Unroll
    def "contains(#array, #target) == #expected"() {
        expect:
            contains(array as short[], target as short) == expected
        where:
            array  | target || expected
            [1, 2] | 1      || true
            []     | 1      || false
            [1, 2] | 3      || false
    }

    def "shuffle(#array)"() {
        given:
            short[] input = [1, 2, 3]
        expect:
            shuffle(input, new Random(123)) != input
    }

    def "swap(#array, a, b)"() {
        given:
            short[] array = [1, 2, 3]
        when:
            swap(array, 1, 2)
        then:
            array == [1, 3, 2] as short[]
    }

    def "concat(#a, #b)"() {
        expect:
            concat([1, 2] as short[], [3] as short[]) == [1, 2, 3] as short[]
        and:
            concat([1, 2] as short[], [] as short[], [3] as short[]) == [1, 2, 3] as short[]
    }

    def "ensureCapacity(#array, #minLength, #padding)"() {
        expect:
            ensureCapacity([1, 2] as short[], 3, 2) == [1, 2, 0, 0, 0] as short[]
        and:
            ensureCapacity([1, 2] as short[], 2, 2) == [1, 2] as short[]
    }

    def "ensureCapacity(#array, #minLength)"() {
        expect:
            ensureCapacity([1, 2] as short[], 3) == [1, 2, 0] as short[]
        and:
            ensureCapacity([1, 2] as short[], 2) == [1, 2] as short[]
    }

    def "reverse(#array)"() {
        given:
            short[] array = [1, 2]
        when:
            reverse(array)
        then:
            array == [2, 1] as short[]
    }

    def "min(#array)"() {
        expect:
            min([1, -1, 2] as short[]) == -1 as short
    }

    def "min([]) should throw error on empty array"() {
        when:
            min([] as short[])
        then:
            thrown(IllegalArgumentException)
    }

    def "max(#array)"() {
        expect:
            max([1, 2, -1] as short[]) == 2 as short
    }

    def "max([]) should throw error on empty array"() {
        when:
            max([] as short[])
        then:
            thrown(IllegalArgumentException)
    }

    @Unroll
    def "contains(#array, #item) == #expected"() {
        expect:
            contains(array as short[], item as short) == expected
        where:
            array      | item || expected
            [1, 2, -1] | 2    || true
            []         | 0    || false
            [1, 2, -1] | -2   || false
    }

    @Unroll
    def "indexOf(#array, #item) == #expected"() {
        expect:
            indexOf(array as short[], item as short) == expected
        where:
            array         | item || expected
            [1, 2, -1, 2] | 2    || 1
            []            | 0    || -1
            [1, 2, -1]    | -2   || -1
    }

    @Unroll
    def "lastIndexOf(#array, #item) == #expected"() {
        expect:
            lastIndexOf(array as short[], item as short) == expected
        where:
            array         | item || expected
            [1, 2, -1, 2] | 2    || 3
            []            | 0    || -1
            [1, 2, -1]    | -2   || -1
    }

    def "toArray(#list)"() {
        when:
            short[] result = toArray([1, 2, -1])
        then:
            result == [1, 2, -1] as short[]
    }
}
