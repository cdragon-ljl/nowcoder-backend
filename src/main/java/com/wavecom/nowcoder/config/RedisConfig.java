package com.wavecom.nowcoder.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * @Author liujilong
 * @Project nowcoder-backend
 * @File RedisConfig
 * @Date 2022/5/27 3:48 PM
 **/
@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);

        //设置key的序列化方式
        redisTemplate.setKeySerializer(RedisSerializer.string());
        //设置value的序列化方式
        redisTemplate.setValueSerializer(RedisSerializer.json());
        //设置hash的key序列化方式
        redisTemplate.setHashKeySerializer(RedisSerializer.string());
        //设置hash的value序列化方式
        redisTemplate.setHashValueSerializer(RedisSerializer.json());

        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }
}
