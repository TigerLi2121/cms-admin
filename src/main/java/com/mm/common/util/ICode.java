package com.mm.common.util;

/**
 * 错误码接口
 *
 * @author lwl
 */
public interface ICode {

    /**
     * 错误码
     *
     * @return
     */
    Integer getCode();

    /**
     * 错误消息
     *
     * @return
     */
    String getMsg();
}
