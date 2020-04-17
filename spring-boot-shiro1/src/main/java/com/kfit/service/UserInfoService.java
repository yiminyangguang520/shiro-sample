package com.kfit.service;

import com.kfit.bean.UserInfo;

/**
 * @author min
 */
public interface UserInfoService {

  /**
   * 通过username查找用户信息
   * @param username
   * @return
   */
  UserInfo findByUsername(String username);

}
