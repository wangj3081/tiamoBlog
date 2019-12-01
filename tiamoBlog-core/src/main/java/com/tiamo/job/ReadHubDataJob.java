package com.tiamo.job;

import com.cxytiandi.elasticjob.annotation.ElasticJobConf;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.tiamo.redis.RedisQueue;
import com.tiamo.webdata.ReadHubData;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

/**
 * 关于 elastic-job-spring-boot-starter 这是开源项目地址，可查看相应内容，这里就不做事件追踪的配置了
 * https://github.com/yinjihuan/elastic-job-spring-boot-starter
 * 执行获取 readhub 数据任务
 * @author wangjian
 * @version 1.0
 * @see com.tiamo.job.ReadHubDataJob
 * @since JDK1.8
 */
@ElasticJobConf(name = "readhubDataJob", cron = "0 0 0/1 * * ? "
        , shardingTotalCount = 1, description = "每到一个小时整点获取一次readhub数据"
        , monitorExecution = true, listener = "com.tiamo.listener.ElasticJobListenerImpl"
        // overwrite 默认为false 则表达式在运行一次后不会修改，需要手动清除注册到 ZooKeeper 中的数据才能写入新的
        , overwrite = true)
@Slf4j
public class ReadHubDataJob implements SimpleJob {
    @Resource
    private RedisQueue redisQueue;

    @Override
    public void execute(ShardingContext shardingContext) {
        try {
            new ReadHubData().start(redisQueue);
        } catch (Exception e) {
            log.error("【Job:{}，执行异常】: {}", shardingContext.getJobName(), e);
        }
    }
}
