package com.coditory.quark.common.util

import spock.lang.Specification
import spock.lang.Unroll

import static Chars.*

class CharsSpec extends Specification {
    def "emptyCharArray() == []"() {
        expect:
            emptyCharArray() == [] as char[]
    }

    @Unroll
    def "first(#input) == #expected"() {
        expect:
            first(input as char[]) == expected as char
        where:
            input  || expected
            [1, 2] || 1
            [2, 1] || 2
    }

    @Unroll
    def "firstOrDefault(#input, #defaultValue) == #expected"() {
        expect:
            firstOrDefault(input as char[], defaultValue as char) == expected as byte
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
            first(input as char[])
        then:
            IllegalArgumentException e = thrown(IllegalArgumentException)
            e.message.startsWith("Expected non-empty array")
        where:
            input << [null, []]
    }

    @Unroll
    def "last(#input) == #expected"() {
        expect:
            last(input as char[]) == expected as byte
        where:
            input  || expected
            [1, 2] || 2
            [2, 1] || 1
    }

    @Unroll
    def "lastOrDefault(#input, #defaultValue) == #expected"() {
        expect:
            lastOrDefault(input as char[], defaultValue as char) == expected as char
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
            last(input as char[])
        then:
            IllegalArgumentException e = thrown(IllegalArgumentException)
            e.message.startsWith("Expected non-empty array")
        where:
            input << [null, []]
    }

    @Unroll
    def "contains(#array, #target) == #expected"() {
        expect:
            contains(array as char[], target as char) == expected
        where:
            array      | target || expected
            ['a', 'b'] | 'b'    || true
            []         | 1      || false
            ['a', 'b'] | 'c'    || false
    }


    def "closestCast(#input) == #expected"() {
        expect:
            closestCast(input as int) == expected
        where:
            input                    || expected
            123                      || 123
            Character.MAX_VALUE + 10 || Character.MAX_VALUE
            Character.MIN_VALUE - 10 || Character.MIN_VALUE
    }

    def "checkedCast(#input)"() {
        expect:
            checkedCast(input as int) == expected
        where:
            input               || expected
            123                 || 123
            Character.MAX_VALUE || Character.MAX_VALUE
            Character.MIN_VALUE || Character.MIN_VALUE
    }

    def "checkedCast(#input) throws error when input overflows int"() {
        when:
            checkedCast(input as int)
        then:
            thrown(IllegalArgumentException)
        where:
            input << [
                    Character.MAX_VALUE + 10L,
                    Character.MIN_VALUE - 10L
            ]
    }

    def "shuffle(#array)"() {
        given:
            char[] input = ['a', 'b', 'c'] as char[]
        expect:
            shuffle(input, new Random(123)) != input
    }

    def "swap(#array, a, b)"() {
        given:
            char[] array = ['a', 'b', 'c'] as char[]
        when:
            swap(array, 1, 2)
        then:
            array == ['a', 'c', 'b'] as char[]
    }

    def "concat(#a, #b)"() {
        expect:
            concat(['a', 'b'] as char[], ['c'] as char[]) == ['a', 'b', 'c'] as char[]
        and:
            concat(['a', 'b'] as char[], [] as char[], ['c'] as char[]) == ['a', 'b', 'c'] as char[]
    }

    def "ensureCapacity(#array, #minLength, #padding)"() {
        expect:
            ensureCapacity(['a', 'b'] as char[], 3, 2) == ['a', 'b', 0, 0, 0] as char[]
        and:
            ensureCapacity(['a', 'b'] as char[], 2, 2) == ['a', 'b'] as char[]
    }

    def "ensureCapacity(#array, #minLength)"() {
        expect:
            ensureCapacity(['a', 'b'] as char[], 3) == ['a', 'b', 0] as char[]
        and:
            ensureCapacity(['a', 'b'] as char[], 2) == ['a', 'b'] as char[]
    }

    def "reverse(#array)"() {
        given:
            char[] array = ['a', 'c', 'b'] as char[]
        when:
            reverse(array)
        then:
            array == ['b', 'c', 'a'] as char[]
    }

    def "min(#array)"() {
        expect:
            min(['a', 'c', 'b'] as char[]) == 'a' as char
    }

    def "min([]) should throw error on empty array"() {
        when:
            min([] as char[])
        then:
            thrown(IllegalArgumentException)
    }

    def "max(#array)"() {
        expect:
            max(['a', 'c', 'b'] as char[]) == 'c' as char
    }

    def "max([]) should throw error on empty array"() {
        when:
            max([] as char[])
        then:
            thrown(IllegalArgumentException)
    }

    @Unroll
    def "contains(#array, #item) == #expected"() {
        expect:
            contains(array as char[], item as char) == expected
        where:
            array           | item || expected
            ['a', 'c', 'b'] | 'c'  || true
            []              | 'a'  || false
            ['a', 'c', 'b'] | 'd'  || false
    }

    @Unroll
    def "indexOf(#array, #item) == #expected"() {
        expect:
            indexOf(array as char[], item as char) == expected
        where:
            array                | item || expected
            ['a', 'c', 'b', 'c'] | 'c'  || 1
            []                   | 'a'  || -1
            ['a', 'c', 'b']      | 'd'  || -1
    }

    @Unroll
    def "lastIndexOf(#array, #item) == #expected"() {
        expect:
            lastIndexOf(array as char[], item as char) == expected
        where:
            array                | item || expected
            ['a', 'c', 'b', 'c'] | 'c'  || 3
            []                   | 'a'  || -1
            ['a', 'c', 'b']      | 'd'  || -1
    }

    def "toArray(#list)"() {
        when:
            char[] result = toArray("abc".toCharArray() as List<Character>)
        then:
            result == "abc".toCharArray()
    }
}
