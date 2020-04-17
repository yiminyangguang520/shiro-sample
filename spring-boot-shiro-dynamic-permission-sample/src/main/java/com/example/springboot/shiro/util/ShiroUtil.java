package com.example.springboot.shiro.util;

import com.example.springboot.shiro.entity.User;
import java.util.Collection;
import java.util.Objects;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.Authenticator;
import org.apache.shiro.authc.LogoutAware;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.crazycake.shiro.RedisSessionDAO;

/**
 * @author min
 */
public class ShiroUtil {

  private static RedisSessionDAO redisSessionDAO = SpringUtil.getBean(RedisSessionDAO.class);

  private ShiroUtil() {
  }

  /**
   * 获取指定用户名的Session
   */
  private static Session getSessionByUsername(Integer userId) {
    Collection<Session> sessions = redisSessionDAO.getActiveSessions();
    User user;
    Object attribute;
    for (Session session : sessions) {
      attribute = session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
      if (attribute == null) {
        continue;
      }
      user = (User) ((SimplePrincipalCollection) attribute).getPrimaryPrincipal();
      if (user == null) {
        continue;
      }
      if (Objects.equals(user.getId(), userId)) {
        return session;
      }
    }
    return null;
  }

  /**
   * 删除用户缓存信息
   *
   * @param userId 用户id
   * @param isRemoveSession 是否删除session，删除后用户需重新登录
   */
  public static void kickOutUser(Integer userId, boolean isRemoveSession) {
    Session session = getSessionByUsername(userId);
    if (session == null) {
      return;
    }

    Object attribute = session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
    if (attribute == null) {
      return;
    }

    User user = (User) ((SimplePrincipalCollection) attribute).getPrimaryPrincipal();
    if (!userId.equals(user.getId())) {
      return;
    }

    //删除session
    if (isRemoveSession) {
      redisSessionDAO.delete(session);
    }

    DefaultWebSecurityManager securityManager = (DefaultWebSecurityManager) SecurityUtils.getSecurityManager();
    Authenticator authc = securityManager.getAuthenticator();
    //删除cache，在访问受限接口时会重新授权
    ((LogoutAware) authc).onLogout((SimplePrincipalCollection) attribute);
  }

}
