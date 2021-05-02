package com.coditory.quark.common.bit

import spock.lang.Specification

class BitSetsSpec extends Specification {
    def "toBitSet(String)"() {
        given:
            String input = "ab𝄞"
        when:
            BitSet result = BitSets.toBitSet(input)
        then:
            result.get(input.codePointAt(0))
            result.get(input.codePointAt(1))
            result.get(input.codePointAt(2))
        and:
            !result.get("A".codePointAt(0))
            !result.get("X".codePointAt(0))
    }

    def "toBitSet(char[])"() {
        given:
            char[] input = "abc".toCharArray()
        when:
            BitSet result = BitSets.toBitSet(input)
        then:
            result.get(input[0] as int)
            result.get(input[1] as int)
            result.get(input[2] as int)
        and:
            !result.get("A".codePointAt(0))
            !result.get("X".codePointAt(0))
    }


    def "toBitSet(int[])"() {
        given:
            int[] input = [5, 8, 10]
        when:
            BitSet result = BitSets.toBitSet(input)
        then:
            result.get(5)
            result.get(8)
            result.get(10)
        and:
            !result.get(4)
            !result.get(100)
    }

    def "set(bitSet, index)"() {
        given:
            BitSet bitSet = new BitSet()
        when:
            BitSet bitSetWithOne = BitSets.set(bitSet, 1)
        then:
            bitSetWithOne.get(1)
            !bitSet.get(1)

        when:
            BitSet bitSetWithoutOne = BitSets.set(bitSetWithOne, 1, false)
        then:
            !bitSetWithoutOne.get(1)
            bitSetWithOne.get(1)
    }

    def "copyOf(bitSet) - should create immutable copy"() {
        given:
            BitSet bitSet = new BitSet()
            bitSet.set(1)
        when:
            BitSet immutable = BitSets.copyOf(bitSet)
        then:
            immutable.get(1)

        when:
            bitSet.set(2)
        then:
            !immutable.get(2)

        when:
            immutable.set(2)
        then:
            thrown(UnsupportedOperationException)
    }

    def "and(bitSets)"() {
        given:
            BitSet a = BitSets.toBitSet([1, 3] as int[])
            BitSet b = BitSets.toBitSet([3, 5] as int[])
        when:
            BitSet result = BitSets.and(a, b)
        then:
            result.get(3)
        and:
            !result.get(1)
            !result.get(5)
    }

    def "or(bitSets)"() {
        given:
            BitSet a = BitSets.toBitSet([1, 3] as int[])
            BitSet b = BitSets.toBitSet([3, 5] as int[])
        when:
            BitSet result = BitSets.or(a, b)
        then:
            result.get(1)
            result.get(3)
            result.get(5)
    }
}
