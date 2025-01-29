package com.challenge.backend.controller.advise;

import com.challenge.backend.model.dto.ExceptionResponse;
import com.challenge.backend.model.exception.BadRequestException;
import com.challenge.backend.model.exception.MissingRequiredParametersException;
import com.challenge.backend.model.exception.ResourceNotFoundException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
public class ControllerAdvise extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {MissingRequiredParametersException.class})
    protected ResponseEntity<ExceptionResponse> missingRequiredParametersException(RuntimeException ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setCode(HttpStatus.BAD_REQUEST.value());
        exceptionResponse.setMessage(ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(exceptionResponse);
    }

    @ExceptionHandler(value = {ResourceNotFoundException.class})
    protected ResponseEntity<ExceptionResponse> resourceNotFoundException(RuntimeException ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setCode(HttpStatus.NOT_FOUND.value());

        exceptionResponse.setMessage(ex.getMessage());
        if (isValid(ex.getMessage())) {
            JsonObject jsonObject = JsonParser.parseString(ex.getMessage())
                    .getAsJsonObject();
            exceptionResponse.setMessage(jsonObject.get("message").getAsString());
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(exceptionResponse);
    }

    @ExceptionHandler(value = {BadRequestException.class})
    protected ResponseEntity<ExceptionResponse> badRequestException(RuntimeException ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setCode(HttpStatus.BAD_REQUEST.value());

        exceptionResponse.setMessage(ex.getMessage());
        if (isValid(ex.getMessage())) {
            JsonObject jsonObject = JsonParser.parseString(ex.getMessage())
                    .getAsJsonObject();
            exceptionResponse.setMessage(jsonObject.get("message").getAsString());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(exceptionResponse);
    }

    public boolean isValid(String json) {
        try {
            JsonParser.parseString(json);
        } catch (JsonSyntaxException e) {
            return false;
        }
        return true;
    }
}