package com.lee.demo.config.shiro;

import com.lee.demo.entity.SysPermission;
import com.lee.demo.entity.SysRole;
import com.lee.demo.entity.UserInfo;
import com.lee.demo.service.UserInfoService;
import javax.annotation.Resource;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

/**
 * @author min
 */
public class CustomShiroRealm extends AuthorizingRealm {

  @Resource
  private UserInfoService userInfoService;


  @Override
  protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

    System.out.println("开始身份验证");
    String username = (String) token.getPrincipal();

    UserInfo userInfo = userInfoService.findByUsername(username);

    if (userInfo == null) {
      //没有返回登录用户名对应的SimpleAuthenticationInfo对象时,就会在LoginController中抛出UnknownAccountException异常
      return null;
    }

    SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(userInfo, userInfo.getPassword(), getName());
    //设置盐
    authenticationInfo.setCredentialsSalt(ByteSource.Util.bytes(userInfo.getCredentialsSalt()));

    return authenticationInfo;
  }

  @Override
  protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

    System.out.println("开始权限配置");

    SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
    UserInfo userInfo = (UserInfo) principals.getPrimaryPrincipal();

    for (SysRole role : userInfo.getRoleList()) {
      authorizationInfo.addRole(role.getRole());
      for (SysPermission p : role.getPermissions()) {
        authorizationInfo.addStringPermission(p.getPermission());
      }
    }

    return authorizationInfo;
  }
}
