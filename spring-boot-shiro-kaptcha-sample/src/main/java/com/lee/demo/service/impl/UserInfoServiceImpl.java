package com.lee.demo.service.impl;

import com.lee.demo.entity.UserInfo;
import com.lee.demo.repository.UserInfoRepository;
import com.lee.demo.service.UserInfoService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 *
 * @author min
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {

  @Resource
  private UserInfoRepository userInfoRepository;

  @Override
  public UserInfo findByUsername(String username) {
    System.out.println("UserInfoServiceImpl.findByUsername");
    return userInfoRepository.findByUsername(username);
  }
}
