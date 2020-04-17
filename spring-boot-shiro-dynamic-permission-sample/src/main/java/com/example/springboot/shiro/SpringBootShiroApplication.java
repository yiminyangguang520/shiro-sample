package com.example.springboot.shiro;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author min
 */
@SpringBootApplication
@MapperScan("com.example.springboot.shiro.mapper")
public class SpringBootShiroApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringBootShiroApplication.class, args);
  }
}
