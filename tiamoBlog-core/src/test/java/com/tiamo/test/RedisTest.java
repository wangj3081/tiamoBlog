package com.tiamo.test;

import com.alibaba.fastjson.JSONObject;
import com.tiamo.Application;
import com.tiamo.es.InsertDataToEsFromQueue;
import com.tiamo.es.index.ReadHubIndex;
import com.tiamo.redis.RedisQueue;
import com.tiamo.search.service.SearchReadHubService;
import com.tiamo.webdata.ReadHubData;
import com.tiamo.webdata.topic.ReadHubTopic;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class RedisTest {

  @Resource
  private RedisTemplate<String,String> redisTemplate;
  @Resource
  private RedisQueue redisQueue;
  @Resource
  private InsertDataToEsFromQueue insertDataToEsFromQueue;
  @Resource
  private SearchReadHubService readHubService;

  @Test
  public void searchTest() {
    Map<String, Object> result = readHubService.queryByTopicNewsLast(ReadHubTopic.TECHNEWS.getCode());
    System.out.println("************************************************");
    System.out.println(JSONObject.toJSONString(result));
    System.out.println("************************************************");
  }


  @Test
  public void  insertToEsValue() {
    insertDataToEsFromQueue.insertData(ReadHubTopic.TECHNEWS.getCode());
    try {
      TimeUnit.SECONDS.sleep(100);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void insertQueueValue() {
    new ReadHubData().start(redisQueue, ReadHubTopic.TECHNEWS.getCode());
    try {
      TimeUnit.SECONDS.sleep(6);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    insertDataToEsFromQueue.insertData(ReadHubTopic.TECHNEWS.getCode());
    try {
      TimeUnit.SECONDS.sleep(100);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }


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
