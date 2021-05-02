package com.coditory.quark.common.encode;

import static com.coditory.quark.common.check.Args.checkNotNull;

public class TranslationCodec {
    private final Translator encoder;
    private final Translator decoder;

    public TranslationCodec(Translator encoder, Translator decoder) {
        this.encoder = checkNotNull(encoder, "encoder");
        this.decoder = checkNotNull(decoder, "decoder");
    }

    public Translator getEncoder() {
        return encoder;
    }

    public Translator getDecoder() {
        return decoder;
    }

    public boolean encode(String input, StringBuilder out) {
        checkNotNull(input, "input");
        checkNotNull(out, "out");
        return encoder.translate(input, out);
    }

    public String encode(String input) {
        checkNotNull(input, "input");
        return encoder.translate(input);
    }

    public boolean decode(String input, StringBuilder out) {
        checkNotNull(input, "input");
        checkNotNull(out, "out");
        return decoder.translate(input, out);
    }

    public String decode(String input) {
        checkNotNull(input, "input");
        return decoder.translate(input);
    }
}
