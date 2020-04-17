package com.study.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author min
 */
@Table(name = "role_resources")
public class RoleResources implements Serializable {

  private static final long serialVersionUID = -8559867942708057891L;

  @Id
  @Column(name = "roleId")
  private Integer roleid;

  @Id
  @Column(name = "resourcesId")
  private String resourcesid;

  /**
   * @return roleId
   */
  public Integer getRoleid() {
    return roleid;
  }

  /**
   *
   */
  public void setRoleid(Integer roleid) {
    this.roleid = roleid;
  }

  public String getResourcesid() {
    return resourcesid;
  }

  public void setResourcesid(String resourcesid) {
    this.resourcesid = resourcesid;
  }
}