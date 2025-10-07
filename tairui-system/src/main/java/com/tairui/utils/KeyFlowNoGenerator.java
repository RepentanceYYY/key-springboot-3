package com.tairui.utils;

import com.tairui.common.core.redis.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 钥匙申请审批唯一流程单生成
 */
@Component
public class KeyFlowNoGenerator {
    @Autowired
    private RedisCache redisCache;

    public String generateFlowNo() {
        // 时间戳到秒
        String timeStr = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        // Redis key: 按秒做 key，避免重复
        String redisKey = "key_flow_no:" + timeStr;
        // Redis 自增
        Long seq = redisCache.redisTemplate.opsForValue().increment(redisKey);
        // 设置过期 2 秒，防止 key 无限增长
        if (seq == 1) {
            redisCache.redisTemplate.expire(redisKey, java.time.Duration.ofSeconds(2));
        }
        // 格式化序号为 3 位，不够补 0
        String seqStr = String.format("%03d", seq);
        return "AP" + timeStr + seqStr;
    }
}
