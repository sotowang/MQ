package com.soto.producer;

import com.soto.utils.JsonConvertUtils;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;


    public void send(com.soto.entity.Order order) {
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(order.getMessageId());
        rabbitTemplate.convertAndSend("order-exchange", //exchange
                "order.abcd", //routingKey
                JsonConvertUtils.convertObjectToJSON(order), //消息体内容
                correlationData  //消息唯一ID
        );

    }
}
