package com.coditory.quark.common.encode;

import com.coditory.quark.common.collection.Maps;
import com.coditory.quark.common.encode.unicode.UnicodeCodec;

import static com.coditory.quark.common.encode.TranslationConstants.CTRL_CHARS_ENCODE;

public final class JsonCodec {
    public static final Translator ENCODER = Translator.builder()
            .translate(Maps.<String, String>builder()
                    .put("\"", "\\\"")
                    .put("\\", "\\\\")
                    .put("/", "\\/")
                    .build()
            )
            .translate(CTRL_CHARS_ENCODE)
            .translate(UnicodeCodec.getEncoder())
            .build();

    private static final Translator DECODER = JavaCodec.DECODER;

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
