package com.example.auth.config;

import ch.qos.logback.classic.spi.LoggingEvent;
import com.example.auth.utils.ApplicationContextUtil;
import lombok.SneakyThrows;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;



import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.Layout;
import org.springframework.util.concurrent.ListenableFuture;

@Component
public class KafkaAppender<E> extends AppenderBase<E> {
    //此处,logback.xml中的logger的name属性,输出到本地
    private static final Logger log = LoggerFactory.getLogger("local");
    protected Layout<E> layout;
//    @Autowired
//    private KafkaTemplate<String, String>  kafkaTemplate;;//kafka生产者
    private final static String TOPIC_NAME = "kafka-log-topic";

    @Override
    public void start() {
        Assert.notNull(layout, "you don't set the layout of KafkaAppender");
        super.start();
    }

    @Override
    public void stop() {
        super.stop();
        KafkaTemplate<String,String> kafkaTemplate = ApplicationContextUtil.applicationContext.getBean(KafkaTemplate.class);
//        kafkaTemplate.destroy();
        System.out.println("[Stopping KafkaAppender !!!]");
    }


    @SneakyThrows
    @Override
    protected void append(E event) {
        //threadName （logname，levelInt，levelStr）
        LoggingEvent loggingEvent= (LoggingEvent) event;
//        loggingEvent.

        String msg = layout.doLayout(event);
        //拼接消息内容
//        ProducerRecord<String, String> producerRecord = new ProducerRecord<String, String>(
//                TOPIC_NAME, msg);
        System.out.println("[推送数据]:" + msg);
        //不使用kafka


        //发送kafka的消息

//        KafkaTemplate<String,String> kafkaTemplate = ApplicationContextUtil.applicationContext.getBean(KafkaTemplate.class);
//
//        ListenableFuture<SendResult<String, String>> send = kafkaTemplate.send(TOPIC_NAME,"key",msg);
//        String s = send.get().toString();
//        System.out.println(s);

    }
    public Layout<E> getLayout() {
        return layout;
    }
    public void setLayout(Layout<E> layout) {
        this.layout = layout;
    }

}
