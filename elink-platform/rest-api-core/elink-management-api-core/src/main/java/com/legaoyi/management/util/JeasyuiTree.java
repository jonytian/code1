package com.legaoyi.management.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JeasyuiTree {

    public static final String TREE_STATE_OPEN = "open";

    public static final String TREE_STATE_CLOSED = "closed";

    private String id;

    private String text;

    private String iconCls;

    private String state;

    private List<JeasyuiTree> children = new ArrayList<JeasyuiTree>();

    private Map<String, Object> attributes = new HashMap<String, Object>();

    public final String getId() {
        return this.id;
    }

    public final void setId(String id) {
        this.id = id;
    }

    public final String getText() {
        return this.text;
    }

    public final void setText(String text) {
        this.text = text;
    }

    public final String getIconCls() {
        return this.iconCls;
    }

    public final void setIconCls(String iconCls) {
        this.iconCls = iconCls;
    }

    public final String getState() {
        return this.state;
    }

    public final void setState(String state) {
        this.state = state;
    }

    public final List<JeasyuiTree> getChildren() {
        return this.children;
    }

    public final void setChildren(List<JeasyuiTree> children) {
        this.children = children;
    }

    public final Map<String, Object> getAttributes() {
        return this.attributes;
    }

    public final void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public void addAttributes(String attribute, Object value) {
        this.attributes.put(attribute, value);
    }

}
