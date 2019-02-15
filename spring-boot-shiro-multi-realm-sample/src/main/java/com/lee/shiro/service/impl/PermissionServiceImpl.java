package com.lee.shiro.service.impl;

import com.lee.shiro.entity.Permission;
import com.lee.shiro.mapper.PermissionMapper;
import com.lee.shiro.service.PermissionService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author Created by pangkunkun on 2017/11/16.
 */
@Service
public class PermissionServiceImpl implements PermissionService {

  @Autowired
  private PermissionMapper permissionMapper;

  @Override
  public List<Permission> findPermissionAndRoleNameByUserId(String userId) {
    List<Permission> permissions = new ArrayList<>(2);
//        Permission permission=new Permission();
//        permission.setPermission("permission1");
//        permissions.add(permission);
//        permission.setPermission("add");
//        permissions.add(permission);
    permissions = permissionMapper.findPermissionAndRoleNameByUserId(userId);
    return permissions;
  }

  @Override
  public void save(Permission permission) {
    System.out.println("permission:" + permission.toString());
    permissionMapper.save(permission);
  }
}
