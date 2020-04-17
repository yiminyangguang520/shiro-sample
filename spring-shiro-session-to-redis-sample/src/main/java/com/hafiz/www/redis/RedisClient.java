package com.hafiz.www.redis;

import java.util.Set;
import org.crazycake.shiro.RedisManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Desc: Jedis 操作客户端 Created by hafiz.zhang on 2017/7/21.
 */
public class RedisClient extends RedisManager {

  private static final Logger LOGGER = LoggerFactory.getLogger(RedisClient.class);

  private JedisPool jedisPool;

  public RedisClient(JedisPool jedisPool) {
    this.jedisPool = jedisPool;
  }

  @Override
  public byte[] get(byte[] key) {
    Jedis jedis = jedisPool.getResource();
    byte[] value;

    try {
      value = jedis.get(key);
    } catch (Exception e) {
      LOGGER.error("redis key:{} get value occur exception", new String(key));
      throw new RuntimeException("redis operation error:", e);
    } finally {
      jedis.close();
    }

    return value;
  }

  public byte[] set(byte[] key, byte[] value) {
    Jedis jedis = jedisPool.getResource();

    try {
      jedis.set(key, value);
      Integer expire = getTimeout();
      if (expire != 0) {
        jedis.expire(key, expire);
      }
    } catch (Exception e) {
      LOGGER.error("redis key:{} set value:{} occur exception", new String(key), new String(value));
      throw new RuntimeException("redis operation error:", e);
    } finally {
      jedis.close();
    }

    return value;
  }

  @Override
  public byte[] set(byte[] key, byte[] value, int expire) {
    Jedis jedis = jedisPool.getResource();

    try {
      jedis.set(key, value);
      if (expire != 0) {
        jedis.expire(key, expire);
      }
    } catch (Exception e) {
      LOGGER.error("redis key:{} set value:{} in expire:{} occur exception", new String(key), new String(value), expire);
      throw new RuntimeException("redis operation error:", e);
    } finally {
      jedis.close();
    }

    return value;
  }

  @Override
  public void del(byte[] key) {
    Jedis jedis = jedisPool.getResource();

    try {
      jedis.del(key);
    } catch (Exception e) {
      LOGGER.error("redis key:{} del value occur exception", new String(key));
      throw new RuntimeException("redis operation error:", e);
    } finally {
      jedis.close();
    }
  }

  public void flushDB() {
    Jedis jedis = jedisPool.getResource();

    try {
      jedis.flushDB();
    } catch (Exception e) {
      LOGGER.error("redis flushDB occur exception");
      throw new RuntimeException("redis operation error:", e);
    } finally {
      jedis.close();
    }

  }

  public Long dbSize() {
    Long dbSize;
    Jedis jedis = jedisPool.getResource();

    try {
      dbSize = jedis.dbSize();
    } catch (Exception e) {
      LOGGER.error("redis get dbSize occur exception");
      throw new RuntimeException("redis operation error:", e);
    } finally {
      jedis.close();
    }

    return dbSize;
  }

  public Set<byte[]> keys(String pattern) {
    Set keys;
    Jedis jedis = jedisPool.getResource();

    try {
      keys = jedis.keys(pattern.getBytes());
    } catch (Exception e) {
      LOGGER.error("redis get keys in pattern:{} occur exception", pattern);
      throw new RuntimeException("redis operation error:", e);
    } finally {
      jedis.close();
    }

    return keys;
  }
}
