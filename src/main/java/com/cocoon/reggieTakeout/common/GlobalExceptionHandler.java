package com.cocoon.reggieTakeout.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/** 全局异常响应处理 **/
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    /** 捕获SQL语句异常 **/
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public GlobalResult<String> exceptionHandler(SQLIntegrityConstraintViolationException sqlException) {
        String exceptionMsg = sqlException.getMessage();
        log.error(exceptionMsg);

        // 处理“条目存在”SQL语句异常
        if (exceptionMsg.contains("Duplicate entry")) {
            String[] splitStr = exceptionMsg.split(" ");
            return GlobalResult.error(splitStr[2] + "已存在");
        }

        // 未知异常统一返回系统接口异常
        return GlobalResult.error("系统接口异常");
    }

    /** 捕获自定义业务异常 **/
    @ExceptionHandler(CustomException.class)
    public GlobalResult<String> exceptionHandler(CustomException customException) {
        String exceptionMsg = customException.getMessage();
        log.error(exceptionMsg);
        return GlobalResult.error(exceptionMsg);
    }
}
