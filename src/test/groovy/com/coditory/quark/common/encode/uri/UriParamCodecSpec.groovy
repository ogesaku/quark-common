package com.coditory.quark.common.encode.uri

import spock.lang.Specification
import spock.lang.Unroll

class UriParamCodecSpec extends Specification {
    @Unroll
    def "should pass without encoding: #input"() {
        expect:
            UriParamCodec.encode(input) == input
        and:
            UriParamCodec.decode(input) == input
        where:
            input << [
                    "",
                    "aZ09",
                    "-._~"
            ]
    }

    @Unroll
    def "should encode and decode: #input"() {
        expect:
            UriParamCodec.encode(input) == output
        and:
            UriParamCodec.decode(output) == input
        where:
            input            || output
            " "              || "+"
            "+"              || "%2B"
            "#abc"           || "%23abc"
            "®"              || "%C2%AE"
            "a語b"            || "a%E8%AA%9Eb"
            "🌉"             || "%F0%9F%8C%89"
            "\n"             || "%0A"
            "!\$&'()*+,;=:@" || "%21%24%26%27%28%29%2A%2B%2C%3B%3D%3A%40"
            "+/?"            || "%2B%2F%3F"
    }
}
