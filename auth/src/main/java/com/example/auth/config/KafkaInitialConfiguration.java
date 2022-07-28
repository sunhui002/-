package com.example.auth.config;


import ch.qos.logback.classic.spi.LoggingEvent;
import com.alibaba.fastjson.JSONObject;
import com.example.auth.Entity.EsLog;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.adapter.RecordFilterStrategy;

@Configuration
public class KafkaInitialConfiguration {

    // 监听器工厂
    @Autowired
    private ConsumerFactory consumerFactory;

    // 配置一个消息过滤策略
    @Bean
    public ConcurrentKafkaListenerContainerFactory myFilterContainerFactory() {
        ConcurrentKafkaListenerContainerFactory factory =
                new ConcurrentKafkaListenerContainerFactory();
        factory.setConsumerFactory(consumerFactory);
        // 被过滤的消息将被丢弃
        factory.setAckDiscarded(true);
        // 消息过滤策略（将消息转换为long类型，判断是奇数还是偶数，把所有奇数过滤，监听器只接收偶数）
        factory.setRecordFilterStrategy(new RecordFilterStrategy() {
            @Override
            public boolean filter(ConsumerRecord consumerRecord) {
                EsLog eslog= JSONObject.parseObject((String) consumerRecord.value(), EsLog.class);

//                if (data % 2 == 0) {
                    return false;
//                }
                //返回true将会被丢弃
//                return true;
            }
        });
        return factory;
    }
}
