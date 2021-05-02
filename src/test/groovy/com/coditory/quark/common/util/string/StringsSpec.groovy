package com.coditory.quark.common.util.string


import com.coditory.quark.commons.throwable.ThrowingSupplier
import spock.lang.Specification
import spock.lang.Unroll

class StringsSpec extends Specification {
    @Unroll
    def "should detect empty string: #value -> #expected"() {
        expect:
            isNullOrEmpty(value) == expected
        and:
            isNotEmpty(value) != expected
        where:
            value || expected
            ""    || true
            null  || true
            " "   || false
            "abc" || false
    }

    @Unroll
    def "should detect blank string: #value -> #expected"() {
        expect:
            isNullOrBlank(value) == expected
        and:
            isNotBlank(value) != expected
        where:
            value || expected
            ""    || true
            null  || true
            " "   || true
            "abc" || false
    }

    @Unroll
    def "blankToNull(#value) == #expected"() {
        expect:
            blankToNull(value) == expected
        where:
            value || expected
            null  || null
            ""    || null
            " "   || null
            "a"   || "a"
    }

    @Unroll
    def "defaultIfBlank(#value, #defaultValue) == #expected"() {
        expect:
            defaultIfBlank(value, defaultValue) == expected
        and:
            defaultIfBlank(value, { defaultValue } as ThrowingSupplier<String>) == expected
        where:
            value | defaultValue || expected
            null  | "x"          || "x"
            " "   | "x"          || "x"
            "a"   | "x"          || "a"
    }

    @Unroll
    def "defaultIfEmpty(#value, #defaultValue) == #expected"() {
        expect:
            defaultIfEmpty(value, defaultValue) == expected
        and:
            defaultIfEmpty(value, { defaultValue } as ThrowingSupplier<String>) == expected
        where:
            value | defaultValue || expected
            null  | "x"          || "x"
            ""    | "x"          || "x"
            " "   | "x"          || " "
            "a"   | "x"          || "a"
    }

    @Unroll
    def "toUpperCase(#value) == #expected"() {
        expect:
            upperCase(value) == expected
        where:
            value       || expected
            ""          || ""
            " a "       || " A "
            "Abc def"   || "ABC DEF"
            "-120,(9)%" || "-120,(9)%"
            "ąćęłńóśżź" || "ĄĆĘŁŃÓŚŻŹ"
            "ĄĆĘŁŃÓŚŻŹ" || "ĄĆĘŁŃÓŚŻŹ"
    }

    @Unroll
    def "toLowerCase(#value) == #expected"() {
        expect:
            lowerCase(value) == expected
        where:
            value       || expected
            ""          || ""
            " A "       || " a "
            "ABC DEF"   || "abc def"
            "-120,(9)%" || "-120,(9)%"
            "ąćęłńóśżź" || "ąćęłńóśżź"
            "ĄĆĘŁŃÓŚŻŹ" || "ąćęłńóśżź"
    }

    @Unroll
    def "indexOfIgnoreCase(#value, #part) == #expected"() {
        expect:
            indexOfIgnoreCase(value, part) == expected
        where:
            value | part || expected
            ""    | ""   || 0
            "x"   | ""   || 0
            ""    | "x"  || -1
            "x"   | "x"  || 0
            "xy"  | "xY" || 0
            "xy"  | "Y"  || 1
            "xy"  | "y"  || 1
            "xyz" | "Yz" || 1
    }

    @Unroll
    def "indexOfIgnoreCase(#value, #part, #from) == #expected"() {
        expect:
            indexOfIgnoreCase(value, part, from) == expected
        where:
            value  | part | from || expected
            ""     | ""   | 0    || 0
            ""     | ""   | 1    || 0
            "x"    | ""   | 0    || 0
            ""     | "x"  | 0    || -1
            "x"    | "x"  | 0    || 0
            "x"    | "x"  | 1    || -1
            "xy"   | "xY" | 0    || 0
            "xy"   | "Y"  | 0    || 1
            "xy"   | "y"  | 0    || 1
            "xyz"  | "Yz" | 0    || 1
            "aBab" | "ab" | 0    || 0
            "aBab" | "ab" | 1    || 2
            "aBab" | "ab" | 2    || 2
            "aBab" | "ab" | 3    || -1
            "ab語c" | "語"  | 0    || 2
            "ab語c" | "c"  | 0    || 3
    }

    @Unroll
    def "stripAccents(#value) == #expected"() {
        expect:
            stripAccents(value) == expected
        where:
            value                || expected
            ""                   || ""
            "aA123! @\t\n#"      || "aA123! @\t\n#"
            "ĄąĆćĘęŁłŃńÓóŚśŹźŻż" || "AaCcEeLlNnOoSsZzZz"
            "ĈĉĜĝĤĥĴĵŜŝŬŭ"       || "CcGgHhJjSsUu"
            "Джокович"           || "Джокович"
            "ab語c"               || "ab語c"
    }

    @Unroll
    def "reverse(#value) == #expected"() {
        expect:
            reverse(value) == expected
        where:
            value     || expected
            ""        || ""
            "abc"     || "cba"
            "ab語c"    || "c語ba"
            "a𝄞b🌉c" || "c🌉b𝄞a"
    }

    @Unroll
    def "firstNonBlank(#value) == #expected"() {
        expect:
            firstNonBlank(*value) == expected
        where:
            value                  || expected
            ["a", " A"]            || "a"
            ["", null, "a", " A"]  || "a"
            ["  ", "\n\t\r", " b"] || " b"
    }

    @Unroll
    def "firstNonBlank(#value) throws error"() {
        when:
            firstNonBlank(*value)
        then:
            thrown(IllegalArgumentException)
        where:
            value << [
                    [],
                    [""],
                    [" ", "", null, "\n"]
            ]
    }

    @Unroll
    def "firstNonEmpty(#value) == #expected"() {
        expect:
            firstNonEmptyOrNull(*value) == expected
        where:
            value                 || expected
            ["a", " A"]           || "a"
            ["", null, "a", " A"] || "a"
            ["", " ", " b"]       || " "
            ["", "", "\n"]        || "\n"
    }

    @Unroll
    def "firstNonEmpty(#value) throws error"() {
        when:
            firstNonEmpty(*value)
        then:
            thrown(IllegalArgumentException)
        where:
            value << [
                    [],
                    [""],
                    ["", ""]
            ]
    }

    @Unroll
    def "nullToEmpty(#value) == #expected"() {
        expect:
            nullToEmpty(value) == expected
        where:
            value || expected
            null  || ""
            ""    || ""
            " "   || " "
            "abc" || "abc"
    }

    @Unroll
    def "emptyToNull(#value) == #expected"() {
        expect:
            emptyToNull(value) == expected
        where:
            value || expected
            null  || null
            ""    || null
            " "   || " "
            "abc" || "abc"
    }

    @Unroll
    def "firstToken(#value) == #expected"() {
        expect:
            firstToken(value, delim as char) == expected
        where:
            value         | delim || expected
            ""            | ","   || ""
            "abc,def,ghi" | ","   || "abc"
            " a ,def,ghi" | ","   || " a "
            ",def,ghi"    | ","   || ""
            "abc"         | ","   || "abc"
    }

    @Unroll
    def "lastToken(#value) == #expected"() {
        expect:
            lastToken(value, delim as char) == expected
        where:
            value         | delim || expected
            ""            | ","   || ""
            "abc,def,ghi" | ","   || "ghi"
            "abc,def, g " | ","   || " g "
            "abc,def,"    | ","   || ""
            "abc"         | ","   || "abc"
    }

    def "multiline(#text)"() {
        expect:
            multiline(
                    "{",
                    "  \"name\": \"John\"",
                    "}"
            ) == "{\n  \"name\": \"John\"\n}"
        and:
            multiline([
                    "{",
                    "  \"name\": \"John\"",
                    "}"
            ]) == "{\n  \"name\": \"John\"\n}"
    }

    @Unroll
    def "compactSpaces(#input) == #expected"() {
        expect:
            compactSpaces(input) == expected
        where:
            input            || expected
            "  b   c d  "    || "b c d"
            "  b \n c\t d  " || "b \n c\t d"
            "  \n\t  "       || ""
            ""               || ""
    }

    @Unroll
    def "compactAllSpaces(#input) == #expected"() {
        expect:
            compactAllSpaces(input) == expected
        where:
            input            || expected
            "  b   c d  "    || "b c d"
            "  b \n c\t d  " || "b c d"
    }

    def "minLength(#text)"() {
        expect:
            minLength(
                    "abc",
                    "  def",
                    null,
                    "g"
            ) == 1
    }

    def "maxLength(#text)"() {
        expect:
            maxLength(
                    "abc",
                    "  def",
                    null,
                    "g"
            ) == 5
    }

    def "lines(#text)"() {
        expect:
            splitLines("abc\ndef\n  x  \n\n") == [
                    "abc",
                    "def",
                    "  x  ",
                    "",
                    ""
            ]
    }

    @Unroll
    def "getDiffIndex(#texts) == #expected"() {
        expect:
            getDiffIndex(*texts) == expected
        where:
            texts                 || expected
            ["abc", "abd", "abc"] || 2
            ["abcd", "ab"]        || 2
            ["ab", "aB"]          || 1
            ["ab", "ab"]          || -1
            ["ab"]                || -1
            ["ab", null, "aB"]    || 0
            [null, null]          || -1
            ["", ""]              || -1
            [null, ""]            || 0
    }

    @Unroll
    def "indent(#text, #spaces)"() {
        expect:
            indent(text, spaces) == expected
        where:
            text         | spaces || expected
            "abc\ndef"   | 2      || "  abc\n  def"
            "abc\ndef"   | 3      || "   abc\n   def"
            "abc\n\ndef" | 2      || "  abc\n\n  def"
            "abc\r\ndef" | 2      || "  abc\r\n  def"
            "abc\n def"  | 2      || "  abc\n   def"
    }

    @Unroll
    def "getCommonPrefix(#texts)"() {
        expect:
            getCommonPrefix(*texts) == expected
        where:
            texts                 || expected
            ["abc", "abx", "abc"] || "ab"
            ["abc", "abc"]        || "abc"
            [null, "abc", "abc"]  || ""
            ["abc", null, "abc"]  || ""
            ["abc", ""]           || ""
    }

    @Unroll
    def "isValidSurrogatePair(#text, #index) == #expected"() {
        expect:
            isValidSurrogatePairAt(text, index) == expected
        where:
            text   | index || expected
            "a𝄞b" | -1    || false
            "a𝄞b" | 0     || false
            "a𝄞b" | 1     || true
            "a𝄞b" | 2     || false
            "a𝄞b" | 3     || false
            "a語b"  | 1     || false
    }

    @Unroll
    def "truncate(#text, #from, #size) == #expected"() {
        expect:
            truncate(text, from, size) == expected
        where:
            text   | from | size || expected
            "abc"  | 0    | 3    || "abc"
            "abc"  | 0    | 5    || "abc"
            "abc"  | 0    | 0    || ""
            "abc"  | 0    | 2    || "ab"
            "abc"  | 1    | 1    || "b"
            "abc"  | 1    | 2    || "bc"
            "a𝄞b" | 0    | 3    || "a𝄞"
    }

    @Unroll
    def "removeSurrogates(#text) == #expected"() {
        expect:
            removeSurrogates(text) == expected
        where:
            text      || expected
            "a_b c"   || "a_b c"
            "a語b"     || "a語b"
            "a𝄞b🌉c" || "abc"
    }

    @Unroll
    def "hasSurrogates(#text) == #expected"() {
        expect:
            hasSurrogates(text) == expected
        where:
            text      || expected
            "a_b c"   || false
            "a語b"     || false
            "a𝄞b🌉c" || true
    }

    @Unroll
    def "countSurrogates(#text) == #expected"() {
        expect:
            countSurrogatePairs(text) == expected
        where:
            text      || expected
            "a_b c"   || 0
            "a語b"     || 0
            "a𝄞b🌉c" || 2
    }

    @Unroll
    def "count(#text #part) == #expected"() {
        expect:
            count(text, part) == expected
        where:
            text      | part  || expected
            "abababa" | "aba" || 2
            "abab"    | "ab"  || 2
            "aaa"     | "a"   || 3
            "aaa"     | "aa"  || 1
            "aaa"     | ""    || 0
            ""        | "aa"  || 0
            "aaa"     | null  || 0
            null      | "aa"  || 0
            "a𝄞b🌉c" | "a"   || 1
    }

    @Unroll
    def "count(#text #part as char) == #expected"() {
        expect:
            count(text, part as char) == expected
        where:
            text      | part || expected
            "abababa" | "a"  || 4
            "abababa" | "b"  || 3
            ""        | "a"  || 0
            null      | "a"  || 0
    }
}
