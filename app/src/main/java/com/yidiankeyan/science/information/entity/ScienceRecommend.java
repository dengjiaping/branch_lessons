package com.yidiankeyan.science.information.entity;

/**
 * Created by zn on 2016/8/3 0003.
 * 公告-科技企业号/网站推荐实体类
 */
public class ScienceRecommend {
    private int imgUrl;//专辑图片
    private String content;//专辑内容
    private String name;//主编名字
    private String contentNumber;//内容量


    public int getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(int imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContentNumber() {
        return contentNumber;
    }

    public void setContentNumber(String contentNumber) {
        this.contentNumber = contentNumber;
    }
}
