package com.shengfei.client.exception;

public class SHAException extends Exception {
    private static final long serialVersionUID = 1L;
    private String message;

    public SHAException(String message) {
        this.message = message;
    }

    public SHAException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getMessage() {
        return this.message;
    }
}
