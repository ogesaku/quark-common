package com.coditory.quark.common.uri;

public final class InvalidUriException extends RuntimeException {
    public InvalidUriException(String message) {
        super(message);
    }

    public InvalidUriException(String message, Throwable cause) {
        super(message, cause);
    }
}
