package com.coditory.quark.common.text

import spock.lang.Specification

class LennySpec extends Specification {
    def "should generate lenny: #expected"() {
        expect:
            actual == expected
        where:
            actual        || expected
            Lenny.shrug() || "¯\\_(ツ)_/¯"
            Lenny.flex()  || "ᕦ(ツ)ᕤ"
            Lenny.smile() || "(ツ)"
    }
}
