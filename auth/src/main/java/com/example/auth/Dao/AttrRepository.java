package com.example.auth.Dao;

import com.example.auth.Entity.EsProduct;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@ResponseBody
public interface AttrRepository extends ElasticsearchRepository<EsProduct,String> {

       List<EsProduct> findEsProductBySpuTitle(String spuTitle);
}
