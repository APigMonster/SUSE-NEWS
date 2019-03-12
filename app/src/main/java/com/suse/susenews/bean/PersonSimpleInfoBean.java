package com.suse.susenews.bean;

public class PersonSimpleInfoBean {

    private String name;
    private String value;

    public PersonSimpleInfoBean() {
    }

    public PersonSimpleInfoBean(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "PersonSimpleInfoBean{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
