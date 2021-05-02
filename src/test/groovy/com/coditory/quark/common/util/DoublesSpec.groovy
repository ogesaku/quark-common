package com.coditory.quark.common.util

import spock.lang.Specification
import spock.lang.Unroll

import static Doubles.*

class DoublesSpec extends Specification {
    def "emptyDoubleArray() == []"() {
        expect:
            emptyDoubleArray() == [] as double[]
    }

    @Unroll
    def "first(#input) == #expected"() {
        expect:
            first(input as double[]) == expected as double
        where:
            input  || expected
            [1, 2] || 1
            [2, 1] || 2
    }

    @Unroll
    def "firstOrDefault(#input, #defaultValue) == #expected"() {
        expect:
            firstOrDefault(input as double[], defaultValue as double) == expected as double
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
            first(input as double[])
        then:
            IllegalArgumentException e = thrown(IllegalArgumentException)
            e.message.startsWith("Expected non-empty array")
        where:
            input << [null, []]
    }

    @Unroll
    def "last(#input) == #expected"() {
        expect:
            last(input as double[]) == expected as double
        where:
            input  || expected
            [1, 2] || 2
            [2, 1] || 1
    }

    @Unroll
    def "lastOrDefault(#input, #defaultValue) == #expected"() {
        expect:
            lastOrDefault(input as double[], defaultValue as double) == expected as double
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
            last(input as double[])
        then:
            IllegalArgumentException e = thrown(IllegalArgumentException)
            e.message.startsWith("Expected non-empty array")
        where:
            input << [null, []]
    }

    @Unroll
    def "should parse double value: #value"() {
        expect:
            parseDoubleOrNull(value) == expected
        where:
            value                     || expected
            "0"                       || 0
            "-1"                      || -1
            "1"                       || 1
            "1.1"                     || 1.1
            "121213"                  || 121213
            "abc"                     || null
            "1.7976931348623157e+308" || Double.MAX_VALUE
            "1.7976931348623157e+309" || null
            null                      || null
    }

    @Unroll
    def "should parse optional double"() {
        expect:
            parseDoubleOrEmpty(value) == expected
        where:
            value || expected
            "1"   || Optional.of(1d)
            "a"   || Optional.empty()
    }

    @Unroll
    def "should parse double or return default value"() {
        expect:
            parseDoubleOrDefault(value, defaultValue) == expected
        where:
            value | defaultValue || expected
            "1"   | 2            || 1
            "2"   | 1            || 2
            "abc" | 2            || 2
    }

    def "should parse double or throw error"() {
        when:
            parseDouble("1") == 1
        then:
            noExceptionThrown()

        when:
            parseDouble("abc")
        then:
            IllegalArgumentException e = thrown(IllegalArgumentException)
            e.message == "Could not parse Double value: 'abc'"
    }


    @Unroll
    def "contains(#array, #target) == #expected"() {
        expect:
            contains(array as double[], target as double) == expected
        where:
            array        | target || expected
            [1.5d, 2.3d] | 1.5d   || true
            []           | 1.5d   || false
            [1.5d, 2.3d] | 1.6d   || false
    }

    def "shuffle(#array)"() {
        given:
            double[] input = [1.2d, 2.3d, 3.4d]
        expect:
            shuffle(input, new Random(123)) != input
    }

    def "swap(#array, a, b)"() {
        given:
            double[] array = [1.2d, 2.3d, 3.4d]
        when:
            swap(array, 1, 2)
        then:
            array == [1.2d, 3.4d, 2.3d] as double[]
    }

    def "concat(#a, #b)"() {
        expect:
            concat([1.2d, 2.3d] as double[], [3.4d] as double[]) == [1.2d, 2.3d, 3.4d] as double[]
        and:
            concat([1.2d, 2.3d] as double[], [] as double[], [3.4d] as double[]) == [1.2d, 2.3d, 3.4d] as double[]
    }

    def "ensureCapacity(#array, #minLength, #padding)"() {
        expect:
            ensureCapacity([1.2d, 2.3d] as double[], 3, 2) == [1.2d, 2.3d, 0, 0, 0] as double[]
        and:
            ensureCapacity([1.2d, 2.3d] as double[], 2, 2) == [1.2d, 2.3d] as double[]
    }

    def "ensureCapacity(#array, #minLength)"() {
        expect:
            ensureCapacity([1.2d, 2.3d] as double[], 3) == [1.2d, 2.3d, 0] as double[]
        and:
            ensureCapacity([1.2d, 2.3d] as double[], 2) == [1.2d, 2.3d] as double[]
    }

    def "reverse(#array)"() {
        given:
            double[] array = [1.2d, 2.3d]
        when:
            reverse(array)
        then:
            array == [2.3d, 1.2d] as double[]
    }

    def "min(#array)"() {
        expect:
            min([1.2d, -0.1d, 2.3d] as double[]) == -0.1d
    }

    def "min([]) should throw error on empty array"() {
        when:
            min([] as double[])
        then:
            thrown(IllegalArgumentException)
    }

    def "max(#array)"() {
        expect:
            max([1.2d, 2.3d, -0.1d] as double[]) == 2.3d
    }

    def "max([]) should throw error on empty array"() {
        when:
            max([] as double[])
        then:
            thrown(IllegalArgumentException)
    }

    @Unroll
    def "contains(#array, #item) == #expected"() {
        expect:
            contains(array as double[], item) == expected
        where:
            array               | item  || expected
            [1.2d, 2.3d, -0.1d] | 2.3d  || true
            []                  | 0     || false
            [1.2d, 2.3d, -0.1d] | -2.3d || false
    }

    @Unroll
    def "indexOf(#array, #item) == #expected"() {
        expect:
            indexOf(array as double[], item) == expected
        where:
            array                     | item  || expected
            [1.2d, 2.3d, -0.1d, 2.3d] | 2.3d  || 1
            []                        | 0     || -1
            [1.2d, 2.3d, -0.1d]       | -2.3d || -1
    }

    @Unroll
    def "lastIndexOf(#array, #item) == #expected"() {
        expect:
            lastIndexOf(array as double[], item) == expected
        where:
            array                     | item  || expected
            [1.2d, 2.3d, -0.1d, 2.3d] | 2.3d  || 3
            []                        | 0d    || -1
            [1.2d, 2.3d, -0.1d]       | -2.3d || -1
    }

    def "toArray(#list)"() {
        when:
            double[] result = toArray([1.2d, 2.3d, -0.1d])
        then:
            result == [1.2d, 2.3d, -0.1d] as double[]
    }
}
