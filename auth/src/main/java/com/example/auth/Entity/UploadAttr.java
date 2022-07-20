package com.example.auth.Entity;

import java.util.List;

public  class UploadAttr{

    public String attrname;

    public List<String> attrvalues;

    public UploadAttr() {
    }

    public UploadAttr(String attrname, List<String> attrvalues) {
        this.attrname = attrname;
        this.attrvalues = attrvalues;
    }

    public UploadAttr(String attrname) {
        this.attrname = attrname;
    }

    public String getAttrname() {
        return attrname;
    }

    public void setAttrname(String attrname) {
        this.attrname = attrname;
    }

    public List<String> getAttrvalues() {
        return attrvalues;
    }

    public void setAttrvalues(List<String> attrvalues) {
        this.attrvalues = attrvalues;
    }
}