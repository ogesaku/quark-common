package com.coditory.quark.common.random

import spock.lang.Specification

import static com.coditory.quark.commons.util.Doubles.fuzzyEquals as doublesEqual
import static com.coditory.quark.commons.util.Floats.fuzzyEquals as floatsEqual

class RandomValueGeneratorSpec extends Specification {
    RandomValueGenerator generator = RandomValueGenerator.randomGenerator(123)

    def "should generate random long"() {
        expect:
            generator.randomLong() == 6670104751878570570
            generator.randomLong() == 9139429171592296533
            generator.randomLong(10) == 9
            generator.randomLong(10) == 9
            generator.randomLong(10) == 3
            generator.randomLong(100, 199) == 186
            generator.randomLong(100, 199) == 188
    }

    def "should generate random int"() {
        expect:
            generator.randomInt() == 1553004782
            generator.randomInt() == 509477450
            generator.randomInt(10) == 6
            generator.randomInt(10) == 9
            generator.randomInt(100, 199) == 166
            generator.randomInt(100, 199) == 132
    }

    def "should generate random double"() {
        expect:
            doublesEqual(generator.randomDouble(), 1.3000453000374975E308)
            doublesEqual(generator.randomDouble(), 1.7813321440790855E308)
            doublesEqual(generator.randomDouble(10), 2.532931055743913)
            doublesEqual(generator.randomDouble(10), 6.088003703785168)
            doublesEqual(generator.randomDouble(100, 199), 179.78108189425745)
            doublesEqual(generator.randomDouble(100, 199), 186.6658657398903)
    }

    def "should generate random float"() {
        expect:
            floatsEqual(generator.randomFloat(), 2.460834E38)
            floatsEqual(generator.randomFloat(), 8.072991E37)
            floatsEqual(generator.randomFloat(10), 9.908989)
            floatsEqual(generator.randomFloat(10), 3.0157375)
            floatsEqual(generator.randomFloat(100, 199), 125.07602)
            floatsEqual(generator.randomFloat(100, 199), 156.83882)
    }

    def "should generate random string"() {
        expect:
            generator.randomString(7, "abcdef") == "cccfdfa"
            generator.randomString(7, "abcdef") == "bfbfacf"
            generator.randomAlphabeticString(5) == "NRcBa"
            generator.randomAlphabeticString(5) == "QKxIy"
            generator.randomNumericString(6) == "838214"
            generator.randomNumericString(6) == "120325"
            generator.randomAlphanumericString(4) == "p2ti"
            generator.randomAlphanumericString(4) == "u6LU"
            generator.randomAsciiString(5) == "8?UuA"
            generator.randomAsciiString(5) == "k!VWj"
    }
}
