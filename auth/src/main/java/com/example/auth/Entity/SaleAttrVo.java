package com.example.auth.Entity;

import java.util.List;

public class SaleAttrVo {

    public int skuid;

    public String attrname;

    public List<ItemInAttrVo> attrvalues;

    public SaleAttrVo() {
    }

    public int getSkuid() {
        return skuid;
    }

    public void setSkuid(int skuid) {
        this.skuid = skuid;
    }

    public String getAttrname() {
        return attrname;
    }

    public void setAttrname(String attrname) {
        this.attrname = attrname;
    }

    public List<ItemInAttrVo> getAttrvalues() {
        return attrvalues;
    }

    public void setAttrvalues(List<ItemInAttrVo> attrvalues) {
        this.attrvalues = attrvalues;
    }
}
