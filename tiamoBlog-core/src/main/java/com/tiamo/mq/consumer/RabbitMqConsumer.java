package com.tiamo.mq.consumer;

import com.alibaba.fastjson.JSONObject;
import com.tiamo.mq.queue.QueueName;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.annotation.RabbitListeners;
import org.springframework.stereotype.Component;

/**
 * RabbitMq 消费
 * @Auther: wangjian
 */
@Slf4j
//@Component
public class RabbitMqConsumer {

    /**
     * 监听消费
     * @param message
     */
//    @RabbitHandler
//    @RabbitListener(queues = QueueName.TOPIC_BOOK)
    public void consumerDouBanBookData(Message message) {
        String value = new String(message.getBody());
        MessageProperties messageProperties = message.getMessageProperties();
        log.info("【消费数据】: 「{}」", value);
        log.info("【消費信息】: 「{}」", JSONObject.toJSONString(messageProperties));
    }

//    @RabbitHandler
//    @RabbitListener(queues = QueueName.TOPIC_BOOK)
    public void consumerDouBanBookData2(Message message) {
        String value = new String(message.getBody());
        MessageProperties messageProperties = message.getMessageProperties();
        log.info("【消费数据2222】: 「{}」", value);
        log.info("【消費信息22222】: 「{}」", JSONObject.toJSONString(messageProperties));
    }
}
