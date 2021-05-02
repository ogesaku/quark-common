package com.coditory.quark.common.util

import spock.lang.Specification
import spock.lang.Unroll

class RangeSpec extends Specification {
    @Unroll
    def "#a overlaps #b == #result"() {
        expect:
            a.overlaps(b) == result
        where:
            a                          | b                       || result
            Range.ofInclusive(0, 1)    | Range.ofInclusive(2, 6) || false
            Range.ofInclusive(0, 2)    | Range.ofInclusive(2, 6) || true
            Range.ofMaxExclusive(0, 2) | Range.ofInclusive(2, 6) || false
            Range.ofInclusive(0, 3)    | Range.ofInclusive(2, 6) || true
            Range.ofInclusive(2, 3)    | Range.ofInclusive(2, 6) || true
            Range.ofInclusive(3, 5)    | Range.ofInclusive(2, 6) || true
            Range.ofInclusive(2, 6)    | Range.ofInclusive(2, 6) || true
            Range.ofInclusive(4, 6)    | Range.ofInclusive(2, 6) || true
            Range.ofInclusive(4, 7)    | Range.ofInclusive(2, 6) || true
            Range.ofInclusive(6, 8)    | Range.ofInclusive(2, 6) || true
            Range.ofMinExclusive(6, 8) | Range.ofInclusive(2, 6) || false
            Range.ofInclusive(7, 8)    | Range.ofInclusive(2, 6) || false
    }

    @Unroll
    def "#a isAdjacent #b == #result"() {
        expect:
            a.isAdjacent(b) == result
        where:
            a                          | b                          || result
            Range.ofMaxExclusive(0, 2) | Range.ofInclusive(2, 6)    || true
            Range.ofInclusive(0, 2)    | Range.ofMinExclusive(2, 6) || true
            Range.ofInclusive(0, 2)    | Range.ofExclusive(2, 6)    || true
            Range.ofExclusive(0, 2)    | Range.ofInclusive(2, 6)    || true
            Range.ofInclusive(0, 1)    | Range.ofInclusive(2, 6)    || false
            Range.ofInclusive(0, 2)    | Range.ofInclusive(2, 6)    || false
            Range.ofInclusive(0, 3)    | Range.ofInclusive(2, 6)    || false
            Range.ofInclusive(2, 3)    | Range.ofInclusive(2, 6)    || false
            Range.ofInclusive(3, 5)    | Range.ofInclusive(2, 6)    || false
            Range.ofInclusive(2, 6)    | Range.ofInclusive(2, 6)    || false
            Range.ofInclusive(4, 6)    | Range.ofInclusive(2, 6)    || false
            Range.ofInclusive(4, 7)    | Range.ofInclusive(2, 6)    || false
            Range.ofInclusive(6, 8)    | Range.ofInclusive(2, 6)    || false
            Range.ofInclusive(7, 8)    | Range.ofInclusive(2, 6)    || false
    }

    @Unroll
    def "#a intersection #b == #result"() {
        expect:
            a.intersection(b) == result
        where:
            a                       | b                       || result
            Range.ofInclusive(0, 2) | Range.ofInclusive(2, 6) || Range.of(2)
            Range.ofInclusive(0, 3) | Range.ofInclusive(2, 6) || Range.ofInclusive(2, 3)
            Range.ofInclusive(2, 3) | Range.ofInclusive(2, 6) || Range.ofInclusive(2, 3)
            Range.ofInclusive(3, 5) | Range.ofInclusive(2, 6) || Range.ofInclusive(3, 5)
            Range.ofInclusive(2, 6) | Range.ofInclusive(2, 6) || Range.ofInclusive(2, 6)
            Range.ofInclusive(4, 6) | Range.ofInclusive(2, 6) || Range.ofInclusive(4, 6)
            Range.ofInclusive(4, 7) | Range.ofInclusive(2, 6) || Range.ofInclusive(4, 6)
            Range.ofInclusive(6, 8) | Range.ofInclusive(2, 6) || Range.of(6)
    }

    @Unroll
    def "#a intersection #b throws error"() {
        when:
            a.intersection(b)
        then:
            thrown(IllegalArgumentException)
        where:
            a                          | b
            Range.ofInclusive(0, 1)    | Range.ofInclusive(2, 6)
            Range.ofMaxExclusive(0, 2) | Range.ofInclusive(2, 6)
            Range.ofMinExclusive(6, 8) | Range.ofInclusive(2, 6)
            Range.ofInclusive(7, 8)    | Range.ofInclusive(2, 6)
    }

    @Unroll
    def "#a sum #b == #result"() {
        expect:
            a.sum(b) == result
        where:
            a                       | b                       || result
            Range.ofInclusive(0, 2) | Range.ofInclusive(2, 6) || Range.ofInclusive(0, 6)
            Range.ofExclusive(0, 2) | Range.ofInclusive(2, 6) || Range.ofMinExclusive(0, 6)
            Range.ofInclusive(0, 2) | Range.ofExclusive(2, 6) || Range.ofMaxExclusive(0, 6)
            Range.ofInclusive(0, 3) | Range.ofInclusive(2, 6) || Range.ofInclusive(0, 6)
            Range.ofInclusive(2, 3) | Range.ofInclusive(2, 6) || Range.ofInclusive(2, 6)
            Range.ofInclusive(2, 6) | Range.ofInclusive(2, 6) || Range.ofInclusive(2, 6)
            Range.ofInclusive(4, 6) | Range.ofInclusive(2, 6) || Range.ofInclusive(2, 6)
            Range.ofInclusive(4, 7) | Range.ofInclusive(2, 6) || Range.ofInclusive(2, 7)
            Range.ofInclusive(6, 8) | Range.ofInclusive(2, 6) || Range.ofInclusive(2, 8)
    }

    @Unroll
    def "#a sum #b throws error"() {
        when:
            a.sum(b)
        then:
            thrown(IllegalArgumentException)
        where:
            a                       | b
            Range.ofInclusive(0, 1) | Range.ofInclusive(2, 6)
            Range.ofInclusive(7, 8) | Range.ofInclusive(2, 6)
    }

    @Unroll
    def "#a minus #b == #result"() {
        expect:
            a.minus(b) == result
        where:
            a                       | b                       || result
            Range.ofInclusive(2, 6) | Range.ofExclusive(0, 1) || Range.ofInclusive(2, 6)
            Range.ofExclusive(2, 6) | Range.ofInclusive(1, 3) || Range.ofExclusive(3, 6)
            Range.ofInclusive(2, 6) | Range.ofInclusive(2, 3) || Range.ofMinExclusive(3, 6)
            Range.ofExclusive(2, 6) | Range.ofInclusive(2, 3) || Range.ofExclusive(3, 6)
            Range.ofExclusive(2, 6) | Range.ofExclusive(2, 3) || Range.ofMaxExclusive(3, 6)
            Range.ofExclusive(2, 6) | Range.ofExclusive(0, 2) || Range.ofExclusive(2, 6)
            Range.ofInclusive(2, 6) | Range.ofInclusive(0, 2) || Range.ofMinExclusive(2, 6)
            Range.ofExclusive(2, 6) | Range.ofExclusive(6, 8) || Range.ofExclusive(2, 6)
            Range.ofInclusive(2, 6) | Range.ofInclusive(6, 8) || Range.ofMaxExclusive(2, 6)
    }

    @Unroll
    def "#a minus #b throws error"() {
        when:
            a.minus(b)
        then:
            thrown(IllegalArgumentException)
        where:
            a                       | b
            Range.ofInclusive(2, 6) | Range.ofExclusive(2, 3)
            Range.ofInclusive(2, 6) | Range.ofInclusive(3, 5)
            Range.ofInclusive(2, 6) | Range.ofInclusive(2, 6)
            Range.ofInclusive(2, 6) | Range.ofInclusive(1, 8)
    }

    @Unroll
    def "#a contains #b == #result"() {
        expect:
            a.contains(b) == result
        where:
            a                       | b                       || result
            Range.ofInclusive(2, 6) | Range.ofExclusive(0, 1) || false
            Range.ofExclusive(2, 6) | Range.ofInclusive(1, 3) || false
            Range.ofInclusive(2, 6) | Range.ofInclusive(2, 3) || true
            Range.ofExclusive(2, 6) | Range.ofInclusive(2, 3) || false
            Range.ofExclusive(2, 6) | Range.ofExclusive(2, 3) || true
            Range.ofInclusive(2, 6) | Range.ofExclusive(2, 3) || true
            Range.ofInclusive(2, 6) | Range.ofInclusive(3, 5) || true
    }

    @Unroll
    def "#a containsElement #b == #result"() {
        expect:
            a.contains(b) == result
        where:
            a                       | b || result
            Range.ofInclusive(2, 6) | 0 || false
            Range.ofExclusive(2, 6) | 2 || false
            Range.ofInclusive(2, 6) | 2 || true
            Range.ofInclusive(2, 6) | 4 || true
            Range.ofInclusive(2, 6) | 6 || true
            Range.ofExclusive(2, 6) | 6 || false
            Range.ofInclusive(2, 6) | 7 || false
    }

    @Unroll
    def "should compare with max value"() {
        expect:
            Range.ofInclusive(2, 6).isMaxAfter(6) == false
            Range.ofMaxExclusive(2, 6).isMaxAfter(6) == false
            Range.ofInclusive(2, 6).isMaxAfterOrEqual(6) == true
            Range.ofMaxExclusive(2, 6).isMaxAfterOrEqual(6) == false
            Range.ofMaxExclusive(2, 6).isMaxAfter(5) == true
            Range.ofInclusive(2, 6).isMaxAfterOrEqual(5) == true
            Range.ofMaxExclusive(2, 6).isMaxAfter(7) == false
            Range.ofInclusive(2, 6).isMaxAfterOrEqual(7) == false
        and:
            Range.ofInclusive(2, 6).isMaxBefore(6) == false
            Range.ofMaxExclusive(2, 6).isMaxBefore(6) == true
            Range.ofInclusive(2, 6).isMaxBeforeOrEqual(6) == true
            Range.ofMaxExclusive(2, 6).isMaxBeforeOrEqual(6) == false
            Range.ofMaxExclusive(2, 6).isMaxBefore(5) == false
            Range.ofInclusive(2, 6).isMaxBeforeOrEqual(5) == false
            Range.ofMaxExclusive(2, 6).isMaxBefore(7) == true
            Range.ofInclusive(2, 6).isMaxBeforeOrEqual(7) == true
    }

    @Unroll
    def "should compare with min value"() {
        expect:
            Range.ofInclusive(2, 6).isMinAfter(2) == false
            Range.ofMinExclusive(2, 6).isMinAfter(2) == true
            Range.ofInclusive(2, 6).isMinAfterOrEqual(2) == true
            Range.ofMinExclusive(2, 6).isMinAfterOrEqual(2) == false
            Range.ofMinExclusive(2, 6).isMinAfter(1) == true
            Range.ofInclusive(2, 6).isMinAfterOrEqual(1) == true
            Range.ofMinExclusive(2, 6).isMinAfter(3) == false
            Range.ofInclusive(2, 6).isMinAfterOrEqual(3) == false
        and:
            Range.ofInclusive(2, 6).isMinBefore(2) == false
            Range.ofMinExclusive(2, 6).isMinBefore(2) == false
            Range.ofInclusive(2, 6).isMinBeforeOrEqual(2) == true
            Range.ofMinExclusive(2, 6).isMinBeforeOrEqual(2) == false
            Range.ofMinExclusive(2, 6).isMinBefore(1) == false
            Range.ofInclusive(2, 6).isMinBeforeOrEqual(1) == false
            Range.ofMinExclusive(2, 6).isMinBefore(3) == true
            Range.ofInclusive(2, 6).isMinBeforeOrEqual(3) == true
    }

    @Unroll
    def "should compare with single value range"() {
        given:
            Range range = Range.of(2)
        expect:
            range.isMinAfter(2) == false
            range.isMinAfterOrEqual(2) == true
            range.isMinAfter(1) == true
            range.isMinAfterOrEqual(1) == true
            range.isMinAfter(3) == false
            range.isMinAfterOrEqual(3) == false
        and:
            range.isMinBefore(2) == false
            range.isMinBeforeOrEqual(2) == true
            range.isMinBefore(1) == false
            range.isMinBeforeOrEqual(1) == false
            range.isMinBefore(3) == true
            range.isMinBeforeOrEqual(3) == true
    }

    def "should keep equals hashcode contract"() {
        given:
            Range rangeA = Range.ofInclusive(2, 5)
            Range rangeB = Range.ofInclusive(2, 5)
            Range rangeC = Range.ofInclusive(3, 5)
        expect:
            rangeA == rangeB
            rangeB == rangeA
            rangeA != rangeC
            rangeC != rangeA
        and:
            rangeA.hashCode() == rangeB.hashCode()
            rangeC.hashCode() != rangeA.hashCode()
    }

    def "should expose values via getters"() {
        given:
            Range range = Range.ofMaxExclusive(2, 5)
        expect:
            range.getMin() == 2
            range.getMax() == 5
            range.isMinInclusive() == true
            range.isMaxInclusive() == false
    }

    def "should build range"() {
        given:
            Range range = Range.builder()
                    .maxExclusive(2)
                    .minInclusive(1)
                    .build()
        expect:
            range == Range.ofMaxExclusive(1, 2)
    }
}
