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

  /**
   * 拉取所有主题数据
   * @param redisQueue
   */
  public void start(RedisQueue redisQueue) {
    logger.info("【启动读取 readhub 数据线程池】");
    ReadHubTopic.mapReadHub.entrySet().forEach(entry->{
      executorService.submit(new ReadDataThread(entry.getKey(), redisQueue));
    });
  }

  /**
   * 读取指定主题数据
   * @param redisQueue
   * @param topic
   */
  public void start(RedisQueue redisQueue, String topic) {
    logger.info("【启动读取 readhub 主题:{} 数据】", topic);
    ReadHubTopic readHubTopic = ReadHubTopic.mapReadHub.get(topic);
    if (readHubTopic != null) {
      executorService.submit(new ReadDataThread(readHubTopic.getCode(), redisQueue));
    }
  }


}
