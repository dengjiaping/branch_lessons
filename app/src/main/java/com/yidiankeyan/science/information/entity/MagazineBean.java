package com.yidiankeyan.science.information.entity;

/**
 * Created by nby on 2016/8/26.
 */
public class MagazineBean {

    /**
     * id : 3
     * name : 科技天下
     * coverimg : /22.img
     * introduction : 科技前沿
     * readnum : 0
     */

    private int id;
    private String name;
    private String coverimg;
    private String introduction;
    private int readnum;
    /**
     * publishtitle : 未知刊目
     * publishtime : 1472217417000
     * createtime : null
     * contenturl : null
     */

    private String publishtitle;
    private long publishtime;
    private Object createtime;
    private Object contenturl;
    /**
     * websiteurl : http://www.baidu.com
     */

    private String websiteurl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCoverimg() {
        return coverimg;
    }

    public void setCoverimg(String coverimg) {
        this.coverimg = coverimg;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public int getReadnum() {
        return readnum;
    }

    public void setReadnum(int readnum) {
        this.readnum = readnum;
    }

    public String getPublishtitle() {
        return publishtitle;
    }

    public void setPublishtitle(String publishtitle) {
        this.publishtitle = publishtitle;
    }

    public long getPublishtime() {
        return publishtime;
    }

    public void setPublishtime(long publishtime) {
        this.publishtime = publishtime;
    }

    public Object getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Object createtime) {
        this.createtime = createtime;
    }

    public Object getContenturl() {
        return contenturl;
    }

    public void setContenturl(Object contenturl) {
        this.contenturl = contenturl;
    }

    public String getWebsiteurl() {
        return websiteurl;
    }

    public void setWebsiteurl(String websiteurl) {
        this.websiteurl = websiteurl;
    }
}
