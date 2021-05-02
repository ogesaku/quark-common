package com.coditory.quark.common.cli;

import com.coditory.quark.common.check.Args;
import com.coditory.quark.common.util.Objects;
import com.coditory.quark.common.util.Strings;
import com.coditory.quark.common.throwable.Throwables;
import org.jetbrains.annotations.Nullable;

import java.io.PrintStream;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.IllegalFormatException;
import java.util.function.Consumer;

import static com.coditory.quark.common.check.Args.checkNotNull;
import static com.coditory.quark.common.util.Strings.rightPad;

public final class Logger {
    public enum Level {
        TRACE, DEBUG, INFO, WARN, ERROR;

        private final String paddedName;

        Level() {
            this.paddedName = rightPad(name(), 5);
        }
    }

    private static final Logger rootLogger = new Logger("Root");

    public static void configure(Consumer<LoggerConfigBuilder> config) {
        LoggerConfigBuilder builder = Logger.config.toBuilder();
        config.accept(builder);
        Logger.config = builder.build();
    }

    public static LoggerConfig getConfig() {
        return Logger.config;
    }

    public static void setConfig(LoggerConfig config) {
        Logger.config = config;
    }

    public static void resetConfig() {
        Logger.config = LoggerConfig.builder().build();
    }

    public static Logger rootLogger() {
        return rootLogger;
    }

    public static Logger forClass(Class<?> clazz) {
        checkNotNull(clazz, "clazz");
        return new Logger(clazz.getSimpleName());
    }

    private static volatile LoggerConfig config = LoggerConfig
            .builder()
            .build();

    private final String name;

    private Logger(String name) {
        Args.checkNotBlank(name, "name");
        this.name = name;
    }

    public static void stdout(String format, Object... args) {
        PrintStream out = config.out;
        out.println(format(format, args));
        out.flush();
    }

    public static void stderr(String format, Object... args) {
        PrintStream out = config.err;
        out.println(format(format, args));
        out.flush();
    }

    public void error(String format, Object... args) {
        log(Level.ERROR, format, args);
    }

    public void warn(String format, Object... args) {
        log(Level.WARN, format, args);
    }

    public void info(String format, Object... args) {
        log(Level.INFO, format, args);
    }

    public void trace(String format, Object... args) {
        log(Level.TRACE, format, args);
    }

    public void debug(String format, Object... args) {
        log(Level.DEBUG, format, args);
    }

    private void log(Level level, String format, Object... args) {
        LoggerConfig config = Logger.config;
        if (level.ordinal() < config.level.ordinal()) {
            return;
        }
        String message = format(format, args);
        if (config.printLoggerName) {
            message = name + ": " + message;
        }
        if (config.printLogLevel) {
            message = level.paddedName + " " + message;
        }
        if (config.printThreadName) {
            message = "[" + Thread.currentThread().getName() + "] " + message;
        }
        if (config.printTimestamp) {
            LocalDateTime ldt = LocalDateTime.ofInstant(config.clock.instant(), config.clock.getZone());
            String ts = config.timestampFormatter.format(ldt);
            message = ts + " " + message;
        }
        PrintStream stream = level.ordinal() >= Level.WARN.ordinal()
                ? config.err
                : config.out;
        stream.println(message);
        stream.flush();
    }

    private static String format(String format, Object... args) {
        Throwable exception = extractException(args);
        Object[] messageArgs = formatMessageArgs(args, exception != null);
        StringBuilder builder = new StringBuilder();
        int percentages = Strings.count(format, '%');
        int escapedPercentages = Strings.count(format, "%%");
        percentages = percentages - escapedPercentages * 2;
        if (percentages > 0 && args.length > 0) {
            try {
                builder.append(String.format(format, messageArgs));
                for (int i = percentages; i < messageArgs.length; ++i) {
                    builder
                            .append(" ")
                            .append(Objects.toString(messageArgs[i]));
                }
            } catch (IllegalFormatException e) {
                builder
                        .append(format)
                        .append(" (Invalid format. Args: ")
                        .append(Objects.join(messageArgs, ", "))
                        .append(")");
            }
        } else if (args.length > 0) {
            builder
                    .append(format)
                    .append(" ")
                    .append(Objects.join(messageArgs, " "));
        } else {
            builder.append(format);
        }
        if (exception != null) {
            builder
                    .append("\n")
                    .append(Throwables.getStackTrace(exception));
        }
        return builder.toString();
    }

    private static Object[] formatMessageArgs(Object[] args, boolean endsWithException) {
        if (args.length == 0 || (args.length == 1 && endsWithException)) {
            return args;
        }
        int size = endsWithException
                ? args.length - 1
                : args.length;
        Object[] result = new Object[size];
        for (int i = 0; i < size; ++i) {
            Object arg = args[i];
            if (arg instanceof boolean[]) {
                result[i] = Arrays.toString((boolean[]) arg);
            } else if (arg instanceof byte[]) {
                result[i] = Arrays.toString((byte[]) arg);
            } else if (arg instanceof char[]) {
                result[i] = Arrays.toString((char[]) arg);
            } else if (arg instanceof double[]) {
                result[i] = Arrays.toString((double[]) arg);
            } else if (arg instanceof float[]) {
                result[i] = Arrays.toString((float[]) arg);
            } else if (arg instanceof int[]) {
                result[i] = Arrays.toString((int[]) arg);
            } else if (arg instanceof long[]) {
                result[i] = Arrays.toString((long[]) arg);
            } else if (arg instanceof short[]) {
                result[i] = Arrays.toString((short[]) arg);
            } else if (arg instanceof Object[]) {
                result[i] = Arrays.toString((Object[]) arg);
            } else {
                result[i] = arg;
            }
        }
        return result;
    }

    @Nullable
    private static Throwable extractException(Object... args) {
        if (args.length == 0) {
            return null;
        }
        Object last = Objects.lastOrNull(args);
        return last instanceof Throwable
                ? (Throwable) last
                : null;
    }

    public static final class LoggerConfig {
        public static LoggerConfigBuilder builder() {
            return new LoggerConfigBuilder();
        }

        final PrintStream out;
        final PrintStream err;
        final Logger.Level level;
        final Clock clock;
        final boolean printLogLevel;
        final boolean printThreadName;
        final boolean printLoggerName;
        final boolean printTimestamp;
        final DateTimeFormatter timestampFormatter;

        LoggerConfig(
                PrintStream out,
                PrintStream err,
                Logger.Level level,
                Clock clock,
                boolean printLogLevel,
                boolean printThreadName,
                boolean printLoggerName,
                boolean printTimestamp,
                DateTimeFormatter timestampFormatter
        ) {
            this.out = out;
            this.err = err;
            this.level = level;
            this.clock = clock;
            this.printLogLevel = printLogLevel;
            this.printThreadName = printThreadName;
            this.printTimestamp = printTimestamp;
            this.printLoggerName = printLoggerName;
            this.timestampFormatter = timestampFormatter;
        }

        public LoggerConfigBuilder toBuilder() {
            return new LoggerConfigBuilder(
                    out, err, level, clock, printLogLevel, printThreadName,
                    printLoggerName, printTimestamp, timestampFormatter
            );
        }
    }

    public static final class LoggerConfigBuilder {
        private Clock clock = Clock.systemDefaultZone();
        private PrintStream out = System.out;
        private PrintStream err = System.err;
        private Logger.Level level = Level.INFO;
        private boolean printLogLevel;
        private boolean printThreadName;
        private boolean printLoggerName;
        private boolean printTimestamp;
        private DateTimeFormatter timestampFormatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

        private LoggerConfigBuilder() {
        }

        private LoggerConfigBuilder(
                PrintStream out,
                PrintStream err,
                Logger.Level level,
                Clock clock,
                boolean printLogLevel,
                boolean printThreadName,
                boolean printLoggerName,
                boolean printTimestamp,
                DateTimeFormatter timestampFormatter
        ) {
            this.out = out;
            this.err = err;
            this.level = level;
            this.clock = clock;
            this.printLogLevel = printLogLevel;
            this.printThreadName = printThreadName;
            this.printTimestamp = printTimestamp;
            this.printLoggerName = printLoggerName;
            this.timestampFormatter = timestampFormatter;
        }

        public LoggerConfigBuilder out(PrintStream out) {
            checkNotNull(out, "out");
            this.out = out;
            return this;
        }

        public LoggerConfigBuilder err(PrintStream err) {
            checkNotNull(err, "err");
            this.err = err;
            return this;
        }

        public LoggerConfigBuilder level(Logger.Level level) {
            checkNotNull(level, "level");
            this.level = level;
            return this;
        }

        public LoggerConfigBuilder clock(Clock clock) {
            checkNotNull(clock, "clock");
            this.clock = clock;
            return this;
        }

        public LoggerConfigBuilder printLogLevel() {
            return printLogLevel(true);
        }

        public LoggerConfigBuilder printLogLevel(boolean printLogLevel) {
            this.printLogLevel = printLogLevel;
            return this;
        }

        public LoggerConfigBuilder printThreadName() {
            return printThreadName(true);
        }

        public LoggerConfigBuilder printThreadName(boolean printThreadName) {
            this.printThreadName = printThreadName;
            return this;
        }

        public LoggerConfigBuilder printTimestamp() {
            return printTimestamp(true);
        }

        public LoggerConfigBuilder printTimestamp(boolean printTimestamp) {
            this.printTimestamp = printTimestamp;
            return this;
        }

        public LoggerConfigBuilder timestampFormatter(DateTimeFormatter timestampFormatter) {
            this.timestampFormatter = timestampFormatter;
            return this;
        }

        public LoggerConfigBuilder printLoggerName() {
            return printLoggerName(true);
        }

        public LoggerConfigBuilder printLoggerName(boolean printLoggerName) {
            this.printLoggerName = printLoggerName;
            return this;
        }

        public LoggerConfigBuilder printAllMetaData() {
            printTimestamp();
            printLoggerName();
            printThreadName();
            printLogLevel();
            return this;
        }

        public LoggerConfig build() {
            return new LoggerConfig(
                    out, err, level, clock, printLogLevel, printThreadName,
                    printLoggerName, printTimestamp, timestampFormatter);
        }
    }
}
