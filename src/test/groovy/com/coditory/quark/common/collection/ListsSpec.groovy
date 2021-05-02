package com.coditory.quark.common.collection

import spock.lang.Specification
import spock.lang.Unroll

import static Lists.*

class ListsSpec extends Specification {
    @Unroll
    def "equalsIgnoreOrder(#a, #b) == #expected"() {
        expect:
            equalsIgnoreOrder(a, b) == expected
        where:
            a          | b               || expected
            ["a", "b"] | ["b", "a"]      || true
            ["a", "b"] | ["a", "b"]      || true
            []         | []              || true
            ["a", "b"] | ["a", "b", "b"] || false
            ["a", "b"] | ["a", "c"]      || false
            ["a", "b"] | ["b", "b"]      || false
            ["a"]      | ["b"]           || false
            null       | null            || true
            null       | []              || false
    }

    @Unroll
    def "equalsIgnoreOrderAndDuplicates(#a, #b) == #expected"() {
        expect:
            equalsIgnoreOrderAndDuplicates(a, b) == expected
        where:
            a          | b               || expected
            ["a", "b"] | ["a", "b", "b"] || true
            ["a", "b"] | ["b", "a"]      || true
            ["a", "b"] | ["a", "b"]      || true
            []         | []              || true
            ["a", "b"] | ["a", "c"]      || false
            ["a", "b"] | ["b", "b"]      || false
            ["a"]      | ["b"]           || false
            null       | null            || true
            null       | []              || false
    }

    @Unroll
    def "equalsIgnoreDuplicates(#a, #b) == #expected"() {
        expect:
            equalsIgnoreDuplicates(a, b) == expected
        where:
            a          | b                    || expected
            ["a", "b"] | ["a", "b", "b"]      || true
            ["a", "b"] | ["a", "b", "b", "a"] || true
            ["a", "b"] | ["a", "b"]           || true
            []         | []                   || true
            ["a", "b"] | ["b", "a"]           || false
            ["a", "b"] | ["b", "b", "a"]      || false
            ["a", "b"] | ["a", "c"]           || false
            ["a", "b"] | ["b", "b"]           || false
            ["a"]      | ["b"]                || false
            null       | null                 || true
            null       | []                   || false
    }

    @Unroll
    def "countItems(#list) == #expected"() {
        expect:
            countItems(list) == expected
        where:
            list            || expected
            ["a"]           || [a: 1]
            ["a", "b"]      || [a: 1, b: 1]
            ["a", "b", "a"] || [a: 2, b: 1]
            []              || [:]
    }

    @Unroll
    def "disjunction(#a, #b) == #expected"() {
        expect:
            disjunction(a, b) == expected
        where:
            a               | b          || expected
            ["a", "b", "c"] | ["b"]      || ["a", "c"]
            ["a", "b"]      | ["b", "a"] || []
            ["a", "b"]      | ["c"]      || ["a", "b", "c"]
            ["a", "b"]      | []         || ["a", "b"]
            ["b", "a"]      | []         || ["b", "a"]
    }

    @Unroll
    def "intersection(#a, #b) == #expected"() {
        expect:
            intersection(a, b) == expected
        where:
            a               | b          || expected
            ["a", "b", "c"] | ["b"]      || ["b"]
            ["a", "b"]      | ["b", "a"] || ["a", "b"]
            ["a", "b"]      | ["c"]      || []
            ["a", "b"]      | []         || []
    }

    @Unroll
    def "addAll(#a, #b) == #expected"() {
        expect:
            addAll(a, b) == expected
        where:
            a               | b          || expected
            ["a", "b", "c"] | ["b"]      || ["a", "b", "c", "b"]
            ["a", "b"]      | ["b", "a"] || ["a", "b", "b", "a"]
            ["a", "b"]      | []         || ["a", "b"]
            []              | ["a", "b"] || ["a", "b"]
            []              | []         || []
    }

    @Unroll
    def "addAllMissing(#a, #b) == #expected"() {
        expect:
            addAllMissing(a, b) == expected
        where:
            a               | b          || expected
            ["a", "b", "c"] | ["b"]      || ["a", "b", "c"]
            ["a", "b", "b"] | ["b"]      || ["a", "b", "b"]
            ["a", "b"]      | ["b", "a"] || ["a", "b"]
            ["a", "b"]      | ["c"]      || ["a", "b", "c"]
            ["a", "b"]      | []         || ["a", "b"]
            []              | ["a", "b"] || ["a", "b"]
            []              | []         || []
    }

    @Unroll
    def "add(#a, #b) == #expected"() {
        expect:
            add(a, b) == expected
        where:
            a          | b   || expected
            ["a", "b"] | "b" || ["a", "b", "b"]
            []         | "a" || ["a"]
    }

    @Unroll
    def "addMissing(#a, #b) == #expected"() {
        expect:
            addMissing(a, b) == expected
        where:
            a          | b   || expected
            ["a", "b"] | "b" || ["a", "b"]
            ["a", "b"] | "c" || ["a", "b", "c"]
            []         | "a" || ["a"]
    }

    @Unroll
    def "remove(#a, #b) == #expected"() {
        expect:
            remove(a, b) == expected
        where:
            a               | b   || expected
            ["a", "b", "b"] | "b" || ["a", "b"]
            ["a", "b", "b"] | "a" || ["b", "b"]
            ["a", "b"]      | "c" || ["a", "b"]
    }

    @Unroll
    def "removeAll(#a, #b) == #expected"() {
        expect:
            removeAll(a, b) == expected
        where:
            a               | b          || expected
            ["a", "b", "c"] | ["b"]      || ["a", "c"]
            ["a", "b", "b"] | ["b"]      || ["a"]
            ["a", "b"]      | ["b", "a"] || []
            ["a", "b"]      | []         || ["a", "b"]
            []              | ["a", "b"] || []
            []              | []         || []
    }

    @Unroll
    def "reverse(#a) == #expected"() {
        expect:
            reverse(a) == expected
        where:
            a               || expected
            ["a", "b", "c"] || ["c", "b", "a"]
            ["a", "b", "b"] || ["b", "b", "a"]
            []              || []
    }

    def "shuffle(#list)"() {
        given:
            List<String> list = ["a", "b", "c"]
        expect:
            shuffle(list, new Random(123)) != list
    }

    @Unroll
    def "distinct(#a) == #expected"() {
        expect:
            distinct(a) == expected
        where:
            a               || expected
            ["a", "b", "c"] || ["a", "b", "c"]
            ["a", "b", "b"] || ["a", "b"]
            ["a", "b", "a"] || ["a", "b"]
            []              || []
    }

    def "first(#a)"() {
        when:
            String result = first(["a", "b"])
        then:
            result == "a"

        when:
            first([])
        then:
            thrown(IllegalArgumentException)

        when:
            first(null)
        then:
            thrown(IllegalArgumentException)
    }

    @Unroll
    def "firstOrNull(#a) == expected"() {
        expect:
            firstOrNull(a) == expected
        where:
            a          || expected
            ["a", "b"] || "a"
            []         || null
            null       || null
    }

    def "last(#a)"() {
        when:
            String result = last(["a", "b"])
        then:
            result == "b"

        when:
            last([])
        then:
            thrown(IllegalArgumentException)

        when:
            last(null)
        then:
            thrown(IllegalArgumentException)
    }

    @Unroll
    def "lastOrNull(#a) == #expected"() {
        expect:
            lastOrNull(a) == expected
        where:
            a          || expected
            ["a", "b"] || "b"
            []         || null
            null       || null
    }

    @Unroll
    def "isNotEmpty(#a) == #expected"() {
        expect:
            isNotEmpty(a) == expected
        where:
            a               || expected
            ["a", "b", "c"] || true
            []              || false
            null            || false
    }

    @Unroll
    def "nullToEmpty(#a) == #expected"() {
        expect:
            nullToEmpty(a) == expected
        where:
            a          || expected
            ["a", "b"] || ["a", "b"]
            []         || []
            null       || []
    }

    @Unroll
    def "emptyToNull(#a) == #expected"() {
        expect:
            emptyToNull(a) == expected
        where:
            a          || expected
            ["a", "b"] || ["a", "b"]
            []         || null
            null       || null
    }

    @Unroll
    def "map(#a, { it + \"X\" }) == #expected"() {
        expect:
            map(a, { it + "x" }) == expected
        where:
            a          || expected
            ["a", "b"] || ["ax", "bx"]
            []         || []
    }
}
