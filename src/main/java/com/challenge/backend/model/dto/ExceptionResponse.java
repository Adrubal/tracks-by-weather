package com.challenge.backend.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExceptionResponse {

    private int code;
    private String message;
}
