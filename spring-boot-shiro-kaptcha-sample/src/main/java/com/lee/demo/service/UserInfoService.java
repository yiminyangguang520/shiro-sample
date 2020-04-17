package com.lee.demo.service;

import com.lee.demo.entity.UserInfo;

/**
 *
 * @author min
 */
public interface UserInfoService {

  /**
   * findByUsername
   * @param username
   * @return
   */
  UserInfo findByUsername(String username);
}
