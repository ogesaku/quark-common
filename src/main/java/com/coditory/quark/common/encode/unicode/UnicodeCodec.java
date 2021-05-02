package com.coditory.quark.common.encode.unicode;

import com.coditory.quark.common.encode.IndexedTranslator;
import com.coditory.quark.common.encode.TranslationCodec;

public final class UnicodeCodec {
    private static final IndexedTranslator ENCODER = UnicodeEncoder.getInstance();
    private static final IndexedTranslator DECODER = UnicodeDecoder.getInstance();
    private static final TranslationCodec CODEC = new TranslationCodec(ENCODER, DECODER);

    public static TranslationCodec getInstance() {
        return CODEC;
    }

    public static IndexedTranslator getEncoder() {
        return ENCODER;
    }

    public static IndexedTranslator getDecoder() {
        return DECODER;
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
