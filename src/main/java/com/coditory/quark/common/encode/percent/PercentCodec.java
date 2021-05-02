package com.coditory.quark.common.encode.percent;

import com.coditory.quark.common.bit.BitSets;
import com.coditory.quark.common.encode.TranslationCodec;
import com.coditory.quark.common.encode.Translator;

import java.nio.charset.Charset;
import java.util.BitSet;

import static com.coditory.quark.common.check.Args.checkNotNull;
import static com.coditory.quark.common.text.Alphabets.URI_UNRESERVED;
import static com.coditory.quark.common.bit.BitSets.toBitSet;
import static java.nio.charset.StandardCharsets.UTF_8;

public final class PercentCodec {
    private static final PercentEncoder ENCODER = PercentEncoder.getInstance();
    private static final PercentDecoder DECODER = PercentDecoder.getInstance();
    private static final TranslationCodec CODEC = new TranslationCodec(ENCODER, DECODER);

    public static TranslationCodec forCharset(Charset charset) {
        PercentEncoder encoder = PercentEncoder.forCharset(charset);
        PercentDecoder decoder = PercentDecoder.forCharset(charset);
        return encoder == ENCODER && decoder == DECODER
                ? CODEC
                : new TranslationCodec(encoder, decoder);
    }

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

    public static boolean encode(String input, StringBuilder out, Charset charset) {
        return ENCODER.translate(input, out, charset);
    }

    public static String decode(String input) {
        return CODEC.decode(input);
    }

    public static boolean decode(String input, StringBuilder out) {
        return CODEC.decode(input, out);
    }

    public static boolean decode(String input, StringBuilder out, Charset charset) {
        return DECODER.translate(input, out, charset);
    }

    public static PercentCodecBuilder builder() {
        return new PercentCodecBuilder();
    }

    public static class PercentCodecBuilder {
        private BitSet safeCharacters = toBitSet(URI_UNRESERVED);
        private boolean decodeSpaceAsPlus = false;
        private boolean encodeSpaceAsPlus = false;
        private Charset charset = UTF_8;

        public PercentCodecBuilder addSafeCharacters(String safeCharacters) {
            checkNotNull(safeCharacters, "safeCharacters");
            this.safeCharacters.or(toBitSet(safeCharacters));
            return this;
        }

        public PercentCodecBuilder safeCharacters(BitSet safeCharacters) {
            checkNotNull(safeCharacters, "safeCharacters");
            this.safeCharacters = safeCharacters;
            return this;
        }

        public PercentCodecBuilder safeCharacters(String safeCharacters) {
            checkNotNull(safeCharacters, "safeCharacters");
            this.safeCharacters = toBitSet(safeCharacters);
            return this;
        }

        public PercentCodecBuilder decodeSpaceAsPlus(boolean spaceAsPlus) {
            this.decodeSpaceAsPlus = spaceAsPlus;
            return this;
        }

        public PercentCodecBuilder encodeSpaceAsPlus(boolean spaceAsPlus) {
            this.encodeSpaceAsPlus = spaceAsPlus;
            return this;
        }

        public PercentCodecBuilder spaceAsPlus(boolean spaceAsPlus) {
            this.decodeSpaceAsPlus = spaceAsPlus;
            this.encodeSpaceAsPlus = spaceAsPlus;
            return this;
        }

        public PercentCodecBuilder charset(Charset charset) {
            checkNotNull(charset, "charset");
            this.charset = charset;
            return this;
        }

        public TranslationCodec build() {
            if (encodeSpaceAsPlus || decodeSpaceAsPlus) {
                safeCharacters = BitSets.set(safeCharacters, '+', false);
            }
            PercentEncoder encoder = PercentEncoder.builder()
                    .safeCharacters(safeCharacters)
                    .spaceAsPlus(encodeSpaceAsPlus)
                    .charset(charset)
                    .build();
            PercentDecoder decoder = PercentDecoder.builder()
                    .spaceAsPlus(decodeSpaceAsPlus)
                    .charset(charset)
                    .build();
            return new TranslationCodec(encoder, decoder);
        }
    }
}
