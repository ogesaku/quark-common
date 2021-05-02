package com.coditory.quark.common.encode


import spock.lang.Specification
import spock.lang.Unroll

class EcmaScriptCodecSpec extends Specification {
    @Unroll
    def "should pass without encoding: #input"() {
        expect:
            EcmaScriptCodec.encode(input) == input
        and:
            EcmaScriptCodec.decode(input) == input
        where:
            input << [
                    "",
                    " ",
                    ",",
                    "&#x80;&#x9F;"
            ]
    }

    @Unroll
    def "should encode and decode: #input"() {
        expect:
            EcmaScriptCodec.encode(input) == output
        and:
            EcmaScriptCodec.decode(output) == input
        where:
            input                                || output
            "'"                                  || "\\'"
            "\""                                 || "\\\""
            "/"                                  || "\\/"
            "\\"                                 || "\\\\"
            "\b\t\r\n"                           || "\\b\\t\\r\\n"
            "\uabcd"                             || "\\uABCD"
            "\uABCD"                             || "\\uABCD"
            "\u1234"                             || "\\u1234"
            "\u0234"                             || "\\u0234"
            "\u0001"                             || "\\u0001"
            "He didn't say, \"stop!\""           || "He didn\\'t say, \\\"stop!\\\""
            "This space is non-breaking: \u00a0" || "This space is non-breaking: \\u00A0"
            "a語b"                                || "a\\u8A9Eb"
            "🌉"                                 || "\\uD83C\\uDF09"
            "a\\45b"                             || "a\\\\45b"
    }

    def "should encode ES sample code"() {
        when:
            String result = EcmaScriptCodec.encode("document.getElementById(\"test\").value = '<script>alert('aaa');</script>';")
        then:
            result == "document.getElementById(\\\"test\\\").value = \\'<script>alert(\\'aaa\\');<\\/script>\\';"
    }
}
