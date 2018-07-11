package com.springboot.config;

import org.crazycake.shiro.RedisManager;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author litz-a
 */
@ConfigurationProperties(prefix = "spring.redis")
public class CustomRedisManager extends RedisManager {

}