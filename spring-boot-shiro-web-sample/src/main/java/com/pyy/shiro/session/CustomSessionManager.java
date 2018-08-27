package com.pyy.shiro.session;

import java.io.Serializable;
import javax.servlet.ServletRequest;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.session.mgt.WebSessionKey;

/**
 *
 * @author Administrator
 * @date 2018/7/1 0001
 */
public class CustomSessionManager extends DefaultWebSessionManager {

  @Override
  protected Session retrieveSession(SessionKey sessionKey) throws UnknownSessionException {
    Serializable sessionId = getSessionId(sessionKey);
    ServletRequest request = null;
    if (sessionKey instanceof WebSessionKey) {
      request = ((WebSessionKey) sessionKey).getServletRequest();
    }
    // 先从request中获取session
    if (request != null && sessionId != null) {
      Session session = (Session) request.getAttribute(sessionId.toString());
      if (session != null) {
        return session;
      }
    }

    // 如果request中没有获取到，从原始方法（redis）中获取，存入到request中
    Session session = super.retrieveSession(sessionKey);
    if (request != null && sessionId != null) {
      request.setAttribute(sessionId.toString(), session);
    }

    return session;
  }
}
