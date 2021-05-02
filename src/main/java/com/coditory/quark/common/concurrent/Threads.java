package com.coditory.quark.common.concurrent;

import java.time.Duration;

public final class Threads {
    private Threads() {
        throw new UnsupportedOperationException("Do not instantiate utility class");
    }

    public void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new IllegalStateException("Could not sleep", e);
        }
    }

    public void sleep(Duration duration) {
        try {
            Thread.sleep(duration.toMillis());
        } catch (InterruptedException e) {
            throw new IllegalStateException("Could not sleep", e);
        }
    }
}
