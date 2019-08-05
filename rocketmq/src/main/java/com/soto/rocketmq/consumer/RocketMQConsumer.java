package com.soto.rocketmq.consumer;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class RocketMQConsumer {

    /**
     * 消费者组名
     */

    @Value("${apache.rocketmq.consumer.PushConsumer}")
    private String consumerGroup;

    /**
     * NameServer地址
     */
    @Value("${apache.rocketmq.namesrvAddr}")
    private String namesrvAddr;


    @PostConstruct
    public void defaultMQPushConsumer() {

        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(consumerGroup);

        consumer.setNamesrvAddr(namesrvAddr);

        try {

            //订阅PushTopuc下Tag为push的消息
            consumer.subscribe("test_quick_topic", "*");

            //设置Consumer第一次启动是从队列头部开始消费 还是队列尾部开始消费
            //如果非第一次启动,那么按照上次消费的位置继续消费
            consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);

            consumer.registerMessageListener((MessageListenerConcurrently) (list, context) -> {
                try {
                    for (MessageExt messageExt : list) {
                        String topic = messageExt.getTopic();
                        String tags = messageExt.getTags();
                        String keys = messageExt.getKeys();
                        String body = new String(messageExt.getBody());

                        System.out.println("消费者响应: ...  topic: " + topic + ",  tags : " + tags + ", keys: " + keys + ", body " + body);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    int reconsumeTimes = list.get(0).getReconsumeTimes();
                    System.out.println("目前重发次数: " + reconsumeTimes);
                    if (reconsumeTimes == 3) {
                        //记录日志
                        //补偿处理
                        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;

                    }
                    //稍后再试
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }
                //消费成功
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            });
            consumer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }
}

