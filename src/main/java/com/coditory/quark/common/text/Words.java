package com.coditory.quark.common.text;

import com.coditory.quark.common.bit.BitSets;

import java.util.BitSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.coditory.quark.common.check.Args.checkNotNull;
import static com.coditory.quark.common.check.Args.checkPositive;
import static java.lang.Character.isWhitespace;

public final class Words {
    private static BitSet ABBREVIATION_DELIMS = BitSets.toBitSet(" \n\t\r,.-?!");

    private Words() {
        throw new UnsupportedOperationException("Do not instantiate utility class");
    }

    public static String abbreviate(String text, int maxLength) {
        return abbreviate(text, maxLength, "...");
    }

    public static String abbreviate(String text, int maxLength, String suffix) {
        checkNotNull(text, "text");
        checkPositive(maxLength, "maxLength");
        checkNotNull(suffix, "suffix");
        if (text.length() <= maxLength) {
            return text;
        }
        int index = maxLength - suffix.length() - 1;
        if (text.length() > index + 1
                && !ABBREVIATION_DELIMS.get(text.charAt(index))
                && ABBREVIATION_DELIMS.get(text.charAt(index + 1))) {
            return text.substring(0, index + 1) + suffix;
        }
        while (index >= 0 && !ABBREVIATION_DELIMS.get(text.charAt(index))) index--;
        while (index >= 0 && ABBREVIATION_DELIMS.get(text.charAt(index))) index--;
        return index >= 0
                ? text.substring(0, index + 1) + suffix
                : text.substring(0, maxLength - suffix.length()) + suffix;
    }

    public static String breakLines(String text, int maxLength) {
        checkNotNull(text, "text");
        checkPositive(maxLength, "maxLength");
        String lineSeparator = System.lineSeparator();
        Pattern patternToWrapOn = Pattern.compile(" |\r\n|\n|\r");
        int inputLineLength = text.length();
        int offset = 0;
        StringBuilder wrappedLine = new StringBuilder(inputLineLength + 32);
        while (offset < inputLineLength) {
            int spaceToWrapAt = -1;
            int limit = Math.min((int) Math.min(Integer.MAX_VALUE, offset + maxLength + 1L), inputLineLength);
            Matcher matcher = patternToWrapOn.matcher(text.substring(offset, limit));
            if (matcher.find()) {
                if (matcher.start() == 0) {
                    offset += matcher.end();
                    continue;
                }
                spaceToWrapAt = matcher.start() + offset;
            }
            if (inputLineLength - offset <= maxLength) {
                break;
            }
            while (matcher.find()) {
                spaceToWrapAt = matcher.start() + offset;
            }
            if (spaceToWrapAt >= offset) {
                wrappedLine.append(text, offset, spaceToWrapAt);
                wrappedLine.append(lineSeparator);
                offset = spaceToWrapAt + 1;
            } else {
                wrappedLine.append(text, offset, maxLength + offset);
                wrappedLine.append(lineSeparator);
                offset += maxLength;
            }
        }
        wrappedLine.append(text, offset, text.length());
        return wrappedLine.toString();
    }

    public static String initials(String str) {
        return initials(str, null);
    }

    public static String initials(String text, char... delimiters) {
        checkNotNull(text, "text");
        if (text.isEmpty()) {
            return text;
        }
        if (delimiters != null && delimiters.length == 0) {
            return "";
        }
        int strLen = text.length();
        char[] buf = new char[strLen / 2 + 1];
        int count = 0;
        boolean lastWasGap = true;
        BitSet delim = delimiters == null
                ? BitSets.empty()
                : BitSets.toBitSet(delimiters);
        for (int i = 0; i < strLen; i++) {
            char ch = text.charAt(i);
            if (delim.get(ch)) {
                lastWasGap = true;
            } else if (lastWasGap) {
                buf[count++] = ch;
                lastWasGap = false;
            }
        }
        return new String(buf, 0, count);
    }

    public static String capitalize(String text) {
        checkNotNull(text, "text");
        return capitalize(text, '-');
    }

    public static String capitalize(String text, char... delimiters) {
        checkNotNull(text, "text");
        if (text.isEmpty()) {
            return text;
        }
        BitSet delim = delimiters == null || delimiters.length == 0
                ? BitSets.empty()
                : BitSets.toBitSet(delimiters);
        char[] buffer = text.toCharArray();
        boolean capitalizeNext = true;
        for (int i = 0; i < buffer.length; i++) {
            final char ch = buffer[i];
            if (isWhitespace(ch) || delim.get(ch)) {
                capitalizeNext = true;
            } else if (capitalizeNext) {
                buffer[i] = Character.toTitleCase(ch);
                capitalizeNext = false;
            }
        }
        return new String(buffer);
    }
}
