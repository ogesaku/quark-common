package com.coditory.quark.common.util

import com.coditory.quark.common.throwable.ThrowingFunction
import com.coditory.quark.common.throwable.ThrowingSupplier
import spock.lang.Specification
import spock.lang.Unroll

import static Objects.*
import static java.util.Comparator.naturalOrder

class ObjectsSpec extends Specification {
    def "emptyObjectArray() == []"() {
        expect:
            emptyObjectArray() == [] as Object[]
    }


    @Unroll
    def "first(#input) == #expected"() {
        expect:
            first(input as Object[]) == expected
        where:
            input      || expected
            ["1", "2"] || "1"
            ["2", "1"] || "2"
    }

    @Unroll
    def "firstOrNull(#input) == #expected"() {
        expect:
            firstOrNull(input as Object[]) == expected
        where:
            input      || expected
            ["1", "2"] || "1"
            []         || null
            null       || null
    }

    @Unroll
    def "firstOrDefault(#input, #defaultValue) == #expected"() {
        expect:
            firstOrDefault(input as Object[], defaultValue) == expected
        where:
            input      | defaultValue || expected
            ["1", "2"] | "3"          || "1"
            ["2", "1"] | "3"          || "2"
            []         | "3"          || "3"
            null       | "3"          || "3"
    }

    @Unroll
    def "first(#input) - should throw error"() {
        when:
            first(input as Object[])
        then:
            IllegalArgumentException e = thrown(IllegalArgumentException)
            e.message.startsWith("Expected non-empty array")
        where:
            input << [null, []]
    }

    @Unroll
    def "last(#input) == #expected"() {
        expect:
            last(input as Object[]) == expected
        where:
            input      || expected
            ["1", "2"] || "2"
            ["2", "1"] || "1"
    }

    @Unroll
    def "lastOrDefault(#input, #defaultValue) == #expected"() {
        expect:
            lastOrDefault(input as Object[], defaultValue) == expected
        where:
            input      | defaultValue || expected
            ["1", "2"] | "3"          || "2"
            ["2", "1"] | "3"          || "1"
            []         | "3"          || "3"
            null       | "3"          || "3"
    }

    @Unroll
    def "lastOrNull(#input) == #expected"() {
        expect:
            lastOrNull(input as Object[]) == expected
        where:
            input      || expected
            ["1", "2"] || "2"
            []         || null
            null       || null
    }

    @Unroll
    def "last(#input) - should throw error"() {
        when:
            last(input as Object[])
        then:
            IllegalArgumentException e = thrown(IllegalArgumentException)
            e.message.startsWith("Expected non-empty array")
        where:
            input << [null, []]
    }

    def "isEmpty(#input) == #expected"() {
        expect:
            isEmpty(input as Object[]) == expected
        where:
            input || expected
            []    || true
            null  || true
            ["x"] || false
    }

    def "isNotEmpty(#input) == #expected"() {
        expect:
            isNotEmpty(input as Object[]) == expected
        where:
            input || expected
            []    || false
            null  || false
            ["x"] || true
    }

    def "toStringOrNull(#input) == #expected"() {
        expect:
            toStringOrNull(input) == expected
        where:
            input || expected
            null  || null
            1     || "1"
            "x"   || "x"
    }

    def "toStringOrEmpty(#input) == #expected"() {
        expect:
            toStringOrEmpty(input) == expected
        where:
            input || expected
            null  || ""
            1     || "1"
            "x"   || "x"
    }

    def "toString(#input) == #expected"() {
        expect:
            toString(input) == expected
        where:
            input || expected
            null  || "null"
            1     || "1"
            "x"   || "x"
    }

    def "mapNotNull(#input, { it + \"x\" }) == #expected"() {
        expect:
            mapNotNull(input, { it + "x" }) == expected
        where:
            input || expected
            null  || null
            "a"   || "ax"
    }

    def "mapNotNullOrDefault(#input, { it + \"x\" }, \"Y\") == #expected"() {
        expect:
            mapNotNullOrDefault(input, { it + "x" }, "Y") == expected
        where:
            input || expected
            null  || "Y"
            "a"   || "ax"
    }

    def "mapNotNullOrDefault(#input, { it + \"x\" }, { \"Y\" }) == #expected"() {
        given:
            ThrowingFunction<String, String> mapper = { it + "x" } as ThrowingFunction<String, String>
            ThrowingSupplier<String> supplier = { "Y" } as ThrowingSupplier<String>
        expect:
            mapNotNullOrDefault(input, mapper, supplier) == expected
        where:
            input || expected
            null  || "Y"
            "a"   || "ax"
    }

    def "onNotNull(#input, consumer); consumed == #expected"() {
        given:
            boolean consumed = false
        when:
            onNotNull(input, { consumed = true })
        then:
            consumed == expected
        where:
            input || expected
            null  || false
            "a"   || true
    }

    def "defaultIfNull(#input, \"Y\") == #expected"() {
        expect:
            defaultIfNull(input, "Y") == expected
        where:
            input || expected
            null  || "Y"
            "a"   || "a"
    }

    def "defaultIfNull(#input, { \"Y\" }) == #expected"() {
        expect:
            defaultIfNull(input, { "Y" } as ThrowingSupplier<String>) == expected
        where:
            input || expected
            null  || "Y"
            "a"   || "a"
    }

    def "firstNonNull(T... values)"() {
        when:
            String result = firstNonNull(null, null, "abc", null, "X")
        then:
            result == "abc"

        when:
            firstNonNull(null, null, null)
        then:
            thrown(IllegalArgumentException)
    }

    def "getFirstNonNull(ThrowingSupplier<T>... suppliers)"() {
        when:
            String result = getFirstNonNull(
                    null as ThrowingSupplier<String>,
                    { null } as ThrowingSupplier<String>,
                    { "abc" } as ThrowingSupplier<String>,
                    { "X" } as ThrowingSupplier<String>
            )
        then:
            result == "abc"

        when:
            getFirstNonNull(
                    null as ThrowingSupplier<String>,
                    { null } as ThrowingSupplier<String>
            )
        then:
            thrown(IllegalArgumentException)
    }

    def "should proxy java.util.Objects: equals, hashCode, hash"() {
        expect:
            equals("abc", "abc") == true
            equals("abc", "xyz") == false
            equals(null, "xyz") == false
        and:
            hashCode("abc") == java.util.Objects.hashCode("abc")
            hashCode("xyz") == java.util.Objects.hashCode("xyz")
            hashCode(null) == java.util.Objects.hashCode(null)
        and:
            hash("abc", 123, null) == java.util.Objects.hash("abc", 123, null)
    }

    @Unroll
    def "contains(#array, #target) == #expected"() {
        expect:
            contains(array as String[], target) == expected
        where:
            array      | target || expected
            ["a", "b"] | "a"    || true
            []         | "a"    || false
            ["a", "b"] | "c"    || false
    }

    def "shuffle(#array)"() {
        given:
            String[] array = ["a", "b", "c"]
        expect:
            shuffle(array, new Random(123)) != array
    }

    def "swap(#array, a, b)"() {
        given:
            String[] array = ["a", "b", "c"]
        when:
            swap(array, 1, 2)
        then:
            array == ["a", "c", "b"] as String[]
    }

    def "concat(#a, #b)"() {
        expect:
            concat(["a", "b"] as String[], ["c"] as String[]) == ["a", "b", "c"] as String[]
        and:
            concat(["a", "b"] as String[], [] as String[], ["c"] as String[]) == ["a", "b", "c"] as String[]
    }

    def "ensureCapacity(#array, #minLength, #padding)"() {
        expect:
            ensureCapacity(["a", "b"] as String[], 3, 2) == ["a", "b", null, null, null] as String[]
        and:
            ensureCapacity(["a", "b"] as String[], 2, 2) == ["a", "b"] as String[]
    }

    def "ensureCapacity(#array, #minLength)"() {
        expect:
            ensureCapacity(["a", "b"] as String[], 3) == ["a", "b", null] as String[]
        and:
            ensureCapacity(["a", "b"] as String[], 2) == ["a", "b"] as String[]
    }

    def "reverse(#array)"() {
        given:
            String[] array = ["a", "b"]
        when:
            reverse(array)
        then:
            array == ["b", "a"] as String[]
    }

    def "min(#array)"() {
        expect:
            min(naturalOrder(), ["a", "b", "a", "c"] as String[]) == "a"
    }

    def "min([]) should throw error on empty array"() {
        when:
            min(naturalOrder(), [] as String[])
        then:
            thrown(IllegalArgumentException)
    }

    def "max(#array)"() {
        expect:
            max(naturalOrder(), ["a", "b", "a", "c"] as String[]) == "c"
    }

    def "max([]) should throw error on empty array"() {
        when:
            max(naturalOrder(), [] as String[])
        then:
            thrown(IllegalArgumentException)
    }

    @Unroll
    def "contains(#array, #item) == #expected"() {
        expect:
            contains(array as String[], item) == expected
        where:
            array                | item || expected
            ["a", "b", "a", "c"] | "a"  || true
            []                   | "a"  || false
            ["a", "b"]           | "c"  || false
    }

    @Unroll
    def "indexOf(#array, #item) == #expected"() {
        expect:
            indexOf(array as String[], item) == expected
        where:
            array                | item || expected
            ["a", "b", "a", "c"] | "a"  || 0
            []                   | "a"  || -1
            ["a", "b"]           | "c"  || -1
    }

    @Unroll
    def "lastIndexOf(#array, #item) == #expected"() {
        expect:
            lastIndexOf(array as String[], item) == expected
        where:
            array                | item || expected
            ["a", "b", "a", "c"] | "a"  || 2
            []                   | "a"  || -1
            ["a", "b"]           | "c"  || -1
    }
}
