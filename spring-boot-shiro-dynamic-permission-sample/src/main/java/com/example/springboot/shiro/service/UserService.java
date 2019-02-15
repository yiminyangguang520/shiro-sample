package com.example.springboot.shiro.service;

import com.example.springboot.shiro.entity.User;
import com.example.springboot.shiro.vo.req.UserLoginReqVO;
import java.util.List;

/**
 * @author litz-a
 */
public interface UserService {

  Void login(UserLoginReqVO vo);

  List<User> list();

  Void logout();
}
