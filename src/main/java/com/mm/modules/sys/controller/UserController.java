package com.mm.modules.sys.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mm.common.annotation.RestPathController;
import com.mm.common.annotation.SysLog;
import com.mm.common.util.Assert;
import com.mm.common.util.R;
import com.mm.common.util.UserUtil;
import com.mm.modules.sys.dto.MenuDTO;
import com.mm.modules.sys.entity.MenuEntity;
import com.mm.modules.sys.entity.UserEntity;
import com.mm.modules.sys.entity.UserRoleEntity;
import com.mm.modules.sys.service.MenuService;
import com.mm.modules.sys.service.UserRoleService;
import com.mm.modules.sys.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 系统用户
 *
 * @author lwl
 */
@Validated
@RestPathController("/api/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private MenuService menuService;

    /**
     * 当前用户信息
     *
     * @return
     */
    @GetMapping("/current")
    public R<UserEntity> current() {
        UserEntity user = userService.getById(UserUtil.getUserId());
        // 获取用户菜单
        List<MenuEntity> menus = menuService.listByUserId(user.getId());
        if (CollUtil.isNotEmpty(menus)) {
            // 获取授权
            List<String> perms = menus.stream().map(MenuEntity::getPerms)
                    .filter(StrUtil::isNotBlank).collect(Collectors.toList());
            user.setPerms(perms);
            List<MenuDTO> pm = menus.stream().filter(e -> NumberUtil.equals(e.getPid(), 0L))
                    .map(MenuDTO::convert).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(pm)) {
                user.setMenus(MenuDTO.getMenus(menus, pm));
            }
        }
        return R.ok(user);
    }

    /**
     * 列表
     */
    @GetMapping
    public R.Page<List<UserEntity>> list(Integer page, Integer limit, String username) {
        LambdaQueryWrapper<UserEntity> qw = new LambdaQueryWrapper<>();
        qw.like(StrUtil.isNotBlank(username), UserEntity::getUsername, username);
        Page<UserEntity> iPage = userService.page(new Page<>(page, limit), qw);
        if (iPage.getTotal() > 0) {
            List<Long> ids = iPage.getRecords().stream().map(UserEntity::getId).collect(Collectors.toList());
            List<UserRoleEntity> urs = userRoleService.list(Wrappers.<UserRoleEntity>lambdaQuery()
                    .in(UserRoleEntity::getUserId, ids));
            for (UserEntity u : iPage.getRecords()) {
                //获取用户所属的角色列表
                if (!urs.isEmpty()) {
                    u.setRoleIds(urs.stream().filter(e -> NumberUtil.equals(u.getId(), e.getUserId()))
                            .map(e -> e.getRoleId()).collect(Collectors.toList()));
                }
            }
        }
        return R.ok(iPage);
    }

    /**
     * 保存修改用户
     */
    @SysLog("用户保存修改")
    @PostMapping
    public R post(@Valid @RequestBody UserEntity entity) {
        if (Objects.isNull(entity.getId())) {
            Assert.isBlank(entity.getPassword(), "密码不能为空");
        }
        if (StringUtils.isNotBlank(entity.getPassword())) {
            entity.setPassword(SecureUtil.md5(entity.getPassword()));
        }
        userService.saveOrUpdate(entity);
        return R.ok();
    }

    /**
     * 删除用户
     */
    @SysLog("删除用户")
    @DeleteMapping
    public R del(@RequestBody List<Long> ids) {
        if (ids.stream().anyMatch(e -> Arrays.asList(1L, UserUtil.getUserId()).contains(e))) {
            return R.error("用户不能删除");
        }
        userService.removeByIds(ids);
        return R.ok();
    }
}
