package com.coditory.quark.common.util.string

import spock.lang.Specification
import spock.lang.Unroll

class StringsContainsSpec extends Specification {
    @Unroll
    def "containsIgnoreCase(#value, #part) == #expected"() {
        expect:
            containsIgnoreCase(value, part) == expected
        where:
            value     | part   || expected
            ""        | ""     || true
            "x"       | ""     || true
            ""        | "x"    || false
            "x"       | "x"    || true
            "x"       | "X"    || true
            "xyz"     | "y"    || true
            "xyz"     | "yz"   || true
            "xyz"     | "zy"   || false
            "xyz"     | "Y"    || true
            "xyZ"     | "Yz"   || true
            "a𝄞b🌉c" | "𝄞B🌉" | true
            "a𝄞bc"   | "𝄞b🌉" | false
    }

    @Unroll
    def "containsNone(#value, #parts) == #expected"() {
        expect:
            containsNone(value, parts) == expected
        where:
            value     | parts       || expected
            ""        | []          || true
            "x"       | []          || true
            "x"       | [""]        || false
            ""        | ["x"]       || true
            "x"       | ["x"]       || false
            "x"       | ["X"]       || true
            "xyz"     | ["a", "y"]  || false
            "xyz"     | ["a", "yz"] || false
            "xyz"     | ["a", "Yz"] || true
            "xYz"     | ["a", "yz"] || true
            "xyz"     | ["a", "zy"] || true
            "a𝄞b🌉c" | ["𝄞B"]      | true
            "a𝄞b🌉c" | ["𝄞b"]      | false
    }

    @Unroll
    def "containsNoneIgnoreCase(#value, #parts) == #expected"() {
        expect:
            containsNoneIgnoreCase(value, parts) == expected
        where:
            value     | parts       || expected
            ""        | []          || true
            "x"       | []          || true
            "x"       | [""]        || false
            ""        | ["x"]       || true
            "x"       | ["x"]       || false
            "x"       | ["X"]       || false
            "xyz"     | ["a", "y"]  || false
            "xyz"     | ["a", "yz"] || false
            "xyz"     | ["a", "Yz"] || false
            "xYz"     | ["a", "yz"] || false
            "xyz"     | ["a", "zy"] || true
            "a𝄞b🌉c" | ["𝄞B"]      | false
            "a𝄞bc"   | ["b🌉"]      | true
    }

    @Unroll
    def "containsNone(#value, #parts char[]) == #expected"() {
        expect:
            containsNone(value, parts as char[]) == expected
        where:
            value | parts      || expected
            ""    | []         || true
            "x"   | []         || true
            ""    | ["x"]      || true
            "x"   | ["x"]      || false
            "x"   | ["X"]      || true
            "xyz" | ["a", "y"] || false
            "xyz" | ["a", "Y"] || true
    }

    @Unroll
    def "containsNoneIgnoreCase(#value, #parts char[]) == #expected"() {
        expect:
            containsNoneIgnoreCase(value, parts as char[]) == expected
        where:
            value | parts      || expected
            ""    | []         || true
            "x"   | []         || true
            ""    | ["x"]      || true
            "x"   | ["x"]      || false
            "x"   | ["X"]      || false
            "xyz" | ["a", "y"] || false
            "xyz" | ["a", "Y"] || false
    }

    @Unroll
    def "containsAny(#value, #parts) == #expected"() {
        expect:
            containsAny(value, parts) == expected
        where:
            value     | parts       || expected
            ""        | []          || true
            "x"       | []          || true
            "x"       | [""]        || true
            ""        | ["x"]       || false
            "x"       | ["x"]       || true
            "x"       | ["X"]       || false
            "xyz"     | ["a", "y"]  || true
            "xyz"     | ["a", "yz"] || true
            "xyz"     | ["a", "Yz"] || false
            "xYz"     | ["a", "yz"] || false
            "xyz"     | ["a", "zy"] || false
            "a𝄞b🌉c" | ["𝄞b"]      | true
            "a𝄞b🌉c" | ["𝄞B"]      | false
    }

    @Unroll
    def "containsAny(#value, #parts as char[]) == #expected"() {
        expect:
            containsAny(value, parts as char[]) == expected
        where:
            value | parts      || expected
            ""    | []         || true
            "x"   | []         || true
            ""    | ["x"]      || false
            "x"   | ["x"]      || true
            "x"   | ["X"]      || false
            "xyz" | ["a", "y"] || true
            "xyz" | ["X", "Y"] || false
            "xyz" | ["a", "b"] || false
    }

    @Unroll
    def "containsAnyIgnoreCase(#value, #parts char[]) == #expected"() {
        expect:
            containsAnyIgnoreCase(value, parts as char[]) == expected
        where:
            value | parts      || expected
            ""    | []         || true
            "x"   | []         || true
            ""    | ["x"]      || false
            "x"   | ["x"]      || true
            "x"   | ["X"]      || true
            "X"   | ["X"]      || true
            "xyz" | ["a", "Y"] || true
            "xYz" | ["a", "y"] || true
            "xyz" | ["a", "z"] || true
            "xyz" | ["a", "B"] || false
    }

    @Unroll
    def "containsAnyIgnoreCase(#value, #parts) == #expected"() {
        expect:
            containsAnyIgnoreCase(value, parts) == expected
        where:
            value     | parts       || expected
            ""        | []          || true
            "x"       | []          || true
            "x"       | [""]        || true
            ""        | ["x"]       || false
            "x"       | ["x"]       || true
            "x"       | ["X"]       || true
            "xyz"     | ["a", "y"]  || true
            "xyz"     | ["a", "yz"] || true
            "xyz"     | ["a", "Yz"] || true
            "xYz"     | ["a", "yz"] || true
            "xyz"     | ["a", "zy"] || false
            "a𝄞b🌉c" | ["𝄞b"]      | true
            "a𝄞b🌉c" | ["𝄞B"]      | true
    }

    @Unroll
    def "containsAll(#value, #parts) == #expected"() {
        expect:
            containsAll(value, parts) == expected
        where:
            value     | parts          || expected
            ""        | []             || true
            "x"       | []             || true
            "x"       | [""]           || true
            ""        | ["x"]          || false
            "x"       | ["x"]          || true
            "x"       | ["x", "x"]     || true
            "x"       | ["X"]          || false
            "xyz"     | ["a", "y"]     || false
            "xyz"     | ["z", "y"]     || true
            "xyz"     | ["z", "Y"]     || false
            "xyz"     | ["xYz", "y"]   || false
            "a𝄞b🌉c" | ["𝄞b", "b🌉"] || true
            "a𝄞b🌉c" | ["𝄞B", "b🌉"] || false
    }

    @Unroll
    def "containsAll(#value, #parts as char[]) == #expected"() {
        expect:
            containsAll(value, parts as char[]) == expected
        where:
            value | parts      || expected
            ""    | []         || true
            "x"   | []         || true
            ""    | ["x"]      || false
            "x"   | ["x"]      || true
            "x"   | ["x", "x"] || true
            "x"   | ["X"]      || false
            "xyz" | ["a", "y"] || false
            "xyz" | ["z", "y"] || true
            "xyz" | ["z", "Y"] || false
    }

    @Unroll
    def "containsAllIgnoreCase(#value, #parts) == #expected"() {
        expect:
            containsAllIgnoreCase(value, parts) == expected
        where:
            value     | parts          || expected
            ""        | []             || true
            "x"       | []             || true
            "x"       | [""]           || true
            ""        | ["x"]          || false
            "x"       | ["x"]          || true
            "x"       | ["x", "x"]     || true
            "x"       | ["X"]          || true
            "xyz"     | ["a", "y"]     || false
            "xyz"     | ["z", "y"]     || true
            "xyz"     | ["z", "Y"]     || true
            "xyz"     | ["xYz", "y"]   || true
            "a𝄞b🌉c" | ["𝄞b", "b🌉"] || true
            "a𝄞b🌉c" | ["𝄞B", "b🌉"] || true
    }

    @Unroll
    def "containsAllIgnoreCase(#value, #parts as char[]) == #expected"() {
        expect:
            containsAllIgnoreCase(value, parts as char[]) == expected
        where:
            value | parts      || expected
            ""    | []         || true
            "x"   | []         || true
            ""    | ["x"]      || false
            "x"   | ["x"]      || true
            "x"   | ["x", "X"] || true
            "x"   | ["X"]      || true
            "xyz" | ["a", "y"] || false
            "xyz" | ["z", "y"] || true
            "xyz" | ["z", "Y"] || true
            "xYz" | ["Z", "y"] || true
    }

    @Unroll
    def "containsAllInOrder(#value, #parts) == #expected"() {
        expect:
            containsAllInOrder(value, parts) == expected
        where:
            value     | parts           || expected
            ""        | []              || true
            "x"       | []              || true
            "x"       | [""]            || true
            ""        | ["x"]           || false
            "x"       | ["x"]           || true
            "x"       | ["X"]           || false
            "xyz"     | ["x", "y", "z"] || true
            "xyz"     | ["x", "yz"]     || true
            "xyz"     | ["yz"]          || true
            "xyz"     | ["x", "yZ"]     || false
            "xyZ"     | ["x", "yz"]     || false
            "xyz"     | ["xyz", "z"]    || false
            "xyz"     | ["xyz", ""]     || true
            "a𝄞b🌉c" | ["𝄞b", "b🌉"]  || false
            "a𝄞b🌉c" | ["𝄞b", "🌉c"]  || true
    }

    @Unroll
    def "containsAllIgnoreCaseInOrder(#value, #parts) == #expected"() {
        expect:
            containsAllIgnoreCaseInOrder(value, parts) == expected
        where:
            value     | parts           || expected
            ""        | []              || true
            "x"       | []              || true
            "x"       | [""]            || true
            ""        | ["x"]           || false
            "x"       | ["x"]           || true
            "x"       | ["X"]           || true
            "xyz"     | ["x", "y", "z"] || true
            "xyz"     | ["x", "yz"]     || true
            "xyz"     | ["yz"]          || true
            "xyz"     | ["x", "yZ"]     || true
            "xyZ"     | ["x", "yz"]     || true
            "xyz"     | ["xyz", "z"]    || false
            "xyz"     | ["xyz", ""]     || true
            "a𝄞b🌉c" | ["𝄞b", "B🌉"]  || false
            "a𝄞b🌉c" | ["𝄞B", "🌉c"]  || true
    }

    @Unroll
    def "containsWhitespaces(#value) == #expected"() {
        expect:
            containsWhitespaces(value) == expected
        where:
            value     || expected
            ""        || false
            "x"       || false
            "x x"     || true
            " x"      || true
            "x "      || true
            "xy\tz"   || true
            "xy\nz"   || true
            "a𝄞b🌉c" || false
    }
}
