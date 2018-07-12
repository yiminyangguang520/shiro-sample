package com.springboot.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import com.springboot.listener.ShiroSessionListener;
import com.springboot.shiro.ShiroRealm;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.MemorySessionDAO;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
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
  public EhCacheManager getEhCacheManager() {
    EhCacheManager em = new EhCacheManager();
    em.setCacheManagerConfigFile("classpath:config/shiro-ehcache.xml");
    return em;
  }

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
    securityManager.setCacheManager(getEhCacheManager());
    securityManager.setSessionManager(sessionManager());
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

  /**
   *  开启shiro aop注解支持.
   *  使用代理方式;所以需要开启代码支持;
   *  开启 权限注解
   *  Controller才能使用@RequiresPermissions
   * @param securityManager
   * @return
   */
  @Bean
  public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
    AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
    authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
    return authorizationAttributeSourceAdvisor;
  }

  @Bean
  public ShiroDialect shiroDialect() {
    return new ShiroDialect();
  }

  @Bean
  public SessionDAO sessionDAO() {
    MemorySessionDAO sessionDAO = new MemorySessionDAO();
    return sessionDAO;
  }

  @Bean
  public SessionManager sessionManager() {
    DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
    Collection<SessionListener> listeners = new ArrayList<>();
    listeners.add(new ShiroSessionListener());
    sessionManager.setSessionListeners(listeners);
    sessionManager.setSessionDAO(sessionDAO());
    return sessionManager;
  }
}
