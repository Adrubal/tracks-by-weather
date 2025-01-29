package com.challenge.backend.model.exception;

public class MissingRequiredParametersException extends RuntimeException {

    public MissingRequiredParametersException() {
        super();
    }

    public MissingRequiredParametersException(String s) {
        super(s);
    }
}
