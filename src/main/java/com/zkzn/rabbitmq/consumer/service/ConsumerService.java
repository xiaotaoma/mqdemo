package com.zkzn.rabbitmq.consumer.service;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ConsumerService {
    /*@Autowired
    private Queue queue;
    @Autowired
    private ConnectionFactory connectionFactory;

    @Bean
    public SimpleMessageListenerContainer messageContainer() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        container.setQueues(queue);
        container.setExposeListenerChannel(true);
        container.setMaxConcurrentConsumers(1);
        container.setConcurrentConsumers(1);
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL); //设置确认模式手工确认
        container.setMessageListener(new ChannelAwareMessageListener() {
            @Override
            public void onMessage(Message message, Channel channel) throws Exception {
                byte[] body = message.getBody();
                System.out.println(Thread.currentThread().getName() + ":" + message.getMessageProperties().getDeliveryTag() + ", " + new String(body));

                int process = process(new String(message.getBody()));
                if (process == 1) {
                    channel.basicAck(message.getMessageProperties().getDeliveryTag(), false); //确认消息成功消费,false只确认当前一个消息收到，true确认所有consumer获得的消息
                }else {
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
        });
        return container;
    }

    *//**
     *
     * @param message
     * @return 1消息出来成功，其他，消息处理失败
     *//*
    private int process(String message) {
        //消息处理
        return 1;
    }*/

}
