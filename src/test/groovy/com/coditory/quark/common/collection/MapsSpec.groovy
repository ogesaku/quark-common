package com.coditory.quark.common.collection

import spock.lang.Specification
import spock.lang.Unroll

import static com.coditory.quark.common.collection.Maps.*

class MapsSpec extends Specification {
    @Unroll
    def "put(#map, #key, #value) == #expected"() {
        expect:
            put(map, key, value) == expected
        where:
            map      | key | value || expected
            [a: "A"] | "b" | "B"   || [a: "A", b: "B"]
            [a: "A"] | "a" | "B"   || [a: "B"]
    }

    @Unroll
    def "putAll(#map, #other) == #expected"() {
        expect:
            putAll(map, other) == expected
        where:
            map      | other    || expected
            [a: "A"] | [b: "B"] || [a: "A", b: "B"]
            [a: "A"] | [a: "B"] || [a: "B"]
            [a: "A"] | [:]      || [a: "A"]
    }

    @Unroll
    def "remove(#map, #key) == #expected"() {
        expect:
            remove(map, key) == expected
        where:
            map              | key || expected
            [a: "A", b: "B"] | "b" || [a: "A"]
            [a: "A"]         | "b" || [a: "A"]
            [a: "A"]         | "a" || [:]
    }

    @Unroll
    def "removeAll(#map, #other) == #expected"() {
        expect:
            removeAll(map, other) == expected
        where:
            map              | other || expected
            [a: "A", b: "B"] | ["b"] || [a: "A"]
            [a: "A"]         | ["a"] || [:]
    }

    @Unroll
    def "invert(#map) == #expected"() {
        expect:
            invert(map) == expected
        where:
            map              || expected
            [a: "A", b: "B"] || [A: "a", B: "b"]
            [a: "A", b: "A"] || [A: "a"]
            [:]              || [:]
    }

    @Unroll
    def "map(#map, {...}) == #expected"() {
        expect:
            map(map, { Map.entry(it.getKey() + "x", it.getValue() + "X") }) == expected
        where:
            map              || expected
            [a: "A", b: "B"] || [ax: "AX", bx: "BX"]
            [:]              || [:]
    }

    @Unroll
    def "mapKeys(#map, {...}) == #expected"() {
        expect:
            mapKeys(map, { it + "X" }) == expected
        where:
            map              || expected
            [a: "A", b: "B"] || [aX: "A", bX: "B"]
            [:]              || [:]
    }

    @Unroll
    def "mapValues(#map, {...}) == #expected"() {
        expect:
            mapValues(map, { it + "X" }) == expected
        where:
            map              || expected
            [a: "A", b: "B"] || [a: "AX", b: "BX"]
            [:]              || [:]
    }

    def "filter(map, predicate)"() {
        when:
            Map<String, String> result = filter([a: "xA", b: "B"], { !it.toString().contains("x") })
        then:
            result == [b: "B"]
    }

    def "filterByKey(map, predicate)"() {
        when:
            Map<String, String> result = filterByKey([a: "xA", xb: "B"], { !it.contains("x") })
        then:
            result == [a: "xA"]
    }

    def "filterByValue(map, predicate)"() {
        when:
            Map<String, String> result = filterByValue([a: "xA", xb: "B"], { !it.contains("x") })
        then:
            result == [xb: "B"]
    }

    @Unroll
    def "nullToEmpty(#a) == #expected"() {
        expect:
            nullToEmpty(a) == expected
        where:
            a        || expected
            [a: "A"] || [a: "A"]
            [:]      || [:]
            null     || [:]
    }

    @Unroll
    def "emptyToNull(#a) == #expected"() {
        expect:
            emptyToNull(a) == expected
        where:
            a        || expected
            [a: "A"] || [a: "A"]
            [:]      || null
            null     || null
    }
}
