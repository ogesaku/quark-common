package com.coditory.quark.common.encode

import spock.lang.Specification
import spock.lang.Unroll

class JsonCodecSpec extends Specification {
    @Unroll
    def "should pass without encoding: #input"() {
        expect:
            JsonCodec.encode(input) == input
        and:
            JsonCodec.decode(input) == input
        where:
            input << [
                    "",
                    "any carnal pleasure",
                    "hello 'John'"
            ]
    }

    @Unroll
    def "should encode and decode: #input"() {
        expect:
            JsonCodec.encode(input) == output
        and:
            JsonCodec.decode(output) == input
        where:
            input              || output
            'hello "John"'     || 'hello \\"John\\"'
            'hello \\"John\\"' || 'hello \\\\\\"John\\\\\\"'
            "slash /"          || "slash \\/"
            "a\nb\tc"          || "a\\nb\\tc"
            "a語b"              || "a\\u8A9Eb"
            "🌉"               || "\\uD83C\\uDF09"
    }

    @Unroll
    def "should decode octal chars: #input"() {
        expect:
            JsonCodec.decode(input) == output
        where:
            input    || output
            "a\\45b" || "a%b"
    }
}
