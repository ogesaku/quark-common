package com.coditory.quark.common.text

import spock.lang.Specification

import static CaseConverter.*

class CaseConverterSpec extends Specification {
    def "toLowerCamelCase(#input) == #expected"() {
        when:
            String result = toLowerCamelCase(input)
        then:
            result == expected
        where:
            input                 || expected
            "abc"                 || "abc"
            "abcDef"              || "abcDef"
            "encodeURIComponents" || "encodeUriComponents"
            "AbcDef"              || "abcDef"
            "Abc-Def"             || "abcDef"
            "ABC_DEF"             || "abcDef"
            "a123bc"              || "a123bc"
            "ab cd-ef_gh|ij"      || "abCdEfGhIj"
            "_abc"                || "abc"
            " abc  def _ ghi"     || "abcDefGhi"
    }

    def "toUpperCamelCase(#input) == #expected"() {
        when:
            String result = toUpperCamelCase(input)
        then:
            result == expected
        where:
            input                 || expected
            "abc"                 || "Abc"
            "abcDef"              || "AbcDef"
            "encodeURIComponents" || "EncodeUriComponents"
            "AbcDef"              || "AbcDef"
            "Abc-Def"             || "AbcDef"
            "ABC_DEF"             || "AbcDef"
            "a123bc"              || "A123bc"
            "ab cd-ef_gh|ij"      || "AbCdEfGhIj"
            "_abc"                || "Abc"
            " abc  def _ ghi"     || "AbcDefGhi"
    }

    def "toLowerSnakeCase(#input) == #expected"() {
        when:
            String result = toLowerSnakeCase(input)
        then:
            result == expected
        where:
            input                 || expected
            "abc"                 || "abc"
            "abcDef"              || "abc_def"
            "encodeURIComponents" || "encode_uri_components"
            "AbcDef"              || "abc_def"
            "Abc-Def"             || "abc_def"
            "ABC_DEF"             || "abc_def"
            "a123bc"              || "a123bc"
            "abc_123"             || "abc_123"
            "ab cd-ef_gh|ij"      || "ab_cd_ef_gh_ij"
            "_abc"                || "abc"
            " abc  def _ ghi"     || "abc_def_ghi"
    }

    def "toUpperSnakeCase(#input) == #expected"() {
        when:
            String result
            result = CaseConverter.toUpperSnakeCase(input)
        then:
            result == expected
        where:
            input                 || expected
            "abc"                 || "ABC"
            "abcDef"              || "ABC_DEF"
            "encodeURIComponents" || "ENCODE_URI_COMPONENTS"
            "AbcDef"              || "ABC_DEF"
            "Abc-Def"             || "ABC_DEF"
            "ABC_DEF"             || "ABC_DEF"
            "a123bc"              || "A123BC"
            "abc_123"             || "ABC_123"
            "ab cd-ef_gh|ij"      || "AB_CD_EF_GH_IJ"
            "_abc"                || "ABC"
            " abc  def _ ghi"     || "ABC_DEF_GHI"
    }
}
