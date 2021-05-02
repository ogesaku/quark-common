package com.coditory.quark.common.util.string

import spock.lang.Specification
import spock.lang.Unroll

import static com.coditory.quark.common.util.Strings.*

class StringsStartsEndsWithSpec extends Specification {
    @Unroll
    def "startsWith(#value, #part) == #expected"() {
        expect:
            startsWith(value, part) == expected
        where:
            value     | part  || expected
            ""        | ""    || true
            "abc"     | "ab"  || true
            "abc"     | "aB"  || false
            "a𝄞b🌉c" | "a𝄞" || true
    }

    @Unroll
    def "startsWithIgnoreCase(#value, #part) == #expected"() {
        expect:
            startsWithIgnoreCase(value, part) == expected
        where:
            value     | part  || expected
            ""        | ""    || true
            "abc"     | "ab"  || true
            "Abc"     | "aB"  || true
            "Abc"     | "xB"  || false
            "a𝄞b🌉c" | "A𝄞" || true
    }

    @Unroll
    def "endsWith(#value, #part) == #expected"() {
        expect:
            endsWith(value, part) == expected
        where:
            value     | part  || expected
            ""        | ""    || true
            "abc"     | "bc"  || true
            "abc"     | "Bc"  || false
            "a𝄞b🌉c" | "🌉c" || true
    }

    @Unroll
    def "endsWithIgnoreCase(#value, #part) == #expected"() {
        expect:
            endsWithIgnoreCase(value, part) == expected
        where:
            value     | part  || expected
            ""        | ""    || true
            "abc"     | "bc"  || true
            "abC"     | "Bc"  || true
            "abX"     | "Bc"  || false
            "a𝄞b🌉c" | "🌉C" || true
    }
}
