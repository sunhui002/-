package com.example.auth.Entity;

import java.util.List;

public class SpuAndSku {

    public int spuid;

    public String spuImg;

    public String spuPrice;

    public String spuTitle;

    public String decription;

    public List<UploadAttr> attrs;

    public SpuAndSku() {
    }

    public int getSpuid() {
        return spuid;
    }

    public void setSpuid(int spuid) {
        this.spuid = spuid;
    }

    public SpuAndSku(String spuImg, String spuPrice, String spuTitle, String decription, List<UploadAttr> attrs) {
        this.spuImg = spuImg;
        this.spuPrice = spuPrice;
        this.spuTitle = spuTitle;
        this.decription = decription;
        this.attrs = attrs;
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

    public List<UploadAttr> getAttrs() {
        return attrs;
    }

    public void setAttrs(List<UploadAttr> attrs) {
        this.attrs = attrs;
    }


}

