package com.example.springboot.shiro.controller;

import com.example.springboot.shiro.entity.Role;
import com.example.springboot.shiro.service.RoleService;
import com.example.springboot.shiro.vo.BaseResponse;
import com.example.springboot.shiro.vo.req.UpdateRoleReqVO;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author min
 */
@RestController
@RequestMapping("/role")
public class RoleController {

  @Autowired
  private RoleService roleService;

  @GetMapping("/list")
  public BaseResponse<List<Role>> list() {
    return new BaseResponse<>(roleService.list());
  }

  @PostMapping("/update")
  public BaseResponse update(@RequestBody UpdateRoleReqVO vo) {
    return new BaseResponse<>(roleService.update(vo));
  }
}
