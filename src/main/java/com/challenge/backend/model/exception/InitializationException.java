package com.challenge.backend.model.exception;

public class InitializationException extends RuntimeException {

    public InitializationException() {
        super();
    }

    public InitializationException(String s) {
        super(s);
    }

    public InitializationException(String s, Throwable e) {
        super(s);
    }
}
