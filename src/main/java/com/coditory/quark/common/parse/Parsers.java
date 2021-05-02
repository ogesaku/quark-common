package com.coditory.quark.common.parse;

import com.coditory.quark.common.data.DataSize;
import com.coditory.quark.common.util.Booleans;
import com.coditory.quark.common.util.Bytes;
import com.coditory.quark.common.util.Doubles;
import com.coditory.quark.common.util.Floats;
import com.coditory.quark.common.util.Integers;
import com.coditory.quark.common.util.Longs;
import com.coditory.quark.common.util.Range;
import com.coditory.quark.common.util.Shorts;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Currency;
import java.util.Locale;
import java.util.Map;

import static java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME;
import static java.util.Map.entry;

public final class Parsers {
    public Parsers() {
        throw new UnsupportedOperationException("Do not instantiate utility class");
    }

    private static final Map<Class<?>, Parser<?>> DEFAULT_PARSERS = Map.ofEntries(
            entry(String.class, (x) -> x),
            entry(Boolean.class, Booleans::parseBoolean),
            entry(Byte.class, Bytes::parseByte),
            entry(Double.class, Doubles::parseDouble),
            entry(Float.class, Floats::parseFloat),
            entry(Integer.class, Integers::parseInteger),
            entry(Long.class, Longs::parseLong),
            entry(Short.class, Shorts::parseShort),
            entry(BigDecimal.class, unifyException(BigDecimal::new, BigDecimal.class)),
            entry(ZonedDateTime.class, unifyException(value -> ZonedDateTime.parse(value, ISO_OFFSET_DATE_TIME), ZonedDateTime.class)),
            entry(Instant.class, unifyException(Instant::parse, Instant.class)),
            entry(Duration.class, DurationParser::parseDuration),
            entry(Locale.class, LocaleParser::parseLocale),
            entry(Currency.class, unifyException(Currency::getInstance, Currency.class)),
            entry(DataSize.class, DataSize::parse),
            entry(Range.class, Range::parse)
    );

    public static <T> T parse(String value, Class<T> type) {
        return getParser(type)
                .parse(value);
    }

    @Nullable
    public static <T> T parseOrNull(String value, Class<T> type) {
        return getParser(type)
                .parseOrNull(value);
    }

    @Nullable
    public static <T> T parseOrDefault(String value, T defaultValue, Class<T> type) {
        return getParser(type)
                .parseOrDefault(value, defaultValue);
    }

    @SuppressWarnings("unchecked")
    private static <T> Parser<T> getParser(Class<T> type) {
        Parser<?> parser = DEFAULT_PARSERS.get(type);
        if (parser == null) {
            throw new IllegalArgumentException("No parser registered for type: " + type.getName());
        }
        return (Parser<T>) parser;
    }

    static <T> Parser<T> unifyException(Parser<T> parser, Class<T> type) {
        return (value) -> {
            try {
                return parser.parse(value);
            } catch (Exception e) {
                throw new IllegalArgumentException("Could not parse " + type.getSimpleName() + " value: " + value, e);
            }
        };
    }

    private interface Parser<T> {
        T parse(String value);

        @Nullable
        default T parseOrNull(String value) {
            return parseOrDefault(value, null);
        }

        @Nullable
        default T parseOrDefault(String value, @Nullable T defaultValue) {
            try {
                return parse(value);
            } catch (Exception e) {
                return defaultValue;
            }
        }
    }
}
