package com.coditory.quark.common.text;

import com.coditory.quark.common.bit.BitSets;
import com.coditory.quark.common.util.Strings;

import java.util.Arrays;
import java.util.BitSet;
import java.util.function.Predicate;

import static com.coditory.quark.common.check.Args.*;

abstract class CodePointMatchers {
    static final CodePointMatcher ANY = new Any();
    static final CodePointMatcher NONE = new None();
    static final CodePointMatcher WHITESPACE = Character::isWhitespace;
    static final CodePointMatcher LETTER = Character::isLetter;
    static final CodePointMatcher DIGIT = Character::isDigit;
    static final CodePointMatcher LETTER_OR_DIGIT = Character::isLetterOrDigit;
    static final CodePointMatcher UPPER_CASE = Character::isUpperCase;
    static final CodePointMatcher LOWER_CASE = Character::isLowerCase;
    static final CodePointMatcher ISO_CONTROL = Character::isISOControl;
    static final CodePointMatcher ASCII = new BitSetMatcher(Alphabets.ASCII);
    static final CodePointMatcher ASCII_PRINTABLE = new BitSetMatcher(Alphabets.ASCII_PRINTABLE);
    static final CodePointMatcher ASCII_CONTROL_CODES = new BitSetMatcher(Alphabets.ASCII_CONTROL_CODES);
    static final CodePointMatcher INVISIBLE = new Invisible();
    static final CodePointMatcher SINGLE_WIDTH = new SingleWidth();

    static CodePointMatcher is(int match) {
        return new Is(match);
    }

    static CodePointMatcher isNot(int match) {
        return new IsNot(match);
    }

    static CodePointMatcher isEither(int match1, int match2) {
        return new IsEither(match1, match2);
    }

    static CodePointMatcher anyOf(CharSequence sequence) {
        String text = sequence.toString();
        CodePointMatcher matcher;
        switch (text.codePointCount(0, text.length())) {
            case 0:
                matcher = NONE;
                break;
            case 1:
                matcher = is(text.codePointAt(0));
                break;
            case 2:
                matcher = isEither(text.codePointAt(0), text.codePointAt(1));
                break;
            default:
                matcher = new BitSetMatcher(sequence);
                break;
        }
        return matcher;
    }

    static CodePointMatcher anyOf(char... chars) {
        CodePointMatcher matcher;
        switch (chars.length) {
            case 0:
                matcher = NONE;
                break;
            case 1:
                matcher = is(chars[0]);
                break;
            case 2:
                matcher = isEither(chars[0], chars[1]);
                break;
            default:
                matcher = new BitSetMatcher(BitSets.toBitSet(chars));
                break;
        }
        return matcher;
    }

    static CodePointMatcher noneOf(CharSequence sequence) {
        return anyOf(sequence).negate();
    }

    static CodePointMatcher inRange(int startInclusive, int endInclusive) {
        return new InRange(startInclusive, endInclusive);
    }

    static CodePointMatcher forPredicate(Predicate<Integer> predicate) {
        return predicate instanceof CodePointMatcher
                ? (CodePointMatcher) predicate
                : new ForPredicate(predicate);
    }

    private static final class BitSetMatcher implements CodePointMatcher {
        private final BitSet table;

        private BitSetMatcher(CharSequence text) {
            this(BitSets.toBitSet(text.toString()));
        }

        private BitSetMatcher(BitSet table) {
            if (table.length() + Long.SIZE < table.size()) {
                table = (BitSet) table.clone();
            }
            this.table = table;
        }

        @Override
        public boolean matches(int c) {
            return table.get(c);
        }
    }

    private static final class IsEither implements CodePointMatcher {
        private final int match1;
        private final int match2;

        IsEither(int match1, int match2) {
            this.match1 = match1;
            this.match2 = match2;
        }

        @Override
        public boolean matches(int c) {
            return c == match1 || c == match2;
        }
    }

    private static final class Any implements CodePointMatcher {
        @Override
        public boolean matches(int c) {
            return true;
        }

        @Override
        public int indexIn(CharSequence sequence) {
            return (sequence.length() == 0) ? -1 : 0;
        }

        @Override
        public int indexIn(CharSequence sequence, int start) {
            int length = sequence.length();
            checkPositionIndex(start, length);
            return (start == length) ? -1 : start;
        }

        @Override
        public int lastIndexIn(CharSequence sequence) {
            return sequence.length() - 1;
        }

        @Override
        public boolean matchesAllOf(CharSequence sequence) {
            checkNotNull(sequence);
            return true;
        }

        @Override
        public boolean matchesNoneOf(CharSequence sequence) {
            return sequence.length() == 0;
        }

        @Override
        public String removeFrom(CharSequence sequence) {
            checkNotNull(sequence);
            return "";
        }

        @Override
        public String replaceFrom(CharSequence sequence, int replacement) {
            String text = sequence.toString();
            int[] array = new int[text.codePointCount(0, text.length())];
            Arrays.fill(array, replacement);
            return new String(array, 0, array.length);
        }

        @Override
        public String replaceFrom(CharSequence sequence, CharSequence replacement) {
            StringBuilder result = new StringBuilder(sequence.length() * replacement.length());
            for (int i = 0; i < sequence.length(); i++) {
                result.append(replacement);
            }
            return result.toString();
        }

        @Override
        public String trimFrom(CharSequence sequence) {
            checkNotNull(sequence);
            return "";
        }

        @Override
        public int countIn(CharSequence sequence) {
            return sequence.length();
        }

        @Override
        public CodePointMatcher and(CodePointMatcher other) {
            return checkNotNull(other);
        }

        @Override
        public CodePointMatcher or(CodePointMatcher other) {
            checkNotNull(other);
            return this;
        }

        @Override
        public CodePointMatcher negate() {
            return NONE;
        }
    }

    private static final class None implements CodePointMatcher {
        @Override
        public boolean matches(int c) {
            return false;
        }

        @Override
        public int indexIn(CharSequence sequence) {
            checkNotNull(sequence);
            return -1;
        }

        @Override
        public int indexIn(CharSequence sequence, int start) {
            int length = sequence.length();
            checkPositionIndex(start, length);
            return -1;
        }

        @Override
        public int lastIndexIn(CharSequence sequence) {
            checkNotNull(sequence);
            return -1;
        }

        @Override
        public boolean matchesAllOf(CharSequence sequence) {
            return sequence.length() == 0;
        }

        @Override
        public boolean matchesNoneOf(CharSequence sequence) {
            checkNotNull(sequence);
            return true;
        }

        @Override
        public String removeFrom(CharSequence sequence) {
            return sequence.toString();
        }

        @Override
        public String replaceFrom(CharSequence sequence, int replacement) {
            return sequence.toString();
        }

        @Override
        public String replaceFrom(CharSequence sequence, CharSequence replacement) {
            checkNotNull(replacement);
            return sequence.toString();
        }

        @Override
        public String trimFrom(CharSequence sequence) {
            return sequence.toString();
        }

        @Override
        public String trimLeadingFrom(CharSequence sequence) {
            return sequence.toString();
        }

        @Override
        public String trimTrailingFrom(CharSequence sequence) {
            return sequence.toString();
        }

        @Override
        public int countIn(CharSequence sequence) {
            checkNotNull(sequence);
            return 0;
        }

        @Override
        public CodePointMatcher and(CodePointMatcher other) {
            checkNotNull(other);
            return this;
        }

        @Override
        public CodePointMatcher or(CodePointMatcher other) {
            return checkNotNull(other);
        }

        @Override
        public CodePointMatcher negate() {
            return ANY;
        }
    }

    private static class RangesMatcher implements CodePointMatcher {
        private final int[] rangeStarts;
        private final int[] rangeEnds;

        RangesMatcher(int[] rangeStarts, int[] rangeEnds) {
            this.rangeStarts = rangeStarts;
            this.rangeEnds = rangeEnds;
            check(rangeStarts.length == rangeEnds.length);
            for (int i = 0; i < rangeStarts.length; i++) {
                check(rangeStarts[i] <= rangeEnds[i]);
                if (i + 1 < rangeStarts.length) {
                    check(rangeEnds[i] < rangeStarts[i + 1]);
                }
            }
        }

        @Override
        public boolean matches(int c) {
            int index = Arrays.binarySearch(rangeStarts, c);
            if (index >= 0) {
                return true;
            } else {
                index = ~index - 1;
                return index >= 0 && c <= rangeEnds[index];
            }
        }
    }

    private static final class Invisible extends RangesMatcher {
        // Plug the following UnicodeSet pattern into
        // https://unicode.org/cldr/utility/list-unicodeset.jsp
        // [[[:Zs:][:Zl:][:Zp:][:Cc:][:Cf:][:Cs:][:Co:]]&[\u0000-\uFFFF]]
        // with the "Abbreviate" option, and get the ranges from there.
        private static final String RANGE_STARTS =
                "\u0000\u007f\u00ad\u0600\u061c\u06dd\u070f\u08e2\u1680\u180e\u2000\u2028\u205f\u2066"
                        + "\u3000\ud800\ufeff\ufff9";
        private static final String RANGE_ENDS = // inclusive ends
                "\u0020\u00a0\u00ad\u0605\u061c\u06dd\u070f\u08e2\u1680\u180e\u200f\u202f\u2064\u206f"
                        + "\u3000\uf8ff\ufeff\ufffb";

        private Invisible() {
            super(RANGE_STARTS.codePoints().toArray(), RANGE_ENDS.codePoints().toArray());
        }
    }

    private static final class SingleWidth extends RangesMatcher {
        private SingleWidth() {
            super(
                    "\u0000\u05be\u05d0\u05f3\u0600\u0750\u0e00\u1e00\u2100\ufb50\ufe70\uff61".codePoints().toArray(),
                    "\u04f9\u05be\u05ea\u05f4\u06ff\u077f\u0e7f\u20af\u213a\ufdff\ufeff\uffdc".codePoints().toArray()
            );
        }
    }

    static final class And implements CodePointMatcher {
        private final CodePointMatcher first;
        private final CodePointMatcher second;

        And(CodePointMatcher a, CodePointMatcher b) {
            first = checkNotNull(a);
            second = checkNotNull(b);
        }

        @Override
        public boolean matches(int c) {
            return first.matches(c) && second.matches(c);
        }
    }

    static final class Or implements CodePointMatcher {
        private final CodePointMatcher first;
        private final CodePointMatcher second;

        Or(CodePointMatcher a, CodePointMatcher b) {
            first = checkNotNull(a);
            second = checkNotNull(b);
        }

        @Override
        public boolean matches(int c) {
            return first.matches(c) || second.matches(c);
        }
    }

    private static final class Is implements CodePointMatcher {
        private final int codePoint;

        Is(int codePoint) {
            this.codePoint = codePoint;
        }

        @Override
        public boolean matches(int c) {
            return c == codePoint;
        }

        @Override
        public String replaceFrom(CharSequence sequence, int replacement) {
            return Strings.replace(sequence.toString(), codePoint, replacement);
        }

        @Override
        public CodePointMatcher and(CodePointMatcher other) {
            return other.matches(codePoint) ? this : new None();
        }

        @Override
        public CodePointMatcher or(CodePointMatcher other) {
            return other.matches(codePoint) ? other : new Or(this, other);
        }

        @Override
        public CodePointMatcher negate() {
            return new IsNot(codePoint);
        }
    }

    private static final class IsNot implements CodePointMatcher {
        private final int codePoint;

        IsNot(int codePoint) {
            this.codePoint = codePoint;
        }

        @Override
        public boolean matches(int codePoint) {
            return this.codePoint != codePoint;
        }

        @Override
        public CodePointMatcher and(CodePointMatcher other) {
            return other.matches(codePoint) ? new And(this, other) : other;
        }

        @Override
        public CodePointMatcher or(CodePointMatcher other) {
            return other.matches(codePoint) ? ANY : this;
        }

        @Override
        public CodePointMatcher negate() {
            return new Is(codePoint);
        }
    }

    private static final class InRange implements CodePointMatcher {
        private final int startInclusive;
        private final int endInclusive;

        InRange(int startInclusive, int endInclusive) {
            check(endInclusive >= startInclusive);
            this.startInclusive = startInclusive;
            this.endInclusive = endInclusive;
        }

        @Override
        public boolean matches(int c) {
            return startInclusive <= c && c <= endInclusive;
        }
    }

    static final class ForPredicate implements CodePointMatcher {
        private final Predicate<Integer> predicate;

        ForPredicate(Predicate<Integer> predicate) {
            this.predicate = checkNotNull(predicate);
        }

        @Override
        public boolean matches(int c) {
            return predicate.test(c);
        }
    }
}
