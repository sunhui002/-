package com.example.auth.controller;

import com.alibaba.fastjson.JSON;
import com.example.auth.Entity.EsLog;
import com.example.auth.Entity.EsProduct;
import com.example.auth.Entity.MessageResult;
import com.example.auth.Enum.EsConstant;
import com.example.auth.Service.LogService;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Controller
@RequestMapping("/api")
public class LogController {

    @Autowired
    RestHighLevelClient client;



    @ResponseBody
    @GetMapping("/getinfolog")
    public MessageResult getinfolog() throws IOException {
        //1. 构建bool query
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        //1.1 bool must
       boolQueryBuilder.must(QueryBuilders.matchQuery("loglevelint", "20000"));
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(boolQueryBuilder);
        searchSourceBuilder.sort("time", SortOrder.DESC);
        Calendar calendar=Calendar.getInstance();
        String nowtime = LogService.simpleDateFormat.format(calendar.getTime());
        String index=nowtime+"_"+"log";
        SearchRequest request = new SearchRequest(new String[]{"2022-07-27_log"}, searchSourceBuilder);
        SearchResponse search = client.search(request, RequestOptions.DEFAULT);
        SearchHits hits = search.getHits();
        List<EsLog> esLogs = new ArrayList<>();
        if (hits.getHits()!=null&&hits.getHits().length>0){

            for (SearchHit hit : hits) {
                String sourceAsString = hit.getSourceAsString();
                EsLog esLog = JSON.parseObject(sourceAsString, EsLog.class);
                esLogs.add(esLog);
            }
        }

        return MessageResult.success(esLogs);
    }
}
