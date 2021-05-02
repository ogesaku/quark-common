package com.coditory.quark.common.encode;

import com.coditory.quark.common.collection.Maps;
import com.coditory.quark.common.encode.numeric.NumericEntityCodec;

import java.util.Map;

import static com.coditory.quark.common.encode.TranslationConstants.BASIC_DECODE;
import static com.coditory.quark.common.encode.TranslationConstants.BASIC_ENCODE;
import static com.coditory.quark.common.encode.TranslationConstants.ISO8859_1_DECODE;
import static com.coditory.quark.common.encode.TranslationConstants.ISO8859_1_ENCODE;

public final class Html4Codec {
    /**
     * Taken from:
     * http://www.w3.org/TR/REC-html40/sgml/entities.html
     */
    private static final Map<String, String> HTML40_EXTENDED_ENCODE = Maps.<String, String>builder()
            // Latin Extended-B
            .put("\u0192", "&fnof;")
            // Greek
            .put("\u0391", "&Alpha;")
            .put("\u0392", "&Beta;")
            .put("\u0393", "&Gamma;")
            .put("\u0394", "&Delta;")
            .put("\u0395", "&Epsilon;")
            .put("\u0396", "&Zeta;")
            .put("\u0397", "&Eta;")
            .put("\u0398", "&Theta;")
            .put("\u0399", "&Iota;")
            .put("\u039A", "&Kappa;")
            .put("\u039B", "&Lambda;")
            .put("\u039C", "&Mu;")
            .put("\u039D", "&Nu;")
            .put("\u039E", "&Xi;")
            .put("\u039F", "&Omicron;")
            .put("\u03A0", "&Pi;")
            .put("\u03A1", "&Rho;")
            // there is no Sigmaf, and no U+03A2 character either
            .put("\u03A3", "&Sigma;")
            .put("\u03A4", "&Tau;")
            .put("\u03A5", "&Upsilon;")
            .put("\u03A6", "&Phi;")
            .put("\u03A7", "&Chi;")
            .put("\u03A8", "&Psi;")
            .put("\u03A9", "&Omega;")
            .put("\u03B1", "&alpha;")
            .put("\u03B2", "&beta;")
            .put("\u03B3", "&gamma;")
            .put("\u03B4", "&delta;")
            .put("\u03B5", "&epsilon;")
            .put("\u03B6", "&zeta;")
            .put("\u03B7", "&eta;")
            .put("\u03B8", "&theta;")
            .put("\u03B9", "&iota;")
            .put("\u03BA", "&kappa;")
            .put("\u03BB", "&lambda;")
            .put("\u03BC", "&mu;")
            .put("\u03BD", "&nu;")
            .put("\u03BE", "&xi;")
            .put("\u03BF", "&omicron;")
            .put("\u03C0", "&pi;")
            .put("\u03C1", "&rho;")
            .put("\u03C2", "&sigmaf;")
            .put("\u03C3", "&sigma;")
            .put("\u03C4", "&tau;")
            .put("\u03C5", "&upsilon;")
            .put("\u03C6", "&phi;")
            .put("\u03C7", "&chi;")
            .put("\u03C8", "&psi;")
            .put("\u03C9", "&omega;")
            .put("\u03D1", "&thetasym;")
            .put("\u03D2", "&upsih;")
            .put("\u03D6", "&piv;")
            // General Punctuation
            .put("\u2022", "&bull;")
            // bullet is NOT the same as bullet operator, U+2219
            .put("\u2026", "&hellip;")
            .put("\u2032", "&prime;")
            .put("\u2033", "&Prime;")
            .put("\u203E", "&oline;")
            .put("\u2044", "&frasl;")
            // Letterlike Symbols
            .put("\u2118", "&weierp;")
            .put("\u2111", "&image;")
            .put("\u211C", "&real;")
            .put("\u2122", "&trade;")
            .put("\u2135", "&alefsym;")
            // alef symbol is NOT the same as hebrew letter alef,U+05D0 although the
            // same glyph could be used to depict both characters
            // Arrows
            .put("\u2190", "&larr;")
            .put("\u2191", "&uarr;")
            .put("\u2192", "&rarr;")
            .put("\u2193", "&darr;")
            .put("\u2194", "&harr;")
            .put("\u21B5", "&crarr;")
            .put("\u21D0", "&lArr;")
            // ISO 10646 does not say that lArr is the same as the 'is implied by'
            // arrow but also does not have any other character for that function.
            // So ? lArr canbe used for 'is implied by' as ISOtech suggests
            .put("\u21D1", "&uArr;")
            .put("\u21D2", "&rArr;")
            // ISO 10646 does not say this is the 'implies' character but does not
            // have another character with this function so ?rArr can be used for
            // 'implies' as ISOtech suggests
            .put("\u21D3", "&dArr;")
            .put("\u21D4", "&hArr;")
            // Mathematical Operators
            .put("\u2200", "&forall;")
            .put("\u2202", "&part;")
            .put("\u2203", "&exist;")
            .put("\u2205", "&empty;")
            .put("\u2207", "&nabla;")
            .put("\u2208", "&isin;")
            .put("\u2209", "&notin;")
            .put("\u220B", "&ni;")
            // should there be a more memorable name than 'ni'?
            .put("\u220F", "&prod;")
            // prod is NOT the same character as U+03A0 'greek capital letter pi'
            // though the same glyph might be used for both
            .put("\u2211", "&sum;")
            // sum is NOT the same character as U+03A3 'greek capital letter sigma'
            // though the same glyph might be used for both
            .put("\u2212", "&minus;")
            .put("\u2217", "&lowast;")
            .put("\u221A", "&radic;")
            .put("\u221D", "&prop;")
            .put("\u221E", "&infin;")
            .put("\u2220", "&ang;")
            .put("\u2227", "&and;")
            .put("\u2228", "&or;")
            .put("\u2229", "&cap;")
            .put("\u222A", "&cup;")
            .put("\u222B", "&int;")
            .put("\u2234", "&there4;")
            .put("\u223C", "&sim;")
            // tilde operator is NOT the same character as the tilde, U+007E,although
            // the same glyph might be used to represent both
            .put("\u2245", "&cong;")
            .put("\u2248", "&asymp;")
            .put("\u2260", "&ne;")
            .put("\u2261", "&equiv;")
            .put("\u2264", "&le;")
            .put("\u2265", "&ge;")
            .put("\u2282", "&sub;")
            .put("\u2283", "&sup;")
            // note that nsup, 'not a superset of, U+2283' is not covered by the
            // Symbol font encoding and is not included. Should it be, for symmetry?
            // It is in ISOamsn,
            .put("\u2284", "&nsub;")
            .put("\u2286", "&sube;")
            .put("\u2287", "&supe;")
            .put("\u2295", "&oplus;")
            .put("\u2297", "&otimes;")
            .put("\u22A5", "&perp;")
            .put("\u22C5", "&sdot;")
            // dot operator is NOT the same character as U+00B7 middle dot
            // Miscellaneous Technical
            .put("\u2308", "&lceil;")
            .put("\u2309", "&rceil;")
            .put("\u230A", "&lfloor;")
            .put("\u230B", "&rfloor;")
            .put("\u2329", "&lang;")
            // lang is NOT the same character as U+003C 'less than' or U+2039 'single left-pointing angle quotation
            // mark'
            .put("\u232A", "&rang;")
            // rang is NOT the same character as U+003E 'greater than' or U+203A
            // 'single right-pointing angle quotation mark'
            // Geometric Shapes
            .put("\u25CA", "&loz;")
            // Miscellaneous Symbols
            .put("\u2660", "&spades;")
            // black here seems to mean filled as opposed to hollow
            .put("\u2663", "&clubs;")
            .put("\u2665", "&hearts;")
            .put("\u2666", "&diams;")

            // Latin Extended-A
            .put("\u0152", "&OElig;")
            .put("\u0153", "&oelig;")
            // ligature is a misnomer, this is a separate character in some languages
            .put("\u0160", "&Scaron;")
            .put("\u0161", "&scaron;")
            .put("\u0178", "&Yuml;")
            // Spacing Modifier Letters
            .put("\u02C6", "&circ;")
            .put("\u02DC", "&tilde;")
            // General Punctuation
            .put("\u2002", "&ensp;")
            .put("\u2003", "&emsp;")
            .put("\u2009", "&thinsp;")
            .put("\u200C", "&zwnj;")
            .put("\u200D", "&zwj;")
            .put("\u200E", "&lrm;")
            .put("\u200F", "&rlm;")
            .put("\u2013", "&ndash;")
            .put("\u2014", "&mdash;")
            .put("\u2018", "&lsquo;")
            .put("\u2019", "&rsquo;")
            .put("\u201A", "&sbquo;")
            .put("\u201C", "&ldquo;")
            .put("\u201D", "&rdquo;")
            .put("\u201E", "&bdquo;")
            .put("\u2020", "&dagger;")
            .put("\u2021", "&Dagger;")
            .put("\u2030", "&permil;")
            .put("\u2039", "&lsaquo;")
            // lsaquo is proposed but not yet ISO standardized
            .put("\u203A", "&rsaquo;")
            // rsaquo is proposed but not yet ISO standardized
            .put("\u20AC", "&euro;")
            .build();

    static final Map<String, String> HTML40_EXTENDED_DECODE = Maps.invert(HTML40_EXTENDED_ENCODE);

    static final Translator ENCODER = Translator.builder()
            .translate(BASIC_ENCODE)
            .translate(ISO8859_1_ENCODE)
            .translate(HTML40_EXTENDED_ENCODE)
            .build();

    static final Translator DECODER = Translator.builder()
            .translate(BASIC_DECODE)
            .translate(ISO8859_1_DECODE)
            .translate(HTML40_EXTENDED_DECODE)
            .translate(NumericEntityCodec.getDecoder())
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
