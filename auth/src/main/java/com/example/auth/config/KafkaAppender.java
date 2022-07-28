package com.example.auth.config;

//import ch.qos.logback.classic.spi.LoggingEvent;

import ch.qos.logback.classic.spi.LoggingEvent;
import com.alibaba.fastjson.JSONObject;
import com.example.auth.Entity.EsLog;
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
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import com.example.auth.Service.LogService;


import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.Layout;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.apache.kafka.clients.producer.KafkaProducer;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;


@Component
public class KafkaAppender<E> extends AppenderBase<E> {
    //此处,logback.xml中的logger的name属性,输出到本地
    public static ThreadPoolExecutor executor;

    private static final Logger log = LoggerFactory.getLogger("local");
    protected Layout<E> layout;
//    @Autowired
//    private KafkaTemplate<String, String>  kafkaTemplate;;//kafka生产者
    private final static String TOPIC_NAME = "kafka-log-topic";

    protected static final String KAFKA_LOGGER_NAME_PREFIX = "org.apache.kafka.";

    protected  String servers;

    protected String keySerializer;


    protected static KafkaProducer<String, String> producer;

    protected String valueSerializer;


    static {
        executor=new ThreadPoolExecutor(10,20,60, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(1000), new CustomizableThreadFactory("LogThread-pool-"),
                new ThreadPoolExecutor.AbortPolicy());

    }

    @Override
    public void start() {
        Assert.notNull(layout, "you don't set the layout of KafkaAppender");
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        properties.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                keySerializer == null
                        ? "org.apache.kafka.common.serialization.StringSerializer"
                        : keySerializer);
        properties.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                valueSerializer == null
                        ? "org.apache.kafka.common.serialization.StringSerializer"
                        : valueSerializer);
        properties.put(ProducerConfig.ACKS_CONFIG, "0");
        properties.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, "1000");
        producer = new KafkaProducer<>(properties);

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
//        loggingEvent
        EsLog esLog = new EsLog();
        esLog.setLogname(loggingEvent.getLoggerName());
        esLog.setThreadname(loggingEvent.getThreadName());
        esLog.setLoglevelint(loggingEvent.getLevel().levelInt);
        esLog.setLoglevel(loggingEvent.getLevel().levelStr);
        esLog.setTime(LogService.simpleDateFormat1.format(loggingEvent.getTimeStamp()));
        esLog.setMsg(loggingEvent.getMessage());
        String msg = layout.doLayout(event);
        //拼接消息内容
        ProducerRecord<String, String> producerRecord = new ProducerRecord<String, String>(
                TOPIC_NAME, JSONObject.toJSONString(esLog));
//        KafkaTemplate<String,String> kafkaTemplate = ApplicationContextUtil.applicationContext.getBean(KafkaTemplate.class);
//        LogService logService = ApplicationContextUtil.applicationContext.getBean(LogService.class);
        System.out.println("[推送数据]:" + msg);

        //不使用kafka
        executor.execute(()->{
            producer.send(producerRecord);
        });

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

    public void setServers(String servers) {
        this.servers = servers;
    }

    public void setKeySerializer(String keySerializer) {
        this.keySerializer = keySerializer;
    }

    public void setValueSerializer(String valueSerializer) {
        this.valueSerializer = valueSerializer;
    }

}
