package com.yidiankeyan.science.subscribe.entity;

/**
 * 打赏记录实体类
 */
public class RewardRecordBean {
    private String imgUrl;//用户头像
    private String title;//打赏标题
    private String name;//用户名字
    private String datatime;//打赏时间
    private double money;//打赏金额

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDatatime() {
        return datatime;
    }

    public void setDatatime(String datatime) {
        this.datatime = datatime;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }
}
