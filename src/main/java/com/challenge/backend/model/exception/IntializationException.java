package com.challenge.backend.model.exception;

public class IntializationException extends RuntimeException {

    public IntializationException() {
        super();
    }

    public IntializationException(String s) {
        super(s);
    }

    public IntializationException(String s, Throwable e) {
        super(s);
    }
}
