package com.springboot.dao;

import com.springboot.pojo.Role;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author min
 */
@Mapper
public interface UserRoleMapper {

  /**
   * findByUserName
   * @param userName
   * @return
   */
  List<Role> findByUserName(String userName);
}
