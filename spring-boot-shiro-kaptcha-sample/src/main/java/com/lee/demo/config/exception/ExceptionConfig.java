package com.lee.demo.config.exception;

import java.util.Properties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

/**
 *
 * @author min
 */
//使用统一异常处理类MyExceptionResolver来处理403异常。该配置也可以处理。
//@Configuration
public class ExceptionConfig {

  @Bean
  public SimpleMappingExceptionResolver resolver() {
    SimpleMappingExceptionResolver resolver = new SimpleMappingExceptionResolver();
    Properties properties = new Properties();
    properties.setProperty("org.apache.shiro.authz.UnauthorizedException", "/403");
    resolver.setExceptionMappings(properties);
    return resolver;
  }
}
