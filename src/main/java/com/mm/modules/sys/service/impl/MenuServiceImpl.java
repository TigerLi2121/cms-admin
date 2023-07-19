package com.mm.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mm.modules.sys.dao.MenuDao;
import com.mm.modules.sys.entity.MenuEntity;
import com.mm.modules.sys.entity.RoleMenuEntity;
import com.mm.modules.sys.service.MenuService;
import com.mm.modules.sys.service.RoleMenuService;
import com.mm.modules.sys.service.UserRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;


/**
 * 菜单
 *
 * @author lwl
 */
@Service("menuService")
@RequiredArgsConstructor
public class MenuServiceImpl extends ServiceImpl<MenuDao, MenuEntity> implements MenuService {
    final UserRoleService userRoleService;
    final RoleMenuService roleMenuService;

    @Override
    public List<MenuEntity> listByUserId(Long userId) {
        return baseMapper.listByUserId(userId);
    }

    @Override
    public boolean removeBatchByIds(Collection<?> ids) {
        super.removeBatchByIds(ids);
        roleMenuService.remove(Wrappers.<RoleMenuEntity>lambdaQuery().in(RoleMenuEntity::getMenuId, ids));
        return true;
    }
}
