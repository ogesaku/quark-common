package com.coditory.quark.common.text

import spock.lang.Specification
import spock.lang.Unroll

import static Alphabets.asciiAlphabet

class AlphabetsSpec extends Specification {
    def "should create basic alphabets"() {
        expect:
            Alphabets.ASCII_PRINTABLE == " !\"#\$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~"
            Alphabets.ASCII_CONTROL_CODES == "\0\u0001\u0002\u0003\u0004\u0005\u0006\u0007\b\t\n\u000B\f\r\u000E\u000F\u0010\u0011\u0012\u0013\u0014\u0015\u0016\u0017\u0018\u0019\u001A\u001B\u001C\u001D\u001E\u001F\u007F"
            Alphabets.URI_UNRESERVED == "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-._~"
    }

    @Unroll
    def "asciiAlphabet(#from, #to) == #expected"() {
        expect:
            asciiAlphabet(from, to) == expected
        where:
            from | to || expected
            66   | 70 || "BCDE"
            66   | 67 || "B"
    }
}
