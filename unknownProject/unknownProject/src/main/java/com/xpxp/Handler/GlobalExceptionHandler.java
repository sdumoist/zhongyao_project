package com.xpxp.Handler;

import com.xpxp.Constant.ErrorConstant;
import com.xpxp.Exception.BaseException;
import com.xpxp.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public Result<String> exceptionHandler(Exception e) {
        log.error("系统异常: ", e); // 记录完整堆栈
        return Result.error(ErrorConstant.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(BaseException.class)
    public Result<String> exceptionHandler(BaseException e) {
        log.error("业务异常: {}", e.getMessage());
        return Result.error(e.getErrorConstant(), e.getMessage());
    }
}
