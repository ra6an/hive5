package com.hive5.hive5.exception;

public class CustomException extends RuntimeException {
    private final String error;
    private final String message;
    private final int statusCode;

    public CustomException(String error, String message, int statusCode) {
        this.error = error;
        this.message = message;
        this.statusCode = statusCode;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
