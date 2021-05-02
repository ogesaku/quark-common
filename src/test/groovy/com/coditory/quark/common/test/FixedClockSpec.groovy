package com.coditory.quark.common.test

import spock.lang.Specification

import java.time.Duration
import java.time.Instant
import java.time.ZoneId

class FixedClockSpec extends Specification {
    def "should create clock with fixed starting point"() {
        given:
            Instant startingPoint = Instant.parse("2015-12-03T10:15:30.123456Z")
            FixedClock clock = new FixedClock(startingPoint)
        when:
            Instant now = clock.instant()
            Instant now2 = clock.instant()
        then:
            now == startingPoint
            now2 == startingPoint
    }

    def "should replace time in the clock"() {
        given:
            Instant firstInstant = Instant.parse("2015-12-03T10:15:30.123456Z")
            Instant secondInstant = Instant.parse("2020-12-03T10:15:30.123456Z")
            FixedClock clock = new FixedClock(firstInstant)
        when:
            clock.setInstant(secondInstant)
            Instant now = clock.instant()
        then:
            now == secondInstant
    }

    def "should increment clock according to added duration"() {
        given:
            Instant startingPoint = Instant.parse("2015-12-03T10:15:30.123456Z")
            int millis = 10
            FixedClock clock = new FixedClock(startingPoint)
            Instant before = clock.instant()
        when:
            clock.plusMillis(millis)
            Instant after = clock.instant()
        then:
            Duration.between(before, after).toMillis() == millis
    }

    def "should replace zone"() {
        given:
            Instant startingPoint = Instant.parse("2015-12-03T10:15:30.123456Z")
            ZoneId zoneId = ZoneId.of("Europe/Warsaw");
            ZoneId newZoneId = ZoneId.of("Asia/Tokyo")
            FixedClock clock = new FixedClock(startingPoint, zoneId)

        when:
            FixedClock clockWithNewZone = clock.withZone(newZoneId)
        then:
            clockWithNewZone.zone == newZoneId

        when:
            Instant now = clockWithNewZone.instant()
        then:
            now == startingPoint
    }
}
