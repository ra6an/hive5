package com.hive5.hive5.utils;

import lombok.Getter;

@Getter
public class ErrorResponse {
    private String error;
    private String message;
    private int statusCode;

    public ErrorResponse(String error, String message, int statusCode) {
        this.error = error;
        this.message = message;
        this.statusCode = statusCode;
    }
}
