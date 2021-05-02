package com.coditory.quark.common.util.string

import spock.lang.Specification
import spock.lang.Unroll

import static com.coditory.quark.common.util.Strings.*

class StringsAppendSpec extends Specification {
    @Unroll
    def "appendIfMissing(#value, #suffix) == #expected"() {
        expect:
            appendIfMissing(value, suffix) == expected
        where:
            value       | suffix || expected
            ""          | "abc"  || "abc"
            "file.txt"  | ".txt" || "file.txt"
            "file.tXt"  | ".txt" || "file.tXt.txt"
            "file.txtx" | ".txt" || "file.txtx.txt"
    }

    @Unroll
    def "appendIfMissingIgnoreCase(#value, #suffix) == #expected"() {
        expect:
            appendIfMissingIgnoreCase(value, suffix) == expected
        where:
            value       | suffix || expected
            ""          | "abc"  || "abc"
            "file.txt"  | ".txt" || "file.txt"
            "file.tXt"  | ".txt" || "file.tXt"
            "file.txtx" | ".txt" || "file.txtx.txt"
    }

    @Unroll
    def "prependIfMissing(#value, #prefix) == #expected"() {
        expect:
            prependIfMissing(value, prefix) == expected
        where:
            value      | prefix || expected
            ""         | "abc"  || "abc"
            "file.txt" | "abc." || "abc.file.txt"
            "aBc.txt"  | "abc." || "abc.aBc.txt"
            "aac.txt"  | "abc." || "abc.aac.txt"
    }

    @Unroll
    def "prependIfMissingIgnoreCase(#value, #prefix) == #expected"() {
        expect:
            prependIfMissingIgnoreCase(value, prefix) == expected
        where:
            value      | prefix || expected
            ""         | "abc"  || "abc"
            "file.txt" | "abc." || "abc.file.txt"
            "aBc.txt"  | "abc." || "aBc.txt"
            "aac.txt"  | "abc." || "abc.aac.txt"
    }

}
