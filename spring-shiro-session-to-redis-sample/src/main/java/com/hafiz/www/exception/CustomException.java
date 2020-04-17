package com.hafiz.www.exception;

/**
 *
 * @author hafiz.zhang
 * @date 2016/8/27
 */
public class CustomException extends Exception {

  private String message;

  public CustomException(String message) {
    super(message);
    this.message = message;
  }

  @Override
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

}
