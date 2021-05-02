package com.coditory.quark.common.encode;

import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;

public final class Base64Codec {
    static final Translator ENCODER = (input, out) -> {
        String result = Base64.getEncoder().encodeToString(input.getBytes(UTF_8));
        out.append(result);
        return true;
    };

    static final Translator DECODER = (input, out) -> {
        byte[] result = Base64.getDecoder().decode(input.getBytes(UTF_8));
        String stringResult = new String(result);
        out.append(stringResult);
        return true;
    };

    private static final TranslationCodec CODEC = new TranslationCodec(ENCODER, DECODER);

    public static TranslationCodec getInstance() {
        return CODEC;
    }

    public static Translator getEncoder() {
        return ENCODER;
    }

    public static Translator getDecoder() {
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
