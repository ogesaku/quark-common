package com.coditory.quark.common.bit;

import java.util.BitSet;

final class ImmutableBitSet extends BitSet {

    public ImmutableBitSet() {
        this(new BitSet());
    }

    public ImmutableBitSet(BitSet original) {
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
