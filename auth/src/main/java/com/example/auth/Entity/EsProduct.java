package com.example.auth.Entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Document(indexName = "product")
public class EsProduct {

    @Id
    public String spuId;

    @Field(type =FieldType.Text)
   public String spuImg;

    @Field(type =FieldType.Float)
    public String spuPrice;

    @Field(type =FieldType.Text)
    public String spuTitle;

    @Field(type =FieldType.Text)
    public String decription;

    @Field(type = FieldType.Nested, includeInParent = true)
    public List<EsAttr> attr;

    public String getSpuId() {
        return spuId;
    }

    public void setSpuId(String spuId) {
        this.spuId = spuId;
    }

    public String getSpuImg() {
        return spuImg;
    }

    public void setSpuImg(String spuImg) {
        this.spuImg = spuImg;
    }

    public String getSpuPrice() {
        return spuPrice;
    }

    public void setSpuPrice(String spuPrice) {
        this.spuPrice = spuPrice;
    }

    public String getSpuTitle() {
        return spuTitle;
    }

    public void setSpuTitle(String spuTitle) {
        this.spuTitle = spuTitle;
    }

    public String getDecription() {
        return decription;
    }

    public void setDecription(String decription) {
        this.decription = decription;
    }

    public List<EsAttr> getAttr() {
        return attr;
    }

    public void setAttr(List<EsAttr> attr) {
        this.attr = attr;
    }
//    @Component
//    public class EsAttr{
//
//       public String attrname;
//
//       public String attrvalue;
//
//        public EsAttr(String attrname, String attrvalue) {
//            this.attrname = attrname;
//            this.attrvalue = attrvalue;
//        }
//
//        public String getAttrname() {
//           return attrname;
//       }
//
//       public void setAttrname(String attrname) {
//           this.attrname = attrname;
//       }
//
//       public String getAttrvalue() {
//           return attrvalue;
//       }
//       public void setAttrvalue(String attrvalue) {
//           this.attrvalue = attrvalue;
//       }
//   }
}
