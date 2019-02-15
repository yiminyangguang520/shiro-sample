package com.example.springboot.shiro.controller;

import com.example.springboot.shiro.entity.User;
import com.example.springboot.shiro.service.UserService;
import com.example.springboot.shiro.vo.BaseResponse;
import com.example.springboot.shiro.vo.req.UserLoginReqVO;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author litz-a
 */
@RestController
@RequestMapping("/user")
public class UserController {

  @Autowired
  private UserService userService;

  @PostMapping("/login")
  public BaseResponse<Void> login(@RequestBody UserLoginReqVO vo) {
    return new BaseResponse<>(userService.login(vo));
  }

  @GetMapping("/list")
  public BaseResponse<List<User>> list() {
    return new BaseResponse<>(userService.list());
  }

  @PostMapping("/logout")
  public BaseResponse logout() {
    return new BaseResponse<>(userService.logout());
  }
}
