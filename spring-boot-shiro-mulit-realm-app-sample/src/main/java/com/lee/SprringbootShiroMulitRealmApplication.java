package com.lee;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author min
 */
@SpringBootApplication
@MapperScan("com.lee.dao")
public class SprringbootShiroMulitRealmApplication {

  public static void main(String[] args) {
    SpringApplication.run(SprringbootShiroMulitRealmApplication.class, args);
  }
}
