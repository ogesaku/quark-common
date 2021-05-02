package com.coditory.quark.common.encode

import spock.lang.Specification
import spock.lang.Unroll

class Html4CodecSpec extends Specification {
    @Unroll
    def "should pass without encoding: #input"() {
        expect:
            Html4Codec.encode(input) == input
        and:
            Html4Codec.decode(input) == input
        where:
            input << [
                    "",
                    " ",
                    ",",
                    "'",
                    "/",
                    "\\",
                    "\uabcd",
                    "\uABCD",
                    "a語b",
                    "🌉",
                    "a\\45b"
            ]
    }

    @Unroll
    def "should encode and decode: #input"() {
        expect:
            Html4Codec.encode(input) == output
        and:
            Html4Codec.decode(output) == input
        where:
            input                                || output
            "\""                                 || "&quot;"
            "He didn't say, \"stop!\""           || "He didn't say, &quot;stop!&quot;"
            "This space is non-breaking: \u00a0" || "This space is non-breaking: &nbsp;"
            "bread & butter"                     || "bread &amp; butter"
            "\"bread\" & butter"                 || "&quot;bread&quot; &amp; butter"
            "greater than >"                     || "greater than &gt;"
            "< less than"                        || "&lt; less than"
            "He didn't say, \"stop!\""           || "He didn't say, &quot;stop!&quot;"
            "English,Français,日本語 (nihongo)"     || "English,Fran&ccedil;ais,日本語 (nihongo)"
            "\u0392"                             || "&Beta;"
    }

    @Unroll
    def "should decode hex chars: #input"() {
        expect:
            Html4Codec.decode(input) == output
        where:
            input          || output
            "&#x80;&#x9F;" || "\u0080\u009F"
    }

    @Unroll
    def "should decode special cases: #input"() {
        expect:
            Html4Codec.decode(input) == output
        where:
            input         || output
            "&lt;P&O&gt;" || "<P&O>"
            "test & &lt;" || "test & <"
    }
}
