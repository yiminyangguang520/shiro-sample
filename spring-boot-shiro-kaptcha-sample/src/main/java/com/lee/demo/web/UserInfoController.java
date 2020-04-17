package com.lee.demo.web;

import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
  @RequiresPermissions("userInfo:view")
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

  @RequestMapping("/userDel")
  @RequiresPermissions("userInfo:del")
  @ExceptionHandler(UnauthorizedException.class)
  public String userInfoDel() {
    return "userInfoDel";
  }
}
