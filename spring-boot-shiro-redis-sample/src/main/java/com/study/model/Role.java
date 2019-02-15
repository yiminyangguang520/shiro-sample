package com.study.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

/**
 * @author litz-a
 */
public class Role implements Serializable {

  private static final long serialVersionUID = -6140090613812307452L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "roleDesc")
  private String roledesc;

  @Transient
  private Integer selected;

  /**
   * @return id
   */
  public Integer getId() {
    return id;
  }

  /**
   *
   */
  public void setId(Integer id) {
    this.id = id;
  }

  /**
   * @return roleDesc
   */
  public String getRoledesc() {
    return roledesc;
  }

  /**
   *
   */
  public void setRoledesc(String roledesc) {
    this.roledesc = roledesc;
  }

  public Integer getSelected() {
    return selected;
  }

  public void setSelected(Integer selected) {
    this.selected = selected;
  }
}