package com.mxt.rabbitmq.producer.service;

import com.mxt.rabbitmq.producer.config.RabbitMqConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ProducerService implements RabbitTemplate.ConfirmCallback{
    private static AtomicInteger counter = new AtomicInteger();
    private RabbitTemplate rabbitTemplate;

    @Autowired
    public ProducerService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitTemplate.setConfirmCallback(this::confirm);
    }

    public void send(Object msg) {
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        int i = counter.incrementAndGet();
        System.out.println("send:" + correlationData.getId() + ", " + msg + "\t\t\t" + i);
        rabbitTemplate.convertAndSend(RabbitMqConfig.DIRECT_EXCHANGE_NAME, RabbitMqConfig.DIRECT_ROUTING_KEY, msg, correlationData);
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean b, String s) {
        System.out.println("confirm:" + b + ",msgId:" + correlationData.getId() + ",errorMsg:" + s);
        if (b) {
            System.out.println("消息发送成功:" + correlationData.getId());
        }else {
            System.out.println("消息发送失败:" + correlationData.getId() + ",error message:" + s);
        }
    }
}
