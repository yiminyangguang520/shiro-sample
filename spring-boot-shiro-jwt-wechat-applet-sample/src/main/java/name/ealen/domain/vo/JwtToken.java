package name.ealen.domain.vo;

import org.apache.shiro.authc.AuthenticationToken;

/**
 *
 * @author EalenXie
 * @date 2018/11/22 18:21
 */
public class JwtToken implements AuthenticationToken {

  private String token;

  public JwtToken(String token) {
    this.token = token;
  }

  @Override
  public Object getPrincipal() {
    return token;
  }

  @Override
  public Object getCredentials() {
    return token;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }
}
