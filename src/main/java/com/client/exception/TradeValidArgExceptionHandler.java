package com.client.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * ExceptionHandler class to handle the response for
 * validation error for the incoming requests
 * 
 */
@ControllerAdvice
public class TradeValidArgExceptionHandler extends ResponseEntityExceptionHandler {
        public static final String DEFAULT_ERROR_MESSAGE = "Invalid Input.";

        @Override
        protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                        HttpHeaders headers, HttpStatus status, WebRequest request) {
                ErrorModel error = new ErrorModel(HttpStatus.BAD_REQUEST,
                                ex.getBindingResult().getFieldError().getField()
                                                + " " + ex.getBindingResult().getFieldError().getDefaultMessage());

                return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        @Override
        protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                        HttpHeaders headers, HttpStatus status, WebRequest request) {
                ErrorModel error = new ErrorModel(HttpStatus.BAD_REQUEST, ex.getMessage());

                return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
}
