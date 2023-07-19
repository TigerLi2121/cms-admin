package com.mm.modules.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mm.modules.sys.dao.UserRoleDao;
import com.mm.modules.sys.entity.UserRoleEntity;
import com.mm.modules.sys.service.UserRoleService;
import org.springframework.stereotype.Service;


/**
 * 用户与角色对应关系
 *
 * @author lwl
 */
@Service("userRoleService")
public class UserRoleServiceImpl extends ServiceImpl<UserRoleDao, UserRoleEntity> implements UserRoleService {

}
