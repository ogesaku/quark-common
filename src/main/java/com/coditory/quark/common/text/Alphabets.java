package com.coditory.quark.common.text;

import static com.coditory.quark.common.check.Args.check;
import static com.coditory.quark.common.check.Args.checkNotNegative;

public final class Alphabets {
    private Alphabets() {
        throw new UnsupportedOperationException("Do not instantiate utility class");
    }

    public static final String ALPHABETIC_UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String ALPHABETIC_LOWER = "abcdefghijklmnopqrstuvwxyz";
    public static final String ALPHABETIC = ALPHABETIC_UPPER + ALPHABETIC_LOWER;
    public static final String NUMERIC = "0123456789";
    public static final String ALPHANUMERIC = ALPHABETIC + NUMERIC;
    public static final String ASCII = asciiAlphabetWithControlCodes(0, 127);
    public static final String ASCII_PRINTABLE = asciiAlphabet(32, 127);
    public static final String ASCII_CONTROL_CODES = asciiAlphabetWithControlCodes(0, 32) + '\u007F';

    public static final String CYRYLLIC_CHARACTERS = "АБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдежзийклмнопрстуфхцчшщъыьэюя";
    public static final String GREEK_CHARACTERS = "ΑΒΓΔΕΖΗΘΙΚΛΜΝΞΟΠΡΣΤΥΦΧΨΩαβγδεζηθικλμνξοπρςστυφχψωΆΈΉΊΌΎΏάέήόίύώΪΫϊϋΰΐ";
    public static final String POLISH_CHARACTERS = "ĄąĆćĘęŁłŃńÓóŚśŹźŻż";
    public static final String CZECH_CHARACTERS = "ÁČĎÉĚÍŇÓŘŠŤÚŮÝŽáčďéěíňóřšťúůýž";
    public static final String SLOVAK_CHARACTERS = "ÁÄČĎžÉÍĹĽŇÓÔŔŠŤÚÝŽáäčďžéíĺľňóôŕšťúýž";
    public static final String ESPERANTO_CHARACTERS = "ĈĉĜĝĤĥĴĵŜŝŬŭ";
    public static final String GERMAN_CHARACTERS = "ÄäÖöÜüẞß";
    public static final String NORDIC_CHARACTERS = "åÅæÆøØÄäÖö";
    public static final String TURKISH_CHARACTERS = "ğĞçCıIiİöÖŞşüÜ";

    /**
     * Unreserved uri characters
     * https://tools.ietf.org/html/rfc3986#section-2.3
     */
    public static final String URI_UNRESERVED = ALPHANUMERIC + "-._~";
    /**
     * @link https://tools.ietf.org/html/rfc3986#section-3.5
     */
    public static final String URI_SEGMENT_UNRESERVED = URI_UNRESERVED + "!$&'()*+,;=:@";
    /**
     * @link https://tools.ietf.org/html/rfc3986#section-3.5
     */
    public static final String URI_FRAGMENT_UNRESERVED = URI_SEGMENT_UNRESERVED + "+/?";

    public static String asciiAlphabet(int asciiFromInclusive, int asciiToExclusive) {
        checkNotNegative(asciiFromInclusive, "asciiFromInclusive");
        checkNotNegative(asciiToExclusive, "asciiToInclusive");
        check(asciiFromInclusive < asciiToExclusive, "Expected: asciiFromInclusive < asciiToExclusive");
        check(asciiFromInclusive >= 32, "Expected: asciiFromInclusive >= 32");
        check(asciiToExclusive <= 177, "Expected: asciiToExclusive <= 176");
        return asciiAlphabetWithControlCodes(asciiFromInclusive, asciiToExclusive);
    }

    public static String asciiAlphabetWithControlCodes(int asciiFromInclusive, int asciiToExclusive) {
        checkNotNegative(asciiFromInclusive, "asciiFromInclusive");
        checkNotNegative(asciiToExclusive, "asciiToInclusive");
        check(asciiFromInclusive < asciiToExclusive, "Expected: asciiFromInclusive < asciiToExclusive");
        char[] chars = new char[asciiToExclusive - asciiFromInclusive];
        for (int i = asciiFromInclusive; i < asciiToExclusive; ++i) {
            chars[i - asciiFromInclusive] = (char) i;
        }
        return String.valueOf(chars);
    }
}
