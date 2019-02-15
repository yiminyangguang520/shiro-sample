package com.study.shiro;

import com.github.pagehelper.util.StringUtil;
import com.study.model.Resources;
import com.study.service.ResourcesService;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author litz-a
 * Created by yangqj on 2017/4/30.
 */
@Service
public class ShiroService {

  @Autowired
  private ShiroFilterFactoryBean shiroFilterFactoryBean;

  @Autowired
  private ResourcesService resourcesService;

  @Autowired
  private RedisSessionDAO redisSessionDAO;

  /**
   * 初始化权限
   */
  public Map<String, String> loadFilterChainDefinitions() {
    // 权限控制map.从数据库获取
    Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>(8);
    filterChainDefinitionMap.put("/logout", "logout");
    filterChainDefinitionMap.put("/css/**", "anon");
    filterChainDefinitionMap.put("/js/**", "anon");
    filterChainDefinitionMap.put("/img/**", "anon");
    filterChainDefinitionMap.put("/font-awesome/**", "anon");
    List<Resources> resourcesList = resourcesService.queryAll();
    for (Resources resources : resourcesList) {
      if (StringUtil.isNotEmpty(resources.getResurl())) {
        String permission = "perms[" + resources.getResurl() + "]";
        filterChainDefinitionMap.put(resources.getResurl(), permission);
      }
    }
    filterChainDefinitionMap.put("/**", "authc");
    return filterChainDefinitionMap;
  }

  /**
   * 重新加载权限
   */
  public void updatePermission() {
    synchronized (shiroFilterFactoryBean) {
      AbstractShiroFilter shiroFilter;
      try {
        shiroFilter = (AbstractShiroFilter) shiroFilterFactoryBean.getObject();
      } catch (Exception e) {
        throw new RuntimeException("get ShiroFilter from shiroFilterFactoryBean error!");
      }

      PathMatchingFilterChainResolver filterChainResolver = (PathMatchingFilterChainResolver) shiroFilter.getFilterChainResolver();
      DefaultFilterChainManager manager = (DefaultFilterChainManager) filterChainResolver.getFilterChainManager();

      // 清空老的权限控制
      manager.getFilterChains().clear();

      shiroFilterFactoryBean.getFilterChainDefinitionMap().clear();
      shiroFilterFactoryBean.setFilterChainDefinitionMap(loadFilterChainDefinitions());
      // 重新构建生成
      Map<String, String> chains = shiroFilterFactoryBean.getFilterChainDefinitionMap();
      for (Map.Entry<String, String> entry : chains.entrySet()) {
        String url = entry.getKey();
        String chainDefinition = entry.getValue().trim().replace(" ", "");
        manager.createChain(url, chainDefinition);
      }

      System.out.println("更新权限成功！！");
    }
  }


}
