package com.coditory.quark.common.util

import spock.lang.Specification
import spock.lang.Unroll

import static Floats.*

class FloatsSpec extends Specification {
    def "emptyFloatArray() == []"() {
        expect:
            emptyFloatArray() == [] as float[]
    }

    @Unroll
    def "first(#input) == #expected"() {
        expect:
            first(input as float[]) == expected as float
        where:
            input  || expected
            [1, 2] || 1
            [2, 1] || 2
    }

    @Unroll
    def "firstOrDefault(#input, #defaultValue) == #expected"() {
        expect:
            firstOrDefault(input as float[], defaultValue as float) == expected as float
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
            first(input as float[])
        then:
            IllegalArgumentException e = thrown(IllegalArgumentException)
            e.message.startsWith("Expected non-empty array")
        where:
            input << [null, []]
    }

    @Unroll
    def "last(#input) == #expected"() {
        expect:
            last(input as float[]) == expected as float
        where:
            input  || expected
            [1, 2] || 2
            [2, 1] || 1
    }

    @Unroll
    def "lastOrDefault(#input, #defaultValue) == #expected"() {
        expect:
            lastOrDefault(input as float[], defaultValue as float) == expected as float
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
            last(input as float[])
        then:
            IllegalArgumentException e = thrown(IllegalArgumentException)
            e.message.startsWith("Expected non-empty array")
        where:
            input << [null, []]
    }

    @Unroll
    def "should parse float value: #value"() {
        expect:
            parseFloatOrNull(value) == expected as Float
        where:
            value           || expected
            "0"             || 0
            "-1"            || -1
            "1"             || 1
            "1.1"           || 1.1
            "121213"        || 121213
            "abc"           || null
            "3.4028235e+38" || Float.MAX_VALUE
            "3.4028235e+39" || null
            null            || null
    }

    @Unroll
    def "should parse optional float"() {
        expect:
            parseFloatOrEmpty(value) == expected
        where:
            value || expected
            "1"   || Optional.of(1f)
            "a"   || Optional.empty()
    }

    @Unroll
    def "should parse float or return default value"() {
        expect:
            parseFloatOrDefault(value, defaultValue) == expected as Float
        where:
            value | defaultValue || expected
            "1"   | 2            || 1
            "2"   | 1            || 2
            "abc" | 2            || 2
    }

    def "should parse float or throw error"() {
        when:
            parseFloat("1") == 1
        then:
            noExceptionThrown()

        when:
            parseFloat("abc")
        then:
            IllegalArgumentException e = thrown(IllegalArgumentException)
            e.message == "Could not parse Float value: 'abc'"
    }

    @Unroll
    def "contains(#array, #target) == #expected"() {
        expect:
            contains(array as float[], target as float) == expected
        where:
            array        | target || expected
            [1.5f, 2.3f] | 1.5f   || true
            []           | 1.5f   || false
            [1.5f, 2.3f] | 1.6f   || false
    }

    def "shuffle(#array)"() {
        given:
            float[] input = [1.2f, 2.3f, 3.4f]
        expect:
            shuffle(input, new Random(123)) != input
    }

    def "swap(#array, a, b)"() {
        given:
            float[] array = [1.2f, 2.3f, 3.4f]
        when:
            swap(array, 1, 2)
        then:
            array == [1.2f, 3.4f, 2.3f] as float[]
    }

    def "concat(#a, #b)"() {
        expect:
            concat([1.2f, 2.3f] as float[], [3.4f] as float[]) == [1.2f, 2.3f, 3.4f] as float[]
        and:
            concat([1.2f, 2.3f] as float[], [] as float[], [3.4f] as float[]) == [1.2f, 2.3f, 3.4f] as float[]
    }

    def "ensureCapacity(#array, #minLength, #padding)"() {
        expect:
            ensureCapacity([1.2f, 2.3f] as float[], 3, 2) == [1.2f, 2.3f, 0, 0, 0] as float[]
        and:
            ensureCapacity([1.2f, 2.3f] as float[], 2, 2) == [1.2f, 2.3f] as float[]
    }

    def "ensureCapacity(#array, #minLength)"() {
        expect:
            ensureCapacity([1.2f, 2.3f] as float[], 3) == [1.2f, 2.3f, 0] as float[]
        and:
            ensureCapacity([1.2f, 2.3f] as float[], 2) == [1.2f, 2.3f] as float[]
    }

    def "reverse(#array)"() {
        given:
            float[] array = [1.2f, 2.3f]
        when:
            reverse(array)
        then:
            array == [2.3f, 1.2f] as float[]
    }

    def "min(#array)"() {
        expect:
            min([1.2f, -0.1f, 2.3f] as float[]) == -0.1f
    }

    def "min([]) should throw error on empty array"() {
        when:
            min([] as float[])
        then:
            thrown(IllegalArgumentException)
    }

    def "max(#array)"() {
        expect:
            max([1.2f, 2.3f, -0.1f] as float[]) == 2.3f
    }

    def "max([]) should throw error on empty array"() {
        when:
            max([] as float[])
        then:
            thrown(IllegalArgumentException)
    }

    @Unroll
    def "contains(#array, #item) == #expected"() {
        expect:
            contains(array as float[], item) == expected
        where:
            array               | item  || expected
            [1.2f, 2.3f, -0.1f] | 2.3f  || true
            []                  | 0     || false
            [1.2f, 2.3f, -0.1f] | -2.3f || false
    }

    @Unroll
    def "indexOf(#array, #item) == #expected"() {
        expect:
            indexOf(array as float[], item) == expected
        where:
            array                     | item  || expected
            [1.2f, 2.3f, -0.1f, 2.3f] | 2.3f  || 1
            []                        | 0     || -1
            [1.2f, 2.3f, -0.1f]       | -2.3f || -1
    }

    @Unroll
    def "lastIndexOf(#array, #item) == #expected"() {
        expect:
            lastIndexOf(array as float[], item) == expected
        where:
            array                     | item  || expected
            [1.2f, 2.3f, -0.1f, 2.3f] | 2.3f  || 3
            []                        | 0     || -1
            [1.2f, 2.3f, -0.1f]       | -2.3f || -1
    }

    def "toArray(#list)"() {
        when:
            float[] result = toArray([1.2f, 2.3f, -0.1f])
        then:
            result == [1.2f, 2.3f, -0.1f] as float[]
    }
}
