package com.tiamo;

import com.tiamo.redis.RedisQueue;
import com.tiamo.webdata.ReadHubData;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * Created by wangjian on 2019/3/9.
 */
@SpringBootApplication
public class Application {
    @Resource
    private RedisQueue redisQueue;

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

    // todo 测试，会单独写一个定时任务去获取数据
    @PostConstruct
    public void getReadHubData() {
      new ReadHubData().start(redisQueue);
    }
}
