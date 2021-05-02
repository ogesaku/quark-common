package com.coditory.quark.common.util

import spock.lang.Specification
import spock.lang.Unroll

import static Longs.*

class LongsSpec extends Specification {
    def "emptyLongArray() == []"() {
        expect:
            emptyLongArray() == [] as long[]
    }

    @Unroll
    def "first(#input) == #expected"() {
        expect:
            first(input as long[]) == expected as long
        where:
            input  || expected
            [1, 2] || 1
            [2, 1] || 2
    }

    @Unroll
    def "firstOrDefault(#input, #defaultValue) == #expected"() {
        expect:
            firstOrDefault(input as long[], defaultValue as long) == expected as long
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
            first(input as long[])
        then:
            IllegalArgumentException e = thrown(IllegalArgumentException)
            e.message.startsWith("Expected non-empty array")
        where:
            input << [null, []]
    }

    @Unroll
    def "last(#input) == #expected"() {
        expect:
            last(input as long[]) == expected as long
        where:
            input  || expected
            [1, 2] || 2
            [2, 1] || 1
    }

    @Unroll
    def "lastOrDefault(#input, #defaultValue) == #expected"() {
        expect:
            lastOrDefault(input as long[], defaultValue as long) == expected as long
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
            last(input as long[])
        then:
            IllegalArgumentException e = thrown(IllegalArgumentException)
            e.message.startsWith("Expected non-empty array")
        where:
            input << [null, []]
    }

    @Unroll
    def "should parse long value: #value"() {
        expect:
            parseLongOrNull(value) == expected as Long
        where:
            value                 || expected
            "0"                   || 0
            "-1"                  || -1
            "1"                   || 1
            "123"                 || 123
            "1.1"                 || null
            "abc"                 || null
            "9223372036854775807" || Long.MAX_VALUE
            "9223372036854775808" || null
            null                  || null
    }

    @Unroll
    def "should parse optional long"() {
        expect:
            parseLongOrEmpty(value) == expected
        where:
            value || expected
            "1"   || Optional.of(1 as Long)
            "a"   || Optional.empty()
    }

    @Unroll
    def "should parse long or return default value"() {
        expect:
            parseLongOrDefault(value, defaultValue as Long) == expected as Long
        where:
            value | defaultValue || expected
            "1"   | 2            || 1
            "2"   | 1            || 2
            "abc" | 2            || 2
    }

    def "should parse long or throw error"() {
        when:
            parseLong("1") == 1 as Byte
        then:
            noExceptionThrown()

        when:
            parseLong("abc")
        then:
            IllegalArgumentException e = thrown(IllegalArgumentException)
            e.message == "Could not parse Long value: 'abc'"
    }

    @Unroll
    def "should format to human readable"() {
        when:
            String result = formatToHumanReadable(number)
        then:
            result == expected
        where:
            number                 || expected
            0                      || "0"
            123                    || "123"
            -1_123                 || "-1.12k"
            1_123                  || "1.12k"
            1_100                  || "1.1k"
            1_000                  || "1k"
            12_123                 || "12.12k"
            123_123                || "123.12k"
            1_123_123              || "1.12M"
            12_123_123             || "12.12M"
            12_123_123_123         || "12.12G"
            12_123_123_123_123     || "12.12T"
            12_123_123_123_123_123 || "12.12P"
            Long.MAX_VALUE         || "9.22E"
            Long.MIN_VALUE         || "-9.22E"
    }

    @Unroll
    def "should format to human readable with out decimals"() {
        when:
            String result = formatToHumanReadable(number, HUGE_NUMBER_FORMATTER_WITHOUT_DECIMALS)
        then:
            result == expected
        where:
            number                 || expected
            123                    || "123"
            -1_123                 || "-1k"
            1_123                  || "1k"
            12_123                 || "12k"
            123_123                || "123k"
            1_123_123              || "1M"
            12_123_123             || "12M"
            12_123_123_123         || "12G"
            12_123_123_123_123     || "12T"
            12_123_123_123_123_123 || "12P"
    }

    @Unroll
    def "contains(#array, #target) == #expected"() {
        expect:
            contains(array as long[], target as long) == expected
        where:
            array  | target || expected
            [1, 2] | 1      || true
            []     | 1      || false
            [1, 2] | 3      || false
    }

    def "shuffle(#array)"() {
        given:
            long[] input = [1, 2, 3]
        expect:
            shuffle(input, new Random(123)) != input
    }

    def "swap(#array, a, b)"() {
        given:
            long[] array = [1, 2, 3]
        when:
            swap(array, 1, 2)
        then:
            array == [1, 3, 2] as long[]
    }

    def "concat(#a, #b)"() {
        expect:
            concat([1, 2] as long[], [3] as long[]) == [1, 2, 3] as long[]
        and:
            concat([1, 2] as long[], [] as long[], [3] as long[]) == [1, 2, 3] as long[]
    }

    def "ensureCapacity(#array, #minLength, #padding)"() {
        expect:
            ensureCapacity([1, 2] as long[], 3, 2) == [1, 2, 0, 0, 0] as long[]
        and:
            ensureCapacity([1, 2] as long[], 2, 2) == [1, 2] as long[]
    }

    def "ensureCapacity(#array, #minLength)"() {
        expect:
            ensureCapacity([1, 2] as long[], 3) == [1, 2, 0] as long[]
        and:
            ensureCapacity([1, 2] as long[], 2) == [1, 2] as long[]
    }

    def "reverse(#array)"() {
        given:
            long[] array = [1, 2]
        when:
            reverse(array)
        then:
            array == [2, 1] as long[]
    }

    def "min(#array)"() {
        expect:
            min([1, -1, 2] as long[]) == -1 as long
    }

    def "min([]) should throw error on empty array"() {
        when:
            min([] as long[])
        then:
            thrown(IllegalArgumentException)
    }

    def "max(#array)"() {
        expect:
            max([1, 2, -1] as long[]) == 2 as long
    }

    def "max([]) should throw error on empty array"() {
        when:
            max([] as long[])
        then:
            thrown(IllegalArgumentException)
    }

    @Unroll
    def "contains(#array, #item) == #expected"() {
        expect:
            contains(array as long[], item as long) == expected
        where:
            array      | item || expected
            [1, 2, -1] | 2    || true
            []         | 0    || false
            [1, 2, -1] | -2   || false
    }

    @Unroll
    def "indexOf(#array, #item) == #expected"() {
        expect:
            indexOf(array as long[], item as long) == expected
        where:
            array         | item || expected
            [1, 2, -1, 2] | 2    || 1
            []            | 0    || -1
            [1, 2, -1]    | -2   || -1
    }

    @Unroll
    def "lastIndexOf(#array, #item) == #expected"() {
        expect:
            lastIndexOf(array as long[], item as long) == expected
        where:
            array         | item || expected
            [1, 2, -1, 2] | 2    || 3
            []            | 0    || -1
            [1, 2, -1]    | -2   || -1
    }

    def "toArray(#list)"() {
        when:
            long[] result = toArray([1, 2, -1])
        then:
            result == [1, 2, -1] as long[]
    }
}
