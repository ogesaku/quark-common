package com.coditory.quark.common.util;

import java.util.Arrays;
import java.util.BitSet;

import static com.coditory.quark.common.check.Args.checkNotNull;

public final class BitSets {
    private static final BitSet EMPTY = new UnmodifiableBitSet(new BitSet());

    private BitSets() {
        throw new UnsupportedOperationException("Do not instantiate utility class");
    }

    public static BitSet empty() {
        return EMPTY;
    }

    public static BitSet and(BitSet bitSet, BitSet... bitSets) {
        checkNotNull(bitSet, "bitSet");
        checkNotNull(bitSets, "bitSets");
        BitSet result = new BitSet();
        result.or(bitSet);
        Arrays.stream(bitSets)
                .forEach(result::and);
        return result;
    }

    public static BitSet or(BitSet... bitSets) {
        checkNotNull(bitSets, "bitSets");
        BitSet bitSet = new BitSet();
        Arrays.stream(bitSets)
                .forEach(bitSet::or);
        return bitSet;
    }

    public static BitSet toBitSet(char[] chars) {
        checkNotNull(chars, "chars");
        if (chars.length == 0) {
            return new BitSet(0);
        }
        BitSet bitSet = new BitSet(Chars.max(chars) + 1);
        for (char c : chars) {
            bitSet.set(c);
        }
        return bitSet;
    }

    public static BitSet toBitSet(int[] ints) {
        checkNotNull(ints, "ints");
        if (ints.length == 0) {
            return new BitSet(0);
        }
        BitSet bitSet = new BitSet(Integers.max(ints) + 1);
        for (int i : ints) {
            bitSet.set(i);
        }
        return bitSet;
    }

    public static BitSet toBitSet(String text) {
        checkNotNull(text, "text");
        if (text.length() == 0) {
            return new BitSet(0);
        }
        int max = text.codePoints().max().getAsInt();
        BitSet bitSet = new BitSet(max + 1);
        text.codePoints()
                .forEach(bitSet::set);
        return bitSet;
    }

    public static BitSet set(BitSet bitSet, int bitIndex) {
        return set(bitSet, bitIndex, true);
    }

    public static BitSet set(BitSet bitSet, int bitIndex, boolean value) {
        checkNotNull(bitSet, "bitSet");
        BitSet result = new BitSet(Math.max(bitSet.length(), bitIndex + 1));
        result.or(bitSet);
        result.set(bitIndex, value);
        return result;
    }

    public static BitSet unmodifiableBitSet(BitSet bitSet) {
        checkNotNull(bitSet, "bitSet");
        return new UnmodifiableBitSet(bitSet);
    }

    static final class UnmodifiableBitSet extends BitSet {

        public UnmodifiableBitSet() {
            this(new BitSet());
        }

        public UnmodifiableBitSet(BitSet original) {
            super.or(original);
        }

        @Override
        public void flip(int bitIndex) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void flip(int fromIndex, int toIndex) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void set(int bitIndex) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void set(int bitIndex, boolean value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void set(int fromIndex, int toIndex) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void set(int fromIndex, int toIndex, boolean value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void clear(int bitIndex) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void clear(int fromIndex, int toIndex) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void clear() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void and(BitSet set) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void or(BitSet set) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void xor(BitSet set) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void andNot(BitSet set) {
            throw new UnsupportedOperationException();
        }
    }
}
