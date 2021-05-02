package com.coditory.quark.common.test

import spock.lang.Specification

import java.time.Duration
import java.time.Instant
import java.time.ZoneId

class IncrementingClockSpec extends Specification {
    def "should create clock with fixed starting point"() {
        given:
            Instant startingPoint = Instant.parse("2015-12-03T10:15:30.123456Z")
            Duration step = Duration.ofMillis(10)
            IncrementingClock clock = new IncrementingClock(startingPoint, step)
        when:
            Instant now = clock.instant()
            Instant now2 = clock.instant()
        then:
            now == startingPoint
            now2 == startingPoint + step
    }

    def "should increment clock according to the defined step"() {
        given:
            Instant startingPoint = Instant.parse("2015-12-03T10:15:30.123456Z")
            long step = 10
            IncrementingClock clock = new IncrementingClock(startingPoint, Duration.ofMillis(step))
            Instant before = clock.instant()
        when:
            Instant after = clock.instant()
            long between = Duration.between(before, after).toMillis()
        then:
            between == step
    }

    def "should replace zone"() {
        given:
            Instant startingPoint = Instant.parse("2015-12-03T10:15:30.123456Z")
            ZoneId zoneId = ZoneId.of("Europe/Warsaw");
            ZoneId newZoneId = ZoneId.of("Asia/Tokyo")
            Duration step = Duration.ofMillis(10)
            IncrementingClock clock = new IncrementingClock(startingPoint, zoneId, step)

        when:
            IncrementingClock clockWithNewZone = clock.withZone(newZoneId)
        then:
            clockWithNewZone.zone == newZoneId

        when:
            Instant now = clockWithNewZone.instant()
            Instant now2 = clockWithNewZone.instant()
        then:
            now == startingPoint
            now2 == startingPoint + step
    }
}
