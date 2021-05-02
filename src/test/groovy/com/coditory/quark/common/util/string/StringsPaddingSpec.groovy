package com.coditory.quark.common.util.string

import spock.lang.Specification
import spock.lang.Unroll

import static com.coditory.quark.common.util.Strings.*

class StringsPaddingSpec extends Specification {
    @Unroll
    def "leftPad(#value, #size) == #expected"() {
        expect:
            leftPad(value, size) == expected
        where:
            value     | size || expected
            ""        | 3    || "   "
            "bat"     | 3    || "bat"
            "bat"     | 5    || "  bat"
            "bat"     | 1    || "bat"
    }

    @Unroll
    def "leftPad(#value, #size, #padChar) == #expected"() {
        expect:
            leftPad(value, size, padChar) == expected
        where:
            value     | size | padChar || expected
            ""        | 3    | 'z'     || "zzz"
            "bat"     | 3    | 'z'     || "bat"
            "bat"     | 5    | 'z'     || "zzbat"
            "bat"     | 1    | 'z'     || "bat"
    }

    @Unroll
    def "leftPad(#value, #size, #padString) == #expected"() {
        expect:
            leftPad(value, size, padString) == expected
        where:
            value     | size | padString || expected
            ""        | 3    | "z"       || "zzz"
            "bat"     | 3    | "yz"      || "bat"
            "bat"     | 5    | "yz"      || "yzbat"
            "bat"     | 8    | "yz"      || "yzyzybat"
            "bat"     | 1    | "yz"      || "bat"
            "bat"     | 5    | ""        || "bat"
    }

    @Unroll
    def "rightPad(#value, #size) == #expected"() {
        expect:
            rightPad(value, size) == expected
        where:
            value     | size || expected
            ""        | 3    || "   "
            "bat"     | 3    || "bat"
            "bat"     | 5    || "bat  "
            "bat"     | 1    || "bat"
    }

    @Unroll
    def "rightPad(#value, #size #padChar) == #expected"() {
        expect:
            rightPad(value, size, padChar) == expected
        where:
            value     | size | padChar || expected
            ""        | 3    | 'z'     || "zzz"
            "bat"     | 3    | 'z'     || "bat"
            "bat"     | 5    | 'z'     || "batzz"
            "bat"     | 1    | 'z'     || "bat"
    }

    @Unroll
    def "rightPad(#value, #size, #padString) == #expected"() {
        expect:
            rightPad(value, size, padString) == expected
        where:
            value     | size | padString || expected
            ""        | 3    | "z"       || "zzz"
            "bat"     | 3    | "yz"      || "bat"
            "bat"     | 5    | "yz"      || "batyz"
            "bat"     | 8    | "yz"      || "batyzyzy"
            "bat"     | 1    | "yz"      || "bat"
    }
}
