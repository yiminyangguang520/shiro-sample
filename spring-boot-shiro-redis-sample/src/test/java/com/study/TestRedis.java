package com.study;


import com.study.model.User;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.exception.SerializationException;
import org.crazycake.shiro.serializer.ObjectSerializer;
import org.junit.Test;

/**
 * Created by yangqj on 2018/1/13.
 */
public class TestRedis {

  @Test
  public void test() throws SerializationException {
    RedisManager redisManager = new RedisManager();

    User user = new User();
    user.setId(2);
    user.setUsername("admin");
    user.setPassword("3esdfffdsdfergfwdfdsfsdfewer");
    user.setEnable(1);
    ObjectSerializer objectSerializer = new ObjectSerializer();
    byte[] serialize = objectSerializer.serialize(user);
    redisManager.set(serialize, "testvalue".getBytes(), 0);
  }


  @Test
  public void test2() throws SerializationException {
    RedisManager redisManager = new RedisManager();
    User user = new User();
    user.setId(2);
    user.setUsername("admin");
    user.setPassword("3esdfffdsdfergfwdfdsfsdfewer");
    user.setEnable(1);
    ObjectSerializer objectSerializer = new ObjectSerializer();
    byte[] serialize = objectSerializer.serialize(user);
    redisManager.del(serialize);
  }
}
