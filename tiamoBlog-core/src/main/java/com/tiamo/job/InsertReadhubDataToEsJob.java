package com.tiamo.job;

import com.cxytiandi.elasticjob.annotation.ElasticJobConf;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.tiamo.es.InsertDataToEsFromQueue;
import com.tiamo.webdata.topic.ReadHubTopic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;

/**
 * 定时读取 redis 中的数据到 ES 之中
 * @author wangjian
 * @version 1.0
 * @see com.tiamo.job.InsertReadhubDataToEsJob
 * @since JDK1.8
 */
@ElasticJobConf(name = "insertReadhubDataToEsJob", cron = "0 5 0/1 * *  ?", overwrite = true
        ,shardingTotalCount = 1,jobParameter = ""
        , listener = "com.tiamo.listener.ElasticJobListenerImpl"
        , monitorExecution = true, description = "读取队列数据写入到ES任务")
@Slf4j
public class InsertReadhubDataToEsJob implements SimpleJob {

    @Resource
    private InsertDataToEsFromQueue dataToEsFromQueue;

    @Override
    public void execute(ShardingContext context) {
        try {
            StopWatch stopWatch = new StopWatch("InsertReadhubDataToEsJob:execute");
            stopWatch.start();
            dataToEsFromQueue.insertData(ReadHubTopic.NEWS.getCode());
            dataToEsFromQueue.insertData(ReadHubTopic.TECHNEWS.getCode());
            dataToEsFromQueue.insertData(ReadHubTopic.BLOCKCHAIN.getCode());
            dataToEsFromQueue.insertData(ReadHubTopic.TOPIC.getCode());
            stopWatch.stop();
            // 打印运行耗时日志
//            stopWatch.prettyPrint();
            log.info(stopWatch.prettyPrint());
        } catch (Exception e) {
            log.error("【job:{}】执行异常：{}", context.getJobName(), e);
        }
    }
}
