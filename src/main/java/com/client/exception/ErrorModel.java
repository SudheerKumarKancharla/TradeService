package com.client.exception;

import org.springframework.http.HttpStatus;

/**
 * ErrorModel class to represent the validation errors
 */
public class ErrorModel {

    private HttpStatus httpStatus;

    private String message;

    public ErrorModel(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getMessage() {
        return message;
    }

}
