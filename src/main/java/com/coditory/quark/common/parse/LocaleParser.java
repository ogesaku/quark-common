package com.coditory.quark.common.parse;

import java.util.Arrays;
import java.util.Locale;

final class LocaleParser {
    static Locale parseLocale(String value) {
        Locale locale = Locale.forLanguageTag(value.replace("_", "-"));
        boolean isAvailable = Arrays.asList(Locale.getAvailableLocales())
                .contains(locale);
        if (!isAvailable) {
            throw new IllegalArgumentException("Got not available locale: " + locale);
        }
        return locale;
    }
}
