package com.hafiz.www.controller;

import com.hafiz.www.po.UserEntity;
import com.hafiz.www.service.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author hafiz.zhang
 * @date 2016/8/27
 */
@Controller
@RequestMapping("/user")
public class UserController {

  @Autowired
  private UserService userService;

  @RequestMapping(value = "/all", method = RequestMethod.GET)
  @ResponseBody
  public List<UserEntity> getAllUsers() {
    List<UserEntity> list = userService.getAllUsers();
    return list;
  }
}
