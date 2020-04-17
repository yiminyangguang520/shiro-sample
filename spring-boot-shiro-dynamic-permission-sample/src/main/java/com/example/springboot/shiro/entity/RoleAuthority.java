package com.example.springboot.shiro.entity;

/**
 * @author min
 */
public class RoleAuthority {

  private Integer roleId;
  private Integer authorityId;

  public Integer getRoleId() {
    return roleId;
  }

  public void setRoleId(Integer roleId) {
    this.roleId = roleId;
  }

  public Integer getAuthorityId() {
    return authorityId;
  }

  public void setAuthorityId(Integer authorityId) {
    this.authorityId = authorityId;
  }
}
