package com.coditory.quark.common.encode;

import com.coditory.quark.common.collection.Maps;
import com.coditory.quark.common.encode.unicode.UnicodeCodec;

import java.util.Map;

import static com.coditory.quark.common.encode.TranslationConstants.CTRL_CHARS_ENCODE;
import static com.coditory.quark.common.encode.TranslationConstants.CTRL_CHARS_DECODE;

public final class EcmaScriptCodec {
    private static final Map<String, String> TRANSLATIONS = Maps.<String, String>builder()
            .put("'", "\\'")
            .put("\"", "\\\"")
            .put("\\", "\\\\")
            .put("/", "\\/")
            .build();

    public static final Translator ENCODER = Translator.builder()
            .translate(TRANSLATIONS)
            .translate(CTRL_CHARS_ENCODE)
            .translate(UnicodeCodec.getEncoder())
            .build();

    private static final Translator DECODER = Translator.builder()
            .translate(Maps.invert(TRANSLATIONS))
            .translate(CTRL_CHARS_DECODE)
            .translate(UnicodeCodec.getDecoder())
            .build();

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
