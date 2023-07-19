package com.mm.common.interceptor;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.mm.common.util.Constant;
import com.mm.common.util.R;
import com.mm.common.util.RedisUtil;
import com.mm.common.util.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;

/**
 * @author lwl
 */
@Slf4j
public class JwtInterceptor implements HandlerInterceptor {

    /**
     * token前缀
     */
    public static final String AUTH_KEY = "xtoken";

    public final List<String> EXCLUDE_URL = Collections.singletonList("/login");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        if (EXCLUDE_URL.contains(uri)) {
            return true;
        }
        String token = request.getHeader(AUTH_KEY);
        log.info("xtoken:{}", token);
        if (StrUtil.isBlank(token)) {
            fail(response, "请登录");
            return false;
        }
        // 校验token是否过期
        if (!RedisUtil.exists(Constant.USER_TOKEN + token)) {
            fail(response, "token过期，请登录");
            return false;
        }
        Long userId = UserUtil.getUserId(token);
        request.setAttribute(Constant.USER_ID, userId);
        return true;
    }

    /**
     * 错误返回
     *
     * @param resp
     * @param msg
     * @throws IOException
     */
    private void fail(HttpServletResponse resp, String msg) throws IOException {
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("application/json; charset=utf-8");
        resp.setStatus(401);
        PrintWriter writer = resp.getWriter();
        writer.write(JSONUtil.toJsonStr(R.code(886, StrUtil.isBlank(msg) ? "登录失败" : msg)));
    }
}
