package com.coditory.quark.common.text

import spock.lang.Specification
import spock.lang.Unroll


class WordsSpec extends Specification {
    @Unroll
    def "capitalize(#text) == #expected"() {
        expect:
            Words.capitalize(text) == expected
        where:
            text            || expected
            "abc def ghi"   || "Abc Def Ghi"
            "Abc Def Ghi"   || "Abc Def Ghi"
            "abc's def-ghi" || "Abc's Def-Ghi"
            ""              || ""
    }

    @Unroll
    def "abbreviate(#value, #marker, #maxWidth) == #expected"() {
        expect:
            Words.abbreviate(value, maxWidth, marker) == expected
        where:
            value     | marker | maxWidth || expected
            ""        | "..."  | 4        || ""
            "abcdefg" | "."    | 5        || "abcd."
            "abcdefg" | "."    | 7        || "abcdefg"
            "abcdefg" | "."    | 8        || "abcdefg"
            "abcdefg" | ".."   | 4        || "ab.."
            "abcdefg" | ".."   | 3        || "a.."
    }

    @Unroll
    def "abbreviate(#value, #maxWidth) == #expected"() {
        expect:
            Words.abbreviate(value, maxWidth) == expected
        where:
            value               | maxWidth || expected
            ""                  | 4        || ""
            "ab cd efg"         | 8        || "ab cd..."
            "ab cd efg"         | 9        || "ab cd efg"
            "ab cd efgh"        | 9        || "ab cd..."
            "ab cd,   \n  efgh" | 9        || "ab cd..."
            "ab cdefgh"         | 8        || "ab..."
            "ab  \n\t  \n\r "   | 8        || "ab..."
    }

    def "breakLines(#input, #length) == #expected"() {
        given:
            String expectedResult = expected.join("\n")
        expect:
            Words.breakLines(input, length) == expectedResult
        where:
            input          | length || expected
            "abcdefghi"    | 3      || ["abc", "def", "ghi"]
            "a\nbcdef"     | 3      || ["a", "bcd", "ef"]
            "abc def  g  " | 3      || ["abc", "def", "g  "]
            "abc def  g  " | 5      || ["abc", "def ", "g  "]
    }
}
