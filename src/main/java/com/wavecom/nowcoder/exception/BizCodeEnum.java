package com.wavecom.nowcoder.exception;

/**
 * @Author liujilong
 * @Project nowcoder-backend
 * @File BizCodeEnum
 * @Date 2022/5/23 4:01 下午
 **/
public enum BizCodeEnum {
    UNKNOWN_EXCEPTION(40000, "系统未知异常"),
    VALID_EXCEPTION(40303, "参数格式校验失败");

    private int code;
    private String msg;
    BizCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
