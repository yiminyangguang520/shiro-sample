package com.study.mapper;

import com.study.model.UserRole;
import com.study.util.MyMapper;
import java.util.List;

/**
 * @author min
 */
public interface UserRoleMapper extends MyMapper<UserRole> {

  /**
   * findUserIdByRoleId
   * @param roleId
   * @return
   */
  List<Integer> findUserIdByRoleId(Integer roleId);
}