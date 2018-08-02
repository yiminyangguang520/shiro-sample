package com.stormpath.shiro.samples.springboot.common.model;

/**
 * @author litz-a
 */
public class ErrorMessage {

  private String error;

  public ErrorMessage(String error) {
    this.error = error;
  }

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }
}
