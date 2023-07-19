package com.mm.modules.sys.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mm.modules.sys.dao.RoleDao;
import com.mm.modules.sys.entity.RoleEntity;
import com.mm.modules.sys.entity.RoleMenuEntity;
import com.mm.modules.sys.entity.UserRoleEntity;
import com.mm.modules.sys.service.RoleMenuService;
import com.mm.modules.sys.service.RoleService;
import com.mm.modules.sys.service.UserRoleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * 角色
 *
 * @author lwl
 */
@Service("roleService")
@AllArgsConstructor
public class RoleServiceImpl extends ServiceImpl<RoleDao, RoleEntity> implements RoleService {

    final RoleMenuService roleMenuService;
    final UserRoleService userRoleService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveOrUpdate(RoleEntity role) {
        if (Objects.nonNull(role.getId())) {
            roleMenuService.remove(Wrappers.<RoleMenuEntity>lambdaQuery().eq(RoleMenuEntity::getRoleId, role.getId()));
        }
        super.saveOrUpdate(role);
        if (CollUtil.isNotEmpty(role.getMenuIds())) {
            List<RoleMenuEntity> rms = role.getMenuIds().stream()
                    .map(e -> RoleMenuEntity.builder().roleId(role.getId()).menuId(e).build())
                    .collect(Collectors.toList());
            roleMenuService.saveBatch(rms);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByIds(Collection<?> ids) {
        //删除角色
        super.removeByIds(ids);

        //删除角色与菜单关联
        roleMenuService.remove(Wrappers.<RoleMenuEntity>lambdaQuery()
                .in(RoleMenuEntity::getRoleId, ids));

        //删除角色与用户关联
        userRoleService.remove(Wrappers.<UserRoleEntity>lambdaQuery()
                .in(UserRoleEntity::getRoleId, ids));
        return true;
    }


}
