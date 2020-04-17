package com.lee.demo.config.shiro;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * @author min
 * 拓展登陆验证字段
 */
public class CaptchaUsernamePasswordToken extends UsernamePasswordToken {

  private static final long serivalVersionUID = 1L;

  /**
   * 验证码字符串
   */
  private String captcha;

  public CaptchaUsernamePasswordToken(String username, char[] password, boolean rememberMe, String host, String captcha) {
    super(username, password, rememberMe, host);
    this.captcha = captcha;
  }

  public static long getSerivalVersionUID() {
    return serivalVersionUID;
  }

  public String getCaptcha() {
    return captcha;
  }

  public void setCaptcha(String captcha) {
    this.captcha = captcha;
  }
}
