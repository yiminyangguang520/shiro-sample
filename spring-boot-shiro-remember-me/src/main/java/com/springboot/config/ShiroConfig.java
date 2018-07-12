package com.springboot.config;

import com.springboot.shiro.ShiroRealm;
import java.util.LinkedHashMap;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author litz-a
 */
@Configuration
public class ShiroConfig {

  /**
   * ShiroFilterFactoryBean 处理拦截资源文件问题。
   * 注意：单独一个ShiroFilterFactoryBean配置是或报错的，因为在
   * 初始化ShiroFilterFactoryBean的时候需要注入：SecurityManager
   * Filter Chain定义说明 1、一个URL可以配置多个Filter，使用逗号分隔 2、当设置多个过滤器时，全部验证通过，才视为通过
   *
   * 部分过滤器可指定参数，如perms，roles
   * @param securityManager
   * @return
   */
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

  /**
   * cookie对象
   */
  public SimpleCookie rememberMeCookie() {
    // 设置cookie名称，对应login.html页面的<input type="checkbox" name="rememberMe"/>
    SimpleCookie cookie = new SimpleCookie("rememberMe");
    // 设置cookie的过期时间，单位为秒，这里为一天
    cookie.setMaxAge(86400);
    return cookie;
  }

  /**
   * cookie管理对象
   */
  public CookieRememberMeManager rememberMeManager() {
    CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
    cookieRememberMeManager.setCookie(rememberMeCookie());
    // rememberMe cookie加密的密钥
    cookieRememberMeManager.setCipherKey(Base64.decode("3AvVhmFLUs0KTA3Kprsdag=="));
    return cookieRememberMeManager;
  }

}
