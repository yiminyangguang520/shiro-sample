package com.lee.shiro.mapper;

import com.lee.shiro.entity.Permission;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Service;


/**
 * @author Created by pangkunkun on 2017/11/20.
 */
@Mapper
@Service
public interface PermissionMapper {

  /**
   * 新增权限
   */
  void save(Permission permission);

  /**
   * 通过userId获取用户所具有的权限
   *
   * @return List<Permission>
   */
  List<Permission> findPermissionAndRoleNameByUserId(String userId);
}
