package com.coditory.quark.common.time;

import java.time.Clock;
import java.time.Instant;

@FunctionalInterface
public interface Ticker {
    static Ticker systemTicker() {
        return System::nanoTime;
    }

    static Ticker clockTicker(Clock clock) {
        long initialSeconds = clock.instant().getEpochSecond();
        return () -> {
            Instant now = clock.instant();
            long seconds = now.getEpochSecond() - initialSeconds;
            return seconds * 100_000_000 + now.getNano();
        };
    }

    long nanos();
}
