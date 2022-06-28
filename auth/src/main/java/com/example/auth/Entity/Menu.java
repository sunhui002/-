package com.example.auth.Entity;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Menu {

    public String Menuguid;
    public String Icon;
    public String IconActive;
    public String Caption;
    public String Funcid;
    public String IconBlue;
    public int MaxVisible;
    public String menu_id;
    public int MinVisible;
    public String parentguid;
    public List<Menu> SubNodes;
    public int type;
    public String RunUrl;
    public String RunClassName;

    public String getRunClassName() {
        return RunClassName;
    }

    public void setRunClassName(String runClassName) {
        RunClassName = runClassName;
    }

    public String getRunUrl() {
        return RunUrl;
    }

    public void setRunUrl(String runUrl) {
        RunUrl = runUrl;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }



    public String getMenuguid() {
        return Menuguid;
    }

    public void setMenuguid(String menuguid) {
        Menuguid = menuguid;
    }

    public String getIcon() {
        return Icon;
    }

    public void setIcon(String icon) {
        Icon = icon;
    }

    public String getIconActive() {
        return IconActive;
    }

    public void setIconActive(String iconActive) {
        IconActive = iconActive;
    }

    public String getCaption() {
        return Caption;
    }

    public void setCaption(String caption) {
        Caption = caption;
    }

    public String getFuncid() {
        return Funcid;
    }

    public void setFuncid(String funcid) {
        Funcid = funcid;
    }

    public String getIconBlue() {
        return IconBlue;
    }

    public void setIconBlue(String iconBlue) {
        IconBlue = iconBlue;
    }

    public int getMaxVisible() {
        return MaxVisible;
    }

    public void setMaxVisible(int maxVisible) {
        MaxVisible = maxVisible;
    }

    public String getMenu_id() {
        return menu_id;
    }

    public void setMenu_id(String menu_id) {
        this.menu_id = menu_id;
    }

    public int getMinVisible() {
        return MinVisible;
    }

    public void setMinVisible(int minVisible) {
        MinVisible = minVisible;
    }

    public String getParentguid() {
        return parentguid;
    }

    public List<Menu> getSubNodes() {
        return SubNodes;
    }

    public void setSubNodes(List<Menu> subNodes) {
        SubNodes = subNodes;
    }

    public void setParentguid(String parentguid) {
        this.parentguid = parentguid;
    }
}
