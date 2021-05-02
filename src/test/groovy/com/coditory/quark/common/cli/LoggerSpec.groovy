package com.coditory.quark.common.cli

import com.coditory.quark.common.test.FixedClock
import spock.lang.Specification

class LoggerSpec extends Specification {
    FixedClock clock = new FixedClock()
    ByteArrayOutputStream outContent = new ByteArrayOutputStream()
    ByteArrayOutputStream errContent = new ByteArrayOutputStream()

    void setup() {
        Logger.configure {
            it
                    .clock(clock)
                    .out(new PrintStream(outContent))
                    .err(new PrintStream(errContent))
        }
    }

    void cleanup() {
        Logger.resetConfig()
    }

    def "should log messages to stdout and stderr"() {
        given:
            Logger logger = Logger.rootLogger()
        when:
            logger.trace("trace")
            logger.debug("debug")
            logger.info("info")
            logger.warn("warn")
            logger.error("error")
        then:
            outContent.toString() == "info\n"
            errContent.toString() == ["warn", "error\n"].join("\n")
    }

    def "should enable trace and debug level logs"() {
        given:
            Logger.configure { it.level(Logger.Level.TRACE) }
            Logger logger = Logger.rootLogger()
        when:
            logger.trace("trace")
            logger.debug("debug")
            logger.info("info")
            logger.warn("warn")
            logger.error("error")
        then:
            outContent.toString() == ["trace", "debug", "info\n"].join("\n")
            errContent.toString() == ["warn", "error\n"].join("\n")
    }

    def "should add all log meta data"() {
        given:
            Logger.configure { it.printAllMetaData() }
            Logger logger = Logger.forClass(LoggerSpec)
        when:
            logger.info("Hello world")
        then:
            outContent.toString() == "11:15:30.123 [Test worker] INFO  LoggerSpec: Hello world\n"
    }

    def "should format String.format message"() {
        given:
            Logger logger = Logger.rootLogger()
        when:
            logger.info("Hello %s. It's your %d visit.", "John", 42)
        then:
            outContent.toString() == "Hello John. It's your 42 visit.\n"
    }

    def "should format non String.format message"() {
        given:
            Logger logger = Logger.forClass(LoggerSpec)
        when:
            logger.info("Hello", "John", "it's your", 42, "visit.")
        then:
            outContent.toString() == "Hello John it's your 42 visit.\n"
    }

    def "should handle invalid String.format message"() {
        given:
            Logger logger = Logger.rootLogger()
        when:
            logger.info("Hello %d. It's your %d visit.", "John", 42)
        then:
            outContent.toString() == "Hello %d. It's your %d visit. (Invalid format. Args: John, 42)\n"
    }

    def "should append message arguments missing in format"() {
        given:
            Logger logger = Logger.rootLogger()
        when:
            logger.info("Hello %s. It's your", "John", 42, "visit")
        then:
            outContent.toString() == "Hello John. It's your 42 visit\n"
    }

    def "should format exception as last message argument"() {
        given:
            Logger logger = Logger.rootLogger()
            RuntimeException exception = new RuntimeException("Testing exception log")
        when:
            logger.warn("Hello %s. It's your", "John", 42, "visit", exception)
        then:
            errContent.toString().startsWith(
                    "Hello John. It's your 42 visit\n" +
                            "java.lang.RuntimeException: Testing exception log\n"
            )
    }

    def "should format array message argument"() {
        given:
            Logger logger = Logger.forClass(LoggerSpec)
        when:
            logger.info("Some values: %s", new int[]{123, 456, 789})
        then:
            outContent.toString() == "Some values: 123 456 789\n"
            outContent.reset()

        when:
            logger.info("%s values: %s", "Some", new int[]{123, 456, 789})
        then:
            outContent.toString() == "Some values: [123, 456, 789]\n"
            outContent.reset()

        when:
            logger.info("Some values:", new int[]{123, 456, 789})
        then:
            outContent.toString() == "Some values: 123 456 789\n"
            outContent.reset()

        when:
            logger.info("Some values:", "x", new int[]{123, 456, 789})
        then:
            outContent.toString() == "Some values: x [123, 456, 789]\n"
    }

    def "should format array of different types"() {
        given:
            Logger logger = Logger.forClass(LoggerSpec)
        when:
            logger.info("Array of", "booleans", new boolean[]{false, true, false})
            logger.info("Array of", "bytes", new byte[]{1, 2, 3})
            logger.info("Array of", "chars", new char[]{'a', 'b', 'c'})
            logger.info("Array of", "doubles", new double[]{123, 456, 789})
            logger.info("Array of", "floats", new float[]{123, 456, 789})
            logger.info("Array of", "ints", new int[]{123, 456, 789})
            logger.info("Array of", "longs", new long[]{123, 456, 789})
            logger.info("Array of", "shorts", new short[]{123, 456, 789})
            logger.info("Array of", "Strings", new String[]{"abc", "def", "ghi"})
        then:
            outContent.toString() == "Array of booleans [false, true, false]\n" +
                    "Array of bytes [1, 2, 3]\n" +
                    "Array of chars [a, b, c]\n" +
                    "Array of doubles [123.0, 456.0, 789.0]\n" +
                    "Array of floats [123.0, 456.0, 789.0]\n" +
                    "Array of ints [123, 456, 789]\n" +
                    "Array of longs [123, 456, 789]\n" +
                    "Array of shorts [123, 456, 789]\n" +
                    "Array of Strings [abc, def, ghi]\n"
    }
}
