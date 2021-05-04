package com.coditory.quark.common.encode.csv;

import com.coditory.quark.common.encode.Translator;
import com.coditory.quark.common.util.Strings;

import static com.coditory.quark.common.util.Strings.containsNone;
import static com.coditory.quark.common.util.Strings.replace;

final class CsvEncoder implements Translator {
    private static final CsvEncoder CSV_INSTANCE = new CsvEncoder(',');
    private static final CsvEncoder TSV_INSTANCE = new CsvEncoder('\t');

    public static CsvEncoder forSeparator(char separator) {
        if (separator == ',') {
            return CSV_INSTANCE;
        }
        if (separator == '\t') {
            return TSV_INSTANCE;
        }
        return new CsvEncoder(separator);
    }

    public static CsvEncoder getInstance() {
        return CSV_INSTANCE;
    }

    static final char CSV_QUOTE = '"';
    static final String CSV_QUOTE_STR = "\"";
    static final String CSV_ESCAPED_QUOTE_STR = CSV_QUOTE_STR + CSV_QUOTE_STR;

    private final char[] searchChars;

    private CsvEncoder(char separator) {
        searchChars = new char[]{separator, '\r', '\n'};
    }

    @Override
    public boolean translate(String input, StringBuilder out) {
        if (containsNone(input, searchChars)) {
            out.append(input);
            return false;
        }
        out.append(CSV_QUOTE);
        out.append(Strings.replace(input, CSV_QUOTE_STR, CSV_ESCAPED_QUOTE_STR));
        out.append(CSV_QUOTE);
        return true;
    }
}
