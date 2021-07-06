package com.uade.api.exceptions;

public class InternalServerException extends RuntimeException {
    private String code;

    public InternalServerException() {
        super();
        this.code = "server_error";
    }

    public InternalServerException(String message) {
        super(message);
        this.code = "server_error";
    }

    public String getCode() {
        return code;
    }
}
