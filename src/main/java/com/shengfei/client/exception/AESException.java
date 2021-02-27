package com.shengfei.client.exception;

public class AESException extends Exception {
    private static final long serialVersionUID = 1L;
    private String message;

    public AESException(String message) {
        this.message = message;
    }

    public AESException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getMessage() {
        return this.message;
    }
}