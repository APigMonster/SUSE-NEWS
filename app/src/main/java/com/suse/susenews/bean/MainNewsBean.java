package com.suse.susenews.bean;

public class MainNewsBean {

    private int icon;
    private String id;
    private String title;

    public MainNewsBean() {
    }

    public MainNewsBean(int icon, String id, String title) {
        this.icon = icon;
        this.id = id;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "MainNewsBean{" +
                "icon=" + icon +
                ", title='" + title + '\'' +
                '}';
    }
}
