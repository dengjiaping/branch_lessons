package com.yidiankeyan.science.information.entity;

/**
 * //  ◥█▄▃▁   ▁▂▂▃▃▂▂▂▂▂▂▂▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▅
 * //.......◥█☆█▅▄▃▁▁▁▁▁▃▄▅▅▅▅▅▅▅▅▅▅▅▅▅▅▄▁█▅●    ...Created by zn on 2016/11/30 0030.
 * //〓▇█████ ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓███▅▄▃███●
 * // 〓〓.๑۩۞۩๑.█████████████████████◤
 * //   ◥█████████◤ ◥        ◢◤
 * //     ◥██████◤        ◥  ◢◤
 * //       ████◤██████◤
 * //       █▓▓▓▓██◤                -墨子读书
 * //      █▓▓▓██◆                      -读书详情
 * //     █▓▓▓██◆                           -相关书籍
 * //    █▓▓▓██◆
 * //   █▓▓▓██◆
 * //  █▓▓▓██◆
 * // █▓▓ ██◆
 * //█▓▓ ██◆
 */
public class MozRelevantReadBean {

    /**
     * id : e3e6dd8c0d2546d09531ef8659c0cd54
     * name : 表单第一次书籍测试
     * price : 12
     * desc : 隔壁老王经验集
     * coverimgurl : /static/upload/cmsweb/image/jpeg2016/11/29/eeea39437bcf422fbc5e80102b72f3ad.jpg
     * author : 隔壁老王
     * mediaurl : /static/upload/cmsweb/audio/mpeg2016/11/29/f2f2719b907e4e12af0026d8076a7c96.mp3
     * subjectid : 12
     * subjectname : 超自然
     * casterid : 21232avccsf22332dsd
     * castername : null
     * casterimgurl : null
     * readnum : 0
     */

    private String id;
    private String name;
    private double price;
    private String desc;
    private String coverimgurl;
    private String author;
    private String mediaurl;
    private String subjectid;
    private String subjectname;

    public int getIsActPriceShow() {
        return isActPriceShow;
    }

    public void setIsActPriceShow(int isActPriceShow) {
        this.isActPriceShow = isActPriceShow;
    }

    private String casterid;
    private String castername;
    private String casterimgurl;
    private int readnum;
    private int medialength;
    private int isActPriceShow;
    private int isbuy;
    private int activityprice;

    public int getActivityprice() {
        return activityprice;
    }

    public void setActivityprice(int activityprice) {
        this.activityprice = activityprice;
    }

    public int getIsBuy() {
        return isbuy;
    }

    public void setIsBuy(int isBuy) {
        this.isbuy = isBuy;
    }

    public int getMedialength() {
        return medialength;
    }

    public void setMedialength(int medialength) {
        this.medialength = medialength;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCoverimgurl() {
        return coverimgurl;
    }

    public void setCoverimgurl(String coverimgurl) {
        this.coverimgurl = coverimgurl;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getMediaurl() {
        return mediaurl;
    }

    public void setMediaurl(String mediaurl) {
        this.mediaurl = mediaurl;
    }

    public String getSubjectid() {
        return subjectid;
    }

    public void setSubjectid(String subjectid) {
        this.subjectid = subjectid;
    }

    public String getSubjectname() {
        return subjectname;
    }

    public void setSubjectname(String subjectname) {
        this.subjectname = subjectname;
    }

    public String getCasterid() {
        return casterid;
    }

    public void setCasterid(String casterid) {
        this.casterid = casterid;
    }

    public String getCastername() {
        return castername;
    }

    public void setCastername(String castername) {
        this.castername = castername;
    }

    public String getCasterimgurl() {
        return casterimgurl;
    }

    public void setCasterimgurl(String casterimgurl) {
        this.casterimgurl = casterimgurl;
    }

    public int getReadnum() {
        return readnum;
    }

    public void setReadnum(int readnum) {
        this.readnum = readnum;
    }
}
