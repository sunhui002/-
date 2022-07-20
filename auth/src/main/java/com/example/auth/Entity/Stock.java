package com.example.auth.Entity;

public class Stock {

    public int stockid;

    public int skuprice;

    public String attrlist;

    public int spuid;

    public int stocknum;

    public Stock() {
    }

    public int getStockid() {
        return stockid;
    }

    public void setStockid(int stockid) {
        this.stockid = stockid;
    }

    public int getSkuprice() {
        return skuprice;
    }

    public void setSkuprice(int skuprice) {
        this.skuprice = skuprice;
    }

    public String getAttrlist() {
        return attrlist;
    }

    public void setAttrlist(String attrlist) {
        this.attrlist = attrlist;
    }

    public int getSpuid() {
        return spuid;
    }

    public void setSpuid(int spuid) {
        this.spuid = spuid;
    }

    public int getStocknum() {
        return stocknum;
    }

    public void setStocknum(int stocknum) {
        this.stocknum = stocknum;
    }
}
