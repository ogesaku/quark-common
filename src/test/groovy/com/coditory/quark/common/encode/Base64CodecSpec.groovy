package com.coditory.quark.common.encode

import spock.lang.Specification

class Base64CodecSpec extends Specification {
    def "should encode and decode: #input"() {
        expect:
            Base64Codec.encode(input) == output
        and:
            Base64Codec.decode(output) == input
        where:
            input                                || output
            ""                                   || ""
            " "                                  || "IA=="
            ","                                  || "LA=="
            "'"                                  || "Jw=="
            "\""                                 || "Ig=="
            "/"                                  || "Lw=="
            "\\"                                 || "XA=="
            "\b\t\r\n"                           || "CAkNCg=="
            "\uabcd"                             || "6q+N"
            "\uABCD"                             || "6q+N"
            "He didn't say, \"stop!\""           || "SGUgZGlkbid0IHNheSwgInN0b3AhIg=="
            "This space is non-breaking: \u00a0" || "VGhpcyBzcGFjZSBpcyBub24tYnJlYWtpbmc6IMKg"
            "a語b"                                || "YeiqnmI="
            "🌉"                                 || "8J+MiQ=="
            "&#x80;&#x9F;"                       || "JiN4ODA7JiN4OUY7"
            "a\\45b"                             || "YVw0NWI="
    }
}
