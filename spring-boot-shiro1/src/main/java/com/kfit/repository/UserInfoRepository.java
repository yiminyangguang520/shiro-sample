package com.kfit.repository;

import com.kfit.bean.UserInfo;
import org.springframework.data.repository.CrudRepository;

/**
 * UserInfo持久化类;
 *
 * @author min
 * @version v.0.1
 */
public interface UserInfoRepository extends CrudRepository<UserInfo, Long> {

  /**
   * 通过username查找用户信息
   * @param username
   * @return
   */
  UserInfo findByUsername(String username);
}
