package com.example.auth.Entity;

import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.stereotype.Component;


public class EsAttr{

    public String skuid;

    public String attrname;

    public String attrvalue;

//    public EsAttr(String attrname, String attrvalue) {
//        this.attrname = attrname;
//        this.attrvalue = attrvalue;
//    }

    public String getAttrname() {
        return attrname;
    }

    public void setAttrname(String attrname) {
        this.attrname = attrname;
    }

    public String getAttrvalue() {
        return attrvalue;
    }
    public void setAttrvalue(String attrvalue) {
        this.attrvalue = attrvalue;
    }
}