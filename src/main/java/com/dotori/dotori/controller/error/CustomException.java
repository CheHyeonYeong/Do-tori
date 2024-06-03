package com.dotori.dotori.controller.error;

public class CustomException extends RuntimeException {
    private int statusCode;

    public CustomException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}