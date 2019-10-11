package com.tiamo.webdata;

import com.tiamo.redis.RedisQueue;
import com.tiamo.util.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.OffsetTime;

/**
 * 读取数据线程类
 */
public class ReadDataThread implements Runnable {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());
  private String  url = "https://api.readhub.cn/";
  /**
   * 指定的topic
   */
  private String topic;
  /**
   * 写入 redis 的 queue
   */
  private RedisQueue redisQueue;

  public ReadDataThread(String topic, RedisQueue redisQueue) {
    this.topic = topic;
    this.redisQueue = redisQueue;
  }


  @Override
  public void run() {
    getReadHubData();
  }

  /**
   * 获取指定 topic 的数据，写入 redis 队列之中
   */
  public void getReadHubData() {
    if (redisQueue == null) {
      logger.error("【读取readhub数据错误，topic: {}】RedisQueue 为 Null", this.topic);
      return;
    }
    url = url + topic;
    long time = LocalDate.now().atTime(OffsetTime.MIN).toEpochSecond();
    String urlVal = url.intern() + "?lastCursor=" + time + "&pageSize=10";
    String result = HttpUtil.doGet(urlVal);
    if (result != null && !"".equals(result.trim())) {
      boolean bFlag = redisQueue.writeQueue(topic, result);
      logger.info("【写入readhub数据结果】{}、topic:{}", bFlag ? "成功" : "失败", this.topic);
    }
  }
}
