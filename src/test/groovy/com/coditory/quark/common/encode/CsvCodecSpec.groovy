package com.coditory.quark.common.encode


import com.coditory.quark.common.encode.csv.CsvCodec
import spock.lang.Specification
import spock.lang.Unroll

class CsvCodecSpec extends Specification {
    @Unroll
    def "should pass without encoding: #input"() {
        expect:
        CsvCodec.encode(input) == input
        and:
            CsvCodec.decode(input) == input
        where:
            input << [
                    "",
                    " ",
                    "'",
                    "\"",
                    "/",
                    "\\",
                    "This space is non-breaking: \u00a0",
                    "a語b",
                    "🌉",
                    "&#x80;&#x9F;",
                    "a\\45b"
            ]
    }

    @Unroll
    def "should encode and decode: #input"() {
        expect:
            CsvCodec.encode(input) == output
        and:
            CsvCodec.decode(output) == input
        where:
            input                      || output
            ","                        || "\",\""
            "He didn't say, \"stop!\"" || "\"He didn't say, \"\"stop!\"\"\""
    }
}
