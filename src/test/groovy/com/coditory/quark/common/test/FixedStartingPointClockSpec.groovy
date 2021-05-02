package com.coditory.quark.common.test

import spock.lang.Specification

import java.time.Duration
import java.time.Instant
import java.time.ZoneId

class FixedStartingPointClockSpec extends Specification {
    def "should create clock with fixed starting point"() {
        given:
            Instant startingPoint = Instant.parse("2015-12-03T10:15:30.123456Z")
            FixedStartingPointClock clock = new FixedStartingPointClock(startingPoint)
        when:
            Instant now = clock.instant()
        then:
            now.isAfter(startingPoint.minusMillis(1))
            now.isBefore(startingPoint.plusSeconds(10))
    }

    def "should increment instant according to real time"() {
        given:
            Instant startingPoint = Instant.parse("2015-12-03T10:15:30.123456Z")
            FixedStartingPointClock clock = new FixedStartingPointClock(startingPoint)
            int step = 5
        and:
            Instant before = clock.instant()
            sleep(step)
        when:
            Instant after = clock.instant()
            long between = Duration.between(before, after).toMillis()
        then:
            between >= step
            between < step * 10
    }

    def "should replace zone"() {
        given:
            Instant startingPoint = Instant.parse("2015-12-03T10:15:30.123456Z")
            ZoneId zoneId = ZoneId.of("Europe/Warsaw");
            ZoneId newZoneId = ZoneId.of("Asia/Tokyo")
            FixedStartingPointClock clock = new FixedStartingPointClock(startingPoint, zoneId)

        when:
            FixedStartingPointClock clockWithNewZone = clock.withZone(newZoneId)
        then:
            clockWithNewZone.zone == newZoneId

        when:
            Instant now = clockWithNewZone.instant()
        then:
            now.isAfter(startingPoint.minusMillis(1))
            now.isBefore(startingPoint.plusSeconds(1))
    }
}
