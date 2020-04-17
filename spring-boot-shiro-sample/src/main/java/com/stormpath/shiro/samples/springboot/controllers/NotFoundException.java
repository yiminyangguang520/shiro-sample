package com.stormpath.shiro.samples.springboot.controllers;

/**
 * @author min
 */
public class NotFoundException extends Exception {

  public NotFoundException(String id) {
    super(id);
  }
}
