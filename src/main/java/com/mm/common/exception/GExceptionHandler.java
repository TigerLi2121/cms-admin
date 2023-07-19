package com.mm.common.exception;

import com.mm.common.util.R;
import com.mm.common.util.RCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

/**
 * 异常处理器
 *
 * @author lwl
 */
@Slf4j
@RestControllerAdvice
public class GExceptionHandler {

    @ExceptionHandler(BindException.class)
    public R handleException(BindException e) {
        String msg = e.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        return R.code(RCode.PARAM_FAILED, msg);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R handleException(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        return R.code(RCode.PARAM_FAILED, msg);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public R handleException(ConstraintViolationException e) {
        String msg = e.getConstraintViolations().stream().map(m -> m.getMessage()).collect(Collectors.toList()).get(0);
        return R.code(RCode.PARAM_FAILED, msg);
    }

    /**
     * 处理自定义异常
     */
    @ExceptionHandler(GException.class)
    public R handleRRException(GException e) {
        return new R(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public R handleException(Exception e) {
        log.error(e.getMessage(), e);
        return R.error();
    }
}
