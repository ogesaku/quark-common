package com.coditory.quark.common.text;

import static com.coditory.quark.common.check.Args.checkNotNull;
import static com.coditory.quark.common.util.Strings.capitalize;
import static com.coditory.quark.common.util.Strings.upperCase;

public final class CaseConverter {
    private CaseConverter() {
        throw new UnsupportedOperationException("Do not instantiate utility class");
    }

    public static String toLowerCamelCase(String text) {
        checkNotNull(text, "text");
        if (text.isEmpty()) {
            return text;
        }
        int strLen = text.length();
        StringBuilder builder = new StringBuilder();
        boolean capitalizeNext = false;
        boolean prevLowerCase = false;
        boolean empty = true;
        for (int index = 0; index < strLen; ) {
            int codePoint = text.codePointAt(index);
            int nextIndex = index + Character.charCount(codePoint);
            boolean nextLowerCase = nextIndex + 1 < strLen && Character.isLowerCase(text.codePointAt(nextIndex));
            if (!CodePointMatcher.isLetterOrDigit(codePoint)) {
                capitalizeNext = !empty;
            } else if (capitalizeNext) {
                builder.appendCodePoint(Character.toTitleCase(codePoint));
                capitalizeNext = false;
                empty = false;
            } else if (Character.isUpperCase(codePoint) && index > 0 && (prevLowerCase || nextLowerCase)) {
                builder.appendCodePoint(codePoint);
                empty = false;
            } else {
                builder.appendCodePoint(Character.toLowerCase(codePoint));
                empty = false;
            }
            prevLowerCase = Character.isLowerCase(codePoint);
            index = nextIndex;
        }
        return builder.toString();
    }

    public static String toUpperCamelCase(String text) {
        return capitalize(toLowerCamelCase(text));
    }

    public static String toLowerSnakeCase(String text) {
        checkNotNull(text, "text");
        if (text.isEmpty()) {
            return text;
        }
        int strLen = text.length();
        StringBuilder builder = new StringBuilder();
        boolean capitalizeNext = false;
        boolean prevLowerCase = false;
        boolean empty = true;
        for (int index = 0; index < strLen; ) {
            int codePoint = text.codePointAt(index);
            int nextIndex = index + Character.charCount(codePoint);
            boolean nextLowerCase = nextIndex + 1 < strLen && Character.isLowerCase(text.codePointAt(nextIndex));
            if (!CodePointMatcher.isLetterOrDigit(codePoint)) {
                capitalizeNext = !empty;
            } else if (capitalizeNext) {
                builder.append('_').appendCodePoint(Character.toLowerCase(codePoint));
                capitalizeNext = false;
                empty = false;
            } else if (Character.isUpperCase(codePoint) && index > 0 && (prevLowerCase || nextLowerCase)) {
                builder.append('_').appendCodePoint(Character.toLowerCase(codePoint));
                empty = false;
            } else {
                builder.appendCodePoint(Character.toLowerCase(codePoint));
                empty = false;
            }
            prevLowerCase = Character.isLowerCase(codePoint);
            index = nextIndex;
        }
        return builder.toString();
    }

    public static String toUpperSnakeCase(String text) {
        return upperCase(toLowerSnakeCase(text));
    }
}
