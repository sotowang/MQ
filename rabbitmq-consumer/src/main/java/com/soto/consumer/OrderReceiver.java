package com.soto.consumer;

import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import com.soto.entity.Order;
import com.soto.utils.JsonConvertUtils;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;


@Component
public class OrderReceiver {
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "order-queue",durable = "true"),
            exchange = @Exchange(value = "order-exchange",durable = "true",type = "topic"),
            key = "order.*"
    ))
    @RabbitHandler
    public void onOrderMessage(@Payload JSONObject object,
                               @Headers Map<String, Object> headers,
                               Channel channel) throws IOException {


        System.out.println("-----------收到消息,开始消费-----------");
        Order order = JsonConvertUtils.convertJSONToObject(object);

        System.out.println("订单ID:  " + order.getId());

        Long deliverTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);

        //ACK
        channel.basicAck(deliverTag, false);

    }


}
