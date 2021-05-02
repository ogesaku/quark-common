package com.coditory.quark.common.test

import com.coditory.quark.common.throwable.Throwables
import spock.lang.Specification

class SimulatedExceptionSpec extends Specification {
    def "should throw SimulatedException without stacktrace"() {
        when:
            throw new SimulatedException()
        then:
            SimulatedException exception = thrown(SimulatedException)
            Throwables.getStackTrace(exception) == "com.coditory.quark.common.test.SimulatedException: ¯\\_(ツ)_/¯ just a simulation\n"
    }

    def "should throw SimulatedCheckedException without stacktrace"() {
        when:
            throw new SimulatedCheckedException()
        then:
            SimulatedCheckedException exception = thrown(SimulatedCheckedException)
            Throwables.getStackTrace(exception) == "com.coditory.quark.common.test.SimulatedCheckedException: ¯\\_(ツ)_/¯ just a simulation\n"
    }
}
