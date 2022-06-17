package com.client.exception;

/**
 * Exception class to handle the creation exception
 */
public class TradeCreationException extends Exception {
    public static final String DEFAULT_ERROR_MESSAGE = "Trade Creation Failed.";

    /**
     * Constructs a Exception with no detail message.
     */
    public TradeCreationException() {
        super();
    }

    /**
     * Constructs a Exception with sepcified message.
     * 
     * @param msg the detail message.
     */
    public TradeCreationException(String msg) {
        super(msg);
    }

    /**
     * Constructs a Exception from an existed Exception
     * 
     * @param ex
     */
    public TradeCreationException(Exception ex) {
        super(ex);
    }
}
