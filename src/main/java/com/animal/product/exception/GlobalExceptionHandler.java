package com.animal.product.exception;

import com.animal.product.common.BaseResponse;
import com.animal.product.common.ErrorCode;
import com.animal.product.common.ResultUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author 咏鹅
 * @version 1.0
 * @description 全局异常
 * @date 2023/5/30 18:55
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public BaseResponse businessExceptionHandler(BusinessException e){
        return ResultUtils.error(e.getCode(),e.getMessage(),e.getDescription());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse runtimeExceptionHandler(BusinessException e){
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR,e.getMessage(),e.getDescription());
    }
}
