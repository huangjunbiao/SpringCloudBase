package com.huang.cloudbase.learn.cache;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * cn.hutool.cache.impl.TimedCache的基本使用样例，参考gateway中token验证对jwt的缓存初始化
 *
 * @author huangjunbiao_cdv
 */
@Component
public class TimedCacheExample implements InitializingBean {

    private final String GATEWAY_USER_MANAGEMENT_JWT = "gateway:user:management:jwt";

    private TimedCache<String, String> _timedCache;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void echo() {
        this.redisTemplate.opsForZSet().add(GATEWAY_USER_MANAGEMENT_JWT, "111", 20 * 1000);
        this._timedCache.put(GATEWAY_USER_MANAGEMENT_JWT, "111", 10 * 1000);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 创建定时缓存
        this._timedCache = CacheUtil.newTimedCache(0);
        //加载缓存中未过期的jwtid
        Cursor<ZSetOperations.TypedTuple<Object>> scan = this.redisTemplate.opsForZSet().scan(GATEWAY_USER_MANAGEMENT_JWT, ScanOptions.NONE);
        while (scan.hasNext()) {
            ZSetOperations.TypedTuple<Object> next = scan.next();
            long l = Objects.isNull(next.getScore()) ? 0 : next.getScore().longValue();
            if (l <= System.currentTimeMillis()) {
                //已过期
                this.redisTemplate.opsForZSet().remove(GATEWAY_USER_MANAGEMENT_JWT, next.getValue());
            } else {
                long timeout = l - System.currentTimeMillis();
                if (timeout > 0) {
                    this._timedCache.put((String) next.getValue(), null, timeout);
                }
            }
        }
        scan.close();
        _timedCache.setListener((key, cachedObject) -> this.redisTemplate.opsForZSet().remove(GATEWAY_USER_MANAGEMENT_JWT, key));
        // 定时清理缓存
        _timedCache.schedulePrune(3600 * 1000);
    }
}
