package com.coditory.quark.common.collection

import spock.lang.Specification
import spock.lang.Unroll

import static com.coditory.quark.common.collection.Sets.*

class SetsSpec extends Specification {
    @Unroll
    def "disjunction(#a, #b) == #expected"() {
        expect:
            disjunction(a as Set, b as Set) == expected as Set
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
            intersection(a as Set, b as Set) == expected as Set
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
            addAll(a as Set, b as Set) == expected as Set
        where:
            a               | b          || expected
            ["a", "b", "c"] | ["b"]      || ["a", "b", "c"]
            ["a", "b"]      | ["b", "a"] || ["a", "b", "a"]
            ["a", "b"]      | []         || ["a", "b"]
            []              | ["a", "b"] || ["a", "b"]
            []              | []         || []
    }

    @Unroll
    def "add(#a, #b) == #expected"() {
        expect:
            add(a as Set, b) == expected as Set
        where:
            a          | b   || expected
            ["a", "b"] | "b" || ["a", "b"]
            []         | "a" || ["a"]
    }

    @Unroll
    def "remove(#a, #b) == #expected"() {
        expect:
            remove(a as Set, b) == expected as Set
        where:
            a          | b   || expected
            ["a", "b"] | "b" || ["a"]
            ["a", "b"] | "c" || ["a", "b"]
            []         | "a" || []
    }

    @Unroll
    def "removeAll(#a, #b) == #expected"() {
        expect:
            removeAll(a as Set, b) == expected as Set
        where:
            a               | b          || expected
            ["a", "b", "c"] | ["b"]      || ["a", "c"]
            ["a", "b"]      | ["b", "a"] || []
            ["a", "b"]      | []         || ["a", "b"]
            []              | ["a", "b"] || []
            []              | []         || []
    }

    def "anyItem(#a)"() {
        when:
            String result = anyItem(["a", "b"] as Set)
        then:
            result == "a"

        when:
            anyItem([] as Set)
        then:
            thrown(IllegalArgumentException)

        when:
            anyItem(null)
        then:
            thrown(IllegalArgumentException)
    }

    @Unroll
    def "anyItemOrNull(#a) == expected"() {
        expect:
            anyItemOrNull(a as Set) == expected
        where:
            a          || expected
            ["a", "b"] || "a"
            []         || null
            null       || null
    }

    @Unroll
    def "isNotEmpty(#a) == #expected"() {
        expect:
            isNotEmpty(a as Set) == expected
        where:
            a               || expected
            ["a", "b", "c"] || true
            []              || false
            null            || false
    }

    @Unroll
    def "nullToEmpty(#a) == #expected"() {
        expect:
            nullToEmpty(a as Set) == expected as Set
        where:
            a          || expected
            ["a", "b"] || ["a", "b"]
            []         || []
            null       || []
    }

    @Unroll
    def "emptyToNull(#a) == #expected"() {
        expect:
            emptyToNull(a as Set) == expected as Set
        where:
            a          || expected
            ["a", "b"] || ["a", "b"]
            []         || null
            null       || null
    }

    @Unroll
    def "map(#a, { it + \"X\" }) == #expected"() {
        expect:
            map(a as Set, { it + "x" }) == expected as Set
        where:
            a          || expected
            ["a", "b"] || ["ax", "bx"]
            []         || []
    }

    def "filter(set, predicate)"() {
        when:
            Set<String> result = filter(["ax", "b", "x", "a"] as Set<String>, { !it.contains("x") })
        then:
            result == ["b", "a"] as Set
    }
}
