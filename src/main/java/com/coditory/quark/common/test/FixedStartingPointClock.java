package com.coditory.quark.common.test;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;

import static com.coditory.quark.common.check.Args.checkNotNull;

public final class FixedStartingPointClock extends Clock {
    public static final Instant DEFAULT_INSTANT = Instant.parse("2015-12-03T10:15:30.123456Z");
    public static final ZoneId DEFAULT_ZONE_ID = ZoneId.of("Europe/Warsaw");

    public static FixedStartingPointClock startAt(String startingPoint) {
        Instant instant = Instant.parse(startingPoint);
        return new FixedStartingPointClock(instant, DEFAULT_ZONE_ID);
    }

    public static FixedStartingPointClock startAt(String startingPoint, String zoneId) {
        Instant instant = Instant.parse(startingPoint);
        return new FixedStartingPointClock(instant, ZoneId.of(zoneId));
    }

    private final ZoneId zoneId;
    private final Instant realStartingPoint;
    private final Instant startingPoint;

    public FixedStartingPointClock() {
        this(DEFAULT_INSTANT, DEFAULT_ZONE_ID);
    }

    public FixedStartingPointClock(Instant startingPoint) {
        this(startingPoint, DEFAULT_ZONE_ID);
    }

    public FixedStartingPointClock(Instant startingPoint, ZoneId zoneId) {
        this(startingPoint, systemUTC().instant(), zoneId);
    }

    private FixedStartingPointClock(Instant startingPoint, Instant realStartingPoint, ZoneId zoneId) {
        this.startingPoint = checkNotNull(startingPoint, "startingPoint");
        this.realStartingPoint = checkNotNull(realStartingPoint, "realStartingPoint");
        this.zoneId = checkNotNull(zoneId, "zoneId");
    }

    @Override
    public ZoneId getZone() {
        return zoneId;
    }

    @Override
    public FixedStartingPointClock withZone(ZoneId zone) {
        return new FixedStartingPointClock(startingPoint, realStartingPoint, zone);
    }

    @Override
    public Instant instant() {
        Instant now = systemUTC().instant();
        Duration difference = Duration.between(realStartingPoint, now);
        return startingPoint.plus(difference);
    }
}
