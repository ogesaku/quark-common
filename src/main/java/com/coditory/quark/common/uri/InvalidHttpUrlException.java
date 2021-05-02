package com.coditory.quark.common.uri;

public final class InvalidHttpUrlException extends RuntimeException {
    public InvalidHttpUrlException(String message) {
        super(message);
    }

    public InvalidHttpUrlException(String message, Throwable cause) {
        super(message, cause);
    }
}
