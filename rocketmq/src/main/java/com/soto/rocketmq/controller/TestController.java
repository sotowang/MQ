package com.soto.rocketmq.controller;

import com.soto.rocketmq.producer.RocketMQProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    RocketMQProducer rocketMQProvider;

    @GetMapping("/test")
    public String testMq() {
        rocketMQProvider.defaultMQProducer();
        return null;
    }
}
