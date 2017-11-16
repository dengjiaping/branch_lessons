package com.yidiankeyan.science.collection.entity;

/**
 * Created by Administrator on 2016/8/16 0016.
 */
public class CollectionBean {


    /**
     * id : 427c0281efca480ba52c1ca5cab3c7cf
     * name : 数学理论
     * coverimgurl : /66.jpg
     * shortimgurl : /static/upload/cmsweb/image/png2016/7/28/1469759882484数学理论222.222.png
     * price : 0
     * priceunit : 4
     * type : 1
     * lastupdatetitle : 欧洲杯主帅也要学好数发学3
     * contentnum : 4
     * size : 0
     * praisenum : 0
     * readnum : 0
     * belongtype : 1
     * belongid : 2
     * ordernum : 0
     * sharenum : null
     * recentupdatetime : 2016-08-05 15:32:22.0
     */

    private String id;
    private String name;
    private String coverimgurl;
    private String shortimgurl;
    private int price;
    private int priceunit;
    private int type;
    private String lastupdatetitle;
    private int contentnum;
    private int size;
    private int praisenum;
    private int readnum;
    private int belongtype;
    private int belongid;
    private int ordernum;
    private Object sharenum;
    private String recentupdatetime;
    private boolean isFile;
    /**
     * albumid : 427c0281efca480ba52c1ca5cab3c7cf
     * subjectname : null
     * albumname : 数学理论
     * albumtype : 1
     * recenttitle : PCA的数学原理
     * albumauthor : 潘建伟
     * authorid : 1
     */
    private boolean isNeedDelete;
    private String albumid;
    private Object subjectname;
    private String albumname;
    private int albumtype;
    private String recenttitle;
    private String albumauthor;
    private String authorid;

    public boolean isNeedDelete() {
        return isNeedDelete;
    }

    public void setNeedDelete(boolean needDelete) {
        isNeedDelete = needDelete;
    }

    public boolean isFile() {
        return isFile;
    }

    public void setFile(boolean file) {
        isFile = file;
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

    public String getCoverimgurl() {
        return coverimgurl;
    }

    public void setCoverimgurl(String coverimgurl) {
        this.coverimgurl = coverimgurl;
    }

    public String getShortimgurl() {
        return shortimgurl;
    }

    public void setShortimgurl(String shortimgurl) {
        this.shortimgurl = shortimgurl;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPriceunit() {
        return priceunit;
    }

    public void setPriceunit(int priceunit) {
        this.priceunit = priceunit;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getLastupdatetitle() {
        return lastupdatetitle;
    }

    public void setLastupdatetitle(String lastupdatetitle) {
        this.lastupdatetitle = lastupdatetitle;
    }

    public int getContentnum() {
        return contentnum;
    }

    public void setContentnum(int contentnum) {
        this.contentnum = contentnum;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getPraisenum() {
        return praisenum;
    }

    public void setPraisenum(int praisenum) {
        this.praisenum = praisenum;
    }

    public int getReadnum() {
        return readnum;
    }

    public void setReadnum(int readnum) {
        this.readnum = readnum;
    }

    public int getBelongtype() {
        return belongtype;
    }

    public void setBelongtype(int belongtype) {
        this.belongtype = belongtype;
    }

    public int getBelongid() {
        return belongid;
    }

    public void setBelongid(int belongid) {
        this.belongid = belongid;
    }

    public int getOrdernum() {
        return ordernum;
    }

    public void setOrdernum(int ordernum) {
        this.ordernum = ordernum;
    }

    public Object getSharenum() {
        return sharenum;
    }

    public void setSharenum(Object sharenum) {
        this.sharenum = sharenum;
    }

    public String getRecentupdatetime() {
        return recentupdatetime;
    }

    public void setRecentupdatetime(String recentupdatetime) {
        this.recentupdatetime = recentupdatetime;
    }

    public String getAlbumid() {
        return albumid;
    }

    public void setAlbumid(String albumid) {
        this.albumid = albumid;
    }

    public Object getSubjectname() {
        return subjectname;
    }

    public void setSubjectname(Object subjectname) {
        this.subjectname = subjectname;
    }

    public String getAlbumname() {
        return albumname;
    }

    public void setAlbumname(String albumname) {
        this.albumname = albumname;
    }

    public int getAlbumtype() {
        return albumtype;
    }

    public void setAlbumtype(int albumtype) {
        this.albumtype = albumtype;
    }

    public String getRecenttitle() {
        return recenttitle;
    }

    public void setRecenttitle(String recenttitle) {
        this.recenttitle = recenttitle;
    }

    public String getAlbumauthor() {
        return albumauthor;
    }

    public void setAlbumauthor(String albumauthor) {
        this.albumauthor = albumauthor;
    }

    public String getAuthorid() {
        return authorid;
    }

    public void setAuthorid(String authorid) {
        this.authorid = authorid;
    }
}
