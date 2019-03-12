package com.suse.susenews.bean;

public class PersonRelateBean {

    private int icon;
    private String title;

    public PersonRelateBean() {
    }

    public PersonRelateBean(int icon, String title) {
        this.icon = icon;
        this.title = title;
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
        return "PersonRelateBean{" +
                "icon=" + icon +
                ", title='" + title + '\'' +
                '}';
    }
}
