package org.inlighting.exception;

/**
 * @author litz-a
 */
public class UnauthorizedException extends RuntimeException {

  public UnauthorizedException(String msg) {
    super(msg);
  }

  public UnauthorizedException() {
    super();
  }
}
