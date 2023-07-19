package com.mm.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mm.modules.sys.entity.RoleEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 角色管理
 *
 * @author lwl
 */
@Mapper
public interface RoleDao extends BaseMapper<RoleEntity> {

}
