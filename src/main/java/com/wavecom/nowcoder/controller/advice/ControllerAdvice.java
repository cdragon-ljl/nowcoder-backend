package com.wavecom.nowcoder.controller.advice;

import com.wavecom.nowcoder.exception.BizCodeEnum;
import com.wavecom.nowcoder.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author liujilong
 * @Project nowcoder-backend
 * @File ControllerAdvice
 * @Date 2022/5/23 5:27 下午
 **/
@Slf4j
@RestControllerAdvice("com.wavecom.nowcoder.controller")
public class ControllerAdvice {
    /**
     * JSR303校验异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result handleValidException(MethodArgumentNotValidException e) {
        log.error("数据校验出现问题{}，异常类型为{}", e.getMessage(), e.getClass());
        BindingResult bindingResult = e.getBindingResult();
        Map<String, String> map = new HashMap<>();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        fieldErrors.forEach(fieldError -> {
            map.put(fieldError.getField(), fieldError.getDefaultMessage());
        });
        return Result.error(BizCodeEnum.VALID_EXCEPTION.getCode(), BizCodeEnum.VALID_EXCEPTION.getMsg(), map);
    }

    /**
     * 全局异常
     * @param t
     * @return
     */
    @ExceptionHandler(value = Throwable.class)
    public Result handleValidException(Throwable t) {
        return Result.error(BizCodeEnum.UNKNOWN_EXCEPTION.getCode(), BizCodeEnum.UNKNOWN_EXCEPTION.getMsg());
    }
}
