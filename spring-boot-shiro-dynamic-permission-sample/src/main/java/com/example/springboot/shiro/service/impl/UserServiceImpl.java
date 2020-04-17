package com.example.springboot.shiro.service.impl;

import com.example.springboot.shiro.entity.User;
import com.example.springboot.shiro.mapper.UserMapper;
import com.example.springboot.shiro.service.UserService;
import com.example.springboot.shiro.util.MD5Util;
import com.example.springboot.shiro.vo.req.UserLoginReqVO;
import java.util.List;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author min
 */
@Service("userService")
public class UserServiceImpl implements UserService {

  @Autowired
  private UserMapper userMapper;

  @Override
  public Void login(UserLoginReqVO vo) {
    //主体,当前状态为没有认证的状态“未认证”
    Subject subject = SecurityUtils.getSubject();
    UsernamePasswordToken token = new UsernamePasswordToken(vo.getUsername(), MD5Util.ecrypt(vo.getPassword()));
    try {
      subject.login(token);
    } catch (Exception e) {
      throw new RuntimeException("用户名或密码错误");
    }
    return null;
  }

  @Override
  public List<User> list() {
    return userMapper.findAllUsers();
  }

  @Override
  public Void logout() {
    Subject subject = SecurityUtils.getSubject();
    subject.logout();
    return null;
  }


}
