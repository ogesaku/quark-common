package com.coditory.quark.common.encode


import com.coditory.quark.commons.encode.lookup.LookupCodec
import spock.lang.Specification
import spock.lang.Unroll

class LookupCodecSpec extends Specification {
    @Unroll
    def "should encode with lookup map: #input"() {
        given:
            TranslationCodec codec = LookupCodec.forLookupMap(a: "XX", b: "YY")
        expect:
            codec.encode(input) == output
        and:
            codec.decode(output) == input
        where:
            input         || output
            ""            || ""
            "Lorem ipsum" || "Lorem ipsum"
            "Jack Rabbit" || "JXXck RXXYYYYit"
            "a語b"         || "XX語YY"
            "🌉a"         || "🌉XX"
    }

    def "should encode each character only once"() {
        given:
            TranslationCodec codec = LookupCodec.forLookupMap(a: "XX", "XX": "YY")
        expect:
            codec.encode("aa") == "XXXX"
    }
}
