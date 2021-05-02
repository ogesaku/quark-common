package com.coditory.quark.common.text

import spock.lang.Specification


class AsciiSpec extends Specification {
    def "isAsciiControlCode(#input) == #expected"() {
        expect:
            Ascii.isAsciiControlCode(input.codePointAt(0)) == expected
        and:
            Ascii.isAsciiControlCode(input.codePointAt(0) as char) == expected
        where:
            input    || expected
            '\t'     || true
            '\u0001' || true
            'a'      || false
            'ł'      || false
            '語'      || false
    }

    def "isAsciiPrintable(#input) == #expected"() {
        expect:
            Ascii.isAsciiPrintable(input.codePointAt(0)) == expected
        and:
            Ascii.isAsciiPrintable(input.codePointAt(0) as char) == expected
        where:
            input    || expected
            '\t'     || false
            '\u0001' || false
            'a'      || true
            'ł'      || false
            '語'      || false
    }

    def "isAscii(#input) == #expected"() {
        expect:
            Ascii.isAscii(input.codePointAt(0)) == expected
        where:
            input    || expected
            '\t'     || true
            '\u0001' || true
            'a'      || true
            'ł'      || false
            '語'      || false
    }
}
