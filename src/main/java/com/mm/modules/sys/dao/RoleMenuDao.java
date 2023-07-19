package com.mm.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mm.modules.sys.entity.RoleMenuEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 角色与菜单对应关系
 *
 * @author lwl
 */
@Mapper
public interface RoleMenuDao extends BaseMapper<RoleMenuEntity> {

}
