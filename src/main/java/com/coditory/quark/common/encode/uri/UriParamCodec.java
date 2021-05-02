package com.coditory.quark.common.encode.uri;

import com.coditory.quark.common.encode.TranslationCodec;
import com.coditory.quark.common.encode.Translator;
import com.coditory.quark.common.encode.percent.PercentCodec;

import static com.coditory.quark.common.text.Alphabets.URI_UNRESERVED;

public final class UriParamCodec {
    private static final TranslationCodec CODEC = PercentCodec.builder()
            .spaceAsPlus(true)
            .safeCharacters(URI_UNRESERVED)
            .build();

    public static TranslationCodec getInstance() {
        return CODEC;
    }

    public static Translator getEncoder() {
        return CODEC.getEncoder();
    }

    public static Translator getDecoder() {
        return CODEC.getDecoder();
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
