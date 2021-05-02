package com.coditory.quark.common.time


import spock.lang.Specification
import spock.lang.Unroll

import java.time.Duration

import static Durations.parseDuration
import static Durations.parseDurationOrDefault
import static Durations.parseDurationOrEmpty
import static Durations.parseDurationOrNull

class DurationsSpec extends Specification {
    @Unroll
    def "should parse Duration value: #value"() {
        expect:
            parseDurationOrNull(value) == expected
        where:
            value       || expected
            "PT20.345S" || Duration.parse("PT20.345S")
            "-PT-6H+3M" || Duration.parse("-PT-6H+3M")
            "10ms"      || Duration.parse("PT0.01S")
            "10.5ms"    || Duration.parse("PT0.0105S")
            "1.5s"      || Duration.parse("PT1.5S")
            "1m"        || Duration.parse("PT1M")
            "2h"        || Duration.parse("PT2H")
            "1.5m"      || null
            "1d"        || null
            "10.5mss"   || null
            "1.0.5ms"   || null
    }

    @Unroll
    def "should parse optional Duration"() {
        expect:
            parseDurationOrEmpty(value) == expected

        where:
            value || expected
            "2h"  || Optional.of(Duration.parse("PT2H"))
            "xx"  || Optional.empty()
    }

    @Unroll
    def "should parse Duration or return default value"() {
        expect:
            parseDurationOrDefault(value, defaultValue) == expected
        where:
            value  | defaultValue           || expected
            "xx"   | Duration.parse("PT2H") || Duration.parse("PT2H")
            "PT1H" | Duration.parse("PT2H") || Duration.parse("PT1H")
    }

    def "should parse Duration or throw error"() {
        when:
            parseDuration("2h") == Duration.parse("PT2H")
        then:
            noExceptionThrown()

        when:
            parseDuration("xx")
        then:
            IllegalArgumentException e = thrown(IllegalArgumentException)
            e.message == "Unrecognized duration format: 'xx'"
    }

    @Unroll
    def "should format duration using compact time formatter"() {
        when:
            String result = Durations.formatToHumanReadable(duration)
        then:
            result == expected

        where:
            duration                              || expected
            Duration.parse("PT2H")                || "2:00h"
            Duration.parse("PT20.345S")           || "20.345s"
            Duration.parse("PT20.3456789S")       || "20.345s"
            Duration.parse("PT2H30M20.3456789S")  || "2:30h"
            Duration.parse("P3DT2H5M20.3456789S") || "3:02d"
            Duration.parse("PT0.3456789S")        || "345ms"
    }

    @Unroll
    def "should format duration using time formatter"() {
        when:
            String result = Durations.formatTime(duration)
        then:
            result == expected

        where:
            duration                              || expected
            Duration.parse("PT2H")                || "02:00:00"
            Duration.parse("PT20.345S")           || "00:00:20"
            Duration.parse("PT20.3456789S")       || "00:00:20"
            Duration.parse("PT2H30M20.3456789S")  || "02:30:20"
            Duration.parse("P3DT2H5M20.3456789S") || "74:05:20"
    }

    @Unroll
    def "should format duration using time formatter with millis"() {
        when:
            String result = Durations.formatTimeWithMillis(duration)
        then:
            result == expected

        where:
            duration                              || expected
            Duration.parse("PT2H")                || "02:00:00.000"
            Duration.parse("PT20.345S")           || "00:00:20.345"
            Duration.parse("PT20.3456789S")       || "00:00:20.345"
            Duration.parse("PT2H30M20.3456789S")  || "02:30:20.345"
            Duration.parse("P3DT2H5M20.3456789S") || "74:05:20.345"
    }

    @Unroll
    def "should format duration using time dynamic time formatter"() {
        when:
            String result = Durations.formatTimeWithMillisDynamically(duration)
        then:
            result == expected

        where:
            duration                              || expected
            Duration.parse("PT2H")                || "02:00:00.000"
            Duration.parse("PT20.345S")           || "20.345"
            Duration.parse("PT20.3456789S")       || "20.345"
            Duration.parse("PT2H30M20.3456789S")  || "02:30:20.345"
            Duration.parse("PT30M20.3456789S")    || "30:20.345"
            Duration.parse("P3DT2H5M20.3456789S") || "74:05:20.345"
    }
}
