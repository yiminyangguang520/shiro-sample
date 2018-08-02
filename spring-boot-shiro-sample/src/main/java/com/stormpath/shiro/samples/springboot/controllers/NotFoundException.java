package com.stormpath.shiro.samples.springboot.controllers;

/**
 * @author litz-a
 */
public class NotFoundException extends Exception {

  public NotFoundException(String id) {
    super(id);
  }
}
