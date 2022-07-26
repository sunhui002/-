package com.example.auth.Service;

import ch.qos.logback.classic.spi.LoggingEvent;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@Service
public class LogService {

    private final RequestOptions options = RequestOptions.DEFAULT;

    @Autowired
    RestHighLevelClient client;

    public static Map<String,Object> indexmap;

    static {
        indexmap=new HashMap<>();
        HashMap<String, String> lognametype = new HashMap<>();
        lognametype.put("type","keyword");
        indexmap.put("logname",lognametype);

        HashMap<String, String> threadnametype = new HashMap<>();
        threadnametype.put("type","keyword");
        indexmap.put("threadname",threadnametype);

        HashMap<String, String> loglevelinttype = new HashMap<>();
        loglevelinttype.put("type","long");
        indexmap.put("loglevelint",loglevelinttype);
        HashMap<String, String> logleveltype = new HashMap<>();
        logleveltype.put("type","keyword");
        indexmap.put("loglevel",logleveltype);
        HashMap<String, String> timetype = new HashMap<>();
        timetype.put("type","date");
        indexmap.put("time",timetype);
        HashMap<String, String> msgtype = new HashMap<>();
        msgtype.put("type","text");
        indexmap.put("msg",msgtype);

    }

    public static SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
    public final String LOG="log";

    public void uploadlog(LoggingEvent loggingEvent ){
        Calendar calendar=Calendar.getInstance();
        String nowtime = simpleDateFormat.format(calendar.getTime());
        String index=nowtime+"_"+LOG;
        if(!checkIndex(index)){
            createIndex(index,indexmap);
        }
        HashMap<String, Object> log = new HashMap<>();
        log.put("logname",loggingEvent.getLoggerName());
        log.put("threadname",loggingEvent.getThreadName());
        log.put("loglevelint",loggingEvent.getLevel().levelInt);
        log.put("loglevel",loggingEvent.getLevel().levelStr);
        log.put("time",nowtime);
        log.put("msg",loggingEvent.getMessage());
        insert(index,log);

    }

    public boolean insert (String indexName, Map<String,Object> dataMap){
        try {
            BulkRequest request = new BulkRequest();
            request.add(new IndexRequest(indexName)
                    .opType("create").source(dataMap, XContentType.JSON));
            this.client.bulk(request, options);
            return Boolean.TRUE ;
        } catch (Exception e){
            e.printStackTrace();
        }
        return Boolean.FALSE;
    }

    public boolean createIndex (String indexName ,Map<String, Object> columnMap){
        try {
            if(!checkIndex(indexName)){
                CreateIndexRequest request = new CreateIndexRequest(indexName);
                if (columnMap != null && columnMap.size()>0) {
                    Map<String, Object> source = new HashMap<>();
                    source.put("properties", columnMap);
                    request.mapping(source);
                }
                this.client.indices().create(request, options);
                return Boolean.TRUE ;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Boolean.FALSE;
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
