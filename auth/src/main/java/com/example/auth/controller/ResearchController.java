package com.example.auth.controller;

import com.example.auth.Dao.AttrRepository;
import com.example.auth.Dao.ProductDao;
import com.example.auth.Entity.*;
import com.example.auth.Service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

@Controller
@RequestMapping("/api")
public class ResearchController {

    @Autowired
    ProductDao productDao;
    @Autowired
    SearchService searchService;

    @GetMapping(value = {"/es/rsearch","/"})
    public String getSearchPage(SearchParam searchParam, Model model, HttpServletRequest request) {
        searchParam.set_queryString(request.getQueryString());
        SearchResult result=searchService.getSearchResult(searchParam);
        model.addAttribute("result", result);
        return "search";
    }

    @ResponseBody
    @GetMapping("/es/research1")
    public MessageResult research(@RequestParam(value = "description") String description, Model model) throws UnsupportedEncodingException {
        String decode = URLDecoder.decode(
                description, "UTF-8"
        );
//        List<Product> research = productDao.research(decode);
        Iterable<EsProduct> research = attrRepository.findEsProductBySpuTitle(decode);

        return MessageResult.success(research);
    }

    @ResponseBody
    @GetMapping("/es/researchProduct")
    public MessageResult researchProduct(@RequestParam(value = "description") String description) throws UnsupportedEncodingException {
        String decode = URLDecoder.decode(
                description, "UTF-8"
        );
//        List<Product> research = productDao.research(decode);
        Iterable<EsProduct> research = attrRepository.findEsProductBySpuTitle(decode);

        return MessageResult.success(research);
    }
    @Autowired
    AttrRepository attrRepository;

    @ResponseBody
    @GetMapping("/es")
    public MessageResult testes()  {
        List<EsProduct> list=new ArrayList<>();
        //        List<EsAttr> findattr = productDao.findattr("1");
        List<Spu> spus = productDao.findaAllSpu();
        List<String> allSpuid=new ArrayList<>();
         spus.stream().forEach((spu)->allSpuid.add(spu.spuId));
         Map<String,Deque<List<EsAttr>>> Esproductmap=new HashMap<>();
        spus.forEach((spu)->{
           String spuid=spu.spuId;
            Deque<List<EsAttr>> deque=new ArrayDeque<>();
//            List<Spu> spuslist = productDao.findaSpubyspuid(spuid);
            List<EsAttr> findattr = productDao.findattr(spuid);
            Map<String,List<EsAttr>> attrMap=new HashMap<>();
            findattr.stream().forEach((esAttr)->{
                List<EsAttr> orDefault = attrMap.getOrDefault(esAttr.attrname, new ArrayList<>());
               orDefault.add(esAttr);
                attrMap.put(esAttr.attrname,orDefault);
            });
            attrMap.keySet().stream().forEach((attrname)->{
                int size=deque.size();
                List<EsAttr> afAttrs = attrMap.get(attrname);
                if(size==0) {
                    afAttrs.stream().forEach((attr) -> {
                        List<EsAttr> esAttrs=new ArrayList<>();
                        esAttrs.add(attr);
                        deque.offerLast(esAttrs);
                    });
                }
               else {
                    for (int i = 0; i < size; i++) {
                        List<EsAttr> prelist = deque.pollFirst();
                        afAttrs.stream().forEach((attr) -> {
                           List<EsAttr> esAttrs = new ArrayList<>(prelist);
                            esAttrs.add(attr);
                            deque.offerLast(esAttrs);
                        });
                    }
                }

            });
//
             while (!deque.isEmpty()){
                 EsProduct esProduct = new EsProduct();
                 esProduct.setDecription(spu.decription);
                 esProduct.setAttr(deque.pollFirst());
                 esProduct.setSpuPrice(spu.spuPrice);
                 esProduct.setSpuTitle(spu.spuTitle) ;
                 esProduct.setSpuImg(spu.spuImg);
                 esProduct.setSpuId(spuid);
                 list.add(esProduct);
             }

//            Esproductmap.put(spuid,deque);
           });
        for (int i=0;i<list.size();i++){
            EsProduct product=list.get(i);
            product.setProductid(i);
            attrRepository.save(product);
        }

//        List<EsProduct> all = attrRepository.findEsProductBySpuTitle("华为");
        Iterable<EsProduct> all = attrRepository.findAll();

        return MessageResult.success(list);
        }

////
////
////
////        List<EsProduct> esProduct = productDao.findEsProduct();
////
////          esProduct.stream().forEach((product)->attrRepository.save(product));
////        Iterable<EsProduct> all = attrRepository.findAll();
////
////        return MessageResult.success(esProduct);

//
//

    public static void main(String[] args) throws InterruptedException {
//        System.out.println(search(new int[]{4,5,6,7,0,1,2},0));
        System.out.println(search(new int[]{5,1,3},2));
    }

    public static int search(int[] nums, int target) {
        int index=-1;
        if(nums[0]<nums[nums.length-1]){
            index= Arrays.binarySearch(nums,target);
            return index;
        }
        if(nums.length<=2) {
            for(int i=0;i<nums.length;i++){
                if(nums[i]==target) return i;

            }
            return index;

        }
        // 3 5 1
        int left=0;
        int right=nums.length-1;
        int mid=(left+right)/2;
        while(left<right){
            if(target==nums[mid]) return mid;
            if(nums[nums.length-1]<nums[mid]){
                if(target<nums[mid]&&target>nums[nums.length-1]) right=mid;
                else left=mid+1;
//                if(target>nums[mid]) left=mid+1;
//                else if(target<nums[mid]){
//                    if(target<nums[nums.length-1]) left=mid+1;
//
//                }
//                if(target<nums[mid]||(target>nums[mid]&&target<nums[nums.length-1])) left=mid+1;
//                else right=mid-1;
            }else  {
                if(target>nums[mid]&&target<=nums[nums.length-1]) left=mid+1;
                else right=mid;
//                if(target>nums[mid]){
//                    if(target>nums[nums.length-1]) right=mid;
//
//                }
//                else if(target<nums[mid]) right=mid;
//                if((target>nums[nums.length-1]&&target>nums[mid])||target<nums[mid]) right=mid-1;
//                else right=mid-1;
            }
            mid=(left+right)/2;
        }

        return nums[mid]==target?mid:-1;



    }
}
