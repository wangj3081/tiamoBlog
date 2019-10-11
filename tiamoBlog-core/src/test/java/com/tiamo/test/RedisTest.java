package com.tiamo.test;

import com.tiamo.Application;
import com.tiamo.redis.RedisQueue;
import com.tiamo.webdata.topic.ReadHubTopic;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class RedisTest {

  @Resource
  private RedisTemplate<String,String> redisTemplate;
  @Resource
  private RedisQueue redisQueue;


  @Test
  public void redisQueueRead() {
    ReadHubTopic.mapReadHub.entrySet().forEach(entity->{
      System.out.println(entity.getValue().getText()+":"+redisQueue.queueLen(entity.getKey()));
      for (int i = 0; i < redisQueue.queueLen(entity.getKey()); i++) {
        System.out.println(redisQueue.readQueue(entity.getKey()));
      }
    });


  }


  @Test
  public void redisQueueTest() {
    System.out.println(redisQueue.writeQueue("listval", "queue1"));
    System.out.println(redisQueue.writeQueue("listval", "queue2"));
    try {
      TimeUnit.SECONDS.sleep(10);
      for (int i = 0; i < 2; i++) {
        String listval = redisQueue.readQueue("listval");
        System.out.println(listval);
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }


  @Test
  public void redisTest() {
    redisTemplate.opsForValue().set("testredis", "test",100, TimeUnit.SECONDS);
  }

}
