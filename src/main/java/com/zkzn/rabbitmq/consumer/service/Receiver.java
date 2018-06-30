package com.zkzn.rabbitmq.consumer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.zkzn.rabbitmq.consumer.config.RabbitMqConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class Receiver {
    private static final Logger logger = LoggerFactory.getLogger(Receiver.class);

    /**
     * 消息监听,单线程处理
     * @param message
     * @param channel
     */
    @RabbitListener(queues = RabbitMqConfig.DIRECT_QUEUE_NAME)
    public void consumer1(Message message, Channel channel) {
        resolveMsg(message, channel);
    }

    /*@RabbitListener(queues = RabbitMqConfig.DIRECT_QUEUE_NAME)
    public void consumer2(Message message, Channel channel) {
        resolveMsg(message, channel);
    }*/

    private void resolveMsg(Message message, Channel channel) {
        logger.info(Thread.currentThread().getId() + ":receive message:" + new String(message.getBody()) +"\t" + DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS").format(LocalDateTime.now()));
        try {
            //确认消息成功消费,false只确认当前一个消息收到，true确认所有consumer获得的消息
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //重新投递
        //channel.basicRecover(false);
        //ack返回false
        //第二个参数multiple：批量确认标志。如果值为true，包含本条消息在内的、所有比该消息deliveryTag值小的 消息都被拒绝了（除了已经被 ack 的以外）;如果值为false，只拒绝三本条消息
        //第三个参数requeue：表示如何处理这条消息，如果值为true，则重新放入RabbitMQ的发送队列，如果值为false，则通知RabbitMQ销毁这条消息
        //channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);

        //拒绝消息，
        //第一个参数deliveryTag：发布的每一条消息都会获得一个唯一的deliveryTag，deliveryTag在channel范围内是唯一的
        //第二个参数requeue：表示如何处理这条消息，如果值为true，则重新放入RabbitMQ的发送队列，如果值为false，则通知RabbitMQ销毁这条消息
        //channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
    }
}
