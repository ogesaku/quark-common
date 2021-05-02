package com.coditory.quark.common.encode.numeric;

import com.coditory.quark.common.encode.IndexedTranslator;
import com.coditory.quark.common.util.Range;

final class NumericEntityEncoder extends IndexedTranslator {
    private static final NumericEntityEncoder INSTANCE = new NumericEntityEncoder();

    static NumericEntityEncoder getInstance() {
        return INSTANCE;
    }

    public static NumericEntityEncoder below(final int codepoint) {
        return outsideOf(codepoint, Integer.MAX_VALUE);
    }

    public static NumericEntityEncoder above(final int codepoint) {
        return outsideOf(0, codepoint);
    }

    public static NumericEntityEncoder between(final int codepointLow, final int codepointHigh) {
        return new NumericEntityEncoder(codepointLow, codepointHigh, true);
    }

    public static NumericEntityEncoder outsideOf(final int codepointLow, final int codepointHigh) {
        return new NumericEntityEncoder(codepointLow, codepointHigh, false);
    }

    private final boolean between;
    private final Range<Integer> range;

    private NumericEntityEncoder(final int below, final int above, final boolean between) {
        this.range = Range.ofInclusive(below, above);
        this.between = between;
    }

    private NumericEntityEncoder() {
        this(0, Integer.MAX_VALUE, true);
    }

    @Override
    public int translate(String input, int index, StringBuilder out) {
        int codepoint = Character.codePointAt(input, index);
        if (this.between != this.range.contains(codepoint)) {
            return 0;
        }
        out.append("&#");
        out.append(Integer.toString(codepoint, 10));
        out.append(';');
        return 1;
    }
}
