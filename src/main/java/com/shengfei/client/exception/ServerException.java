package com.shengfei.client.exception;

public class ServerException extends Exception {
    private static final long serialVersionUID = 1L;
    private String message;

    public ServerException(String message) {
        this.message = message;
    }

    public ServerException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getMessage() {
        return this.message;
    }
}