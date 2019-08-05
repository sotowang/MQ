package com.soto.rocketmq.producer;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
@Service
public class RocketMQProducer {
    /**
     * 生产者组名
     */
    @Value("${apache.rocketmq.producer.producerGroup}")
    private String producerGroup;

    /**
     * NameServer 地址
     */
    @Value("${apache.rocketmq.namesrvAddr}")
    private String namesrvAddr;

    public void defaultMQProducer() {

        //生产者的组名
        DefaultMQProducer producer = new DefaultMQProducer(producerGroup);

        //指定NameServer地址 ,多个地址以; 隔开
        producer.setNamesrvAddr(namesrvAddr);
        try {

            /**
             * Producer对象 在使用之前必须要调用start初始化,初始化一次即可
             * 注意:切记不可在每次发送消息时,都调用start方法
             */
            producer.start();

            StopWatch stop = new StopWatch();
            stop.start();


            for (int i = 0; i < 5; i++) {
                //创建一个消息实例,包含topic  tag  和消息体
                Message message = new Message("test_quick_topic",
                        "TagA",  //标签
                        "key"+i,  //用户自定义的key,唯一标识
                        ("发送消息...."+i).getBytes()  //消息内容实体
                );

//                SendResult result = producer.send(message, new MessageQueueSelector() {
//                    @Override
//                    public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
//                        Integer id = (Integer) arg;
//                        int index = id % mqs.size();
//                        return mqs.get(index);
//                    }
//
//                }, 1);

                /**
                 * 1. 同步发消息
                 */
//                SendResult result = producer.send(message);
//                System.out.println("消息发出:   "+ result);

                /**
                 * 2. 异步发消息
                 */
                producer.send(message, new SendCallback() {
                    @Override
                    public void onSuccess(SendResult sendResult) {
                        System.out.println("msgId: " + sendResult.getMsgId() +
                                "sendStatus: " + sendResult.getSendStatus());

                    }

                    @Override
                    public void onException(Throwable e) {

                    }
                });
            }
            stop.stop();

            System.out.println("______-----------发送10条消息耗时 :" + stop.getTotalTimeMillis());

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            producer.shutdown();
        }


    }
}
