package com.coditory.quark.common.encode.percent;

import com.coditory.quark.common.encode.Translator;
import com.coditory.quark.common.bit.BitSets;

import java.io.CharArrayWriter;
import java.nio.charset.Charset;
import java.util.BitSet;

import static com.coditory.quark.common.bit.BitSets.toBitSet;
import static com.coditory.quark.common.check.Args.checkNotNull;
import static com.coditory.quark.common.text.Alphabets.URI_UNRESERVED;
import static java.nio.charset.StandardCharsets.UTF_8;

final class PercentEncoder implements Translator {
    private static final PercentEncoder INSTANCE = PercentEncoder.builder()
            .safeCharacters(URI_UNRESERVED)
            .build();

    public static PercentEncoder getInstance() {
        return INSTANCE;
    }

    public static PercentEncoder forCharset(Charset charset) {
        return INSTANCE.charset == charset
                ? INSTANCE
                : PercentEncoder.builder()
                .charset(charset)
                .safeCharacters(URI_UNRESERVED)
                .build();
    }

    private static final int CASE_DIFF = ('a' - 'A');
    private final boolean spaceAsPlus;
    private final Charset charset;
    private final BitSet safeCharacters;

    private PercentEncoder(BitSet safeCharacters, boolean spaceAsPlus, Charset charset) {
        this.spaceAsPlus = spaceAsPlus;
        this.charset = checkNotNull(charset, "charset");
        this.safeCharacters = checkNotNull(safeCharacters, "safeCharacters");
    }

    @Override
    public boolean translate(String text, StringBuilder dst) {
        return translate(text, dst, charset);
    }

    public boolean translate(String text, StringBuilder dst, Charset charset) {
        checkNotNull(dst, "dst");
        checkNotNull(text, "text");
        boolean needToChange = false;
        StringBuilder out = new StringBuilder(text.length());
        CharArrayWriter charArrayWriter = new CharArrayWriter();

        for (int i = 0; i < text.length(); ) {
            int cp = text.codePointAt(i);
            if (cp == ' ' && spaceAsPlus) {
                out.append('+');
                needToChange = true;
                i++;
            } else if (safeCharacters.get(cp) && (cp != '+' || !spaceAsPlus)) {
                out.appendCodePoint(cp);
                i += Character.charCount(cp);
            } else {
                do {
                    char c = text.charAt(i);
                    charArrayWriter.write(c);
                    if (c >= 0xD800 && c <= 0xDBFF) {
                        if ((i + 1) < text.length()) {
                            char d = text.charAt(i + 1);
                            if (d >= 0xDC00 && d <= 0xDFFF) {
                                charArrayWriter.write(d);
                                i++;
                            }
                        }
                    }
                    i++;
                } while (i < text.length() && !safeCharacters.get(text.codePointAt(i)));
                charArrayWriter.flush();
                byte[] bytes = new String(charArrayWriter.toCharArray())
                        .getBytes(charset);
                for (int j = 0; j < bytes.length; j++) {
                    out.append('%');
                    char c = Character.forDigit((bytes[j] >> 4) & 0xF, 16);
                    if (Character.isLetter(c)) {
                        c -= CASE_DIFF;
                    }
                    out.append(c);
                    c = Character.forDigit(bytes[j] & 0xF, 16);
                    if (Character.isLetter(c)) {
                        c -= CASE_DIFF;
                    }
                    out.append(c);
                }
                charArrayWriter.reset();
                needToChange = true;
            }
        }
        dst.append(out);
        return needToChange;
    }

    public static PercentEncoderBuilder builder() {
        return new PercentEncoderBuilder();
    }

    public static class PercentEncoderBuilder {
        private BitSet safeCharacters = toBitSet(URI_UNRESERVED);
        private boolean spaceAsPlus = false;
        private Charset charset = UTF_8;

        public PercentEncoderBuilder addSafeCharacters(String safeCharacters) {
            checkNotNull(safeCharacters, "safeCharacters");
            this.safeCharacters.or(toBitSet(safeCharacters));
            return this;
        }

        public PercentEncoderBuilder safeCharacters(BitSet safeCharacters) {
            checkNotNull(safeCharacters, "safeCharacters");
            this.safeCharacters = BitSets.copyOf(safeCharacters);
            return this;
        }

        public PercentEncoderBuilder safeCharacters(String safeCharacters) {
            checkNotNull(safeCharacters, "safeCharacters");
            this.safeCharacters = toBitSet(safeCharacters);
            return this;
        }

        public PercentEncoderBuilder spaceAsPlus(boolean spaceAsPlus) {
            this.spaceAsPlus = spaceAsPlus;
            return this;
        }

        public PercentEncoderBuilder charset(Charset charset) {
            checkNotNull(charset, "charset");
            this.charset = charset;
            return this;
        }

        public PercentEncoder build() {
            if (spaceAsPlus) {
                safeCharacters = BitSets.set(safeCharacters, '+', false);
            }
            return new PercentEncoder(safeCharacters, spaceAsPlus, charset);
        }
    }
}
