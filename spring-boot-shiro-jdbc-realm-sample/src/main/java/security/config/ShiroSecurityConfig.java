package security.config;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.Filter;
import javax.sql.DataSource;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.AnonymousFilter;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.apache.shiro.web.filter.authc.UserFilter;
import org.apache.shiro.web.filter.authz.RolesAuthorizationFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

/**
 * @author min
 */
@Configuration
public class ShiroSecurityConfig {

  @Autowired
  private DataSource dataSource;

  @Bean(name = "shiroFilter")
  public AbstractShiroFilter shiroFilter() throws Exception {
    ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
    Map<String, String> filterChainDefinitionMapping = new HashMap<>(5);
    filterChainDefinitionMapping.put("/api/health", "authc,roles[guest]");
    // filterChainDefinitionMapping.put("/api/health", "authc,roles[guest],ssl[8443]");
    filterChainDefinitionMapping.put("/login", "authc");
    filterChainDefinitionMapping.put("/logout", "logout");
    shiroFilter.setFilterChainDefinitionMap(filterChainDefinitionMapping);
    shiroFilter.setSecurityManager(securityManager());
    shiroFilter.setLoginUrl("/login");
    Map<String, Filter> filters = new HashMap<>(5);
    filters.put("anon", new AnonymousFilter());
    filters.put("authc", new FormAuthenticationFilter());
    LogoutFilter logoutFilter = new LogoutFilter();
    logoutFilter.setRedirectUrl("/login?logout");
    filters.put("logout", logoutFilter);
    filters.put("roles", new RolesAuthorizationFilter());
    filters.put("user", new UserFilter());
    shiroFilter.setFilters(filters);
    return (AbstractShiroFilter) shiroFilter.getObject();
  }

  @Bean(name = "securityManager")
  public DefaultWebSecurityManager securityManager() {
    DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
    securityManager.setRealm(jdbcRealm());
    return securityManager;
  }

  /**
   * 从数据库中读取权限配置,类路径下的shiro-users.properties无用,采用PropertiesRealm时,才会默认中类路径下加载shiro-users.properties
   * @return
   */
  @Bean(name = "jdbcRealm")
  @DependsOn("lifecycleBeanPostProcessor")
  public JdbcRealm jdbcRealm() {
    JdbcRealm realm = new JdbcRealm();
    HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
    credentialsMatcher.setHashAlgorithmName(Sha256Hash.ALGORITHM_NAME);
    realm.setCredentialsMatcher(credentialsMatcher);
    realm.setDataSource(dataSource);
    realm.init();
    return realm;
  }

  @Bean
  public static LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
    return new LifecycleBeanPostProcessor();
  }
}