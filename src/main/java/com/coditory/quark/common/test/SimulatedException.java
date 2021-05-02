package com.coditory.quark.common.test;

import com.coditory.quark.common.text.Lenny;

public class SimulatedException extends RuntimeException {
    public static final String DEFAULT_MESSAGE = Lenny.shrug() + " just a simulation";

    public SimulatedException() {
        this(DEFAULT_MESSAGE, null);
    }

    public SimulatedException(String message) {
        this(message, null);
    }

    public SimulatedException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    synchronized public Throwable fillInStackTrace() {
        return this;
    }
}
