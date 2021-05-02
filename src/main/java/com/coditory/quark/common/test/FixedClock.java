package com.coditory.quark.common.test;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;

import static com.coditory.quark.common.check.Args.checkNotNull;

public final class FixedClock extends Clock {
    public static final Instant DEFAULT_INSTANT = Instant.parse("2015-12-03T10:15:30.123456Z");
    public static final ZoneId DEFAULT_ZONE_ID = ZoneId.of("Europe/Warsaw");

    public static FixedClock startAt(String startingPoint) {
        Instant instant = Instant.parse(startingPoint);
        return new FixedClock(instant, DEFAULT_ZONE_ID);
    }

    public static FixedClock startAt(String startingPoint, String zoneId) {
        Instant instant = Instant.parse(startingPoint);
        return new FixedClock(instant, ZoneId.of(zoneId));
    }

    private final ZoneId zoneId;
    private Instant instant;

    public FixedClock() {
        this(DEFAULT_INSTANT, DEFAULT_ZONE_ID);
    }

    public FixedClock(Instant instant) {
        this(instant, DEFAULT_ZONE_ID);
    }

    public FixedClock(Instant instant, ZoneId zoneId) {
        this.instant = checkNotNull(instant, "instant");
        this.zoneId = checkNotNull(zoneId, "zoneId");
    }

    @Override
    public ZoneId getZone() {
        return zoneId;
    }

    @Override
    public FixedClock withZone(ZoneId zone) {
        return new FixedClock(this.instant, zone);
    }

    @Override
    public Instant instant() {
        return instant;
    }

    void setInstant(Instant instant) {
        this.instant = instant;
    }

    void plus(Duration duration) {
        this.instant = this.instant.plus(duration);
    }

    public void minus(Duration duration) {
        instant = instant.minus(duration);
    }

    void plusNano() {
        plusNanos(1);
    }

    void plusNanos(long nanos) {
        plus(Duration.ofNanos(nanos));
    }

    void plusMilli() {
        plusMillis(1);
    }

    void plusMillis(long millis) {
        plus(Duration.ofMillis(millis));
    }

    void plusSecond() {
        plusSeconds(1);
    }

    void plusSeconds(long seconds) {
        plus(Duration.ofSeconds(seconds));
    }

    void plusMinute() {
        plusMinutes(1);
    }

    void plusMinutes(long minutes) {
        plus(Duration.ofMinutes(minutes));
    }

    void plusHour() {
        plusHours(1);
    }

    void plusHours(long hours) {
        plus(Duration.ofHours(hours));
    }

    void plusDay() {
        plusDays(1);
    }

    void plusDays(long days) {
        plus(Duration.ofDays(days));
    }
}
