package com.example.auth.Entity;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class User {
     public String password;
    public String roleguid;
    public String rolename;
    public String tenementid;
    public String tenementname;
    public String user_id;
    public String username;
    public List<Menu> SubNodes;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRoleguid() {
        return roleguid;
    }

    public void setRoleguid(String roleguid) {
        this.roleguid = roleguid;
    }

    public String getRolename() {
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename;
    }

    public String getTenementid() {
        return tenementid;
    }

    public void setTenementid(String tenementid) {
        this.tenementid = tenementid;
    }

    public String getTenementname() {
        return tenementname;
    }

    public void setTenementname(String tenementname) {
        this.tenementname = tenementname;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Menu> getSubNodes() {
        return SubNodes;
    }

    public void setSubNodes(List<Menu> subNodes) {
        SubNodes = subNodes;
    }
}
