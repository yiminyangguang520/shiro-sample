package com.springboot.config;

import com.springboot.shiro.ShiroRealm;
import java.util.LinkedHashMap;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

/**
 * @author litz-a
 */
@Configuration
public class ShiroConfig {

  @Bean
  public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
    ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
    shiroFilterFactoryBean.setSecurityManager(securityManager);
    shiroFilterFactoryBean.setLoginUrl("/login");
    shiroFilterFactoryBean.setSuccessUrl("/index");
    shiroFilterFactoryBean.setUnauthorizedUrl("/403");
    LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<>();

    filterChainDefinitionMap.put("/css/**", "anon");
    filterChainDefinitionMap.put("/js/**", "anon");
    filterChainDefinitionMap.put("/fonts/**", "anon");
    filterChainDefinitionMap.put("/img/**", "anon");
    filterChainDefinitionMap.put("/druid/**", "anon");
    filterChainDefinitionMap.put("/logout", "logout");
    filterChainDefinitionMap.put("/", "anon");
    filterChainDefinitionMap.put("/**", "user");

    shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

    return shiroFilterFactoryBean;
  }

  @Bean
  public SecurityManager securityManager() {
    DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
    securityManager.setRealm(shiroRealm());
    securityManager.setRememberMeManager(rememberMeManager());
    //在Shiro中加入缓存可以使权限相关操作尽可能快，避免频繁访问数据库获取权限信息，因为对于一个用户来说，其权限在短时间内基本是不会变化的
    securityManager.setCacheManager(cacheManager());
    return securityManager;
  }

  @Bean(name = "lifecycleBeanPostProcessor")
  public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
    return new LifecycleBeanPostProcessor();
  }

  @Bean
  public ShiroRealm shiroRealm() {
    ShiroRealm shiroRealm = new ShiroRealm();
    return shiroRealm;
  }

  public SimpleCookie rememberMeCookie() {
    SimpleCookie cookie = new SimpleCookie("rememberMe");
    cookie.setMaxAge(86400);
    return cookie;
  }

  public CookieRememberMeManager rememberMeManager() {
    CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
    cookieRememberMeManager.setCookie(rememberMeCookie());
    cookieRememberMeManager.setCipherKey(Base64.decode("4AvVhmFLUs0KTA3Kprsdag=="));
    return cookieRememberMeManager;
  }

  @Bean
  @DependsOn({"lifecycleBeanPostProcessor"})
  public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
    DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
    advisorAutoProxyCreator.setProxyTargetClass(true);
    return advisorAutoProxyCreator;
  }

  @Bean
  public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
    AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
    authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
    return authorizationAttributeSourceAdvisor;
  }

  /**
   * https://mrbird.cc/Spring-Boot-Shiro%20cache.html
   * @return
   */
  @Bean
  public RedisManager redisManager() {
    RedisManager redisManager = new RedisManager();
    return redisManager;
  }

  /**
   * redis 管理,加载自定义属性
   */
  @Bean
  public CustomRedisManager customRedisManager() {
    CustomRedisManager customRedisManager = new CustomRedisManager();
    return customRedisManager;
  }

  @Bean
  public RedisCacheManager cacheManager() {
    RedisCacheManager redisCacheManager = new RedisCacheManager();
    redisCacheManager.setRedisManager(customRedisManager());
    return redisCacheManager;
  }

}
