package com.coditory.quark.common.text;

import java.util.function.Predicate;

import static com.coditory.quark.common.check.Args.check;

public interface CodePointMatcher {
    static CodePointMatcher any() {
        return CodePointMatchers.ANY;
    }

    static CodePointMatcher none() {
        return CodePointMatchers.NONE;
    }

    static boolean isWhitespace(int codePoint) {
        return whitespace().matches(codePoint);
    }

    static CodePointMatcher whitespace() {
        return CodePointMatchers.WHITESPACE;
    }

    static boolean isInvisible(int codePoint) {
        return invisible().matches(codePoint);
    }

    static CodePointMatcher invisible() {
        return CodePointMatchers.INVISIBLE;
    }

    static boolean isSingleWidth(int codePoint) {
        return singleWidth().matches(codePoint);
    }

    static CodePointMatcher singleWidth() {
        return CodePointMatchers.SINGLE_WIDTH;
    }

    static boolean isAscii(int codePoint) {
        return ascii().matches(codePoint);
    }

    static CodePointMatcher ascii() {
        return CodePointMatchers.ASCII;
    }

    static boolean isAsciiPrintable(int codePoint) {
        return asciiPrintable().matches(codePoint);
    }

    static CodePointMatcher asciiPrintable() {
        return CodePointMatchers.ASCII_PRINTABLE;
    }

    static boolean isAsciiControlCode(int codePoint) {
        return asciiControlCode().matches(codePoint);
    }

    static CodePointMatcher asciiControlCode() {
        return CodePointMatchers.ASCII_CONTROL_CODES;
    }

    static boolean isDigit(int codePoint) {
        return digit().matches(codePoint);
    }

    static CodePointMatcher digit() {
        return CodePointMatchers.DIGIT;
    }

    static boolean isLetter(int codePoint) {
        return letter().matches(codePoint);
    }

    static CodePointMatcher letter() {
        return CodePointMatchers.LETTER;
    }

    static boolean isUpperCase(int codePoint) {
        return upperCase().matches(codePoint);
    }

    static CodePointMatcher upperCase() {
        return CodePointMatchers.UPPER_CASE;
    }

    static boolean isLowerCase(int codePoint) {
        return lowerCase().matches(codePoint);
    }

    static CodePointMatcher lowerCase() {
        return CodePointMatchers.LOWER_CASE;
    }

    static boolean isLetterOrDigit(int codePoint) {
        return letterOrDigit().matches(codePoint);
    }

    static CodePointMatcher letterOrDigit() {
        return CodePointMatchers.LETTER_OR_DIGIT;
    }

    static boolean isIsoControl(int codePoint) {
        return isoControl().matches(codePoint);
    }

    static CodePointMatcher isoControl() {
        return CodePointMatchers.ISO_CONTROL;
    }

    static CodePointMatcher is(int match) {
        return CodePointMatchers.is(match);
    }

    static CodePointMatcher isNot(int match) {
        return CodePointMatchers.isNot(match);
    }

    static CodePointMatcher anyOf(CharSequence sequence) {
        return CodePointMatchers.anyOf(sequence);
    }

    static CodePointMatcher anyOf(char... chars) {
        return CodePointMatchers.anyOf(chars);
    }

    static CodePointMatcher noneOf(CharSequence sequence) {
        return CodePointMatchers.noneOf(sequence);
    }

    static CodePointMatcher inRange(int startInclusive, int endInclusive) {
        return CodePointMatchers.inRange(startInclusive, endInclusive);
    }

    static CodePointMatcher forPredicate(Predicate<Integer> predicate) {
        return CodePointMatchers.forPredicate(predicate);
    }

    boolean matches(int codePoint);

    default boolean matchesAnyOf(CharSequence sequence) {
        return !matchesNoneOf(sequence);
    }

    default boolean matchesAllOf(CharSequence sequence) {
        for (int i = sequence.length() - 1; i >= 0; i--) {
            if (!matches(sequence.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    default boolean matchesNoneOf(CharSequence sequence) {
        return indexIn(sequence) == -1;
    }

    default int indexIn(CharSequence sequence) {
        return indexIn(sequence, 0);
    }

    default int indexIn(CharSequence sequence, int start) {
        int length = sequence.length();
        check(start >= 0 && start < length, "Expected valid index");
        for (int i = start; i < length; i++) {
            if (matches(sequence.charAt(i))) {
                return i;
            }
        }
        return -1;
    }

    default int countIn(CharSequence sequence) {
        int count = 0;
        for (int i = 0; i < sequence.length(); i++) {
            if (matches(sequence.charAt(i))) {
                count++;
            }
        }
        return count;
    }

    default String removeFrom(CharSequence sequence) {
        String string = sequence.toString();
        int pos = indexIn(string);
        if (pos == -1) {
            return string;
        }

        char[] chars = string.toCharArray();
        int spread = 1;

        // This unusual loop comes from extensive benchmarking
        OUT:
        while (true) {
            pos++;
            while (true) {
                if (pos == chars.length) {
                    break OUT;
                }
                if (matches(chars[pos])) {
                    break;
                }
                chars[pos - spread] = chars[pos];
                pos++;
            }
            spread++;
        }
        return new String(chars, 0, pos - spread);
    }

    default String replaceFrom(CharSequence sequence, int replacement) {
        String string = sequence.toString();
        int pos = indexIn(string);
        if (pos == -1) {
            return string;
        }
        int[] replaced = string.codePoints()
                .map(cp -> matches(cp) ? replacement : cp)
                .toArray();
        return new String(replaced, 0, replaced.length);
    }

    default String replaceFrom(CharSequence sequence, CharSequence replacement) {
        int replacementLen = replacement.length();
        if (replacementLen == 0) {
            return removeFrom(sequence);
        }
        if (replacementLen == 1) {
            return replaceFrom(sequence, replacement.charAt(0));
        }

        String string = sequence.toString();
        int pos = indexIn(string);
        if (pos == -1) {
            return string;
        }

        int len = string.length();
        StringBuilder buf = new StringBuilder((len * 3 / 2) + 16);

        int oldpos = 0;
        do {
            buf.append(string, oldpos, pos);
            buf.append(replacement);
            oldpos = pos + 1;
            pos = indexIn(string, oldpos);
        } while (pos != -1);

        buf.append(string, oldpos, len);
        return buf.toString();
    }

    default String trimFrom(CharSequence sequence) {
        int len = sequence.length();
        int first;
        int last;

        for (first = 0; first < len; first++) {
            if (!matches(sequence.charAt(first))) {
                break;
            }
        }
        for (last = len - 1; last > first; last--) {
            if (!matches(sequence.charAt(last))) {
                break;
            }
        }

        return sequence.subSequence(first, last + 1).toString();
    }

    default String trimLeadingFrom(CharSequence sequence) {
        int len = sequence.length();
        for (int first = 0; first < len; first++) {
            if (!matches(sequence.charAt(first))) {
                return sequence.subSequence(first, len).toString();
            }
        }
        return "";
    }

    default String trimTrailingFrom(CharSequence sequence) {
        int len = sequence.length();
        for (int last = len - 1; last >= 0; last--) {
            if (!matches(sequence.charAt(last))) {
                return sequence.subSequence(0, last + 1).toString();
            }
        }
        return "";
    }

    default int lastIndexIn(CharSequence sequence) {
        for (int i = sequence.length() - 1; i >= 0; i--) {
            if (matches(sequence.charAt(i))) {
                return i;
            }
        }
        return -1;
    }

    default CodePointMatcher negate() {
        return cp -> !this.matches(cp);
    }

    default CodePointMatcher and(CodePointMatcher other) {
        return new CodePointMatchers.And(this, other);
    }

    default CodePointMatcher or(CodePointMatcher other) {
        return new CodePointMatchers.Or(this, other);
    }

    default Predicate<Integer> toPredicate() {
        return this::matches;
    }
}
