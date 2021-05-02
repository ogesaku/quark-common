package com.coditory.quark.common.util.string

import spock.lang.Specification
import spock.lang.Unroll

class StringsRemoveSpec extends Specification {
    @Unroll
    def "remove(#value, #part) == #expected"() {
        expect:
            remove(value, part) == expected
        where:
            value     | part  || expected
            ""        | ""    || ""
            "abc"     | ""    || "abc"
            "abcbd"   | "b"   || "acd"
            "abcbd"   | "cb"  || "abd"
            "abcbd"   | "bdx" || "abcbd"
            "abcbd"   | "cB"  || "abcbd"
            "a𝄞b🌉c" | "🌉"  || "a𝄞bc"
    }

    @Unroll
    def "removeFirst(#value, #part) == #expected"() {
        expect:
            removeFirst(value, part) == expected
        where:
            value     | part  || expected
            ""        | ""    || ""
            "abc"     | ""    || "abc"
            "abcbd"   | "b"   || "acbd"
            "abcbd"   | "cb"  || "abd"
            "abcbd"   | "bdx" || "abcbd"
            "abcbd"   | "cB"  || "abcbd"
            "a𝄞b𝄞c" | "𝄞"  || "ab𝄞c"
    }

    @Unroll
    def "removeFirstIgnoreCase(#value, #part) == #expected"() {
        expect:
            removeFirstIgnoreCase(value, part) == expected
        where:
            value   | part  || expected
            ""      | ""    || ""
            "abc"   | ""    || "abc"
            "abcbd" | "b"   || "acbd"
            "abcbd" | "cb"  || "abd"
            "abcbd" | "bdx" || "abcbd"
            "abcbd" | "cB"  || "abd"
    }

    @Unroll
    def "removeIgnoreCase(#value, #part) == #expected"() {
        expect:
            removeIgnoreCase(value, part) == expected
        where:
            value     | part  || expected
            ""        | ""    || ""
            "abc"     | ""    || "abc"
            "abcbd"   | "b"   || "acd"
            "abcbd"   | "cb"  || "abd"
            "abcbd"   | "bdx" || "abcbd"
            "abCbd"   | "cB"  || "abd"
            "a𝄞b𝄞c" | "𝄞"  || "abc"
    }

    @Unroll
    def "removePrefix(#value, #prefix) == #expected"() {
        expect:
            removePrefix(value, prefix) == expected
        where:
            value     | prefix || expected
            ""        | "a"    || ""
            "a"       | ""     || "a"
            "abc"     | "a"    || "bc"
            "abc"     | "A"    || "abc"
            "abcbd"   | "ab"   || "cbd"
            "abcbd"   | "b"    || "abcbd"
            "abcbd"   | "d"    || "abcbd"
            "a𝄞b🌉c" | "a𝄞"  || "b🌉c"
    }

    @Unroll
    def "removePrefixIgnoreCase(#value, #prefix) == #expected"() {
        expect:
            removePrefixIgnoreCase(value, prefix) == expected
        where:
            value   | prefix || expected
            ""      | "a"    || ""
            "a"     | ""     || "a"
            "Abc"   | "a"    || "bc"
            "abc"   | "A"    || "bc"
            "abcbd" | "aB"   || "cbd"
            "abcbd" | "b"    || "abcbd"
            "abcbd" | "d"    || "abcbd"
    }

    @Unroll
    def "removePrefix(#value, #count) == #expected"() {
        expect:
            removePrefix(value, count) == expected
        where:
            value | count || expected
            ""    | 5     || ""
            "abc" | 2     || "c"
            "abc" | 10    || ""
    }

    @Unroll
    def "removeSuffix(#value, #suffix) == #expected"() {
        expect:
            removeSuffix(value, suffix) == expected
        where:
            value   | suffix || expected
            ""      | "a"    || ""
            "a"     | ""     || "a"
            "abc"   | "c"    || "ab"
            "abC"   | "c"    || "abC"
            "abc"   | "C"    || "abc"
            "abcbd" | "bd"   || "abc"
            "abcbd" | "b"    || "abcbd"
            "abcbd" | "a"    || "abcbd"
    }

    @Unroll
    def "removeSuffixIgnoreCase(#value, #suffix) == #expected"() {
        expect:
            removeSuffixIgnoreCase(value, suffix) == expected
        where:
            value   | suffix || expected
            ""      | "a"    || ""
            "a"     | ""     || "a"
            "abC"   | "c"    || "ab"
            "abc"   | "C"    || "ab"
            "abcbd" | "bD"   || "abc"
            "abcbd" | "b"    || "abcbd"
            "abcbd" | "a"    || "abcbd"
    }

    @Unroll
    def "removeSuffix(#value, #count) == #expected"() {
        expect:
            removeSuffix(value, count) == expected
        where:
            value     | count || expected
            ""        | 5     || ""
            "abc"     | 2     || "a"
            "abc"     | 10    || ""
    }
}
