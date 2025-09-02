package com.xpxp.Exception;

import com.xpxp.Constant.ErrorConstant;
import lombok.Getter;

/**
 * BaseException
 */
@Getter
public class BaseException extends RuntimeException {
    private final ErrorConstant errorConstant;

    public BaseException(String message) {
        super(message);
        this.errorConstant = ErrorConstant.INTERNAL_SERVER_ERROR; // 默认错误
    }

    public BaseException(ErrorConstant errorConstant) {
        super(errorConstant.getMessage());
        this.errorConstant = errorConstant;
    }

    public BaseException(ErrorConstant errorConstant, String customMessage) {
        super(customMessage);
        this.errorConstant = errorConstant;
    }

}