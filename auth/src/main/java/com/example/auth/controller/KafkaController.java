package com.example.auth.controller;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class KafkaController {
    private final static String TOPIC_NAME = "my-replicated-topic";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @RequestMapping("/send")
    public String send(@RequestParam("msg") String msg) {
        kafkaTemplate.send(TOPIC_NAME, "key", msg);
        return String.format("消息 %s 发送成功！", msg);
    }

    @KafkaListener(topics = "kafka-log-topic", groupId = "jihuGroup",containerFactory = "myFilterContainerFactory")
    public void listenJihuGroup(ConsumerRecord<String, String> record) {
        String value = record.value();
        System.out.println("jihuGroup message: " + value);
//        System.out.println("jihuGroup record: " + record);
        //手动提交offset，一般是提交一个banch，幂等性防止重复消息
        // === 每条消费完确认性能不好！
//        ack.acknowledge();
    }

    //配置多个消费组
    @KafkaListener(topics = "my-replicated-topic", groupId = "jihuGroup2",containerFactory = "myFilterContainerFactory")
    public void listenJihuGroup2(ConsumerRecord<String, String> record, Acknowledgment ack) {
        String value = record.value();
        System.out.println("jihuGroup2 message: " + value);
        System.out.println("jihuGroup2 record: " + record);
        //手动提交offset
        ack.acknowledge();
    }

}
