package com.example.auth.Entity;

import java.util.List;

public class StockAttrVo {
    public String attrname;

    public int   skuid;



    public List<String> attrvalues;

    public StockAttrVo() {
    }

    public String getAttrname() {
        return attrname;
    }

    public void setAttrname(String attrname) {
        this.attrname = attrname;
    }

    public int getSkuid() {
        return skuid;
    }

    public void setSkuid(int skuid) {
        this.skuid = skuid;
    }

    public List<String> getAttrvalues() {
        return attrvalues;
    }

    public void setAttrvalues(List<String> attrvalues) {
        this.attrvalues = attrvalues;
    }
}
