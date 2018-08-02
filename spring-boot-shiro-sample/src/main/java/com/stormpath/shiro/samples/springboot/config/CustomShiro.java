package com.stormpath.shiro.samples.springboot.config;

import com.stormpath.shiro.samples.springboot.common.dao.DefaultStormtrooperDao;
import com.stormpath.shiro.samples.springboot.common.dao.StormtrooperDao;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.realm.text.PropertiesRealm;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author litz-a
 */
@Configuration
public class CustomShiro {

  @Bean
  protected StormtrooperDao stormtrooperDao() {
    return new DefaultStormtrooperDao();
  }

  @Bean
  public Realm realm() {

    // uses 'classpath:shiro-users.properties' by default
    PropertiesRealm realm = new PropertiesRealm();

    // Caching isn't needed in this example, but we can still turn it on
    realm.setCachingEnabled(true);
    return realm;
  }

  @Bean
  public ShiroFilterChainDefinition shiroFilterChainDefinition() {
    DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
    // use permissive to NOT require authentication, our controller Annotations will decide that
    chainDefinition.addPathDefinition("/**", "authcBasic[permissive]");
    return chainDefinition;
  }

  @Bean
  public CacheManager cacheManager() {
    // Caching isn't needed in this example, but we will use the MemoryConstrainedCacheManager for this example.
    return new MemoryConstrainedCacheManager();
  }
}
