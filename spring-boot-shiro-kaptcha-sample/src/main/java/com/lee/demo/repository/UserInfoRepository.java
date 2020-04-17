package com.lee.demo.repository;

import com.lee.demo.entity.UserInfo;
import org.springframework.data.repository.CrudRepository;

/**
 * @author min
 */
public interface UserInfoRepository extends CrudRepository<UserInfo, Long> {

  UserInfo findByUsername(String username);
}
