package com.springboot.dao;

import com.springboot.pojo.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author min
 */
@Mapper
public interface UserMapper {

  /**
   *
   * @param userName
   * @return
   */
  User findByUserName(String userName);
}
