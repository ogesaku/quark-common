package com.coditory.quark.common.time;

import java.time.ZoneId;

public final class ZoneIds {
    private ZoneIds() {
        throw new UnsupportedOperationException("Do not instantiate utility class");
    }

    public static final ZoneId GMT = ZoneId.of("GMT");
}
