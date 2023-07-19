package com.mm.common.util;

import cn.hutool.core.convert.Convert;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

import static com.mm.common.util.Constant.USER_ID;

/**
 * 用户信息
 *
 * @author lwl
 */
@Component
public class UserUtil {

    private static HttpServletRequest request;

    public UserUtil(HttpServletRequest request) {
        UserUtil.request = request;
    }

    public static String getToken(Long userId) {
        Map<String, Object> payload = new HashMap<>();
        payload.put(USER_ID, userId);
        String token = JWTUtil.createToken(payload, "jwt".getBytes());
        RedisUtil.set(Constant.USER_TOKEN + token, Convert.toStr(userId), RedisUtil.DEFAULT_EXPIRE);
        return token;
    }

    public static Long getUserId(String token) {
        JWT jwt = JWTUtil.parseToken(token);
        Long userId = Convert.toLong(jwt.getPayload(USER_ID));
        return userId;
    }


    public static Long getUserId() {
        Long userId = Convert.toLong(request.getAttribute(USER_ID));
        return userId;
    }
}
