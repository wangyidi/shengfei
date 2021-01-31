package com.shengfei.exception;


import com.shengfei.shiro.vo.ResultVO;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionHandle {

    @ExceptionHandler(value = ServiceException.class)
    @ResponseBody
    public ResultVO handleServiceException(ServiceException e) {
        return ResultVO.systemError(e.getMessage());
    }

    @ExceptionHandler(BindException.class)
    @ResponseBody
    public ResultVO handle(BindException e) {
        StringBuilder errorInfo = new StringBuilder();
        String msg = e.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining("，"));
        return ResultVO.systemError(msg);
    }

}
