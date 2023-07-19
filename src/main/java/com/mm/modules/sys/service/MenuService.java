package com.mm.modules.sys.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.mm.modules.sys.entity.MenuEntity;

import java.util.List;


/**
 * 菜单管理
 *
 * @author lwl
 */
public interface MenuService extends IService<MenuEntity> {

    /**
     * 获取用户菜单列表
     */
    List<MenuEntity> listByUserId(Long userId);

}
