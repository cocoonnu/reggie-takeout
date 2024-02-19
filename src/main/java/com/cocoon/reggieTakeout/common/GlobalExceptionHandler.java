package com.cocoon.reggieTakeout.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/** 全局异常请求处理 **/
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public GlobalResult<String> exceptionHandler(SQLIntegrityConstraintViolationException exception) {
        String exceptionMsg = exception.getMessage();
        log.error(exceptionMsg);

        // 处理“条目存在”SQL语句异常
        if (exceptionMsg.contains("Duplicate entry")) {
            String[] splitStr = exceptionMsg.split(" ");
            return GlobalResult.error(splitStr[2] + "已存在");
        }

        // 未知异常统一返回系统接口异常
        return GlobalResult.error("系统接口异常");
    }
}
