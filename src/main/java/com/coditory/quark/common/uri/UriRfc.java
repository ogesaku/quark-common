package com.coditory.quark.common.uri;

import com.coditory.quark.common.encode.TranslationCodec;
import com.coditory.quark.common.encode.percent.PercentCodec;
import com.coditory.quark.common.text.Alphabets;
import com.coditory.quark.common.util.Strings;

import java.util.BitSet;

import static com.coditory.quark.common.check.Args.checkNotNull;
import static com.coditory.quark.common.util.BitSets.toBitSet;
import static com.coditory.quark.common.util.Strings.removeChars;

public enum UriRfc {
    SCHEME(UriRfcCharacters.SCHEME_ALLOWED),
    SCHEME_SPECIFIC_PART(UriRfcCharacters.SCHEME_SPECIFIC_PART_ALLOWED),
    USER_INFO(UriRfcCharacters.USER_INFO_ALLOWED),
    HOST(UriRfcCharacters.HOST_IPV6_ALLOWED),
    PORT(UriRfcCharacters.PORT_ALLOWED),
    PATH_SEGMENT(UriRfcCharacters.PATH_SEGMENT_ALLOWED),
    QUERY(UriRfcCharacters.QUERY_ALLOWED, true),
    QUERY_PARAM(UriRfcCharacters.QUERY_PARAM_ALLOWED, true),
    FRAGMENT(UriRfcCharacters.FRAGMENT_ALLOWED);

    private final BitSet allowed;
    private final TranslationCodec codec;

    UriRfc(String allowed) {
        this(allowed, false);
    }

    UriRfc(String allowed, boolean decodeSpaceAsPlus) {
        this.allowed = toBitSet(allowed);
        String encode = decodeSpaceAsPlus
                ? removeChars(allowed, "+")
                : allowed;
        this.codec = PercentCodec.builder()
                .safeCharacters(encode)
                .decodeSpaceAsPlus(decodeSpaceAsPlus)
                .build();
    }

    public String validateAndDecode(String source) {
        checkValidEncoded(source);
        return decode(source);
    }

    public String decode(String source) {
        StringBuilder builder = new StringBuilder();
        decode(source, builder);
        return builder.toString();
    }

    public void decode(String source, StringBuilder builder) {
        codec.decode(source, builder);
    }

    public String encode(String source) {
        StringBuilder builder = new StringBuilder();
        encode(source, builder);
        return builder.toString();
    }

    public void encode(String source, StringBuilder builder) {
        codec.encode(source, builder);
    }

    public void checkValidEncoded(String source) {
        String error = checkValidEncodedWithErrorMessage(source);
        if (error != null) {
            throw new InvalidUriException(error);
        }
    }

    private String checkValidEncodedWithErrorMessage(String source) {
        checkNotNull(source, "source");
        int length = source.length();
        for (int i = 0; i < length; i++) {
            char ch = source.charAt(i);
            if (ch == '%') {
                if ((i + 2) < length) {
                    char hex1 = source.charAt(i + 1);
                    char hex2 = source.charAt(i + 2);
                    int u = Character.digit(hex1, 16);
                    int l = Character.digit(hex2, 16);
                    if (u == -1 || l == -1) {
                        return "Invalid encoded sequence \"" + source.substring(i) + "\"";
                    }
                    i += 2;
                } else {
                    return "Invalid encoded sequence \"" + source.substring(i) + "\"";
                }
            } else if (!allowed.get(ch)) {
                return "Invalid character '" + ch + "' for " + Strings.lowerCase(name()) + " in \"" + source + "\"";
            }
        }
        return null;
    }

    private static class UriRfcCharacters {
        static final String DELIMITER = ":/?#[]@";
        static final String SUB_DELIMITER = "!$&'()*+,;=";
        static final String RESERVED = DELIMITER + SUB_DELIMITER;
        static final String UNRESERVED = Alphabets.ALPHABETIC + Alphabets.NUMERIC + "-._~";
        static final String PCHAR = ":@" + UNRESERVED + SUB_DELIMITER;

        static final String SCHEME_ALLOWED = Alphabets.ALPHANUMERIC + "+-.";
        static final String SCHEME_SPECIFIC_PART_ALLOWED = SCHEME_ALLOWED + PCHAR + RESERVED;
        static final String USER_INFO_ALLOWED = UNRESERVED + SUB_DELIMITER + ":";
        static final String HOST_IPV6_ALLOWED = UNRESERVED + SUB_DELIMITER + "[]:";
        static final String PORT_ALLOWED = Alphabets.NUMERIC;
        static final String PATH_SEGMENT_ALLOWED = PCHAR;
        static final String QUERY_ALLOWED = PCHAR + "/?";
        static final String QUERY_PARAM_ALLOWED = removeChars(QUERY_ALLOWED, "=&");
        static final String FRAGMENT_ALLOWED = PCHAR + "/?";
    }
}
