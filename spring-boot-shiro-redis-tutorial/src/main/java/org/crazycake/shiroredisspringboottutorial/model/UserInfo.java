package org.crazycake.shiroredisspringboottutorial.model;

import java.io.Serializable;

/**
 * @author min
 */
public class UserInfo implements Serializable {

  private Integer mid;

  private String username;

  public Integer getMid() {
    return mid;
  }

  public void setMid(Integer mid) {
    this.mid = mid;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }
}