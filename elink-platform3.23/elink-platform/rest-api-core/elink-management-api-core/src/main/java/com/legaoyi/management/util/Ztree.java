package com.legaoyi.management.util;

import java.util.ArrayList;
import java.util.List;

public class Ztree {

    private String id;

    private String name;

    private String type;

    private String icon;

    private boolean open;

    private boolean isParent = true;

    private List<Ztree> children = new ArrayList<Ztree>();

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIcon() {
        return this.icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public boolean getOpen() {
        return this.open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public boolean getIsParent() {
        return this.isParent;
    }

    public void setIsParent(boolean isParent) {
        this.isParent = isParent;
    }

    public List<Ztree> getChildren() {
        return this.children;
    }

    public void setChildren(List<Ztree> children) {
        this.children = children;
    }

}
