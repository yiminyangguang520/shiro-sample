package org.crazycake.shiroredisspringboottutorial.realm;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.PostConstruct;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.crazycake.shiroredisspringboottutorial.model.UserInfo;
import org.springframework.stereotype.Repository;

/**
 * This realm is only for tutorial
 *
 * @author Alex Yang
 */
@Repository
public class CustomRealm extends AuthorizingRealm {

  @Override
  protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
    SimpleAuthorizationInfo authInfo = new SimpleAuthorizationInfo();
    // only for tutorial
    authInfo.addRoles(Arrays.asList("schwartz"));
    return authInfo;
  }

  @Override
  protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
    UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
    UserInfo userInfo = new UserInfo();
    userInfo.setMid(ThreadLocalRandom.current().nextInt(1, 100));
    userInfo.setUsername(usernamePasswordToken.getUsername());
    // Expect password is "123456"
    return new SimpleAuthenticationInfo(userInfo, "123456", getName());
  }

  @PostConstruct
  public void initCredentialsMatcher() {
    setCredentialsMatcher(new SimpleCredentialsMatcher());
  }

}