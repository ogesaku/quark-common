package com.coditory.quark.common.time;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;

import static com.coditory.quark.common.check.Args.checkNotNull;

public final class Instants {
    private Instants() {
        throw new UnsupportedOperationException("Do not instantiate utility class");
    }

    public static Instant min(Instant a, Instant b) {
        checkNotNull(a);
        checkNotNull(b);
        return b.isBefore(a) ? b : a;
    }

    public static Instant max(Instant a, Instant b) {
        checkNotNull(a);
        checkNotNull(b);
        return b.isAfter(a) ? b : a;
    }

    public static boolean isAfterOrEqual(Instant a, Instant b) {
        checkNotNull(a);
        checkNotNull(b);
        return a.equals(b) || a.isAfter(b);
    }

    public static boolean isBeforeOrEqual(Instant a, Instant b) {
        checkNotNull(a);
        checkNotNull(b);
        return a.equals(b) || a.isBefore(b);
    }

    public static Instant future(Clock clock, Duration duration) {
        checkNotNull(clock, "clock");
        checkNotNull(duration, "duration");
        return clock.instant().plus(duration);
    }

    public static Instant past(Clock clock, Duration duration) {
        checkNotNull(clock, "clock");
        checkNotNull(duration, "duration");
        return clock.instant().minus(duration);
    }
}
