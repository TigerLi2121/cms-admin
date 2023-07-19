package com.mm.modules.sys.controller;

import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mm.common.annotation.RestPathController;
import com.mm.common.annotation.SysLog;
import com.mm.common.util.R;
import com.mm.modules.sys.entity.RoleEntity;
import com.mm.modules.sys.entity.RoleMenuEntity;
import com.mm.modules.sys.service.RoleMenuService;
import com.mm.modules.sys.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 角色管理
 *
 * @author lwl
 */
@Validated
@RestPathController("/api/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleMenuService roleMenuService;

    /**
     * 角色列表
     */
    @GetMapping
    public R.Page<List<RoleEntity>> list(Integer page, Integer limit) {
        if (Objects.isNull(page) || Objects.isNull(limit)) {
            List<RoleEntity> list = roleService.list();
            return R.ok(list, list.size());
        }
        Page<RoleEntity> iPage = roleService.page(new Page<>(page, limit));
        if (iPage.getTotal() > 0) {
            List<Long> ids = iPage.getRecords().stream().map(RoleEntity::getId).collect(Collectors.toList());
            List<RoleMenuEntity> rms = roleMenuService.list(Wrappers.<RoleMenuEntity>lambdaQuery()
                    .in(RoleMenuEntity::getRoleId, ids));
            for (RoleEntity r : iPage.getRecords()) {
                List<Long> menuIds = rms.stream().filter(e -> NumberUtil.equals(e.getRoleId(), r.getId()))
                        .map(RoleMenuEntity::getMenuId).collect(Collectors.toList());
                r.setMenuIds(menuIds);
            }
        }
        return R.ok(iPage);
    }

    /**
     * 保存角色
     */
    @SysLog("保存角色")
    @PostMapping
    public R post(@RequestBody RoleEntity role) {
        roleService.saveOrUpdate(role);
        return R.ok();
    }

    /**
     * 删除角色
     */
    @SysLog("删除角色")
    @DeleteMapping
    public R del(@RequestBody List<Long> ids) {
        roleService.removeBatchByIds(ids);
        return R.ok();
    }
}
