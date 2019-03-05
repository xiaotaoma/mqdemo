package com.mxt.rabbitmq.consumer.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    public static final String DIRECT_EXCHANGE_NAME = "direct_exchange";
    public static final String DIRECT_QUEUE_NAME = "direct_queue";
    public static final String DIRECT_ROUTING_KEY = "direct_routing_key";

    @Value("${spring.rabbitmq.host}")
    private String rabbitmqHost;
    @Value("${spring.rabbitmq.port}")
    private int rabbitmqPort;
    @Value("${spring.rabbitmq.username}")
    private String username;
    @Value("${spring.rabbitmq.password}")
    private String password;
    @Value("${spring.rabbitmq.virtual-host}")
    private String virtualHost;
    @Value("${spring.rabbitmq.publisher-confirms}")
    private boolean publisherConfirms;

    private static final Logger logger = LoggerFactory.getLogger(RabbitMqConfig.class);

    @Bean
    public CachingConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(rabbitmqHost, rabbitmqPort);
        connectionFactory.setPublisherConfirms(true);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost(virtualHost);
        return connectionFactory;
    }

    @Bean
    public Queue queue() {
        return new Queue(DIRECT_QUEUE_NAME);
    }

    /*@Bean
    public SimpleMessageListenerContainer messageContainer(Queue queue, ConnectionFactory connectionFactory) {
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
    }*/

    @Bean
    public SimpleRabbitListenerContainerFactory listenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory listenerContainerFactory = new SimpleRabbitListenerContainerFactory();
        listenerContainerFactory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        listenerContainerFactory.setPrefetchCount(50);
        listenerContainerFactory.setConcurrentConsumers(10);
        listenerContainerFactory.setConnectionFactory(connectionFactory);
        return listenerContainerFactory;
    }

    @Bean
    public RabbitListenerEndpointRegistrar registrar(SimpleRabbitListenerContainerFactory listenerContainerFactory) {
        RabbitListenerEndpointRegistrar rabbitListenerEndpointRegistrar = new RabbitListenerEndpointRegistrar();
        rabbitListenerEndpointRegistrar.setContainerFactory(listenerContainerFactory);

        return rabbitListenerEndpointRegistrar;
    }
}
