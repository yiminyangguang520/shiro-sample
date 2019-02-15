package com.lee.shiro.service;

import com.lee.shiro.entity.User;

/**
 * @author Created by pangkunkun on 2017/11/16.
 */
public interface UserService {

  /**
   * 通过userName获取用户信息
   *
   * @return User
   */
  User findByUsername(String username);

  /**
   * 新增user
   */
  void save(User user);
}
