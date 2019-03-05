package com.mxt.rabbitmq.producer.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

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

    @Bean(name = "rabbitmqConnectionFactory")
    public CachingConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(rabbitmqHost, rabbitmqPort);
        connectionFactory.setPublisherConfirms(true);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost(virtualHost);
        return connectionFactory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(@Qualifier("rabbitmqConnectionFactory") CachingConnectionFactory cachingConnectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());//使用jackson 消息转换器
        rabbitTemplate.setEncoding("utf-8");
        return rabbitTemplate;
    }

    @Bean
    public Binding binding(Exchange exchange, Queue queue) {
        return BindingBuilder.bind(queue).to(exchange).with(DIRECT_QUEUE_NAME).noargs();
    }

    @Bean
    public Queue queue() {
        // 参数1 name ：队列名
        // 参数2 durable ：是否持久化
        // 参数3 exclusive ：仅创建者可以使用的私有队列，断开后自动删除
        // 参数4 autoDelete : 当所有消费客户端连接断开后，是否自动删除队列
        return new Queue(DIRECT_QUEUE_NAME, true);
    }

    @Bean
    public Exchange exchange() {
        // 参数1 name ：交互器名
        // 参数2 durable ：是否持久化
        // 参数3 autoDelete ：当所有消费客户端连接断开后，是否自动删除队列
        return new DirectExchange(DIRECT_EXCHANGE_NAME, true, false);
    }
}
