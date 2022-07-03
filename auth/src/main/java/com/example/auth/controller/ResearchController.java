package com.example.auth.controller;

import com.example.auth.Dao.AttrRepository;
import com.example.auth.Dao.ProductDao;
import com.example.auth.Entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

@Controller
@RequestMapping("/api")
public class ResearchController {

    @Autowired
    ProductDao productDao;

    @ResponseBody
    @GetMapping("/es/researchProduct")
    public MessageResult researchProduct(@RequestParam(value = "description") String description) throws UnsupportedEncodingException {
        String decode = URLDecoder.decode(
                description, "UTF-8"
        );
        List<Product> research = productDao.research(decode);

        return MessageResult.success(research);
    }
    @Autowired
    AttrRepository attrRepository;

//    @ResponseBody
//    @GetMapping("/es")
//    public MessageResult testes()  {
//////        List<EsAttr> findattr = productDao.findattr("1");、
////        List<Spu> spus = productDao.findaAllSpu();
////        List<String> allSpuid=new ArrayList<>();
////         spus.stream().forEach((spu)->allSpuid.add(spu.spuId));
////         Map<String,List<EsProduct>> Esproductmap=new HashMap<>();
////        allSpuid.forEach((spuid)->{
////            List<Spu> spuslist = productDao.findaSpubyspuid(spuid);
////            List<EsAttr> findattr = productDao.findattr(spuid);
////            Map<String,List<EsAttr>> attrMap=new HashMap<>();
////            findattr.stream().forEach((esAttr)->{
////                List<EsAttr> orDefault = attrMap.getOrDefault(esAttr.attrname, new ArrayList<>());
////               orDefault.add(esAttr);
////            });
////            List<List<EsAttr>> values = (List<List<EsAttr>>) attrMap.values();
//////            List<EsProduct> esProductList=new ArrayList<>();
//////            Esproductmap.put(spuid,esProductList);
////            List<EsAttr> alist=values.get(0);
////            List<List<EsAttr>> sumlist=new ArrayList<>();
////            for (int i = 0; i <values.size() ; i++) {
////                List<EsAttr> blist = values.get(i);
////
////            }
////
////           });
////        });
////
////
////
////
////        List<EsProduct> esProduct = productDao.findEsProduct();
////
////          esProduct.stream().forEach((product)->attrRepository.save(product));
////        Iterable<EsProduct> all = attrRepository.findAll();
////
////        return MessageResult.success(esProduct);
//    }
//
//
}
