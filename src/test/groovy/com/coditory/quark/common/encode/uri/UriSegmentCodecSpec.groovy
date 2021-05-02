package com.coditory.quark.common.encode.uri

import spock.lang.Specification
import spock.lang.Unroll

class UriSegmentCodecSpec extends Specification {
    @Unroll
    def "should pass without encoding: #input"() {
        expect:
            UriSegmentCodec.encode(input) == input
        and:
            UriSegmentCodec.decode(input) == input
        where:
            input << [
                    "",
                    "aZ09+",
                    "-._~",
                    "!\$&'()*+,;=:@"
            ]
    }

    @Unroll
    def "should encode and decode: #input"() {
        expect:
            UriSegmentCodec.encode(input) == output
        and:
            UriSegmentCodec.decode(output) == input
        where:
            input  || output
            " "    || "%20"
            "#abc" || "%23abc"
            "®"    || "%C2%AE"
            "/?"   || "%2F%3F"
            "a語b"  || "a%E8%AA%9Eb"
            "🌉"   || "%F0%9F%8C%89"
            "\n"   || "%0A"
    }
}
