package com.example.springboot.shiro.service;

import java.util.Map;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;

/**
 * @author litz-a
 */
public interface ShiroService {

  Map<String, String> loadFilterChainDefinitions();

  void updatePermission(ShiroFilterFactoryBean shiroFilterFactoryBean, Integer roleId);
}
