package com.example.auth.Entity;

import java.util.List;

public class StockVo {

    public int spuid;

    public int stockid;

    public List<EsAttr> attrs;

    public int stocknum;

    public int getStockid() {
        return stockid;
    }

    public void setStockid(int stockid) {
        this.stockid = stockid;
    }

    public StockVo() {
    }

    public int getSpuid() {
        return spuid;
    }

    public void setSpuid(int spuid) {
        this.spuid = spuid;
    }

    public List<EsAttr> getAttrs() {
        return attrs;
    }

    public void setAttrs(List<EsAttr> attrs) {
        this.attrs = attrs;
    }

    public int getStocknum() {
        return stocknum;
    }

    public void setStocknum(int stocknum) {
        this.stocknum = stocknum;
    }
}
