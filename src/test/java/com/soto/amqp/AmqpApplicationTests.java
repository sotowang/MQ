package com.soto.amqp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AmqpApplicationTests {

    @Autowired
    RabbitTemplate rabbitTemplate;

    /**
     * 1.单播(点对点)
     */
    @Test
    public void contextLoads() {
        //Message需要自己定义;定义消息体内容和消息头
        //rabbitTemplate.send();

        //只需要传入要发送的对象,自动序列化发送给rabbitMQ
        //rabbitTemplate.convertAndSend();
        Map<String, Object> map = new HashMap<>();
        map.put("msg","这是一个消息 ");
        map.put("data", Arrays.asList("helloworld", 123, true));

        //对象默认序列化发送
        rabbitTemplate.convertAndSend("exchange.direct","atguigu.news",map);
    }

    @Test
    public void receive() {

        Object o = rabbitTemplate.receiveAndConvert("atguigu.news");
        System.out.println(o.getClass());
        System.out.println(o);
    }

}
