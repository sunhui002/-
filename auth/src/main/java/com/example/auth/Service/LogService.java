package com.example.auth.Service;

import ch.qos.logback.classic.spi.LoggingEvent;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@Service
public class LogService {

    private final RequestOptions options = RequestOptions.DEFAULT;

    @Autowired
    RestHighLevelClient client;

    public static SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
    public final String LOG="log";

    public void uploadlog(LoggingEvent loggingEvent ){
        Calendar calendar=Calendar.getInstance();
        String nowtime = simpleDateFormat.format(calendar.getTime());
        String index=nowtime+"_"+LOG;
        if(!checkIndex(index)){
//            createIndex()
        }
    }

    /**
     * 判断索引是否存在
     */
    public boolean checkIndex (String index) {
        try {
            return client.indices().exists(new GetIndexRequest(index), options);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Boolean.FALSE ;
    }
}
