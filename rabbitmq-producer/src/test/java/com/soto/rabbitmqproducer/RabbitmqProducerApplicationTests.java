package com.soto.rabbitmqproducer;

import com.soto.entity.Order;
import com.soto.producer.OrderSender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitmqProducerApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Autowired
    private OrderSender orderSender;
    @Test
    public void testSendOrder1() {
        Order order = new Order();
        order.setId("2019080500001");
        order.setMessageId(System.currentTimeMillis()+"$"+ UUID.randomUUID().toString());
        order.setName("测试订单1");
        orderSender.send(order);

    }
}
