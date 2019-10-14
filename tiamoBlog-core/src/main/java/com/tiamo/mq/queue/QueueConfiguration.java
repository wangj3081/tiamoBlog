package com.tiamo.mq.queue;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建 Queue
 * @Auther: wangjian
 */
@Slf4j
//@Configuration
public class QueueConfiguration {

//    @Bean
    public List<Binding> getBinding(DirectExchange exchange, List<Queue> queue) {

        List<Binding> bindingList = new ArrayList<>();
        queue.forEach(entity->{
            log.info("「交换机:{}、队列名:{}」",exchange.getName(), entity.getName());
            Binding binding = BindingBuilder.bind(entity).to(exchange).with(entity.getName());
            bindingList.add(binding);
        });
        return bindingList;
    }

    // 交换机、是否持久化、是否消费后自动删除
//    @Bean
    public DirectExchange getDirectDouBanExchange() {
        log.info("【创建点对点交换机】");
        return new DirectExchange(ExchangeConstant.EXCHANGE_DOUBAN, true, false);
    }

//    @Bean
    public TopicExchange getTopicExchange() {
        log.info("【創建訂閲主題交換機】");
        return new TopicExchange(ExchangeConstant.TOPIC_DOUBAN, true, false);
    }

//    @Bean
    public Queue douBanQueue() {
        log.info("【创建队列】");
        return new Queue(QueueName.DOUBAN_BOOK);
    }

//    @Bean
    public Queue douBanQueue2() {
        log.info("【创建队列】");
        return new Queue(QueueName.DOUBAN_MOVIE);
    }

//    @Bean
    public Queue topicQueue() {
        return new Queue(QueueName.TOPIC_BOOK);
    }

}
