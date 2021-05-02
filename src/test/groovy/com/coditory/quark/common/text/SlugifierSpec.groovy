package com.coditory.quark.common.text

import spock.lang.Specification
import spock.lang.Unroll

class SlugifierSpec extends Specification {
    Slugifier slugifier = Slugifier.getInstance()

    @Unroll
    def "should not change: #text"() {
        when:
            String result = slugifier.slugify(text)
        then:
            result == text
        where:
            text << [
                    "",
                    Alphabets.ALPHANUMERIC,
                    "A-B-C",
                    "a-b-c"
            ]
    }

    @Unroll
    def "should slugify whitespaces and interpunction: #input"() {
        when:
            String result = slugifier.slugify(input)
        then:
            result == output
        where:
            input                  || output
            "   , \t\n"            || ""
            "a--b_c  d e"          || "a-b-c-d-e"
            "a's b\"c  d\"\"e.f's" || "a-s-b-c-d-e-f-s"
            "a--b__c"              || "a-b-c"
            "--ab_ "               || "ab"
            "a \t\nb \t\n"         || "a-b"
    }

    @Unroll
    def "should slugify: #input -> #output"() {
        when:
            String result = slugifier.slugify(input)
        then:
            result == output
        where:
            input                                                                   || output
            "ĄąĆćĘęŁłŃńÓóŚśŹźŻż"                                                    || "AaCcEeLlNnOoSsZzZz"
            "ÁČĎÉĚÍŇÓŘŠŤÚŮÝŽáčďéěíňóřšťúůýž"                                        || "ACDEEINORSTUUYZacdeeinorstuuyz"
            "ÁÄČĎžÉÍĹĽŇÓÔŔŠŤÚÝŽáäčďžéíĺľňóôŕšťúýž"                                  || "AAeCDzEILLNOORSTUYZaaecdzeillnoorstuyz"
            "ĈĉĜĝĤĥĴĵŜŝŬŭ"                                                          || "CcGgHhJjSsUu"
            "a語b"                                                                   || "ab"
            "語"                                                                     || ""
            "ÄäÖöÜüẞß"                                                              || "AeaeOeoeUeueSsss"
            "åÅæÆøØ"                                                                || "aaAaaeAeoeOe"
            "ĞğİıŞş"                                                                || "GgIiSs"
            "АБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдежзийклмнопрстуфхцчшщъыьэюя"      || "ABVGDEZhZIJKLMNOPRSTUFHTsChShShch-Y-EYuYaabvgdezhzijklmnoprstufhtschshshch-y-eyuya"
            "ΑΒΓΔΕΖΗΘΙΚΛΜΝΞΟΠΡΣΤΥΦΧΨΩαβγδεζηθικλμνξοπρςστυφχψωΆΈΉΊΌΎΏάέήόίύώΪΫϊϋΰΐ" || "ABGDEZHTHIKLMNKSOPRSTYFXPSWabgdezhthiklmnksoprsstyfxpswAEHIOYWaehoiywIYiuui"
            "أبتثجحخدذرزسشصضطظعغفقكلمنهوي"                                          || "abtthghkhdthrzsshsdtthaaghfkklmnhoy"
    }

    def "should slugify text with custom configuration"() {
        given:
            Slugifier slugifier = Slugifier.builder()
                    .lowerCase()
                    .underscoreSeparator()
                    .replacement("XXX", "X")
                    .build()
        when:
            String result = slugifier.slugify(text)
        then:
            result == expected
        where:
            text                   || expected
            "ABCdef"               || "abcdef"
            "abcXXXdef"            || "abcxdef"
            "abcXXXXXdef"          || "abcxxxdef"
            "__ab- "               || "ab"
            "a's b\"c  d\"\"e.f's" || "a_s_b_c_d_e_f_s"
            "a--b__c"              || "a_b_c"
    }

    @Unroll
    def "Slugifier.slug(#value) == #expected"() {
        expect:
            Slugifier.slug(value) == expected
        where:
            value            || expected
            "aą bć 012-,+=X" || "aa-bc-012-X"
    }
}
