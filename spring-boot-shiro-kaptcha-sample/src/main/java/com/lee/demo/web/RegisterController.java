package com.lee.demo.web;

import com.lee.demo.entity.SysRole;
import com.lee.demo.entity.UserInfo;
import com.lee.demo.repository.SysRoleRepository;
import com.lee.demo.repository.UserInfoRepository;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author min
 */
@Controller
public class RegisterController {

  @Resource
  private SysRoleRepository sysRoleRepository;

  @Resource
  private UserInfoRepository userInfoRepository;

  @RequestMapping(value = "/register", method = RequestMethod.GET)
  public String register(Map<String, Object> map) {

    List<String> names = new LinkedList<>();
    List<SysRole> roleList = (List<SysRole>) sysRoleRepository.findAll();
    for (SysRole role : roleList) {
      names.add(role.getDescription());
    }
    map.put("roles", names);

    return "register";
  }

  @RequestMapping(value = "/register", method = RequestMethod.POST)
  public String register(@RequestParam("username") String userName,
      @RequestParam("password") String password,
      @RequestParam("passwordAgain") String passwordAgain,
      HttpServletRequest request,
      Map<String, Object> map) {
    if (!password.equals(passwordAgain)) {
      map.put("msg", "密码不一致");
      return "register";
    }

    UserInfo userInfo = new UserInfo();
    userInfo.setUsername(userName);
    userInfo.setState((byte) 1);
    userInfo.setSalt(createSalt().toString());
    String passwordMd5 = new Md5Hash(password, userInfo.getCredentialsSalt(), 2).toHex();
    userInfo.setPassword(passwordMd5);

    String[] roleNames = request.getParameterValues("checkbox");
    List<SysRole> roles = sysRoleRepository.findAllByDescriptionIn(Arrays.asList(roleNames));
    userInfo.setRoleList(roles);

    userInfoRepository.save(userInfo);
    return "login";
  }

  /**
   * 生成盐
   */
  public static byte[] createSalt() {
    byte[] salt = new byte[16];
    try {
      SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
      random.nextBytes(salt);
      return salt;
    } catch (NoSuchAlgorithmException e) {
      return null;
    }
  }
}
