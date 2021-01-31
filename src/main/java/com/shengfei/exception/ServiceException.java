package com.shengfei.exception;

import lombok.Getter;

@Getter
public class ServiceException extends RuntimeException {

    Integer code;

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(Integer code,String message) {
        super(message);
        this.code = code;
    }
}
