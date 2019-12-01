package com.tiamo.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * redis 队列
 */
@Component
public class RedisQueue {

  @Resource
  private RedisTemplate<String, String> redisTemplate;

  /**
   * 写入队列
   * @param key  写入的新闻类别 key
   * @param value 内容
   * @return
   */
  public  boolean writeQueue(String key, String value) {
    Long rightPush = redisTemplate.opsForList().rightPush(key, value);
    return rightPush > 0;
  }

  /**
   * 弹出队列
   * @param topic 存储主题
   * @return
   */
  public String readQueue(String topic) {
    String value = redisTemplate.opsForList().leftPop(topic);
    return value;
  }

  /**
   * 队列内的数据长度
   * @param topic
   * @return
   */
  public Long queueLen(String topic) {
    return  redisTemplate.opsForList().size(topic);
  }

}
