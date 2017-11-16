package com.yidiankeyan.science.my.entity;

/**
 * Created by zn on 2016/8/30 0030.
 */
public class BirthData {
    private boolean tag = false;
    private String year;

    public BirthData(boolean tag, String year) {
        this.tag = tag;
        this.year = year;
    }

    public BirthData(String year) {
        this.year = year;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public boolean isTag() {
        return tag;
    }

    public void setTag(boolean tag) {
        this.tag = tag;
    }
}
