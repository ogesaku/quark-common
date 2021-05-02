package com.coditory.quark.common.time

import com.coditory.quark.commons.test.FakeTicker
import spock.lang.Specification
import spock.lang.Unroll

import java.time.Duration
import java.util.function.Supplier

import static com.coditory.quark.commons.test.FakeTicker.fakeTicker

class StopwatchSpec extends Specification {
    FakeTicker ticker = fakeTicker()

    def "should create started timer"() {
        when:
            Stopwatch stopwatch = Stopwatch.started(ticker)
        then:
            stopwatch.isRunning()
        when:
            ticker.plusNanos(123)
        then:
            stopwatch.elapsed() == Duration.ofNanos(123)
    }

    def "should create stopped timer"() {
        when:
            Stopwatch stopwatch = Stopwatch.stopped(ticker)
        then:
            stopwatch.isRunning() == false
        when:
            ticker.plusNanos(123)
        then:
            stopwatch.elapsed() == Duration.ofNanos(0)
    }

    def "should sum two measurements"() {
        given:
            Stopwatch stopwatch = Stopwatch.started(ticker)
        when:
            ticker.plusNanos(1)
            stopwatch.stop()
            ticker.plusNanos(2)
            stopwatch.start()
            ticker.plusNanos(5)
        then:
            stopwatch.elapsed() == Duration.ofNanos(6)
    }

    def "should reset a timer"() {
        given:
            Stopwatch stopwatch = Stopwatch.started(ticker)
        when:
            ticker.plusNanos(1)
            stopwatch.reset()
            ticker.plusNanos(1)
        then:
            stopwatch.isRunning() == false
            stopwatch.elapsed() == Duration.ofNanos(0)
    }

    def "should measure a runnable"() {
        given:
            Stopwatch stopwatch = Stopwatch.stopped(ticker)
        when:
            stopwatch.measure({
                ticker.plusNanos(5)
            })
        then:
            stopwatch.isRunning() == false
            stopwatch.elapsed() == Duration.ofNanos(5)
    }

    def "should measure a supplier"() {
        given:
            String value = "value"
            Stopwatch stopwatch = Stopwatch.stopped(ticker)
        when:
            String result = stopwatch.measure({
                ticker.plusNanos(5)
                return value
            } as Supplier<String>)
        then:
            stopwatch.isRunning() == false
            stopwatch.elapsed() == Duration.ofNanos(5)
            result == value
    }

    def "should throw error when measuring with started stopwatch"() {
        given:
            Stopwatch stopwatch = Stopwatch.started()
        when:
            stopwatch.measure({
                ticker.plusNanos(5)
            })
        then:
            thrown(IllegalStateException)
        when:
            stopwatch.measure({
                ticker.plusNanos(5)
                return 123
            })
        then:
            thrown(IllegalStateException)
    }

    def "should create a stopwatch with a measurement"() {
        when:
            Stopwatch stopwatch = Stopwatch.measurement({
                ticker.plusNanos(5)
            }, ticker)
        then:
            stopwatch.isRunning() == false
            stopwatch.elapsed() == Duration.ofNanos(5)
    }

    @Unroll
    def "should format elapsed time: #duration -> #expected"() {
        given:
            Stopwatch stopwatch = Stopwatch.stopped(ticker)
        when:
            stopwatch.measure({ ticker.plus(duration) })
        then:
            stopwatch.toString() == expected

        where:
            duration                    || expected
            Duration.ofNanos(1)         || "1ns"
            Duration.ofNanos(21)        || "21ns"
            Duration.ofNanos(321)       || "321ns"
            Duration.ofNanos(1321)      || "1.32μs"
            Duration.ofNanos(21321)     || "21.32μs"
            Duration.ofNanos(321321)    || "321.32μs"
            Duration.ofNanos(321000)    || "321μs"
            Duration.ofNanos(1321321)   || "1.32ms"
            Duration.ofNanos(21321321)  || "21.32ms"
            Duration.ofNanos(321321321) || "321.32ms"
            Duration.ofMillis(321)      || "321ms"
            Duration.ofMillis(1321)     || "1.32s"
            Duration.ofMillis(21321)    || "21.32s"
            Duration.ofSeconds(21)      || "21s"
            Duration.ofSeconds(90)      || "1.5min"
            Duration.ofMinutes(90)      || "1.5h"
            Duration.ofHours(36)        || "1.5d"
    }
}
