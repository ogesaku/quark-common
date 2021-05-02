package com.coditory.quark.common.encode.uri

import spock.lang.Specification
import spock.lang.Unroll

class UriFragmentCodecSpec extends Specification {
    @Unroll
    def "should pass without encoding: #input"() {
        expect:
            UriFragmentCodec.encode(input) == input
        and:
            UriFragmentCodec.decode(input) == input
        where:
            input << [
                    "",
                    "aZ09",
                    "-._~",
                    "!\$&'()*+,;=:@",
                    "+/?"
            ]
    }

    @Unroll
    def "should encode and decode: #input"() {
        expect:
            UriFragmentCodec.encode(input) == output
        and:
            UriFragmentCodec.decode(output) == input
        where:
            input  || output
            " "    || "%20"
            "#abc" || "%23abc"
            "®"    || "%C2%AE"
            "a語b"  || "a%E8%AA%9Eb"
            "🌉"   || "%F0%9F%8C%89"
            "\n"   || "%0A"
    }
}
