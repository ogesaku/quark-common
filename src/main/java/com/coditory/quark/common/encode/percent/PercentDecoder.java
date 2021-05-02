package com.coditory.quark.common.encode.percent;

import com.coditory.quark.common.encode.Translator;

import java.nio.charset.Charset;

import static com.coditory.quark.common.check.Args.checkNotNull;
import static java.nio.charset.StandardCharsets.UTF_8;

final class PercentDecoder implements Translator {
    private static final PercentDecoder INSTANCE = new PercentDecoder(false, UTF_8);

    public static PercentDecoder getInstance() {
        return INSTANCE;
    }

    public static PercentDecoder forCharset(Charset charset) {
        return INSTANCE.charset == charset
                ? INSTANCE
                : new PercentDecoder(false, charset);
    }

    private final Charset charset;
    private final boolean spaceAsPlus;

    private PercentDecoder(boolean spaceAsPlus, Charset charset) {
        this.spaceAsPlus = spaceAsPlus;
        this.charset = checkNotNull(charset, "charset");
    }

    @Override
    public boolean translate(String text, StringBuilder dst) {
        return translate(text, dst, charset);
    }

    public boolean translate(String text, StringBuilder dst, Charset charset) {
        checkNotNull(dst, "dst");
        checkNotNull(text, "text");
        boolean needToChange = false;
        int length = text.length();
        StringBuilder sb = new StringBuilder(length > 500 ? length / 2 : length);
        int i = 0;
        char c;
        byte[] bytes = null;
        while (i < length) {
            c = text.charAt(i);
            switch (c) {
                case '+':
                    if (!spaceAsPlus) {
                        sb.append('+');
                    } else {
                        sb.append(' ');
                        needToChange = true;
                    }
                    i++;
                    break;
                case '%':
                    try {
                        if (bytes == null) {
                            bytes = new byte[(length - i) / 3];
                        }
                        int pos = 0;
                        while ((i + 2) < length && c == '%') {
                            int v = Integer.parseInt(text, i + 1, i + 3, 16);
                            if (v < 0) {
                                throw new IllegalArgumentException("Illegal hex characters in escape (%) pattern - negative value");
                            }
                            bytes[pos++] = (byte) v;
                            i += 3;
                            if (i < length) {
                                c = text.charAt(i);
                            }
                        }
                        if (i < length && c == '%') {
                            throw new IllegalArgumentException("Incomplete trailing escape (%) pattern");
                        }
                        sb.append(new String(bytes, 0, pos, charset));
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("Illegal hex characters in escape (%) pattern - " + e.getMessage());
                    }
                    needToChange = true;
                    break;
                default:
                    sb.append(c);
                    i++;
                    break;
            }
        }
        dst.append(sb);
        return needToChange;
    }

    public static PercentDecoderBuilder builder() {
        return new PercentDecoderBuilder();
    }

    public static class PercentDecoderBuilder {
        private boolean spaceAsPlus = false;
        private Charset charset = UTF_8;

        public PercentDecoderBuilder spaceAsPlus(boolean spaceAsPlus) {
            this.spaceAsPlus = spaceAsPlus;
            return this;
        }

        public PercentDecoderBuilder charset(Charset charset) {
            checkNotNull(charset, "charset");
            this.charset = charset;
            return this;
        }

        public PercentDecoder build() {
            return new PercentDecoder(spaceAsPlus, charset);
        }
    }
}
