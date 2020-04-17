package com.kfit.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author min
 */
@Controller
@RequestMapping("/userInfo")
public class UserInfoController {

  /**
   * 用户查询.
   */
  @RequestMapping("/userList")
  public String userInfo() {
    return "userInfo";
  }

  /**
   * 用户添加;
   */
  @RequestMapping("/userAdd")
  @RequiresPermissions("userInfo:add")
  public String userInfoAdd() {
    return "userInfoAdd";
  }

  /**
   * 用户删除;
   */
  @RequestMapping("/userDel")
  @RequiresPermissions("userInfo:del")
  public String userDel() {
    return "userInfoDel";
  }

}
