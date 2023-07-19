package com.mm.common.util;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Redis工具类
 *
 * @author lwl
 */
@Slf4j
@Component
public class RedisUtil {

    /**
     * 默认过期时间一天
     */
    public static final Long DEFAULT_EXPIRE = 86400L;
    public static RedisTemplate redisTemplate;

    public RedisUtil(RedisTemplate redisTemplate) {
        RedisUtil.redisTemplate = redisTemplate;
    }

    /**
     * 批量删除对应的value
     *
     * @param keys
     */
    public static void remove(String... keys) {
        for (String key : keys) {
            remove(key);
        }
    }

    /**
     * 批量删除key
     *
     * @param pattern
     */
    public static void removePattern(String pattern) {
        Set<String> keys = redisTemplate.keys(pattern);
        if (keys.size() > 0) {
            redisTemplate.delete(keys);
        }
    }

    /**
     * 删除对应的value
     *
     * @param key
     */
    public static boolean remove(String key) {
        return redisTemplate.delete(key);
    }

    /**
     * 判断缓存中是否有对应的value
     *
     * @param key
     * @return
     */
    public static boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 读取缓存
     *
     * @param key
     * @return
     */
    public static Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    public static boolean set(final String key, Object value) {
        return set(key, value, null);
    }

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @param expireTime 单位秒
     * @return
     */
    public static boolean set(final String key, Object value, Long expireTime) {
        try {
            if (Objects.isNull(expireTime)) {
                redisTemplate.opsForValue().set(key, value);
            } else {
                redisTemplate.opsForValue().set(key, value, Duration.ofSeconds(expireTime));
            }
            return true;
        } catch (Exception e) {
            log.error("set cache error", e);
        }
        return false;
    }

    /**
     * 写入hash
     *
     * @param key
     * @param hk
     * @param hv
     * @return
     */
    public static boolean hashSet(String key, String hk, Object hv) {
        try {
            redisTemplate.opsForHash().put(key, hk, hv);
            return true;
        } catch (Exception e) {
            log.error("hashSet cache error", e);
        }
        return false;
    }

    /**
     * 获取hash
     *
     * @param key
     * @param hk
     * @return
     */
    public static Object hashGet(String key, String hk) {
        return redisTemplate.opsForHash().get(key, hk);
    }

    /**
     * 获取hash列表
     *
     * @param key
     * @return
     */
    public static Set hashKeyList(String key) {
        return redisTemplate.opsForHash().keys(key);
    }

    /**
     * 写入list
     *
     * @param key
     * @param obj
     */
    public static void addList(String key, Object obj) {
        redisTemplate.opsForList().rightPush(key, obj);
    }

    /**
     * 写入list
     *
     * @param key
     * @param objs
     */
    public static void addLists(String key, List objs) {
        final RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
        redisTemplate.executePipelined((RedisCallback<Object>) redisConnection -> {
            redisConnection.openPipeline();
            byte[][] bytes = new byte[objs.size()][];
            for (int i = 0; i < objs.size(); i++) {
                bytes[i] = serializer.serialize(JSONUtil.toJsonStr(objs.get(i)));
            }
            redisConnection.lPush(serializer.serialize(key), bytes);
            redisConnection.closePipeline();
            return null;
        });
        log.info("redis addLists is done");
    }

    /**
     * 分页获取list
     *
     * @param key
     * @param clazz
     * @param start
     * @param end
     * @return
     */
    public static List getListPage(String key, Class clazz, int start, int end) {
        List<Object> result = new ArrayList<>();
        List<String> list = redisTemplate.opsForList().range(key, start, end);
        for (String str : list) {
            Object obj = JSONUtil.toBean(str, clazz);
            result.add(obj);
        }
        return result;
    }

    /**
     * 获取list大小
     *
     * @param key
     * @return
     */
    public static Long getListSize(String key) {
        return redisTemplate.opsForList().size(key);
    }
}
