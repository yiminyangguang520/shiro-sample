package com.example.springboot.shiro.mapper;

import com.example.springboot.shiro.entity.User;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * @author min
 */
@Repository
public interface UserMapper {

  User findUserByUserName(String username);

  List<User> findAllUsers();

  List<User> findUsersByRoleId(Integer roleId);
}
