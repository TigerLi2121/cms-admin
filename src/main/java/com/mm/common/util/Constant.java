package com.mm.common.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 常量
 *
 * @author lwl
 */
public class Constant {
    /**
     * 查询最后一条数据
     */
    public static final String LIMIT_1 = "limit 1";
    /**
     * 超级管理员ID
     */
    public static final int SUPER_ADMIN = 1;

    /**
     * jwt user id key
     */
    public static final String USER_ID = "user_id";

    /**
     * jwt user token key
     */
    public static final String USER_TOKEN = "user_token:";
    /**
     * 验证码
     */
    public static final String CAPTCHA = "captcha";

    /**
     * 菜单类型
     */
    @Getter
    @AllArgsConstructor
    public enum MenuType {
        /**
         * 目录
         */
        CATALOG(0),
        /**
         * 菜单
         */
        MENU(1),
        /**
         * 按钮
         */
        BUTTON(2);

        private int value;
    }

}
