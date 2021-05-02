package com.coditory.quark.common.test;

import com.coditory.quark.common.time.Ticker;

import java.time.Duration;

import static com.coditory.quark.common.check.Args.checkNotNegative;

public final class FakeTicker implements Ticker {
    public static final long DEFAULT_NANOS_INITIAL = 40000000000000L;
    private long autoIncrementNanos;
    private long nanos;

    public static FakeTicker fakeTicker() {
        return new FakeTicker(DEFAULT_NANOS_INITIAL, 0);
    }

    public static FakeTicker autoIncrementingFakeTicker() {
        return autoIncrementingFakeTicker(1);
    }

    public static FakeTicker autoIncrementingFakeTicker(Duration duration) {
        return autoIncrementingFakeTicker(duration.toNanos());
    }

    public static FakeTicker autoIncrementingFakeTicker(long autoIncrementNanos) {
        return new FakeTicker(DEFAULT_NANOS_INITIAL, autoIncrementNanos);
    }

    private FakeTicker(long nanos, long autoIncrementNanos) {
        checkNotNegative(nanos, "nanos");
        checkNotNegative(autoIncrementNanos, "autoIncrementNanos");
        this.autoIncrementNanos = autoIncrementNanos;
        this.nanos = nanos;
    }

    @Override
    public long nanos() {
        nanos = nanos + autoIncrementNanos;
        return nanos;
    }

    public void autoIncrement() {
        autoIncrement(1);
    }

    public void autoIncrement(boolean autoIncrement) {
        autoIncrementNanos = autoIncrement ? 1 : 0;
    }

    public void autoIncrement(Duration duration) {
        autoIncrement(duration.toNanos());
    }

    public void autoIncrement(long nanos) {
        checkNotNegative(nanos, "nanos");
        autoIncrementNanos = nanos;
    }

    public void setNanos(long nanos) {
        checkNotNegative(nanos, "nanos");
        this.nanos = nanos;
    }

    void plus(Duration duration) {
        nanos = nanos + duration.toNanos();
    }

    public void plusNano() {
        plusNanos(1);
    }

    public void plusNanos(long nanos) {
        plus(Duration.ofNanos(nanos));
    }

    public void plusMilli() {
        plusMillis(1);
    }

    public void plusMillis(long millis) {
        plus(Duration.ofMillis(millis));
    }

    public void plusSecond() {
        plusSeconds(1);
    }

    public void plusSeconds(long seconds) {
        plus(Duration.ofSeconds(seconds));
    }

    public void plusMinute() {
        plusMinutes(1);
    }

    public void plusMinutes(long minutes) {
        plus(Duration.ofMinutes(minutes));
    }

    public void plusHour() {
        plusHours(1);
    }

    public void plusHours(long hours) {
        plus(Duration.ofHours(hours));
    }

    public void plusDay() {
        plusDays(1);
    }

    public void plusDays(long days) {
        plus(Duration.ofDays(days));
    }
}
