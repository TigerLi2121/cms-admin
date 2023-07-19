package com.mm.modules.sys.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 登录
 *
 * @author lwl
 */
@Data
public class LoginDTO {

    /**
     * 用户名
     */
    @NotBlank(message = "username is null")
    private String username;

    /**
     * 密码
     */
    @NotBlank(message = "password is null")
    private String password;
}
