package com.coditory.quark.common.util.string

import spock.lang.Specification
import spock.lang.Unroll

import static com.coditory.quark.common.util.Strings.*

class StringsSubstringSpec extends Specification {
    @Unroll
    def "truncate(#value, #size) == #expected"() {
        expect:
            truncate(value, size) == expected
        where:
            value     | size || expected
            ""        | 4    || ""
            "abcdefg" | 4    || "abcd"
            "abcdefg" | 6    || "abcdef"
            "abcdefg" | 7    || "abcdefg"
            "abcdefg" | 8    || "abcdefg"
    }


    @Unroll
    def "substring(#value, #from) == #expected"() {
        expect:
            substring(value, from) == expected
        where:
            value | from || expected
            ""    | 3    || ""
            "abc" | 0    || "abc"
            "abc" | 2    || "c"
            "abc" | 4    || ""
            "abc" | -2   || "bc"
            "abc" | -4   || "abc"
    }

    @Unroll
    def "substring(#value, #from, #to) == #expected"() {
        expect:
            substring(value, from, to) == expected
        where:
            value | from | to || expected
            ""    | 0    | 2  || ""
            "abc" | 0    | 2  || "ab"
            "abc" | 2    | 0  || ""
            "abc" | 2    | 4  || "c"
            "abc" | 4    | 6  || ""
            "abc" | 2    | 2  || ""
            "abc" | -2   | -1 || "b"
            "abc" | -4   | 2  || "ab"
    }
}
