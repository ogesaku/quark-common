package com.coditory.quark.common.data;

import com.coditory.quark.common.util.Strings;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.coditory.quark.common.check.Args.check;
import static com.coditory.quark.common.check.Args.checkNotNull;
import static com.coditory.quark.common.data.DataUnit.BYTES;
import static java.lang.Math.multiplyExact;
import static java.util.Arrays.stream;

public final class DataSize implements Comparable<DataSize> {
    private static final Pattern PATTERN = Pattern.compile("^([+\\-]?\\d+)([a-zA-Z]{0,2})$");
    private static final long BYTES_PER_KB = 1024;
    private static final long BYTES_PER_MB = BYTES_PER_KB * 1024;
    private static final long BYTES_PER_GB = BYTES_PER_MB * 1024;
    private static final long BYTES_PER_TB = BYTES_PER_GB * 1024;
    private static final long BYTES_PER_PB = BYTES_PER_TB * 1024;

    private final long bytes;

    private DataSize(long bytes) {
        this.bytes = bytes;
    }

    public static DataSize ofBytes(long bytes) {
        return new DataSize(bytes);
    }

    public static DataSize ofKilobytes(long kilobytes) {
        return new DataSize(multiplyExact(kilobytes, BYTES_PER_KB));
    }

    public static DataSize ofMegabytes(long megabytes) {
        return new DataSize(multiplyExact(megabytes, BYTES_PER_MB));
    }

    public static DataSize ofGigabytes(long gigabytes) {
        return new DataSize(multiplyExact(gigabytes, BYTES_PER_GB));
    }

    public static DataSize ofTerabytes(long terabytes) {
        return new DataSize(multiplyExact(terabytes, BYTES_PER_TB));
    }

    public static DataSize ofPetabytes(long petabytes) {
        return new DataSize(multiplyExact(petabytes, BYTES_PER_PB));
    }

    public static DataSize of(long amount, DataUnit unit) {
        checkNotNull(unit, "unit");
        return new DataSize(multiplyExact(amount, unit.size().toBytes()));
    }

    public static DataSize parse(String text) {
        return parse(text, null);
    }

    public static DataSize parse(String text, @Nullable DataUnit defaultUnit) {
        checkNotNull(text, "text");
        try {
            Matcher matcher = PATTERN.matcher(text);
            check(matcher.matches(), "Does not match data size pattern");
            DataUnit unit = determineDataUnit(matcher.group(2), defaultUnit);
            long amount = Long.parseLong(matcher.group(1));
            return DataSize.of(amount, unit);
        } catch (Exception ex) {
            throw new IllegalArgumentException("'" + text + "' is not a valid data size", ex);
        }
    }

    private static DataUnit determineDataUnit(String suffix, @Nullable DataUnit defaultUnit) {
        DataUnit defaultUnitToUse = (defaultUnit != null ? defaultUnit : BYTES);
        return Strings.isNotEmpty(suffix) ? DataUnit.fromSuffix(suffix) : defaultUnitToUse;
    }

    public boolean isNegative() {
        return this.bytes < 0;
    }

    public long toBytes() {
        return this.bytes;
    }

    public long toKilobytes() {
        return this.bytes / BYTES_PER_KB;
    }

    public long toMegabytes() {
        return this.bytes / BYTES_PER_MB;
    }

    public long toGigabytes() {
        return this.bytes / BYTES_PER_GB;
    }

    public long toTerabytes() {
        return this.bytes / BYTES_PER_TB;
    }

    public long toPetabytes() {
        return this.bytes / BYTES_PER_PB;
    }

    public String format() {
        return biggestUnit()
                .format(this);
    }

    public String formatWithDecimals() {
        return biggestUnit()
                .formatWithDecimals(this);
    }

    public String formatToBytes() {
        return BYTES.format(this);
    }

    private DataUnit biggestUnit() {
        return stream(DataUnit.values())
                .filter(it -> it.size().compareTo(this) > 0)
                .findFirst()
                .orElse(BYTES);
    }

    @Override
    public int compareTo(DataSize other) {
        return Long.compare(this.bytes, other.bytes);
    }

    @Override
    public String toString() {
        return formatToBytes();
    }

    @Override
    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        DataSize otherSize = (DataSize) other;
        return (this.bytes == otherSize.bytes);
    }

    @Override
    public int hashCode() {
        return Long.hashCode(this.bytes);
    }
}

