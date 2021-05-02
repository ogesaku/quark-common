package com.coditory.quark.common.encode;

import com.coditory.quark.common.collection.Maps;
import com.coditory.quark.common.encode.unicode.UnicodeCodec;

import static com.coditory.quark.common.encode.TranslationConstants.CTRL_CHARS_DECODE;
import static com.coditory.quark.common.encode.TranslationConstants.CTRL_CHARS_ENCODE;

public final class JavaCodec {
    static final Translator ENCODER = Translator.builder()
            .translate(Maps.<String, String>builder()
                    .put("\"", "\\\"")
                    .put("\\", "\\\\")
                    .build()
            )
            .translate(CTRL_CHARS_ENCODE)
            .translate(UnicodeCodec.getEncoder())
            .build();

    static final Translator DECODER = Translator.builder()
            .translate(new OctalDecoder())
            .translate(UnicodeCodec.getDecoder())
            .translate(CTRL_CHARS_DECODE)
            .translate(Maps.<String, String>builder()
                    .put("\\\\", "\\")
                    .put("\\\"", "\"")
                    .put("\\'", "'")
                    .put("\\", "")
                    .build()
            )
            .build();

    private static class OctalDecoder extends IndexedTranslator {
        @Override
        protected int translate(String input, int index, StringBuilder out) {
            final int remaining = input.length() - index - 1;
            final StringBuilder builder = new StringBuilder();
            if (input.charAt(index) == '\\' && remaining > 0 && isOctalDigit(input.charAt(index + 1))) {
                int next = index + 1;
                int next2 = index + 2;
                int next3 = index + 3;
                builder.append(input.charAt(next));
                if (remaining > 1 && isOctalDigit(input.charAt(next2))) {
                    builder.append(input.charAt(next2));
                    if (remaining > 2 && isZeroToThree(input.charAt(next)) && isOctalDigit(input.charAt(next3))) {
                        builder.append(input.charAt(next3));
                    }
                }
                out.append((char) Integer.parseInt(builder.toString(), 8));
                return 1 + builder.length();
            }
            return 0;
        }

        private boolean isOctalDigit(final char ch) {
            return ch >= '0' && ch <= '7';
        }

        private boolean isZeroToThree(final char ch) {
            return ch >= '0' && ch <= '3';
        }
    }

    private static final TranslationCodec CODEC = new TranslationCodec(ENCODER, DECODER);

    public static TranslationCodec getInstance() {
        return CODEC;
    }

    public static String encode(String input) {
        return CODEC.encode(input);
    }

    public static boolean encode(String input, StringBuilder out) {
        return CODEC.encode(input, out);
    }

    public static String decode(String input) {
        return CODEC.decode(input);
    }

    public static boolean decode(String input, StringBuilder out) {
        return CODEC.decode(input, out);
    }
}
