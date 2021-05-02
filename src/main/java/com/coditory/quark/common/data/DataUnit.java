package com.coditory.quark.common.data;

import java.text.DecimalFormat;

public enum DataUnit {
    BYTES("B", DataSize.ofBytes(1)),
    KILOBYTES("KB", DataSize.ofKilobytes(1)),
    MEGABYTES("MB", DataSize.ofMegabytes(1)),
    GIGABYTES("GB", DataSize.ofGigabytes(1)),
    TERABYTES("TB", DataSize.ofTerabytes(1)),
    PETABYTES("PB", DataSize.ofPetabytes(1));

    private static final DecimalFormat FORMAT_WITH_DECIMALS = new DecimalFormat("###.##");

    private final String suffix;
    private final DataSize size;

    DataUnit(String suffix, DataSize size) {
        this.suffix = suffix;
        this.size = size;
    }

    DataSize size() {
        return this.size;
    }

    public String format(DataSize dataSize) {
        long amount = dataSize.toBytes() / size().toBytes();
        return String.format("%f" + suffix, amount);
    }

    public String formatWithDecimals(DataSize dataSize) {
        double amount = dataSize.toBytes() * 1d / size().toBytes();
        return FORMAT_WITH_DECIMALS.format(amount) + suffix;
    }

    public static DataUnit fromSuffix(String suffix) {
        for (DataUnit candidate : values()) {
            if (candidate.suffix.equals(suffix)) {
                return candidate;
            }
        }
        throw new IllegalArgumentException("Unknown data unit: '" + suffix + "'");
    }

}
