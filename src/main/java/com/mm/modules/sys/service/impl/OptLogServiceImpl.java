package com.mm.modules.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mm.modules.sys.dao.OptLogDao;
import com.mm.modules.sys.entity.OptLogEntity;
import com.mm.modules.sys.service.OptLogService;
import org.springframework.stereotype.Service;


/**
 * 系统日志
 *
 * @author lwl
 */
@Service("optLogService")
public class OptLogServiceImpl extends ServiceImpl<OptLogDao, OptLogEntity> implements OptLogService {

}
