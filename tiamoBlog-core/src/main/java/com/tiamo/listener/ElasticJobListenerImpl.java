package com.tiamo.listener;

import com.dangdang.ddframe.job.executor.ShardingContexts;
import com.dangdang.ddframe.job.lite.api.listener.ElasticJobListener;
import lombok.extern.slf4j.Slf4j;

/**
 * elasticJob 执行任务的监听器,需要在Job中配置了方可使用
 * @author wangjian
 * @version 1.0
 * @see ElasticJobListenerImpl
 * @since JDK1.8
 */
@Slf4j
public class ElasticJobListenerImpl implements ElasticJobListener {


    @Override
    public void beforeJobExecuted(ShardingContexts shardingContexts) {
        // 任务执行前
        log.info("【开始执行Job任务:{}】", shardingContexts.getJobName());
    }

    @Override
    public void afterJobExecuted(ShardingContexts shardingContexts) {
        // 任务执行后
        log.info("【Job任务:{} 执行完毕】", shardingContexts.getJobName());
    }
}
