package com.coditory.quark.common.bit;

import java.util.Arrays;
import java.util.BitSet;

import static com.coditory.quark.common.check.Args.checkNotNull;

public final class BitSets {
    private static final BitSet EMPTY = new ImmutableBitSet(new BitSet());

    private BitSets() {
        throw new UnsupportedOperationException("Do not instantiate utility class");
    }

    public static BitSet empty() {
        return EMPTY;
    }

    public static BitSet copyOf(BitSet bitSet) {
        checkNotNull(bitSet, "bitSet");
        return new ImmutableBitSet(bitSet);
    }

    public static BitSet mutableCopyOf(BitSet bitSet) {
        checkNotNull(bitSet, "bitSet");
        BitSet result = new BitSet();
        result.or(bitSet);
        return result;
    }

    public static BitSet and(BitSet bitSet, BitSet... bitSets) {
        checkNotNull(bitSet, "bitSet");
        checkNotNull(bitSets, "bitSets");
        BitSet result = new BitSet();
        result.or(bitSet);
        Arrays.stream(bitSets)
                .forEach(result::and);
        return copyOf(result);
    }

    public static BitSet or(BitSet... bitSets) {
        checkNotNull(bitSets, "bitSets");
        BitSet bitSet = new BitSet();
        Arrays.stream(bitSets)
                .forEach(bitSet::or);
        return copyOf(bitSet);
    }

    public static BitSet toBitSet(char[] chars) {
        checkNotNull(chars, "chars");
        BitSet bitSet = new BitSet();
        for (char c : chars) {
            bitSet.set(c);
        }
        return copyOf(bitSet);
    }

    public static BitSet toBitSet(int[] ints) {
        checkNotNull(ints, "ints");
        BitSet bitSet = new BitSet();
        for (int i : ints) {
            bitSet.set(i);
        }
        return copyOf(bitSet);
    }

    public static BitSet toBitSet(String text) {
        checkNotNull(text, "text");
        BitSet bitSet = new BitSet();
        text.codePoints()
                .forEach(bitSet::set);
        return copyOf(bitSet);
    }

    public static BitSet set(BitSet bitSet, int bitIndex) {
        return set(bitSet, bitIndex, true);
    }

    public static BitSet set(BitSet bitSet, int bitIndex, boolean value) {
        checkNotNull(bitSet, "bitSet");
        BitSet result = mutableCopyOf(bitSet);
        result.set(bitIndex, value);
        return copyOf(result);
    }
}
