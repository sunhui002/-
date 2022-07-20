package com.example.auth.Entity;

import java.util.List;

public class ItemResult {

   public Item item;

   public List<SaleAttrVo> saleAttr;


   public ItemResult() {
   }

   public Item getItem() {
      return item;
   }

   public void setItem(Item item) {
      this.item = item;
   }

   public List<SaleAttrVo> getSaleAttr() {
      return saleAttr;
   }

   public void setSaleAttr(List<SaleAttrVo> saleAttr) {
      this.saleAttr = saleAttr;
   }


}
