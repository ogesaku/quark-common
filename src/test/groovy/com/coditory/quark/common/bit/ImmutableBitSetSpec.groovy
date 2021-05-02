package com.coditory.quark.common.bit

import spock.lang.Specification

class ImmutableBitSetSpec extends Specification {
    def "should create immutable bitSet"() {
        given:
            BitSet bitSet = new ImmutableBitSet()
        expect:
            throwsException { bitSet.flip(0) }
            throwsException { bitSet.flip(0, 1) }
            throwsException { bitSet.set(0) }
            throwsException { bitSet.set(0, true) }
            throwsException { bitSet.set(0, 1) }
            throwsException { bitSet.set(0, 1, true) }
            throwsException { bitSet.clear() }
            throwsException { bitSet.clear(0) }
            throwsException { bitSet.clear(0, 1) }
            throwsException { bitSet.and(bitSet) }
            throwsException { bitSet.or(bitSet) }
            throwsException { bitSet.xor(bitSet) }
            throwsException { bitSet.andNot(bitSet) }
    }

    boolean throwsException(Runnable runnable) {
        try {
            runnable.run()
            return false
        } catch (UnsupportedOperationException e) {
            return true
        }
    }
}
