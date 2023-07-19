package com.mm.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mm.modules.sys.entity.UserRoleEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户与角色对应关系
 *
 * @author lwl
 */
@Mapper
public interface UserRoleDao extends BaseMapper<UserRoleEntity> {
}
