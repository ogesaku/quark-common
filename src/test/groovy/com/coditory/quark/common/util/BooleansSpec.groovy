package com.coditory.quark.common.util

import spock.lang.Specification
import spock.lang.Unroll

import static Booleans.*

class BooleansSpec extends Specification {
    def "emptyBooleanArray() == []"() {
        expect:
            emptyBooleanArray() == [] as boolean[]
    }

    @Unroll
    def "should parse boolean value: #value"() {
        expect:
            parseBooleanOrNull(value) == expected
        where:
            value   || expected
            "true"  || true
            "false" || false
            "TrUe"  || true
            "FaLsE" || false
            "1"     || null
            "0"     || null
            "abc"   || null
            null    || null
    }

    @Unroll
    def "should parse optional boolean"() {
        expect:
            parseBooleanOrEmpty(value) == expected
        where:
            value   || expected
            "true"  || Optional.of(true)
            "false" || Optional.of(false)
            "abc"   || Optional.empty()
    }

    @Unroll
    def "should parse boolean or return default value"() {
        expect:
            parseBooleanOrDefault(value, defaultValue) == expected
        where:
            value   | defaultValue || expected
            "true"  | false        || true
            "false" | true         || false
            "abc"   | true         || true
    }

    def "should parse boolean or throw error"() {
        when:
            parseBoolean("true") == true
        then:
            noExceptionThrown()

        when:
            parseBoolean("truee")
        then:
            IllegalArgumentException e = thrown(IllegalArgumentException)
            e.message == "Could not parse Boolean value: 'truee'. Expected true/false."
    }

    @Unroll
    def "first(#input) == #expected"() {
        expect:
            first(input as boolean[]) == expected
        where:
            input          || expected
            [true, false]  || true
            [false, true]  || false
            [true, true]   || true
            [false, false] || false
    }

    @Unroll
    def "firstOrDefault(#input, #defaultValue) == #expected"() {
        expect:
            firstOrDefault(input as boolean[], defaultValue) == expected
        where:
            input          | defaultValue || expected
            [true, false]  | true         || true
            [false, true]  | true         || false
            [true, true]   | true         || true
            [false, false] | true         || false
            []             | true         || true
            []             | false        || false
            null           | true         || true
            null           | false        || false
    }

    @Unroll
    def "first(#input) - should throw error"() {
        when:
            first(input as boolean[])
        then:
            IllegalArgumentException e = thrown(IllegalArgumentException)
            e.message.startsWith("Expected non-empty array")
        where:
            input << [null, []]
    }

    @Unroll
    def "last(#input) == #expected"() {
        expect:
            last(input as boolean[]) == expected
        where:
            input          || expected
            [true, false]  || false
            [false, true]  || true
            [true, true]   || true
            [false, false] || false
    }

    @Unroll
    def "lastOrDefault(#input, #defaultValue) == #expected"() {
        expect:
            lastOrDefault(input as boolean[], defaultValue) == expected
        where:
            input          | defaultValue || expected
            [true, false]  | true         || false
            [false, true]  | true         || true
            [true, true]   | true         || true
            [false, false] | true         || false
            []             | true         || true
            []             | false        || false
            null           | true         || true
            null           | false        || false
    }

    @Unroll
    def "last(#input) - should throw error"() {
        when:
            last(input as boolean[])
        then:
            IllegalArgumentException e = thrown(IllegalArgumentException)
            e.message.startsWith("Expected non-empty array")
        where:
            input << [null, []]
    }

    @Unroll
    def "contains(#array, #target) == #expected"() {
        expect:
            contains(array as boolean[], target as boolean) == expected
        where:
            array          | target || expected
            [true, false]  | true   || true
            [true, false]  | false  || true
            [true, true]   | false  || false
            [false, false] | true   || false
            []             | true   || false
            []             | false  || false
    }

    def "shuffle(#array)"() {
        given:
            boolean[] input = [true, false, true]
        expect:
            shuffle(input, new Random(123)) != input
    }

    def "swap(#array, a, b)"() {
        given:
            boolean[] array = [true, false, true]
        when:
            swap(array, 1, 2)
        then:
            array == [true, true, false] as boolean[]
    }

    def "concat(#a, #b)"() {
        expect:
            concat([true, false] as boolean[], [true] as boolean[]) == [true, false, true] as boolean[]
        and:
            concat([true, false] as boolean[], [] as boolean[], [true] as boolean[]) == [true, false, true] as boolean[]
    }

    def "ensureCapacity(#array, #minLength, #padding)"() {
        expect:
            ensureCapacity([true, false] as boolean[], 3, 2) == [true, false, false, false, false] as boolean[]
        and:
            ensureCapacity([true, false] as boolean[], 2, 2) == [true, false] as boolean[]
    }

    def "ensureCapacity(#array, #minLength)"() {
        expect:
            ensureCapacity([true, false] as boolean[], 3) == [true, false, false] as boolean[]
        and:
            ensureCapacity([true, false] as boolean[], 2) == [true, false] as boolean[]
    }

    def "reverse(#array)"() {
        given:
            boolean[] array = [true, false]
        when:
            reverse(array)
        then:
            array == [false, true] as boolean[]
    }

    @Unroll
    def "contains(#array, #item) == #expected"() {
        expect:
            contains(array as boolean[], item) == expected
        where:
            array         | item  || expected
            [true, false] | true  || true
            []            | true  || false
            [true, true]  | false || false
    }

    @Unroll
    def "indexOf(#array, #item) == #expected"() {
        expect:
            indexOf(array as boolean[], item) == expected
        where:
            array                      | item  || expected
            [true, false, true, false] | false || 1
            []                         | true  || -1
            [true, true]               | false || -1
    }

    @Unroll
    def "lastIndexOf(#array, #item) == #expected"() {
        expect:
            lastIndexOf(array as boolean[], item) == expected
        where:
            array                      | item  || expected
            [true, false, true, false] | true  || 2
            []                         | true  || -1
            [true, true]               | false || -1
    }

    def "toArray(#list)"() {
        when:
            boolean[] result = toArray([true, false, true])
        then:
            result == [true, false, true] as boolean[]
    }
}
