package com.coditory.quark.common.text

import spock.lang.Specification

class TokenizerSpec extends Specification {
    def "should create a tokenizer for a non empty string"() {
        when:
            Tokenizer tokenizer = new Tokenizer(" abc def  ghi\n\tjkl ")
        then:
            tokenizer.atBeginning() == true
            tokenizer.atEnd() == false
            tokenizer.position == 0
    }

    def "should create a tokenizer for empty string"() {
        when:
            Tokenizer tokenizer = new Tokenizer("")
        then:
            tokenizer.atBeginning() == true
            tokenizer.atEnd() == true
            tokenizer.position == 0
    }
}
