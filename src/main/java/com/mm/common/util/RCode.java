package com.mm.common.util;

import lombok.Getter;

/**
 * 错误码
 *
 * @author lwl
 */
@Getter
public enum RCode implements ICode {
    SUCCESS(0, "success"),

    // 1xx 参数校验错误
    PARAM_FAILED(100, "参数错误"),

    // 签名错误
    SIGNATURE_CHECK_FAILED(110, "签名验证失败"),

    // 5xx 服务异常错误
    SYSTEM_ERR(500, "未知异常，请联系管理员");

    private Integer code;

    private String msg;

    RCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
