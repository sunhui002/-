package com.example.auth.Entity;

import org.springframework.stereotype.Component;

@Component
public class Spu {

    public String spuId;

    public String spuImg;

    public String spuPrice;

    public String spuTitle;

    public String decription;

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
}
