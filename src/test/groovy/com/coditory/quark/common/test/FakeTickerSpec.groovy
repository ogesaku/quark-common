package com.coditory.quark.common.test

import spock.lang.Specification
import spock.lang.Unroll

import java.time.Duration

class FakeTickerSpec extends Specification {
    def "should create auto incrementing fake ticker"() {
        given:
            FakeTicker ticker = FakeTicker.autoIncrementingFakeTicker()
        when:
            long result1 = ticker.nanos()
            long result2 = ticker.nanos()
            long result3 = ticker.nanos()
        then:
            result1 < result2
            result2 < result3
    }

    def "should disable auto incrementing"() {
        given:
            FakeTicker ticker = FakeTicker.autoIncrementingFakeTicker()
        when:
            long result1 = ticker.nanos()
            ticker.autoIncrement(false)
            long result2 = ticker.nanos()
        then:
            result1 == result2
    }

    def "should change auto incrementing time"() {
        given:
            FakeTicker ticker = FakeTicker.autoIncrementingFakeTicker(Duration.ofMillis(5))
        when:
            long result1 = ticker.nanos()
            long result2 = ticker.nanos()
            ticker.autoIncrement(Duration.ofNanos(8))
            long result3 = ticker.nanos()
        then:
            result2 - result1 == 5_000_000
            result3 - result2 == 8
    }

    def "should create a fake ticker"() {
        given:
            FakeTicker ticker = FakeTicker.fakeTicker()

        when:
            long result1 = ticker.nanos()
            long result2 = ticker.nanos()
        then:
            result1 == result2

        when:
            ticker.plusNano()
            result2 = ticker.nanos()
        then:
            result2 - result1 == 1
    }

    def "should set nanos in the ticker"() {
        given:
            FakeTicker ticker = FakeTicker.fakeTicker()
        when:
            ticker.setNanos(123)
        then:
            ticker.nanos() == 123
    }

    @Unroll
    def "should increment ticker.#name"() {
        given:
            FakeTicker ticker = FakeTicker.fakeTicker()
            long initNanos = ticker.nanos()

        when:
            modifier.call(ticker)

        then:
            ticker.nanos() - initNanos == expectedNanos

        where:
            name           | modifier            | expectedNanos
            "plusNano()"   | { it.plusNano() }   | 1
            "plusMilli()"  | { it.plusMilli() }  | 1_000_000
            "plusSecond()" | { it.plusSecond() } | 1_000_000_000
            "plusMinute()" | { it.plusMinute() } | 60_000_000_000
            "plusHour()"   | { it.plusHour() }   | 3_600_000_000_000
            "plusDay()"    | { it.plusDay() }    | 86_400_000_000_000
    }
}
