package com.springboot.config;

import org.crazycake.shiro.RedisManager;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author min
 */
@ConfigurationProperties(prefix = "spring.redis")
public class CustomRedisManager extends RedisManager {

}