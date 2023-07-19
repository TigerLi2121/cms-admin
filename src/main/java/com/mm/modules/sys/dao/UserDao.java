package com.mm.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mm.modules.sys.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统用户
 *
 * @author lwl
 */
@Mapper
public interface UserDao extends BaseMapper<UserEntity> {

}
