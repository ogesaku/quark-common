package com.coditory.quark.common.util.string

import spock.lang.Specification
import spock.lang.Unroll

class StringsReplaceSpec extends Specification {
    @Unroll
    def "replace(#value, #search, #replacement) == #expected"() {
        expect:
            replace(value, search, replacement) == expected
        where:
            value     | search | replacement || expected
            ""        | ""     | "x"         || ""
            ""        | "x"    | "x"         || ""
            "x"       | "x"    | "xx"        || "xx"
            "x"       | "xx"   | "y"         || "x"
            "x"       | "x"    | ""          || ""
            "xyxyx"   | "x"    | "y"         || "yyyyy"
            "xyxyx"   | "xy"   | "zzz"       || "zzzzzzx"
            "ABC"     | "a"    | "x"         || "ABC"
            "a𝄞b𝄞c" | "𝄞"   | "🌉"        || "a🌉b🌉c"
            "a𝄞b𝄞c" | "b"    | "🌉"        || "a𝄞🌉𝄞c"
    }

    @Unroll
    def "replaceIgnoreCase(#value, #search, #replacement) == #expected"() {
        expect:
            replaceIgnoreCase(value, search, replacement) == expected
        where:
            value     | search | replacement || expected
            ""        | ""     | "x"         || ""
            ""        | "x"    | "x"         || ""
            "x"       | "x"    | "xx"        || "xx"
            "x"       | "xx"   | "y"         || "x"
            "x"       | "x"    | ""          || ""
            "xyxyx"   | "x"    | "y"         || "yyyyy"
            "xyxyx"   | "xy"   | "zzz"       || "zzzzzzx"
            "AbC"     | "aB"   | "x"         || "xC"
            "a𝄞b𝄞c" | "𝄞"   | "🌉"        || "a🌉b🌉c"
    }

    @Unroll
    def "replaceFirst(#value, #search, #replacement) == #expected"() {
        expect:
            replaceFirst(value, search, replacement) == expected
        where:
            value     | search | replacement || expected
            ""        | ""     | "x"         || ""
            ""        | "x"    | "x"         || ""
            "x"       | "x"    | "xx"        || "xx"
            "x"       | "xx"   | "y"         || "x"
            "x"       | "x"    | ""          || ""
            "xyxyx"   | "x"    | "y"         || "yyxyx"
            "xyxyx"   | "xy"   | "zzz"       || "zzzxyx"
            "AbabC"   | "aB"   | "x"         || "AbabC"
            "a𝄞b𝄞c" | "𝄞"   | "🌉"        || "a🌉b𝄞c"
    }

    @Unroll
    def "replaceFirstIgnoreCase(#value, #search, #replacement) == #expected"() {
        expect:
            replaceFirstIgnoreCase(value, search, replacement) == expected
        where:
            value     | search | replacement || expected
            ""        | ""     | "x"         || ""
            ""        | "x"    | "x"         || ""
            "x"       | "x"    | "xx"        || "xx"
            "x"       | "xx"   | "y"         || "x"
            "x"       | "x"    | ""          || ""
            "xyxyx"   | "x"    | "y"         || "yyxyx"
            "xyxyx"   | "xy"   | "zzz"       || "zzzxyx"
            "AbC"     | "Bc"   | "x"         || "Ax"
            "a𝄞b𝄞c" | "𝄞"   | "🌉"        || "a🌉b𝄞c"
    }

    @Unroll
    def "replaceLast(#value, #search, #replacement) == #expected"() {
        expect:
            replaceLast(value, search, replacement) == expected
        where:
            value     | search | replacement || expected
            ""        | ""     | "x"         || ""
            ""        | "x"    | "x"         || ""
            "x"       | "x"    | "xx"        || "xx"
            "x"       | "xx"   | "y"         || "x"
            "x"       | "x"    | ""          || ""
            "xyxyx"   | "x"    | "y"         || "xyxyy"
            "xyxyx"   | "xy"   | "zzz"       || "xyzzzx"
            "AbabC"   | "aB"   | "x"         || "AbabC"
            "a𝄞b𝄞c" | "𝄞"   | "🌉"        || "a𝄞b🌉c"
    }

    @Unroll
    def "replaceLastIgnoreCase(#value, #search, #replacement) == #expected"() {
        expect:
            replaceLastIgnoreCase(value, search, replacement) == expected
        where:
            value     | search | replacement || expected
            ""        | ""     | "x"         || ""
            ""        | "x"    | "x"         || ""
            "x"       | "x"    | "xx"        || "xx"
            "x"       | "xx"   | "y"         || "x"
            "x"       | "x"    | ""          || ""
            "xyxyx"   | "x"    | "y"         || "xyxyy"
            "xyxyx"   | "xy"   | "zzz"       || "xyzzzx"
            "AbC"     | "Bc"   | "x"         || "Ax"
            "a𝄞b𝄞c" | "𝄞"   | "🌉"        || "a𝄞b🌉c"
    }
}
