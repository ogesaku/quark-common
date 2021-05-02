package com.coditory.quark.common.encode.csv;

import com.coditory.quark.common.encode.Translator;
import com.coditory.quark.common.util.Strings;

import static com.coditory.quark.common.encode.csv.CsvEncoder.CSV_ESCAPED_QUOTE_STR;
import static com.coditory.quark.common.encode.csv.CsvEncoder.CSV_QUOTE;
import static com.coditory.quark.common.encode.csv.CsvEncoder.CSV_QUOTE_STR;
import static com.coditory.quark.common.util.Strings.replace;

final class CsvDecoder implements Translator {
    private static final CsvDecoder CSV_INSTANCE = new CsvDecoder(',');
    private static final CsvDecoder TSV_INSTANCE = new CsvDecoder('\t');

    public static CsvDecoder forSeparator(char separator) {
        if (separator == ',') {
            return CSV_INSTANCE;
        }
        if (separator == '\t') {
            return TSV_INSTANCE;
        }
        return new CsvDecoder(separator);
    }

    public static CsvDecoder getInstance() {
        return CSV_INSTANCE;
    }

    private final char[] searchChars;

    private CsvDecoder(char separator) {
        searchChars = new char[]{separator, '\r', '\n'};
    }

    @Override
    public boolean translate(String input, StringBuilder out) {
        if (input.isEmpty()) {
            return false;
        }
        if (input.charAt(0) != CSV_QUOTE || input.charAt(input.length() - 1) != CSV_QUOTE) {
            out.append(input);
            return false;
        }
        if (input.length() == 1) {
            return false;
        }
        String quoteless = input.subSequence(1, input.length() - 1).toString();
        if (Strings.containsAny(quoteless, searchChars)) {
            out.append(replace(quoteless, CSV_ESCAPED_QUOTE_STR, CSV_QUOTE_STR));
        } else {
            out.append(quoteless);
        }
        return true;
    }
}
