package com.coditory.quark.common.check


import com.coditory.quark.common.check.base.ArgsChecker
import com.coditory.quark.common.check.base.Checker
import spock.lang.Unroll

class ArgsSpec extends CheckSpec {
    Checker checker = new ArgsChecker()

    @Unroll
    def "checkNoBlanks(#value) throw error"() {
        when:
            Args.checkNoBlanks(value)
        then:
            IllegalArgumentException e = thrown(IllegalArgumentException)
            e.message.contains(message)
        where:
            value || message
            ""    || "Expected non-empty string"
            null  || "Expected non-empty string"
            "x x" || "Expected string without whitespaces"
            "x\t" || "Expected string without whitespaces"
            "\nx" || "Expected string without whitespaces"
    }

    @Unroll
    def "checkNoBlanks(#value) throws no error"() {
        when:
            Args.checkNoBlanks(value)
        then:
            noExceptionThrown()
        where:
            value << ["a", "abc", "\\t"]
    }
}
