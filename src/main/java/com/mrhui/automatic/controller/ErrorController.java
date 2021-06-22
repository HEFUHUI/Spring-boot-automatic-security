package com.mrhui.automatic.controller;

import com.mrhui.automatic.exception.ParamsException;
import com.mrhui.automatic.pojo.StandardResult;
import com.mrhui.automatic.exception.DatabaseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestControllerAdvice
public class ErrorController {
    @ExceptionHandler(DatabaseException.class)
    public StandardResult<String> customException(Exception e) {
        return StandardResult.failed(e.getMessage(),501);
    }

    @ExceptionHandler(ParamsException.class)
    public StandardResult<String> paramsException(HttpServletResponse response,Exception e){
        response.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
        return StandardResult.failed(e.getMessage());
    }
}
