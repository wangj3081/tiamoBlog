package com.tiamo.webdata;

import com.tiamo.redis.RedisQueue;
import com.tiamo.webdata.topic.ReadHubTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 读取 ReadHub 数据
 */
public class ReadHubData {

  private ExecutorService executorService = Executors.newFixedThreadPool(5);
  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  public void start(RedisQueue redisQueue) {
    logger.info("【启动读取 readhub 数据线程池】");
    ReadHubTopic.mapReadHub.entrySet().forEach(entry->{
      executorService.submit(new ReadDataThread(entry.getKey(), redisQueue));
    });
  }


}
