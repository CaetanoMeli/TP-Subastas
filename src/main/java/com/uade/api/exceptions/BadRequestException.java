package com.uade.api.exceptions;

public class BadRequestException extends RuntimeException {
    private String code;

    public BadRequestException(String message) {
        super(message);
        this.code = "bad_request";
    }

    public String getCode() {
        return code;
    }
}
