package com.example.springboot.shiro.mapper;

import com.example.springboot.shiro.entity.Authority;
import com.example.springboot.shiro.entity.RoleAuthority;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * @author min
 */
@Repository
public interface AuthorityMapper {

  List<Authority> findAuthorities();

  List<Authority> findAuthoritiesByRoleId(Integer roleId);

  List<Authority> findAuthoritiesByIds(List<Integer> authorityIds);

  void insertAuthorities(List<RoleAuthority> roleAuthorities);

  void updatePermissions(List<Authority> authorities);
}
