package com.coditory.quark.common.time;

import java.text.DecimalFormat;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import static com.coditory.quark.common.check.Args.checkNotNull;
import static com.coditory.quark.common.check.Asserts.assertThat;
import static java.util.concurrent.TimeUnit.DAYS;
import static java.util.concurrent.TimeUnit.HOURS;
import static java.util.concurrent.TimeUnit.MICROSECONDS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

public final class Stopwatch {
    private static final DecimalFormat format = new DecimalFormat("0.##");

    static {
        format.setMaximumFractionDigits(2);
    }

    private final Ticker ticker;
    private boolean isRunning;
    private long elapsedNanos;
    private long startNanos;

    public static Stopwatch measurement(Runnable runnable) {
        return measurement(runnable, Ticker.systemTicker());
    }

    public static Stopwatch measurement(Runnable runnable, Ticker ticker) {
        Stopwatch stopwatch = Stopwatch.stopped(ticker);
        stopwatch.start();
        runnable.run();
        stopwatch.stop();
        return stopwatch;
    }

    public static Stopwatch stopped() {
        return new Stopwatch();
    }

    public static Stopwatch stopped(Ticker ticker) {
        return new Stopwatch(ticker);
    }

    public static Stopwatch started() {
        return new Stopwatch().start();
    }

    public static Stopwatch started(Ticker ticker) {
        return new Stopwatch(ticker).start();
    }

    Stopwatch() {
        this(Ticker.systemTicker());
    }

    Stopwatch(Ticker clock) {
        this.ticker = checkNotNull(clock, "ticker");
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void measure(Runnable runnable) {
        assertThat(!isRunning, "This stopwatch is already running.");
        start();
        try {
            runnable.run();
        } finally {
            stop();
        }
    }

    public <T> T measure(Supplier<T> supplier) {
        assertThat(!isRunning, "This stopwatch is already running.");
        T result;
        start();
        try {
            result = supplier.get();
        } finally {
            stop();
        }
        return result;
    }

    public Stopwatch start() {
        assertThat(!isRunning, "This stopwatch is already running.");
        isRunning = true;
        startNanos = ticker.nanos();
        return this;
    }

    public Stopwatch stop() {
        long now = ticker.nanos();
        assertThat(isRunning, "This stopwatch is already stopped.");
        isRunning = false;
        elapsedNanos += now - startNanos;
        return this;
    }

    public Stopwatch reset() {
        elapsedNanos = 0;
        isRunning = false;
        return this;
    }

    private long elapsedNanos() {
        return isRunning
                ? ticker.nanos() - startNanos + elapsedNanos
                : elapsedNanos;
    }

    public Duration elapsed() {
        return Duration.ofNanos(elapsedNanos());
    }

    @Override
    public String toString() {
        long nanos = elapsedNanos();
        TimeUnit unit = chooseUnit(nanos);
        long unitNanos = NANOSECONDS.convert(1, unit);
        double value = (double) nanos / unitNanos;
        return format.format(value) + abbreviate(unit);
    }

    private static TimeUnit chooseUnit(long nanos) {
        if (DAYS.convert(nanos, NANOSECONDS) > 0) {
            return DAYS;
        }
        if (HOURS.convert(nanos, NANOSECONDS) > 0) {
            return HOURS;
        }
        if (MINUTES.convert(nanos, NANOSECONDS) > 0) {
            return MINUTES;
        }
        if (SECONDS.convert(nanos, NANOSECONDS) > 0) {
            return SECONDS;
        }
        if (MILLISECONDS.convert(nanos, NANOSECONDS) > 0) {
            return MILLISECONDS;
        }
        if (MICROSECONDS.convert(nanos, NANOSECONDS) > 0) {
            return MICROSECONDS;
        }
        return NANOSECONDS;
    }

    private static String abbreviate(TimeUnit unit) {
        String abbreviation = null;
        switch (unit) {
            case NANOSECONDS: abbreviation = "ns"; break;
            case MICROSECONDS: abbreviation = "\u03bcs"; break; // μs
            case MILLISECONDS: abbreviation = "ms"; break;
            case SECONDS: abbreviation = "s"; break;
            case MINUTES: abbreviation = "min"; break;
            case HOURS: abbreviation = "h"; break;
            case DAYS: abbreviation = "d"; break;
        }
        if (abbreviation == null) {
            throw new IllegalStateException("Could not match abbreviation to time unit: " + unit);
        }
        return abbreviation;
    }
}
