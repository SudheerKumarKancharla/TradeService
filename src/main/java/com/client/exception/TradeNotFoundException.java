package com.client.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception class to handle Trade Not found Runt time Exception
 * with status Code 404
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class TradeNotFoundException extends RuntimeException {

    public TradeNotFoundException(String msg) {
        super(msg);
    }
}
