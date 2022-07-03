package com.example.auth.Entity;

public class Spu {

    public String spuId;

    public String spuImg;

    public String spuPrice;

    public String spuTitle;

    public String decription;

    public Spu(String spuId, String spuImg, String spuPrice, String spuTitle, String decription) {
        this.spuId = spuId;
        this.spuImg = spuImg;
        this.spuPrice = spuPrice;
        this.spuTitle = spuTitle;
        this.decription = decription;
    }
}
