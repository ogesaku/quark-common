package com.coditory.quark.common.encode.unicode;

import com.coditory.quark.common.encode.IndexedTranslator;

final class UnicodeDecoder extends IndexedTranslator {
    private static final UnicodeDecoder INSTANCE = new UnicodeDecoder();

    public static UnicodeDecoder getInstance() {
        return INSTANCE;
    }

    @Override
    public int translate(String input, int index, StringBuilder out) {
        if (input.charAt(index) == '\\' && index + 1 < input.length() && input.charAt(index + 1) == 'u') {
            int i = 2;
            while (index + i < input.length() && input.charAt(index + i) == 'u') {
                i++;
            }
            if (index + i < input.length() && input.charAt(index + i) == '+') {
                i++;
            }
            if (index + i + 4 <= input.length()) {
                final CharSequence unicode = input.subSequence(index + i, index + i + 4);
                try {
                    int value = Integer.parseInt(unicode.toString(), 16);
                    out.append((char) value);
                } catch (final NumberFormatException nfe) {
                    throw new IllegalArgumentException("Unable to parse unicode value: " + unicode, nfe);
                }
                return i + 4;
            }
            throw new IllegalArgumentException("Less than 4 hex digits in unicode value: '"
                    + input.subSequence(index, input.length())
                    + "' due to end of CharSequence");
        }
        return 0;
    }
}
