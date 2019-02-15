package shiro.service;

import java.util.Date;
import java.util.Random;
import org.springframework.stereotype.Service;
import shiro.entity.User;

/**
 * created by CaiBaoHong at 2018/4/18 16:08<br>
 * @author litz-a
 */
@Service
public class UserService {

  /**
   * 模拟查询返回用户信息
   */
  public User findUserByName(String uname) {
    User user = new User();
    user.setUname(uname);
    user.setNick(uname + "NICK");
    //密码明文是123456
    user.setPwd("J/ms7qTJtqmysekuY8/v1TAS+VKqXdH5sB7ulXZOWho=");
    //加密密码的盐值
    user.setSalt("wxKYXuTPST5SG0jMQzVPsg==");
    //随机分配一个id
    user.setCreated(new Date());
    user.setUid(new Random().nextLong());
    return user;
  }

}
