package com.hafiz.www.service.impl;

import com.hafiz.www.mapper.UserEntityMapper;
import com.hafiz.www.po.UserEntity;
import com.hafiz.www.service.UserService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Desc:用户表相关的servie接口实现类
 *
 *
 * @author hafiz.zhang
 * @date 2016/8/27
 */
@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private UserEntityMapper mapper;

  public List<UserEntity> getAllUsers() {
    return mapper.getAllUsers();
  }

  public List<UserEntity> getByUserName(String userName) {
    if (userName == null) {
      return new ArrayList<UserEntity>();
    }
    return mapper.getByUserName(userName);
  }
}
