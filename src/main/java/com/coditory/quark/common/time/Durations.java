package com.coditory.quark.common.time;

import java.time.Duration;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.coditory.quark.common.check.Args.checkNotNull;

public final class Durations {
    private static final Pattern PARSE_PATTERN = Pattern.compile(" *\\d+(\\.\\d+)? *(ms|s|m|h) *");

    private Durations() {
        throw new UnsupportedOperationException("Do not instantiate utility class");
    }

    public static Duration parseDuration(String value) {
        checkNotNull(value, "value");
        if (value.startsWith("-P") || value.startsWith("P")) {
            return durationParse(value);
        }
        Matcher matcher = PARSE_PATTERN.matcher(value);
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
        throw new IllegalArgumentException("Unrecognized duration format: '" + value + "'");
    }

    private static Duration durationParse(String value) {
        try {
            return Duration.parse(value);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Could not parse Duration: '" + value + "'");
        }
    }

    public static Duration parseDurationOrNull(String value) {
        try {
            return parseDuration(value);
        } catch (Exception e) {
            return null;
        }
    }

    public static Duration parseDurationOrDefault(String value, Duration defaultValue) {
        Duration result = parseDurationOrNull(value);
        return result == null ? defaultValue : result;
    }

    public static Optional<Duration> parseDurationOrEmpty(String value) {
        return Optional.ofNullable(parseDurationOrNull(value));
    }

    /**
     * Formats time to "hh:mm:ss"
     *
     * @param duration duration to be formatted
     */
    public static String formatTime(Duration duration) {
        long totalMs = duration.toMillis();
        long seconds = TimeUnit.MILLISECONDS.toSeconds(totalMs) % 60;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(totalMs) % 60;
        long hours = TimeUnit.MILLISECONDS.toHours(totalMs);
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    /**
     * Formats time to "hh:mm:ss.SSS"
     *
     * @param duration duration to be formatted
     */
    public static String formatTimeWithMillis(Duration duration) {
        long totalMs = duration.toMillis();
        long ms = totalMs % 1000;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(totalMs) % 60;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(totalMs) % 60;
        long hours = TimeUnit.MILLISECONDS.toHours(totalMs);
        return String.format("%02d:%02d:%02d.%03d", hours, minutes, seconds, ms);
    }

    /**
     * Formats time to "hh:mm:ss.SSS".
     * New time units are added when duration expands.
     *
     * @param duration duration to be formatted
     */
    public static String formatTimeWithMillisDynamically(Duration duration) {
        long totalMs = duration.toMillis();
        long ms = totalMs % 1000;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(totalMs) % 60;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(totalMs) % 60;
        long hours = TimeUnit.MILLISECONDS.toHours(totalMs);
        String result;
        if (hours > 0) {
            result = String.format("%02d:%02d:%02d.%03d", hours, minutes, seconds, ms);
        } else if (minutes > 0) {
            result = String.format("%02d:%02d.%03d", minutes, seconds, ms);
        } else if (seconds > 0) {
            result = String.format("%02d.%03d", seconds, ms);
        } else {
            result = String.format("%03d", ms);
        }
        return result;
    }

    /**
     * Formats time with up to two most significant time units.
     *
     * @param duration duration to be formatted
     */
    public static String formatToHumanReadable(Duration duration) {
        long totalMs = duration.toMillis();
        long ms = totalMs % 1000;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(totalMs) % 60;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(totalMs) % 60;
        long hours = TimeUnit.MILLISECONDS.toHours(totalMs) % 24;
        long days = TimeUnit.MILLISECONDS.toDays(totalMs);
        String result;
        if (days > 0) {
            result = String.format("%d:%02dd", days, hours);
        } else if (hours > 0) {
            result = String.format("%d:%02dh", hours, minutes);
        } else if (minutes > 0) {
            result = String.format("%d:%02dm", minutes, seconds);
        } else if (seconds > 0) {
            result = String.format("%d.%03ds", seconds, ms);
        } else {
            result = String.format("%dms", ms);
        }
        return result;
    }
}
