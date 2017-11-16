package com.yidiankeyan.science.subscribe.entity;

/**
 *  -所有专辑实体类
 */
public class BusinessAllBean {
    private int imgUrl;//专辑图片
    private String title;//专辑标题
    private String contentId;//内容ID
    private String content;//专辑内容
    private String name;//主编名字
    private int contentNumber;//内容量
    private int toPeekNumber;//偷看数
    private String updateTime;//更新时间
    /**
     * albumid : test_novle_id
     * subjectname : null
     * albumname : 测试小说专辑
     * albumtype : 0
     * recenttitle : 测试赛思小说最后update文章名
     * albumauthor : 中和
     * coverimgurl : /5.jpg
     * recentupdatetime : 1470302514000
     * contentnum : 1
     * readnum : 0
     */

    private String albumid;
    private Object subjectname;
    private String albumname;
    private int albumtype;
    private String recenttitle;
    private String albumauthor;
    private String coverimgurl;
    private long recentupdatetime;
    private int contentnum;
    private int readnum;
    /**
     * orderid : test_order_novle_id
     * authorname : 中和
     * updates : 1
     * istop : 0
     */

    private String orderid;
    private String authorname;
    private int updates;
    private int istop;
    /**
     * authorid : 1
     */

    private String authorid;
    /**
     * id : 02970b25ce03447590246b376c072590
     * shortimgurl : /static/upload/cmsweb/image/png2016/8/19/6de568c29c494ed4b8f9291f0509a559.png
     * price : 0
     * priceunit : 4
     * type : 1
     * lastupdatetitle : 庞大的战神家族：122毫米系列火炮
     * size : 0
     * praisenum : 0
     * belongtype : 1
     * belongid : 38
     * ordernum : 0
     * sharenum : null
     */

    private String id;
    private String shortimgurl;
    private int price;
    private int priceunit;
    private int type;
    private String lastupdatetitle;
    private int size;
    private int praisenum;
    private int belongtype;
    private int belongid;
    private int ordernum;
    private Object sharenum;
    /**
     * checkstatus : null
     */

    private Object checkstatus;


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

    public int getContentNumber() {
        return contentNumber;
    }

    public void setContentNumber(int contentNumber) {
        this.contentNumber = contentNumber;
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

    public int getToPeekNumber() {
        return toPeekNumber;
    }

    public void setToPeekNumber(int toPeekNumber) {
        this.toPeekNumber = toPeekNumber;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
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

    public String getCoverimgurl() {
        return coverimgurl;
    }

    public void setCoverimgurl(String coverimgurl) {
        this.coverimgurl = coverimgurl;
    }

    public long getRecentupdatetime() {
        return recentupdatetime;
    }

    public void setRecentupdatetime(long recentupdatetime) {
        this.recentupdatetime = recentupdatetime;
    }

    public int getContentnum() {
        return contentnum;
    }

    public void setContentnum(int contentnum) {
        this.contentnum = contentnum;
    }

    public int getReadnum() {
        return readnum;
    }

    public void setReadnum(int readnum) {
        this.readnum = readnum;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getAuthorname() {
        return authorname;
    }

    public void setAuthorname(String authorname) {
        this.authorname = authorname;
    }

    public int getUpdates() {
        return updates;
    }

    public void setUpdates(int updates) {
        this.updates = updates;
    }

    public int getIstop() {
        return istop;
    }

    public void setIstop(int istop) {
        this.istop = istop;
    }

    public String getAuthorid() {
        return authorid;
    }

    public void setAuthorid(String authorid) {
        this.authorid = authorid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Object getCheckstatus() {
        return checkstatus;
    }

    public void setCheckstatus(Object checkstatus) {
        this.checkstatus = checkstatus;
    }
}
