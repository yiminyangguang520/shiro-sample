package com.lee.demo.web;

import com.lee.demo.config.exception.IncorrectCaptchaException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author min
 */
@Controller
public class HomeController {

  @RequestMapping("/logoutTest")
  public String logout() {
    return "logoutTest";
  }

  @RequestMapping({"/", "/index"})
  public String index() {
    return "index";
  }

  @RequestMapping("/login")
  public String login(HttpServletRequest request, Map<String, Object> map) throws Exception {

    System.out.println("HomeController.login");

    // 登录失败从request中获取shiro处理的异常信息。
    // shiroLoginFailure:就是shiro异常类的全类名.
    Object exception = request.getAttribute("shiroLoginFailure");
//        String exception = (String) request.getAttribute("shiroLoginFailure");
    String msg = "";
    if (exception != null) {
      if (UnknownAccountException.class.isInstance(exception)) {
        System.out.println("账户不存在");
        msg = "账户不存在或密码不正确";
      } else if (IncorrectCredentialsException.class.isInstance(exception)) {
        System.out.println("密码不正确");
        msg = "账户不存在或密码不正确";
      } else if (IncorrectCaptchaException.class.isInstance(exception)) {
        System.out.println("验证码不正确");
        msg = "验证码不正确";
      } else {
        System.out.println("其他异常");
        msg = "其他异常";
      }
    }

    map.put("msg", msg);
    // 此方法不处理登录成功,由shiro进行处理.
    return "login";
  }
}
