package com.tdj.spring.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class RedisUtil {

    private static Logger logger = LoggerFactory.getLogger(RedisUtil.class);

    private static RedisTemplate redisTemplate;

//    private final static DefaultRedisScript<Long> LOCK_LUA_SCRIPT = new DefaultRedisScript<>(
//            "if redis.call(\"setnx\", KEYS[1], ARGV[1]) == 1 then return redis.call(\"pexpire\", KEYS[1], ARGV[2]) else return 0 end"
//            , Long.class);

    private final static DefaultRedisScript<Long> LOCK_LUA_SCRIPT = new DefaultRedisScript<>(
            "if redis.call(\"setnx\", KEYS[1], ARGV[1]) == 1 then return redis.call(\"pexpire\", KEYS[1], ARGV[2]) else return 0 end"
            , Long.class);

    private final static DefaultRedisScript<Long> UNLOCK_LUA_SCRIPT = new DefaultRedisScript<>(
            "if redis.call(\"get\",KEYS[1]) == ARGV[1] then return redis.call(\"del\",KEYS[1]) else return -1 end"
            , Long.class);

    private final static DefaultRedisScript<Long> RENEW_LUA_SCRIPT = new DefaultRedisScript<>(
            "if redis.call(\"get\",KEYS[1]) == ARGV[1] then return redis.call(\"pexpire\", KEYS[1], ARGV[2]) else return -1 end"
            , Long.class);

    private final static DefaultRedisScript<Long> GET_LUA_SCRIPT = new DefaultRedisScript<>(
            "return redis.call(\"get\",KEYS[1]) == ARGV[1]", Long.class);
    public static long defaultTTL = 300000;

    @Autowired
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        RedisUtil.redisTemplate = redisTemplate;
        System.out.println("11");
    }

    public static boolean lock(String key, String value, long ttl) {
        logger.info("lock redisKey = {} , redisValue = {}, ttl = {}", key, value, ttl);
        List<String> keys = Arrays.asList(key);
        Long result = (Long) redisTemplate.execute(LOCK_LUA_SCRIPT, keys, value, String.valueOf(ttl));
        if (result != null && result == 1) {
            logger.info("lock success, key:{} , value:{}", key, value);
            return true;
        } else {
            logger.error("lock failed, key:{} , value:{}", key, value);
            return false;
        }
    }

    public static boolean unlock(String key, String value) {
        logger.info("unlock redisKey = {} , redisValue = {}", key, value);
        List<String> keys = Arrays.asList(key);
        Long result = (Long) redisTemplate.execute(UNLOCK_LUA_SCRIPT, keys, value);
        if (result != null && result == 1) {
            logger.info("unlock success, key:{} , value:{}", key, value);
            return true;
        } else if (result != null && result == -1) {
            logger.info("value mismatch, key:{} , value:{}", key, value);
        } else {
            logger.error("unlock failed, key:{} , value:{}", key, value);
        }
        return false;
    }


    public static boolean renew(String key, String value, long ttl) {
        logger.info("renew redisKey = {} , redisValue = {}, ttl = {}", key, value, ttl);
        List<String> keys = Arrays.asList(key);
        Long result = (Long) redisTemplate.execute(RENEW_LUA_SCRIPT, keys, value, String.valueOf(ttl));
        if (result != null && result == 1) {
            logger.info("renew success, key:{} , value:{}", key, value);
            return true;
        } else if (result != null && result == -1) {
            logger.info("value mismatch, key:{} , value:{}", key, value);
        } else {
            logger.error("renew failed, key:{} , value:{}", key, value);
        }
        return false;
    }

    public static boolean checkLock(String key, String value) {
        logger.info("get redisKey = {} , redisValue = {}", key, value);
        List<String> keys = Arrays.asList(key);
        Long result = (Long) redisTemplate.execute(GET_LUA_SCRIPT, keys, value);
        if (result != null && result == 1) {
            logger.info("check success, key:{} , value:{}", key, value);
            return true;
        } else {
            logger.error("check failed, key:{} , value:{}", key, value);
        }
        return false;
    }
}
