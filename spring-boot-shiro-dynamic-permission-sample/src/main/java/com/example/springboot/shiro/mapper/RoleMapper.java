package com.example.springboot.shiro.mapper;

import com.example.springboot.shiro.entity.Role;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * @author litz-a
 */
@Repository
public interface RoleMapper {

  Role findRoleById(Integer roleId);

  List<Role> findAllRoles();

  void updateById(Role role);

  void deleteRoleAuthorities(Integer roleId);
}
