package com.coditory.quark.common.parse;

import java.time.Duration;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class DurationParser {
    private static final Pattern pattern = Pattern.compile("\\d+(\\.\\d+)? *(ms|s|m|h)");

    static Duration parseDuration(String value) {
        if (value.startsWith("-P") || value.startsWith("P")) {
            return durationParse(value);
        }
        Matcher matcher = pattern.matcher(value);
        if (matcher.matches()) {
            String unit = matcher.group(2).toUpperCase();
            String number = value.substring(0, value.length() - unit.length()).trim();
            float durationNumber = Float.parseFloat(number);
            if (unit.equals("MS")) {
                unit = "S";
                durationNumber = durationNumber / 1000;
            }
            return durationNumber % 1 == 0
                    ? durationParse("PT" + (int) durationNumber + unit)
                    : durationParse("PT" + durationNumber + unit);
        }
        throw new IllegalArgumentException("Could not parse Duration: " + value);
    }

    private static Duration durationParse(String value) {
        try {
            return Duration.parse(value);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Could not parse Duration: " + value);
        }
    }
}
