package com.kfit.service.impl;

import com.kfit.bean.UserInfo;
import com.kfit.repository.UserInfoRepository;
import com.kfit.service.UserInfoService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @author min
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {

  @Resource
  private UserInfoRepository userInfoRepository;

  @Override
  public UserInfo findByUsername(String username) {
    System.out.println("UserInfoServiceImpl.findByUsername()");
    return userInfoRepository.findByUsername(username);
  }

}
