package com.tr.vue.common.advice;

import com.tr.vue.common.exception.BusinessException;
import com.tr.vue.common.result.Result;
import com.tr.vue.common.result.ResultEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 统一异常处理
 *
 * @author TR
 * @date 2022/6/25 上午8:56
 */
@Slf4j
@RestControllerAdvice
public class ControllerExceptionAdvice {

    @ExceptionHandler(BusinessException.class)
    public Result handle(BusinessException businessException) {
        return Result.fail(businessException.getCode(), businessException.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public Result handle() {
        return Result.fail(ResultEnum.WRONG_PASSWORD);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result handle(MethodArgumentNotValidException exception) {
        return Result.fail(exception.getFieldError().getField() + exception.getFieldError().getDefaultMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result handle(Exception e) {
        log.error("[系统异常]", e);
        return Result.fail(ResultEnum.UNKNOWN_ERROR.getCode(), e.getMessage());
    }

}
