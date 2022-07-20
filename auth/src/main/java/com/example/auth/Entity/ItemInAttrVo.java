package com.example.auth.Entity;

public class ItemInAttrVo {

    public int attrid;

    public String attrvalue;

    public int skuid;

    public String attrcode;

    public boolean isstock=false;

    public int stocknum;

    public String getAttrcode() {
        return attrcode;
    }

    public void setAttrcode(String attrcode) {
        this.attrcode = attrcode;
    }

    public int getStocknum() {
        return stocknum;
    }

    public void setStocknum(int stocknum) {
        this.stocknum = stocknum;
    }

    public boolean isIsstock() {
        return isstock;
    }

    public void setIsstock(boolean isstock) {
        this.isstock = isstock;
    }

    public ItemInAttrVo(int attrid, String attrvalue, int skuid) {
        this.attrid = attrid;
        this.attrvalue = attrvalue;
        this.skuid = skuid;
    }

    public ItemInAttrVo() {
    }

    public int getAttrid() {
        return attrid;
    }

    public void setAttrid(int attrid) {
        this.attrid = attrid;
    }

    public String getAttrvalue() {
        return attrvalue;
    }

    public void setAttrvalue(String attrvalue) {
        this.attrvalue = attrvalue;
    }

    public int getSkuid() {
        return skuid;
    }

    public void setSkuid(int skuid) {
        this.skuid = skuid;
    }
}
