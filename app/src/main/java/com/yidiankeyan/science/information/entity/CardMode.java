package com.yidiankeyan.science.information.entity;

import java.util.List;

/**
 * Created zn 2016-12-23.
 * 知识红包bean
 */
public class CardMode {

    /**
     * id : 302bcdef6e6646be952d45a7bdb4d220
     * name :  Apple Pay全书 关于它的所有细节
     * content : <p>对中国用户来说，移动支付其实已经不是什么陌生事物，抢红包和支付宝早完成用户启蒙。但与这两者有区别的是，Apple Pay只是苹果搭建的一个支付服务，它链接银行、店面及用户，但又不像支付宝那样把钱存在自己这。 微信:提现收费后仍亏本</p>
     * publishtime : 1482422400000
     * imgurl : /static/upload/cmsweb/image/jpeg2016/12/23/8b8f419666894f1f9dd837693bb3b531.jpg
     */
//
//    private String id;
//    private String name;
//    private String content;
//    private long publishtime;
//    private String imgurl;
//
//
//
//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getContent() {
//        return content;
//    }
//
//    public void setContent(String content) {
//        this.content = content;
//    }
//
//    public long getPublishtime() {
//        return publishtime;
//    }
//
//    public void setPublishtime(long publishtime) {
//        this.publishtime = publishtime;
//    }
//
//    public String getImgurl() {
//        return imgurl;
//    }
//
//    public void setImgurl(String imgurl) {
//        this.imgurl = imgurl;
//    }

    private String name;
    private int year;
    private List<String> images;

    public CardMode(String name, int year, List<String> images) {
        this.name = name;
        this.year = year;
        this.images = images;
    }

    public String getName() {
        return name;
    }

    public int getYear() {
        return year;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<String> getImages() {
        return images;
    }
}
