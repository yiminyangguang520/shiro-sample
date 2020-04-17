package com.lee.demo.config.shiro;

import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.Filter;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @author min
 */
@Configuration
public class ShiroConfiguration {

  @Bean
  public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {

    ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
    shiroFilterFactoryBean.setSecurityManager(securityManager);

    shiroFilterFactoryBean.setLoginUrl("/login");
    shiroFilterFactoryBean.setSuccessUrl("/index");
//        shiroFilterFactoryBean.setUnauthorizedUrl("/403"); //这里设置403并不会起作用，参考http://www.jianshu.com/p/e03f5b54838c

    //验证码过滤器
    Map<String, Filter> filterMap = shiroFilterFactoryBean.getFilters();
    KaptchaFilter kaptchaFilter = new KaptchaFilter();
    filterMap.put("kaptchaFilter", kaptchaFilter);
    shiroFilterFactoryBean.setFilters(filterMap);

    Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
    filterChainDefinitionMap.put("/logout", "logout");
    filterChainDefinitionMap.put("/index", "user");
    filterChainDefinitionMap.put("/", "user");
    filterChainDefinitionMap.put("/favicon.ico", "anon");
    //图片验证码(kaptcha框架)
    filterChainDefinitionMap.put("/kaptcha.jpg", "anon");
    filterChainDefinitionMap.put("/register", "anon");
    filterChainDefinitionMap.put("/login", "kaptchaFilter");
    filterChainDefinitionMap.put("/**", "authc");

    shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

    return shiroFilterFactoryBean;
  }


  @Bean
  public SecurityManager securityManager() {
    DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
    securityManager.setRealm(myShiroRealm());
    securityManager.setCacheManager(ehCacheManager());
    securityManager.setRememberMeManager(cookieRememberMeManager());
    return securityManager;
  }

  @Bean
  public CustomShiroRealm myShiroRealm() {
    CustomShiroRealm customShiroRealm = new CustomShiroRealm();
    customShiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
    return customShiroRealm;
  }

  @Bean
  public HashedCredentialsMatcher hashedCredentialsMatcher() {
    HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
    //散列算法:这里使用MD5算法;
    hashedCredentialsMatcher.setHashAlgorithmName("md5");
    //散列的次数，比如散列两次，相当于 md5(md5(""));
    hashedCredentialsMatcher.setHashIterations(2);
    return hashedCredentialsMatcher;
  }

  @Bean
  public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
    AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
    advisor.setSecurityManager(securityManager);
    return advisor;
  }

  @Bean
  public EhCacheManager ehCacheManager() {
    System.out.println("ShiroConfiguration.getEhCacheManager()");
    EhCacheManager ehCacheManager = new EhCacheManager();
    ehCacheManager.setCacheManagerConfigFile("classpath:config/ehcache-shiro.xml");
    return ehCacheManager;
  }

  /**
   * cookie对象
   * @return
   */
  @Bean
  public SimpleCookie rememberMeCookie() {
    System.out.println("ShiroConfiguration.rememberMeCookie()");
    //这个参数是cookie的名称，对应前端的checkbox的name = rememberMe
    SimpleCookie simpleCookie = new SimpleCookie("rememberMe");

    //<!-- 记住我cookie生效时间30天 ,单位秒;-->
    simpleCookie.setMaxAge(259200);
    return simpleCookie;
  }

  /**
   * cookie管理对象
   * @return
   */
  @Bean
  public CookieRememberMeManager cookieRememberMeManager() {
    System.out.println("ShiroConfiguration.rememberMeManager()");
    CookieRememberMeManager manager = new CookieRememberMeManager();
    manager.setCookie(rememberMeCookie());
    return manager;
  }
}
