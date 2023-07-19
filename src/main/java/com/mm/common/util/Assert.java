package com.mm.common.util;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.mm.common.exception.GException;

/**
 * 数据校验
 *
 * @author lwl
 */
public class Assert {

    /**
     * 判断布尔值返回错误消息
     *
     * @param bool
     * @param message
     */
    public static void bool(Boolean bool, String message) {
        if (bool) {
            throw new GException(message);
        }
    }

    /**
     * 判断布尔值返回错误枚举
     *
     * @param bool
     * @param rCode
     */
    public static void bool(Boolean bool, RCode rCode) {
        if (bool) {
            throw new GException(rCode);
        }
    }

    /**
     * 判断是否为空字符串返回错误消息
     *
     * @param str
     * @param message
     */
    public static void isBlank(String str, String message) {
        if (StrUtil.isBlank(str)) {
            throw new GException(message);
        }
    }

    /**
     * 判断是否为空字符串返回错误枚举
     *
     * @param str
     * @param rCode
     */
    public static void isBlank(String str, RCode rCode) {
        if (StrUtil.isBlank(str)) {
            throw new GException(rCode);
        }
    }

    /**
     * 判断是否为空返回错误消息
     *
     * @param object
     * @param message
     */
    public static void isNull(Object object, String message) {
        if (ObjectUtil.isEmpty(object)) {
            throw new GException(message);
        }
    }

    /**
     * 判断是否为空返回错误枚举
     *
     * @param object
     * @param rCode
     */
    public static void isNull(Object object, RCode rCode) {
        if (ObjectUtil.isEmpty(object)) {
            throw new GException(rCode);
        }
    }
}
