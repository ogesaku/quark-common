package com.coditory.quark.common.data

import spock.lang.Specification

class DataSizeSpec extends Specification {
    def "should parse data size: #input"() {
        when:
            DataSize dataSize = DataSize.parse(input)
        then:
            dataSize == expected
        where:
            input    || expected
            "1B"     || DataSize.ofBytes(1)
            "100B"   || DataSize.ofBytes(100)
            "10000B" || DataSize.ofBytes(10000)
    }
}
