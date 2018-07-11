package com.springboot.config;

import org.crazycake.shiro.RedisManager;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author litz-a
 */
//@Configuration
@ConfigurationProperties(prefix = "spring.redis")
public class CustomRedisManager extends RedisManager {

}
