package com.example.auth.controller;

import com.example.auth.Dao.ProductDao;
import com.example.auth.Entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

@RequestMapping("/api/item")
@Controller
public class ItemController {

    @Autowired
    ProductDao productDao;

    @GetMapping("{spuid}")
    public String getItem(@PathVariable("spuid") String spuid, Model model){
        ItemResult result = new ItemResult();
        Item item = productDao.findItembyspuid(spuid);
        List<SaleAttrVo> saleAttr = productDao.findAllskubyspuid(spuid);
        saleAttr.stream().forEach((Attr)->{
            List<ItemInAttrVo> attrvalues = productDao.findAllattrbyskuid(String.valueOf(Attr.skuid));
            Attr.setAttrvalues(attrvalues);
        });
       result.setItem(item);
       result.setSaleAttr(saleAttr);
       model.addAttribute("result",result);
        return "item";
    }

    @GetMapping("attr/{spuid}")
    public String getItemStock(@PathVariable("spuid") String spuid, Model model,String... attrs){


        ItemResult result = new ItemResult();
        Item item = productDao.findItembyspuid(spuid);
        List<SaleAttrVo> saleAttr = productDao.findAllskubyspuid(spuid);
        saleAttr.stream().forEach((Attr)->{
            List<ItemInAttrVo> attrvalues = productDao.findAllattrbyskuidfalse(String.valueOf(Attr.skuid));
            Attr.setAttrvalues(attrvalues);
        });
        result.setItem(item);
        result.setSaleAttr(saleAttr);
        if (!Optional.ofNullable(attrs).isPresent()) {
            model.addAttribute("result", result);
            return "item";
        }
        List<String> stockids=new ArrayList<>();
        Map<Integer,List<String>> skuidandattrcodemap=new HashMap<>();
        Set<Integer> selectedskuid=new HashSet<>();
        Arrays.asList(attrs).stream().forEach((attrid)-> {
            selectedskuid.add(productDao.findskuidbyattrid(attrid));
            String stockid =productDao.findstockidsbyattrid(attrid);
            if(Optional.ofNullable(stockid).isPresent()) {
                if (stockids.size() <= 0) {
                    stockids.addAll(Arrays.asList(stockid.split(",")));
                } else {
                    stockids.retainAll(Arrays.asList(stockid.split(",")));
                }
            }
                });
        List<String > attrlists=new ArrayList<>();
       stockids.stream().forEach(stockid->attrlists.add(productDao.findattrlistbystockid(stockid)));
       attrlists.forEach(attrlist->{
           Arrays.stream(attrlist.replace("]", "").
                   replace("[", "").split(",")).
                   forEach(skuidandattrcode->{
                       //没有属性的商品，不能选规格参数，也就不会出问题，恶意的呢。
               String[] replace = skuidandattrcode.split(":");
                       List<String> orDefault = skuidandattrcodemap.getOrDefault(replace[0], new ArrayList<>());
                       orDefault.add(replace[1]);
                       skuidandattrcodemap.put(Integer.parseInt(replace[0]),orDefault);
           });

       });
        saleAttr.forEach((Attr)->{
            if(skuidandattrcodemap.containsKey(Attr.skuid)){
                if(selectedskuid.contains(Attr.skuid)){
                    Attr.attrvalues.forEach(attr->attr.setIsstock(true));
                }else {
                    skuidandattrcodemap.get(Attr.skuid).forEach(attrcode1 -> {
                        Attr.attrvalues.forEach(attr -> {
                            if (attrcode1.equals(attr.attrcode)) attr.setIsstock(true);
                        });
                    });
                }
            }

        });

        model.addAttribute("result", result);
        return "item";
    }

}
