package com.hafiz.www.service;

import com.hafiz.www.po.UserEntity;
import java.util.List;

/**
 *
 * @author hafiz.zhang
 * @date 2016/8/27
 */
public interface UserService {

  /**
   * 获取所有的用户信息
   */
  List<UserEntity> getAllUsers();

  /**
   * 通过用户名获取用户信息
   *
   * @param userName 用户名
   */
  List<UserEntity> getByUserName(String userName);
}
