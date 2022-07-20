package com.example.auth.Entity;

public class Item {
    public int spuid;

    public String sputitle;

    public String spuprice;

    public String decription;

    public String spuimg;

    public String getSpuimg() {
        return spuimg;
    }

    public void setSpuimg(String spuimg) {
        this.spuimg = spuimg;
    }

    public Item() {
    }

    public int getSpuid() {
        return spuid;
    }

    public void setSpuid(int spuid) {
        this.spuid = spuid;
    }

    public String getSputitle() {
        return sputitle;
    }

    public void setSputitle(String sputitle) {
        this.sputitle = sputitle;
    }

    public String getSpuprice() {
        return spuprice;
    }

    public void setSpuprice(String spuprice) {
        this.spuprice = spuprice;
    }

    public String getDecription() {
        return decription;
    }

    public void setDecription(String decription) {
        this.decription = decription;
    }
}
