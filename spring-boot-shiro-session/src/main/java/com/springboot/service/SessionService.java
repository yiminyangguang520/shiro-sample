package com.springboot.service;

import com.springboot.pojo.UserOnline;
import java.util.List;

/**
 * @author min
 */
public interface SessionService {

  /**
   * list
   * @return
   */
  List<UserOnline> list();

  /**
   * forceLogout
   * @param sessionId
   * @return
   */
  boolean forceLogout(String sessionId);
}
