package org.inlighting.exception;

/**
 * @author min
 */
public class UnauthorizedException extends RuntimeException {

  public UnauthorizedException(String msg) {
    super(msg);
  }

  public UnauthorizedException() {
    super();
  }
}
