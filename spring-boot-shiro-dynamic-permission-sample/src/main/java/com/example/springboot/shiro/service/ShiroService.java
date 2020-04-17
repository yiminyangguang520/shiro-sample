package com.example.springboot.shiro.service;

import java.util.Map;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;

/**
 * @author min
 */
public interface ShiroService {

  Map<String, String> loadFilterChainDefinitions();

  void updatePermission(ShiroFilterFactoryBean shiroFilterFactoryBean, Integer roleId);
}
