package com.buddyapp.paymybuddy.exception;

public class ExceptionHandler extends RuntimeException{

    public ExceptionHandler(String message) {
        super(message);
    }

    public ExceptionHandler(String message, Throwable cause) {
        super(message, cause);
    }
}
