package com.coditory.quark.common.text

import spock.lang.Specification
import spock.lang.Unroll

class TokenizerSpec extends Specification {
    def "should create a tokenizer for a non empty string"() {
        when:
            Tokenizer tokenizer = new Tokenizer(" abc def  ghi\n\tjkl ")
        then:
            tokenizer.atBeginning() == true
            tokenizer.atEnd() == false
            tokenizer.position == 0
            tokenizer.hasNextToken() == true
            tokenizer.hasNextChar() == true
            tokenizer.tokens() == ["abc", "def", "ghi", "jkl"]
    }

    def "should create a tokenizer for empty string"() {
        when:
            Tokenizer tokenizer = new Tokenizer("")
        then:
            tokenizer.atBeginning() == true
            tokenizer.atEnd() == true
            tokenizer.hasNextToken() == false
            tokenizer.hasNextChar() == false
            tokenizer.tokens() == []
    }

    def "should create a tokenizer for a blank string"() {
        when:
            Tokenizer tokenizer = new Tokenizer(" \n\t ")
        then:
            tokenizer.atBeginning() == true
            tokenizer.atEnd() == false
            tokenizer.hasNextToken() == false
            tokenizer.hasNextChar() == true
            tokenizer.tokens() == []
    }

    def "should iterate over characters"() {
        given:
            Tokenizer tokenizer = new Tokenizer(" ab\n\tc ")
        when:
            List<Character> result = new ArrayList<>()
            while (tokenizer.hasNextChar()) {
                result.add(tokenizer.nextChar())
            }
        then:
            result == [' ', 'a', 'b', '\n', '\t', 'c', ' '] as List<Character>
    }

    @Unroll
    def "should iterate over tokens: #text"() {
        given:
            Tokenizer tokenizer = new Tokenizer(text)
        when:
            List<String> result = new ArrayList<>()
            while (tokenizer.hasNextToken()) {
                result.add(tokenizer.nextToken())
            }
        then:
            result == expected

        when:
            result == tokenizer.tokens()
        then:
            result == expected

        where:
            text             || expected
            " ab cd "        || ["ab", "cd"]
            " ab\n\tcd "     || ["ab", "cd"]
            "ab ' '"         || ["ab", " "]
            "ab \" \""       || ["ab", " "]
            "ab 'c d'"       || ["ab", "c d"]
            "ab \"c d\""     || ["ab", "c d"]
            "ab'c d'"        || ["ab", "c d"]
            "ab\"c d\""      || ["ab", "c d"]
            "ab'c \\' d'"    || ["ab", "c ' d"]
            "ab\"c \\\" d\"" || ["ab", "c \" d"]
    }
}
