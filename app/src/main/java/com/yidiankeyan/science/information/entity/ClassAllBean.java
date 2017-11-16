package com.yidiankeyan.science.information.entity;

/**
 *  -所有专辑实体类
 */
public class ClassAllBean {
    private int imgUrl;//专辑图片
    private String title;//专辑标题
    private String contentId;//内容ID
    private String content;//专辑内容
    private String name;//主编名字
    private int contentNumber;//内容量
    private int toPeekNumber;//偷看数
    private String updateTime;//更新时间
    private int imgType;//专辑类型图片
    /**
     * albumid : test_company_id
     * subjectname : null
     * albumname : 测试企业号专辑
     * albumtype : 4
     * recenttitle : 测试企业号最后update文章名
     * albumauthor : 伟大的测试人员
     * coverimgurl : /static/1.jpg
     * recentupdatetime : 1470301831000
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
     * orderid : null
     * authorname : null
     * updates : 0
     * istop : 0
     */

    private String orderid;
    private String authorname;
    private int updates;
    private int istop;

    public int getImgType() {
        return imgType;
    }

    public void setImgType(int imgType) {
        this.imgType = imgType;
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
}
