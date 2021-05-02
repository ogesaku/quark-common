package com.coditory.quark.common.encode.unicode;

import com.coditory.quark.common.encode.IndexedTranslator;

import java.util.Locale;

class UnicodeEncoder extends IndexedTranslator {
    private static final UnicodeEncoder INSTANCE = builder().build();

    public static UnicodeEncoder getInstance() {
        return INSTANCE;
    }

    private static final char[] HEX_DIGITS = new char[]{
            '0', '1', '2', '3',
            '4', '5', '6', '7',
            '8', '9', 'A', 'B',
            'C', 'D', 'E', 'F'
    };
    private final int minSafeCodepoint;
    private final int maxSafeCodepoint;
    private boolean splitSurrogate = false;

    UnicodeEncoder(int minSafeCodepoint, int maxSafeCodepoint, boolean splitSurrogate) {
        this.minSafeCodepoint = minSafeCodepoint;
        this.maxSafeCodepoint = maxSafeCodepoint;
        this.splitSurrogate = splitSurrogate;
    }

    @Override
    public int translate(String input, int index, StringBuilder out) {
        final int codepoint = Character.codePointAt(input, index);
        final boolean consumed = translate(codepoint, out);
        return consumed ? 1 : 0;
    }

    private boolean translate(int codepoint, StringBuilder out) {
        if (codepoint >= minSafeCodepoint && codepoint <= maxSafeCodepoint) {
            return false;
        }
        if (codepoint > 0xffff) {
            out.append(toUtf16Escape(codepoint));
        } else {
            out.append("\\u");
            out.append(HEX_DIGITS[(codepoint >> 12) & 15]);
            out.append(HEX_DIGITS[(codepoint >> 8) & 15]);
            out.append(HEX_DIGITS[(codepoint >> 4) & 15]);
            out.append(HEX_DIGITS[(codepoint) & 15]);
        }
        return true;
    }

    private String toUtf16Escape(int codepoint) {
        if (!splitSurrogate) {
            return "\\u" + hex(codepoint);
        }
        char[] surrogatePair = Character.toChars(codepoint);
        return "\\u" + hex(surrogatePair[0]) + "\\u" + hex(surrogatePair[1]);
    }

    private String hex(int codepoint) {
        return Integer.toHexString(codepoint)
                .toUpperCase(Locale.ROOT);
    }

    public static UnicodeEscaperBuilder builder() {
        return new UnicodeEscaperBuilder();
    }

    public static class UnicodeEscaperBuilder {
        private int minSafeCodepoint = 32;
        private int maxSafeCodepoint = 0x7f;
        private boolean splitSurrogate = true;

        public UnicodeEscaperBuilder minSafeCodepoint(int minSafeCodepoint) {
            this.minSafeCodepoint = minSafeCodepoint;
            return this;
        }

        public UnicodeEscaperBuilder maxSafeCodepoint(int maxSafeCodepoint) {
            this.maxSafeCodepoint = maxSafeCodepoint;
            return this;
        }

        public UnicodeEscaperBuilder splitSurrogate(boolean splitSurrogate) {
            this.splitSurrogate = splitSurrogate;
            return this;
        }

        public UnicodeEncoder build() {
            return new UnicodeEncoder(minSafeCodepoint, maxSafeCodepoint, splitSurrogate);
        }
    }
}
