package com.coditory.quark.common.concurrent;

import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

import static com.coditory.quark.common.check.Args.check;
import static com.coditory.quark.common.check.Args.checkNotNull;
import static java.lang.Thread.MAX_PRIORITY;
import static java.lang.Thread.MIN_PRIORITY;

public class NamedThreadFactory implements ThreadFactory {
    private final String nameFormat;
    private final Boolean daemon;
    private final Integer priority;
    private final Thread.UncaughtExceptionHandler uncaughtExceptionHandler;
    private final ThreadFactory backingThreadFactory;
    private final AtomicLong count = new AtomicLong(0);

    private NamedThreadFactory(
            String nameFormat,
            Boolean daemon,
            Integer priority,
            Thread.UncaughtExceptionHandler uncaughtExceptionHandler,
            ThreadFactory backingThreadFactory) {
        this.nameFormat = nameFormat;
        this.daemon = daemon;
        this.priority = priority;
        this.uncaughtExceptionHandler = uncaughtExceptionHandler;
        this.backingThreadFactory = backingThreadFactory;
    }

    @Override
    public Thread newThread(Runnable runnable) {
        Thread thread = backingThreadFactory.newThread(runnable);
        if (nameFormat != null) {
            thread.setName(format(nameFormat, count.getAndIncrement()));
        }
        if (daemon != null) {
            thread.setDaemon(daemon);
        }
        if (priority != null) {
            thread.setPriority(priority);
        }
        if (uncaughtExceptionHandler != null) {
            thread.setUncaughtExceptionHandler(uncaughtExceptionHandler);
        }
        return thread;
    }

    private String format(String format, Object... args) {
        return String.format(Locale.ROOT, format, args);
    }

    public static InstrumentedThreadFactoryBuilder builder() {
        return new InstrumentedThreadFactoryBuilder();
    }

    public static class InstrumentedThreadFactoryBuilder {
        private String nameFormat = "";
        private Boolean daemon = null;
        private Integer priority = null;
        private Thread.UncaughtExceptionHandler uncaughtExceptionHandler = null;
        private ThreadFactory backingThreadFactory = Executors.defaultThreadFactory();

        public InstrumentedThreadFactoryBuilder nameFormat(String nameFormat) {
            checkNameFormat(nameFormat);
            this.nameFormat = nameFormat;
            return this;
        }

        public InstrumentedThreadFactoryBuilder daemonThreads(boolean daemon) {
            this.daemon = daemon;
            return this;
        }

        public InstrumentedThreadFactoryBuilder priority(int priority) {
            check(priority >= MIN_PRIORITY, "Expected: thread priority (%s) >= %s", priority, MIN_PRIORITY);
            check(priority <= MAX_PRIORITY, "Expected: thread priority (%s) <= %s", priority, MAX_PRIORITY);
            this.priority = priority;
            return this;
        }

        public InstrumentedThreadFactoryBuilder uncaughtExceptionHandler(Thread.UncaughtExceptionHandler uncaughtExceptionHandler) {
            this.uncaughtExceptionHandler = checkNotNull(uncaughtExceptionHandler, "uncaughtExceptionHandler");
            return this;
        }

        public InstrumentedThreadFactoryBuilder backingThreadFactory(ThreadFactory backingThreadFactory) {
            this.backingThreadFactory = checkNotNull(backingThreadFactory, "backingThreadFactory");
            return this;
        }

        private void checkNameFormat(String format) {
            format(format, 0);
        }

        private String format(String format, Object... args) {
            return String.format(Locale.ROOT, format, args);
        }

        ThreadFactory build() {
            return new NamedThreadFactory(nameFormat, daemon, priority, uncaughtExceptionHandler, backingThreadFactory);
        }
    }
}
