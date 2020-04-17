package com.stormpath.shiro.samples.springboot.handler;

import com.stormpath.shiro.samples.springboot.SpringBootApp;
import com.stormpath.shiro.samples.springboot.common.model.ErrorMessage;
import com.stormpath.shiro.samples.springboot.controllers.NotFoundException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author min
 */
@ControllerAdvice
public class ExceptionHandler {

  private static Logger log = LoggerFactory.getLogger(SpringBootApp.class);

  @org.springframework.web.bind.annotation.ExceptionHandler(UnauthenticatedException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public void handleException(UnauthenticatedException e) {
    log.debug("{} was thrown", e.getClass(), e);
  }

  @org.springframework.web.bind.annotation.ExceptionHandler(AuthorizationException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public void handleException(AuthorizationException e) {
    log.debug("{} was thrown", e.getClass(), e);
  }

  @org.springframework.web.bind.annotation.ExceptionHandler(NotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public @ResponseBody
  ErrorMessage handleException(NotFoundException e) {
    String id = e.getMessage();
    return new ErrorMessage("Trooper Not Found: " + id + ", why aren't you at your post? " + id + ", do you copy?");
  }

}
