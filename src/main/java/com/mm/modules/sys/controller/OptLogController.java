package com.mm.modules.sys.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mm.common.annotation.RestPathController;
import com.mm.common.util.R;
import com.mm.modules.sys.entity.OptLogEntity;
import com.mm.modules.sys.entity.UserEntity;
import com.mm.modules.sys.service.OptLogService;
import com.mm.modules.sys.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * 系统日志
 *
 * @author lwl
 */
@RestPathController("/opt_log")
public class OptLogController {
    @Autowired
    private OptLogService optLogService;
    @Autowired
    private UserService userService;

    /**
     * 列表
     */
    @GetMapping
    public R.Page<OptLogEntity> page(Integer page, Integer limit, String key) {
        LambdaQueryWrapper<OptLogEntity> qw = new LambdaQueryWrapper<>();
        qw.like(StrUtil.isNotBlank(key), OptLogEntity::getOperation, key);
        IPage<OptLogEntity> iPage = optLogService.page(new Page<>(page, limit), qw);
        if (CollUtil.isNotEmpty(iPage.getRecords())) {
            List<Long> userIds = iPage.getRecords().stream().map(OptLogEntity::getUserId).collect(Collectors.toList());
            List<UserEntity> users = userService.list(Wrappers.<UserEntity>lambdaQuery()
                    .in(UserEntity::getId, userIds));
            for (OptLogEntity sysLog : iPage.getRecords()) {
                Optional<UserEntity> user = users.stream()
                        .filter(e -> NumberUtil.equals(sysLog.getUserId(), e.getId())).findFirst();
                if (user.isPresent()) {
                    sysLog.setUsername(user.get().getUsername());
                }
            }
        }
        return R.ok(iPage);
    }

}
