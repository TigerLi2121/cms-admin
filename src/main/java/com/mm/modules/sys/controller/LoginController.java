package com.mm.modules.sys.controller;

import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mm.common.annotation.RestPathController;
import com.mm.common.util.Assert;
import com.mm.common.util.Constant;
import com.mm.common.util.R;
import com.mm.common.util.UserUtil;
import com.mm.modules.sys.dto.LoginDTO;
import com.mm.modules.sys.entity.UserEntity;
import com.mm.modules.sys.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录
 *
 * @author lwl
 */
@RestPathController("/login")
public class LoginController {

    @Autowired
    private UserService userService;

    /**
     * 登录
     *
     * @param dto
     * @return
     */
    @PostMapping
    public R login(@Valid @RequestBody LoginDTO dto) {
        LambdaQueryWrapper<UserEntity> qw = new LambdaQueryWrapper<>();
        qw.eq(UserEntity::getUsername, dto.getUsername());
        qw.eq(UserEntity::getPassword, SecureUtil.md5(dto.getPassword()));
        qw.last(Constant.LIMIT_1);
        UserEntity user = userService.getOne(qw);
        Assert.isNull(user, "用户名或密码错误");
        Map<String, Object> data = new HashMap<>();
        data.put("token", UserUtil.getToken(user.getId()));
        return R.ok(data);
    }

}
