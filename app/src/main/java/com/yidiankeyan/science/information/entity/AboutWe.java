package com.yidiankeyan.science.information.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 公告
 * -关于我们
 */
public class AboutWe {
    private int type;
    private List<AboutUs> aboutList;
    private String title;
    private boolean loadFinish;

    public boolean isLoadFinish() {
        return loadFinish;
    }

    public void setLoadFinish(boolean loadFinish) {
        this.loadFinish = loadFinish;
    }

    public AboutWe(String title, int type) {
        this.title = title;
        this.type = type;
        aboutList = new ArrayList<>();
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void createChild(AboutUs aboutUs) {
        aboutList.add(aboutUs);
    }

    public void removeAll(){
        aboutList.removeAll(aboutList);
    }

    public List<AboutUs> getAboutList() {
        return aboutList;
    }

    public void setAboutList(List<AboutUs> aboutList) {
        this.aboutList = aboutList;
    }

}
