package com.coditory.quark.common.util.string

import spock.lang.Specification
import spock.lang.Unroll

import static com.coditory.quark.commons.util.Strings.repeat

class StringsRepeatSpec extends Specification {
    @Unroll
    def "repeat(#value, #count) == #expected"() {
        expect:
            repeat(value, count) == expected
        where:
            value | count || expected
            ""    | 0     || ""
            ""    | 2     || ""
            "e"   | 1     || "e"
            "a"   | 3     || "aaa"
            "ab"  | 2     || "abab"
            "𝄞"  | 3     || "𝄞𝄞𝄞"
    }

    @Unroll
    def "repeat(#charValue, #count) == #expected"() {
        expect:
            repeat(charValue as char, count) == expected
        where:
            charValue | count || expected
            'e'       | 0     || ""
            'e'       | 1     || "e"
            'e'       | 3     || "eee"
    }

    @Unroll
    def "repeat(#value, #separator, #count) == #expected"() {
        expect:
            repeat(value, separator, count) == expected
        where:
            value  | separator | count || expected
            ""     | ""        | 2     || ""
            ""     | "x"       | 3     || "xx"
            "?"    | "| "      | 3     || "?| ?| ?"
            "a𝄞b" | "🌉"      | 3     || "a𝄞b🌉a𝄞b🌉a𝄞b"
    }
}
