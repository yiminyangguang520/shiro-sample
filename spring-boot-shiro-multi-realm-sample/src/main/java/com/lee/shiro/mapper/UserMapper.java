package com.lee.shiro.mapper;

import com.lee.shiro.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Service;

/**
 * @author Created by pangkunkun on 2017/11/20.
 */
@Mapper
@Service
public interface UserMapper {

  /**
   * 通过username获取用户信息
   *
   * @return User
   */
  User findByUsername(String username);

  /**
   * 新增user
   */
  void save(User user);
}
