package com.coditory.quark.common.test;

import com.coditory.quark.common.text.Lenny;

public class SimulatedCheckedException extends Exception {
    public static final String DEFAULT_MESSAGE = Lenny.shrug() + " just a simulation";

    public SimulatedCheckedException() {
        this(DEFAULT_MESSAGE, null);
    }

    public SimulatedCheckedException(String message) {
        this(message, null);
    }

    public SimulatedCheckedException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    synchronized public Throwable fillInStackTrace() {
        return this;
    }
}
