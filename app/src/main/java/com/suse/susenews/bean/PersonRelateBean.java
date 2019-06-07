package com.suse.susenews.bean;

public class PersonRelateBean{

    private int icon;
    private String title;
    private String object;

    public PersonRelateBean() {
    }

    public PersonRelateBean(int icon, String title) {
        this(icon, title, null);
    }

    public PersonRelateBean(int icon, String title, String object) {
        this.icon = icon;
        this.title = title;
        this.object = object;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }
}
