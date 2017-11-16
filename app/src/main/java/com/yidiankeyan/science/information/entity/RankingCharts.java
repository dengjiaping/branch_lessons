package com.yidiankeyan.science.information.entity;

import java.util.List;

/**
 * 排行榜分类
 */
public class RankingCharts {


    private String className;
    /**
     * type : 4
     * titles : ["数学理论","天文器材"]
     */

    private int type;
    private List<String> titles;
    private String title;
    private int imgUrl;

    public RankingCharts(int type,int imgUrl, String title) {
        this.type = type;
        this.title = title;
        this.imgUrl=imgUrl;
    }

    public int getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(int imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public RankingCharts(String className) {
        this.className = className;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<String> getTitles() {
        return titles;
    }

    public void setTitles(List<String> titles) {
        this.titles = titles;
    }
}
