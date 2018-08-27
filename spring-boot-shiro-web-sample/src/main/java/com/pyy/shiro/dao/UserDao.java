package com.pyy.shiro.dao;

import com.pyy.shiro.vo.User;
import java.util.List;

/**
 *
 * @author Administrator
 * @date 2018/6/24 0024
 */
public interface UserDao {

  /**
   * findUserByUsername
   * @param username
   * @return
   */
  User findUserByUsername(String username);

  /**
   * findRolesByUsername
   * @param username
   * @return
   */
  List<String> findRolesByUsername(String username);
}
