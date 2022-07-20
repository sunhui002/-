package com.example.auth.Entity;

import java.util.List;

public class StockResult {

    public List<StockVo> stockVos;

    public List<StockAttrVo> allattrs;

    public List<StockVo> getStockVos() {
        return stockVos;
    }

    public void setStockVos(List<StockVo> stockVos) {
        this.stockVos = stockVos;
    }

    public List<StockAttrVo> getAllattrs() {
        return allattrs;
    }

    public void setAllattrs(List<StockAttrVo> allattrs) {
        this.allattrs = allattrs;
    }
}
