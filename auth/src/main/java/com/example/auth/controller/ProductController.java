package com.example.auth.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.auth.Dao.ProductDao;
import com.example.auth.Entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    ProductDao productDao;

    @Transactional
    @PostMapping("/add/addspuandsku")
    @ResponseBody
    public MessageResult adduploadspuandsku(@RequestBody SpuAndSku spuAndSku) {
//        SpuAndSku spuAndSku = (SpuAndSku)JSONObject.parse(spuAndSku1.toJSONString());
        Spu spu = new Spu();
        spu.setSpuImg(spuAndSku.spuImg);
        spu.setDecription(spuAndSku.decription);
        spu.setSpuTitle(spuAndSku.spuTitle);
        spu.setSpuPrice(spuAndSku.spuPrice);
        if(spuAndSku.spuid!=-1){
//            productDao.updatespu(spu);
         //剔除和增加一些属性，不会影响stock
            // 属性名和同一属性名下的属性值都不能有重复的。///
            spu.setSpuId(String.valueOf(spuAndSku.spuid));
            productDao.updatespu(spu);
            List<StockAttr> allsku = productDao.findAllattr(String.valueOf(spuAndSku.spuid));

            Map<String, List<String>> attrnameandattrmap=new HashMap<>();
            Map<String,Set<String>> attrnameandattrvaluecodemap=new HashMap<>();
            Map<String,String> attnameandskuidmap=new HashMap<>();
            allsku.stream().forEach(stockAttr -> {
                attnameandskuidmap.put(stockAttr.attrname,stockAttr.attrcode);
                List<String> orDefault = attrnameandattrmap.getOrDefault(stockAttr.attrname, new ArrayList<>());
                orDefault.add(stockAttr.attrvalue);
                attrnameandattrmap.put(stockAttr.attrname,orDefault);
                Set<String> put = attrnameandattrvaluecodemap.getOrDefault(stockAttr.attrname, new HashSet<>());
                put.add(stockAttr.attrcode);
                attrnameandattrvaluecodemap.put(stockAttr.attrname,put);
            });
            Map<String,List<String>> newattrnameandattrvaluemap=new HashMap<>();
            spuAndSku.getAttrs().forEach(uploadAttr -> {
                newattrnameandattrvaluemap.put(uploadAttr.attrname,uploadAttr.getAttrvalues());
            });
            //重复属性放到前端筛选
            List<String> deleteattrname = attrnameandattrmap.keySet().stream().filter(attrname -> !newattrnameandattrvaluemap.containsKey(attrname)).collect(Collectors.toList());
            if(deleteattrname.size()>0) deleteattrname.forEach(attrname->{
                productDao.deleteattrbyattrname(attrname,spu.spuId);
                productDao.deleteskubyattrname(attrname,spu.spuId);});
            if(deleteattrname.size()<newattrnameandattrvaluemap.size()){
                List<String> addattrname = newattrnameandattrvaluemap.keySet().stream().filter(attrname -> !attrnameandattrmap.containsKey(attrname)).collect(Collectors.toList());
                addattrname.stream().forEach(attrname-> {
                    Sku sku = new Sku();
                    sku.setAttrname(attrname);
                    sku.setSpuid(spu.spuId);
                    productDao.uploadsku(sku);
                    int code=0;
                   for(String attrvalue:newattrnameandattrvaluemap.get(attrname)){
                        Attr attr = new Attr();
                        attr.setAttrcode(String.valueOf(code));
                        attr.setSkuid(sku.skuid);
                        attr.setAttrvalue(attrvalue);
                        code++;
                        productDao.uploadattr(attr );
                   }

                });
                List<String> covercollect = newattrnameandattrvaluemap.keySet().stream().filter(attrname -> attrnameandattrmap.containsKey(attrname)).collect(Collectors.toList());
                covercollect.forEach(attrname->{
                    List<String> oldvalues = attrnameandattrmap.get(attrname);
                    List<String> newvalues = newattrnameandattrvaluemap.get(attrname);
                    List<String> deletecollect = oldvalues.stream().filter(attvalue -> !newvalues.contains(attvalue)).collect(Collectors.toList());
                    deletecollect.stream().forEach(attrvalue->productDao.deleteattrvaluebyattrvalue(attrvalue,spu.spuId,attrname));
                    newvalues.removeAll(oldvalues);
                    int code=0;
                    Set<String> codes = attrnameandattrvaluecodemap.get(attrname);
                    for(String attrvalue:newvalues){
                        while (codes.contains(String.valueOf(code))) {
                            code++;
                        }
                        Attr attr = new Attr();
                        attr.setAttrcode(String.valueOf(code));
                        attr.setAttrvalue(attrvalue);
                        attr.setSkuid(Integer.parseInt(attnameandskuidmap.get(attrname)));
                        productDao.uploadattr(attr);
                    }
                });
            }

        }
        else{
        int uploadspu = productDao.insertuseGeneratedKeys(spu);
        spuAndSku.getAttrs().stream().forEach(uploadAttr -> {
            Sku sku = new Sku();
            sku.setSpuid(spu.spuId);
            sku.setAttrname(uploadAttr.attrname);
            productDao.uploadsku(sku);
            int code = 0;
            for(String attrvalue: uploadAttr.getAttrvalues() ) {
                Attr attr = new Attr();
                attr.setAttrcode(String.valueOf(code));
                attr.setSkuid(sku.getSkuid());
                attr.setAttrvalue(attrvalue);
                productDao.uploadattr(attr);
                code++;
            }
        });
}       return MessageResult.Ok(true);

    }



    //做个对比，全量删除再曾
    @GetMapping("/update/deleteskuandattrvules")
    @ResponseBody
    public MessageResult deleteskuandattrvules(String spuid) {
//        SpuAndSku spuAndSku = (SpuAndSku)JSONObject.parse(spuAndSku1.toJSONString());
       //先两个attrname求不同部分，xin不同部分增加，old不同部分删除

        //再更具每个不同的attrname求不同value部分

        //查出全部属性
        List<StockAttr> allsku = productDao.findAllstockattr(spuid);
        Map<Integer, Map<String,String>> skuandattrmap=new HashMap<>();
        Map<Integer,String> skuidandattrnamemap=new HashMap<>();
        allsku.stream().forEach(stockAttr -> {
            Map<String, String> codeandvaluemap = skuandattrmap.getOrDefault(stockAttr.skuid, new HashMap<>());
            codeandvaluemap.put(stockAttr.attrcode,stockAttr.attrvalue);
            skuandattrmap.put(stockAttr.skuid,codeandvaluemap);
            skuidandattrnamemap.put(stockAttr.skuid,stockAttr.attrname);
        });
        List<StockAttrVo> stockAttrVos=new ArrayList<>();
        skuandattrmap.keySet().stream().forEach(skuid->{
            StockAttrVo stockAttrVo = new StockAttrVo();
            stockAttrVo.setSkuid(skuid);
            stockAttrVo.setAttrname(skuidandattrnamemap.get(skuid));
            stockAttrVo.setAttrvalues((new ArrayList<>(skuandattrmap.get(skuid).values())));
            stockAttrVos.add(stockAttrVo);
        });

      //查出库存属性
        List<Stock> stocks=  productDao.findstockbyspuid(spuid);
        Map<Integer,Map<String,String>> skuidandvaluesmap=new HashMap<>();
        stocks.forEach(stock->{
            String[] split = stock.attrlist.replace("]", "").replace("[", "").split(",");
            Arrays.stream(split).forEach(attr->{
                String[] split1 = attr.split(":");
                String attrvalue = skuandattrmap.get(Integer.parseInt(split1[0])).get(split1[1]);

                Map<String, String> orDefault = skuidandvaluesmap.getOrDefault(Integer.parseInt(split1[0]), new HashMap<>());
                orDefault.put(split1[1],attrvalue);
                skuidandvaluesmap.put(Integer.parseInt(split1[0]),orDefault);
            });
        });
        //求出可删除属性
        Set<Integer> deletedskuid = skuandattrmap.keySet().stream().filter(skuid -> !skuidandvaluesmap.keySet().contains(skuid)).collect(Collectors.toSet());
        Map<Integer,List<String>> deleteattrvaluemap=new HashMap<>();
        skuidandvaluesmap.keySet().forEach(skuid->{
            Map<String, String> retain = skuidandvaluesmap.get(skuid);
            Map<String, String> all = skuandattrmap.get(skuid);
            List<String> deleteattrvcodes = all.keySet().stream().filter(attrcode -> !retain.keySet().contains(attrcode)).collect(Collectors.toList());
             List<String> deletevalues=new ArrayList<>();
             deleteattrvcodes.stream().forEach(attrcode->deletevalues.add(all.get(attrcode)));
            deleteattrvaluemap.put(skuid,deletevalues);
        });
        DeleteAttrsVo deleteAttrsVo = new DeleteAttrsVo();
        deleteAttrsVo.setDeletedskuid(deletedskuid);
        deleteAttrsVo.setDeleteattrvaluemap(deleteattrvaluemap);
        return MessageResult.success(deleteAttrsVo);
    }


    //商品上架，首先查出全部可上架商品(已上架的要不要查出来？
    // +)
    @PostMapping("/all")
    @ResponseBody
    public MessageResult uploadspuandsku() {
        List<Spu> spus = productDao.findaAllSpu();
        return MessageResult.success(spus);
    }
    @GetMapping("/allstock")
    @ResponseBody
    public MessageResult uploadspuandsku(@RequestParam("spuid") String spuid) {
        List<StockAttr> allsku = productDao.findAllstockattr(spuid);
        Map<Integer, Map<String,String>> skuandattrmap=new HashMap<>();
        Map<Integer,String> skuidandattrnamemap=new HashMap<>();
        allsku.stream().forEach(stockAttr -> {
            Map<String, String> codeandvaluemap = skuandattrmap.getOrDefault(stockAttr.skuid, new HashMap<>());
            codeandvaluemap.put(stockAttr.attrcode,stockAttr.attrvalue);
            skuandattrmap.put(stockAttr.skuid,codeandvaluemap);
            skuidandattrnamemap.put(stockAttr.skuid,stockAttr.attrname);
        });
        List<StockAttrVo> stockAttrVos=new ArrayList<>();
        skuandattrmap.keySet().stream().forEach(skuid->{
            StockAttrVo stockAttrVo = new StockAttrVo();
            stockAttrVo.setSkuid(skuid);
            stockAttrVo.setAttrname(skuidandattrnamemap.get(skuid));
            stockAttrVo.setAttrvalues((new ArrayList<>(skuandattrmap.get(skuid).values())));
            stockAttrVos.add(stockAttrVo);
        });

       List<Stock> stocks=  productDao.findstockbyspuid(spuid);
       List<StockVo> stockVoList =new ArrayList<>();
       stocks.forEach(stock->{
           StockVo stockVo =  new StockVo();
           String[] split = stock.attrlist.replace("]", "").replace("[", "").split(",");
           List<EsAttr> attrs=new ArrayList<>();
           Arrays.stream(split).forEach(attr->{
               EsAttr esattr = new EsAttr();
               String[] split1 = attr.split(":");
               String attrvalue = skuandattrmap.get(Integer.parseInt(split1[0])).get(split1[1]);
               esattr.setAttrvalue(attrvalue);
               esattr.setSkuid(split1[0]);
               esattr.setAttrname(skuidandattrnamemap.get(Integer.parseInt(split1[0])));
               attrs.add(esattr);
           });
           stockVo.setAttrs(attrs);
           stockVo.setStockid(stock.stockid);
           stockVo.setStocknum(stock.stocknum);
           stockVo.setSpuid(stock.spuid);
           stockVoList.add(stockVo);
       });
        StockResult stockResult = new StockResult();
        stockResult.setStockVos(stockVoList);
        stockResult.setAllattrs(stockAttrVos);
        return MessageResult.success(stockResult);
    }

    public static void main(String[] args) {
        System.out.println(smallestTrimmedNumbers(
                new String[]{"24","37","96","04"},new int[][]{{2,1},{2,2}}));
    }
    public static int[] smallestTrimmedNumbers(String[] nums, int[][] queries) {
       int[] dp=new int[queries.length];
       int l=nums[0].length();
        for (int i = 0; i <queries.length ; i++) {
            int k=queries[i][0];
            int trim=queries[i][1];
            String[][] shu=new String[nums.length][2];

            for (int j = 0; j <nums.length ; j++) {
                String substring = nums[j].substring(l - trim);
          int index=0;
            while (index<substring.length()&&substring.charAt(index)=='0'){
              index++;
               }
                substring=substring.substring(index);
            shu[j][0]=substring;
            shu[j][1]=String.valueOf(j);
            }
            Arrays.sort(shu, new Comparator<String[]>() {
                @Override
                public int compare(String[] o1, String[] o2) {
                    if(o1[0].length()>o2[0].length()) return 1;
                    else if(o1[0].length()<o2[0].length()) return -1;
                    else {
                        if(o1[0].equals(o2[0])) return Integer.parseInt(o1[1])-Integer.parseInt(o2[1]);
                        for (int j = 0; j < o1[0].length(); j++) {
                    if(o1[0].charAt(j)<o2[0].charAt(j)) return -1;
                    else if (o1[0].charAt(j)>o2[0].charAt(j)) return 1;
                }
                    }
                    return 0;
                }
            });
            dp[i]=Integer.parseInt(shu[k-1][1]);
        }
        return dp;
    }

    private boolean check(String a, String b) {

        if(a.length()<b.length()) return true;
        else if(a.length()>b.length()) return false;
        else {

                for (int j = 0; j < a.length(); j++) {
                    if(a.charAt(j)>b.charAt(j)) return false;
                    else if (a.charAt(j)<b.charAt(j)) return true;

            }
        }
        return true;
    }

//    PriorityQueue<String> queue = new PriorityQueue<String>((a, b) -> {
//        if(a.length()<b.length()) return 1;
//        else if(a.length()>b.length()) return -1;
//        else {
//            if (a.equals(b)) return -1;
//            else {
//                for (int j = 0; j < a.length(); j++) {
//                    if(a.charAt(j)>b.charAt(j)) return -1;
//                    else if (a.charAt(j)<b.charAt(j)) return 1;
//                }
//            }
//        }
//        return 1;
//    });
//    String substring = nums[i].substring(l - trim);
//    int index=0;
//            while (index<substring.length()&&substring.charAt(index)!='0'){
//        index++;
//    }
//    substring=substring.substring(index);
//            if(substring.length()<=0) continue;
//            if(!queue.contains(substring)) {
//
//        if(queue.size()<k) {
//            queue.offer(substring);
//        }else {
//            if(!check(queue.peek(),substring)){
//                queue.poll();
//                queue.offer(substring);
//            }
//        }
//
//    }
//    dp[i]=(String)queue.toArray()[0]
}