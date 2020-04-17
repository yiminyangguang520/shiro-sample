package org.inlighting.controller;

import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.ShiroException;
import org.inlighting.bean.ResponseBean;
import org.inlighting.exception.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author min
 */
@RestControllerAdvice
public class ExceptionController {

  /**
   * 捕捉shiro的异常
   * @param e
   * @return
   */
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  @ExceptionHandler(ShiroException.class)
  public ResponseBean handle401(ShiroException e) {
    return new ResponseBean(401, e.getMessage(), null);
  }

  /**
   * 捕捉UnauthorizedException
   * @return
   */
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  @ExceptionHandler(UnauthorizedException.class)
  public ResponseBean handle401() {
    return new ResponseBean(401, "Unauthorized", null);
  }

  /**
   * 捕捉其他所有异常
   * @param request
   * @param ex
   * @return
   */
  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseBean globalException(HttpServletRequest request, Throwable ex) {
    return new ResponseBean(getStatus(request).value(), ex.getMessage(), null);
  }

  private HttpStatus getStatus(HttpServletRequest request) {
    Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
    if (statusCode == null) {
      return HttpStatus.INTERNAL_SERVER_ERROR;
    }
    return HttpStatus.valueOf(statusCode);
  }
}

