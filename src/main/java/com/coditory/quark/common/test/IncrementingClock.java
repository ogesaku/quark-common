package com.coditory.quark.common.test;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;

import static com.coditory.quark.common.check.Args.checkNotNull;

public final class IncrementingClock extends Clock {
    public static final Instant DEFAULT_INSTANT = Instant.parse("2015-12-03T10:15:30.123456Z");
    public static final ZoneId DEFAULT_ZONE_ID = ZoneId.of("Europe/Warsaw");
    public static final Duration DEFAULT_STEP = Duration.ofMillis(1);

    public static IncrementingClock startAt(String startingPoint) {
        Instant instant = Instant.parse(startingPoint);
        return new IncrementingClock(instant, DEFAULT_ZONE_ID, DEFAULT_STEP);
    }

    public static IncrementingClock startAt(String startingPoint, Duration step) {
        Instant instant = Instant.parse(startingPoint);
        return new IncrementingClock(instant, DEFAULT_ZONE_ID, step);
    }

    public static IncrementingClock startAt(String startingPoint, String zoneId) {
        Instant instant = Instant.parse(startingPoint);
        return new IncrementingClock(instant, ZoneId.of(zoneId), DEFAULT_STEP);
    }

    public static IncrementingClock startAt(String startingPoint, String zoneId, Duration step) {
        Instant instant = Instant.parse(startingPoint);
        return new IncrementingClock(instant, ZoneId.of(zoneId), step);
    }

    private final Duration step;
    private final ZoneId zoneId;
    private Instant instant;

    public IncrementingClock() {
        this(DEFAULT_INSTANT, DEFAULT_ZONE_ID, DEFAULT_STEP);
    }

    public IncrementingClock(Instant instant, Duration step) {
        this(instant, DEFAULT_ZONE_ID, step);
    }

    public IncrementingClock(Instant instant, ZoneId zoneId) {
        this(instant, zoneId, DEFAULT_STEP);
    }

    public IncrementingClock(Instant instant, ZoneId zoneId, Duration step) {
        this.instant = checkNotNull(instant, "instant");
        this.zoneId = checkNotNull(zoneId, "zoneId");
        this.step = checkNotNull(step, "step");
    }

    @Override
    public ZoneId getZone() {
        return zoneId;
    }

    @Override
    public IncrementingClock withZone(ZoneId zone) {
        return new IncrementingClock(this.instant, zone, step);
    }

    @Override
    public Instant instant() {
        Instant result = instant;
        instant = instant.plus(step);
        return result;
    }
}
