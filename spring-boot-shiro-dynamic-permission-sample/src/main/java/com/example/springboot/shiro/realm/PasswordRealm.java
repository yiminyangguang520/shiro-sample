package com.example.springboot.shiro.realm;

import com.example.springboot.shiro.entity.Authority;
import com.example.springboot.shiro.entity.Role;
import com.example.springboot.shiro.entity.User;
import com.example.springboot.shiro.mapper.AuthorityMapper;
import com.example.springboot.shiro.mapper.RoleMapper;
import com.example.springboot.shiro.mapper.UserMapper;
import java.util.Collection;
import java.util.List;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author min
 */
public class PasswordRealm extends AuthorizingRealm {

  @Autowired
  private RoleMapper roleMapper;

  @Autowired
  private UserMapper userMapper;

  @Autowired
  private AuthorityMapper authorityMapper;

  /**
   * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用,负责在应用程序中决定用户的访问控制的方法
   */
  @Override
  protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
    User user = (User) principalCollection.getPrimaryPrincipal();
    System.out.println(user.getUsername() + "进行授权操作");
    SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
    Integer roleId = user.getRoleId();
    Role role = roleMapper.findRoleById(roleId);
    info.addRole(role.getRoleName());
    List<Authority> authorities = authorityMapper.findAuthoritiesByRoleId(roleId);
    if (authorities.size() == 0) {
      return null;
    }
    return info;
  }

  /**
   * 认证回调函数，登录信息和用户验证信息验证
   */
  @Override
  protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
    //toke强转
    UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) authenticationToken;
    String username = usernamePasswordToken.getUsername();
    //根据用户名查询密码，由安全管理器负责对比查询出的数据库中的密码和页面输入的密码是否一致
    User user = userMapper.findUserByUserName(username);
    if (user == null) {
      return null;
    }

    //单用户登录
    //处理session
    DefaultWebSecurityManager securityManager = (DefaultWebSecurityManager) SecurityUtils.getSecurityManager();
    DefaultWebSessionManager sessionManager = (DefaultWebSessionManager) securityManager.getSessionManager();
    //获取当前已登录的用户session列表
    Collection<Session> sessions = sessionManager.getSessionDAO().getActiveSessions();
    User temp;
    for (Session session : sessions) {
      //清除该用户以前登录时保存的session，强制退出
      Object attribute = session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
      if (attribute == null) {
        continue;
      }

      temp = (User) ((SimplePrincipalCollection) attribute).getPrimaryPrincipal();
      if (username.equals(temp.getUsername())) {
        sessionManager.getSessionDAO().delete(session);
      }
    }

    String password = user.getPassword();
    //最后的比对需要交给安全管理器,三个参数进行初步的简单认证信息对象的包装,由安全管理器进行包装运行
    return new SimpleAuthenticationInfo(user, password, getName());
  }
}
