package com.example.springboot.shiro.service;

import com.example.springboot.shiro.entity.Role;
import com.example.springboot.shiro.vo.req.UpdateRoleReqVO;
import java.util.List;

/**
 * @author min
 */
public interface RoleService {

  List<Role> list();

  Void update(UpdateRoleReqVO vo);
}
