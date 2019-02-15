package com.example.springboot.shiro.service;

import com.example.springboot.shiro.entity.Role;
import com.example.springboot.shiro.vo.req.UpdateRoleReqVO;
import java.util.List;

/**
 * @author litz-a
 */
public interface RoleService {

  List<Role> list();

  Void update(UpdateRoleReqVO vo);
}
