package com.soto.rocketmq.consumer;

import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.swing.tree.ExpandVetoException;

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
            consumer.subscribe("Topic", "push");

            //设置Consumer第一次启动是从队列头部开始消费 还是队列尾部开始消费
            //如果非第一次启动,那么 按照 上次消费的位置继续消费
            consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
            consumer.registerMessageListener((MessageListenerConcurrently) (list, context) -> {
                try {
                    for (MessageExt messageExt : list) {
                        System.out.println("messageExt: " + messageExt);

                        String messageBody = new String(messageExt.getBody());

                        //输出消息内容
                        System.out.println("消费响应:msgId: " + messageExt.getMsgId() + ", msgBody: " + messageBody);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
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

