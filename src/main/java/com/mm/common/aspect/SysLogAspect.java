package com.mm.common.aspect;

import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.json.JSONUtil;
import com.mm.common.annotation.SysLog;
import com.mm.common.util.UserUtil;
import com.mm.modules.sys.entity.OptLogEntity;
import com.mm.modules.sys.service.OptLogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * 系统日志，切面处理类
 *
 * @author lwl
 */
@Aspect
@Component
public class SysLogAspect {


    @Autowired
    private HttpServletRequest request;

    @Autowired
    private OptLogService optLogService;

    @Pointcut("@annotation(com.mm.common.annotation.SysLog)")
    public void logPointCut() {

    }

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        StopWatch sw = new StopWatch();
        sw.start();
        //执行方法
        Object result = point.proceed();
        //执行时长(毫秒)
        sw.stop();
        //保存日志
        saveSysLog(point, sw.getTotalTimeMillis());

        return result;
    }

    private void saveSysLog(ProceedingJoinPoint joinPoint, long time) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        OptLogEntity sysLog = new OptLogEntity();
        SysLog syslog = method.getAnnotation(SysLog.class);
        if (syslog != null) {
            //注解上的描述
            sysLog.setOperation(syslog.value());
        }

        //请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        sysLog.setMethod(className + "." + methodName + "()");

        //请求的参数
        Object[] args = joinPoint.getArgs();
        try {

            String params = JSONUtil.toJsonStr(args[0]);
            sysLog.setParams(params);
        } catch (Exception e) {

        }
        //设置IP地址
        sysLog.setIp(ServletUtil.getClientIP(request));
        //用户名
        sysLog.setUserId(UserUtil.getUserId());
        sysLog.setTime(time);
        //保存系统日志
        optLogService.save(sysLog);
    }
}
