package com.springboot.dao;

import com.springboot.pojo.Permission;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author litz-a
 */
@Mapper
public interface UserPermissionMapper {

  /**
   * findByUserName
   * @param userName
   * @return
   */
  List<Permission> findByUserName(String userName);
}
