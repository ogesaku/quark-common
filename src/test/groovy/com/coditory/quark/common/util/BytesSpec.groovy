package com.coditory.quark.common.util

import spock.lang.Specification
import spock.lang.Unroll

import static Bytes.*

class BytesSpec extends Specification {
    def "emptyByteArray() == []"() {
        expect:
            emptyByteArray() == [] as byte[]
    }

    @Unroll
    def "first(#input) == #expected"() {
        expect:
            first(input as byte[]) == expected as byte
        where:
            input  || expected
            [1, 2] || 1
            [2, 1] || 2
    }

    @Unroll
    def "firstOrDefault(#input, #defaultValue) == #expected"() {
        expect:
            firstOrDefault(input as byte[], defaultValue as byte) == expected as byte
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
            first(input as byte[])
        then:
            IllegalArgumentException e = thrown(IllegalArgumentException)
            e.message.startsWith("Expected non-empty array")
        where:
            input << [null, []]
    }

    @Unroll
    def "last(#input) == #expected"() {
        expect:
            last(input as byte[]) == expected as byte
        where:
            input  || expected
            [1, 2] || 2
            [2, 1] || 1
    }

    @Unroll
    def "lastOrDefault(#input, #defaultValue) == #expected"() {
        expect:
            lastOrDefault(input as byte[], defaultValue as byte) == expected as byte
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
            last(input as byte[])
        then:
            IllegalArgumentException e = thrown(IllegalArgumentException)
            e.message.startsWith("Expected non-empty array")
        where:
            input << [null, []]
    }

    @Unroll
    def "should parse byte value: #value"() {
        expect:
            parseByteOrNull(value) == expected as Byte
        where:
            value || expected
            "0"   || 0
            "-1"  || -1
            "1"   || 1
            "123" || 123
            "1.1" || null
            "abc" || null
            "127" || Byte.MAX_VALUE
            "128" || null
            null  || null
    }

    @Unroll
    def "should parse optional byte"() {
        expect:
            parseByteOrEmpty(value) == expected
        where:
            value || expected
            "1"   || Optional.of(1 as Byte)
            "a"   || Optional.empty()
    }

    @Unroll
    def "should parse byte or return default value"() {
        expect:
            parseByteOrDefault(value, defaultValue as Byte) == expected as Byte
        where:
            value | defaultValue || expected
            "1"   | 2            || 1
            "2"   | 1            || 2
            "abc" | 2            || 2
    }

    def "should parse byte or throw error"() {
        when:
            parseByte("1") == 1 as Byte
        then:
            noExceptionThrown()

        when:
            parseByte("abc")
        then:
            IllegalArgumentException e = thrown(IllegalArgumentException)
            e.message == "Could not parse Byte value: 'abc'"
    }

    def "closestCast(#input) == #expected"() {
        expect:
            closestCast(input) == expected as byte
        where:
            input               || expected
            123                 || 123
            Byte.MAX_VALUE + 10 || Byte.MAX_VALUE
            Byte.MIN_VALUE - 10 || Byte.MIN_VALUE
    }

    def "checkedCast(#input)"() {
        expect:
            checkedCast(input) == expected as byte
        where:
            input          || expected
            123            || 123
            Byte.MAX_VALUE || Byte.MAX_VALUE
            Byte.MIN_VALUE || Byte.MIN_VALUE
    }

    def "checkedCast(#input) throws error when input overflows int"() {
        when:
            checkedCast(input as int)
        then:
            thrown(IllegalArgumentException)
        where:
            input << [
                    Byte.MAX_VALUE + 10L,
                    Byte.MIN_VALUE - 10L
            ]
    }

    @Unroll
    def "contains(#array, #target) == #expected"() {
        expect:
            contains(array as byte[], target as byte) == expected
        where:
            array  | target || expected
            [1, 2] | 1      || true
            []     | 1      || false
            [1, 2] | 3      || false
    }

    def "shuffle(#array)"() {
        given:
            byte[] input = [1, 2, 3]
        expect:
            shuffle(input, new Random(123)) != input
    }

    def "swap(#array, a, b)"() {
        given:
            byte[] array = [1, 2, 3]
        when:
            swap(array, 1, 2)
        then:
            array == [1, 3, 2] as byte[]
    }

    def "concat(#a, #b)"() {
        expect:
            concat([1, 2] as byte[], [3] as byte[]) == [1, 2, 3] as byte[]
        and:
            concat([1, 2] as byte[], [] as byte[], [3] as byte[]) == [1, 2, 3] as byte[]
    }

    def "ensureCapacity(#array, #minLength, #padding)"() {
        expect:
            ensureCapacity([1, 2] as byte[], 3, 2) == [1, 2, 0, 0, 0] as byte[]
        and:
            ensureCapacity([1, 2] as byte[], 2, 2) == [1, 2] as byte[]
    }

    def "ensureCapacity(#array, #minLength)"() {
        expect:
            ensureCapacity([1, 2] as byte[], 3) == [1, 2, 0] as byte[]
        and:
            ensureCapacity([1, 2] as byte[], 2) == [1, 2] as byte[]
    }

    def "reverse(#array)"() {
        given:
            byte[] array = [1, 2]
        when:
            reverse(array)
        then:
            array == [2, 1] as byte[]
    }

    def "min(#array)"() {
        expect:
            min([1, -1, 2] as byte[]) == -1 as byte
    }

    def "min([]) should throw error on empty array"() {
        when:
            min([] as byte[])
        then:
            thrown(IllegalArgumentException)
    }

    def "max(#array)"() {
        expect:
            max([1, 2, -1] as byte[]) == 2 as byte
    }

    def "max([]) should throw error on empty array"() {
        when:
            max([] as byte[])
        then:
            thrown(IllegalArgumentException)
    }

    @Unroll
    def "contains(#array, #item) == #expected"() {
        expect:
            contains(array as byte[], item as byte) == expected
        where:
            array      | item || expected
            [1, 2, -1] | 2    || true
            []         | 0    || false
            [1, 2, -1] | -2   || false
    }

    @Unroll
    def "indexOf(#array, #item) == #expected"() {
        expect:
            indexOf(array as byte[], item as byte) == expected
        where:
            array         | item || expected
            [1, 2, -1, 2] | 2    || 1
            []            | 0    || -1
            [1, 2, -1]    | -2   || -1
    }

    @Unroll
    def "lastIndexOf(#array, #item) == #expected"() {
        expect:
            lastIndexOf(array as byte[], item as byte) == expected
        where:
            array         | item || expected
            [1, 2, -1, 2] | 2    || 3
            []            | 0    || -1
            [1, 2, -1]    | -2   || -1
    }

    def "toArray(#list)"() {
        when:
            byte[] result = toArray([1, 2, -1])
        then:
            result == [1, 2, -1] as byte[]
    }
}
