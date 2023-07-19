package com.mm.common.exception;

import com.mm.common.util.ICode;
import lombok.Getter;
import lombok.Setter;

/**
 * 自定义异常
 *
 * @author lwl
 */
@Getter
@Setter
public class GException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * 错误消息
     */
    private String msg;

    /**
     * 错误码
     */
    private int code = 500;

    public GException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public GException(ICode iCode) {
        super(iCode.getMsg());
        this.code = iCode.getCode();
        this.msg = iCode.getMsg();
    }

    public GException(String msg, Throwable e) {
        super(msg, e);
        this.msg = msg;
    }

    public GException(int code, String msg) {
        super(msg);
        this.msg = msg;
        this.code = code;
    }

    public GException(int code, String msg, Throwable e) {
        super(msg, e);
        this.msg = msg;
        this.code = code;
    }

}
