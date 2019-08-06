package com.soto.amqp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 自动配置
 * 1. RabbitAutoConfiguration
 * 2.有自动配置了连接工厂ConnectionFactory
 * 3.RabbitProperties 封装了 RabbitMQ的配置
 * 4.RabbitTemplate:给RabbitMQ发送和接收消息
 * 5. AmqpAdmin  RabbitMQ系统管理组件
 */
@SpringBootApplication
public class AmqpApplication {

    public static void main(String[] args) {
        SpringApplication.run(AmqpApplication.class, args);
    }

}
