package com.yidiankeyan.science.information.entity;

/**
 * Created by zn on 2016/8/6 0006.
 * 已订专辑实体类
 */
public class BookedAlbumBean {


    /**
     * orderid : 6d92cee057e04cfb9ca4050ced281ef1
     * albumid : 6d92cee057e04cfb9ca4050ced281ef1
     * albumname : 趣味数学
     * authorname : 伟大的测试人员
     * albumtype : 1
     * subjectname : 哲学
     * recenttitle : 为什么不能在加时赛中抛硬币
     * coverimgurl : /78.jpg
     * recentupdatetime : 1469701503000
     * contentnum : 11
     * readnum : 0
     * updates : 11
     * istop : 0
     */

    private String orderid;
    private String albumid;
    private String albumname;
    private String authorname;
    private int albumtype;
    private String subjectname;
    private String recenttitle;
    private String coverimgurl;
    private long recentupdatetime;
    private int contentnum;
    private int readnum;
    private int updates;
    private int istop;
    /**
     * albumauthor : 手机用户
     * authorid : 17ab18c16efe442bbd265f55f81942c9
     */

    private String albumauthor;
    private String authorid;

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getAlbumid() {
        return albumid;
    }

    public void setAlbumid(String albumid) {
        this.albumid = albumid;
    }

    public String getAlbumname() {
        return albumname;
    }

    public void setAlbumname(String albumname) {
        this.albumname = albumname;
    }

    public String getAuthorname() {
        return authorname;
    }

    public void setAuthorname(String authorname) {
        this.authorname = authorname;
    }

    public int getAlbumtype() {
        return albumtype;
    }

    public void setAlbumtype(int albumtype) {
        this.albumtype = albumtype;
    }

    public String getSubjectname() {
        return subjectname;
    }

    public void setSubjectname(String subjectname) {
        this.subjectname = subjectname;
    }

    public String getRecenttitle() {
        return recenttitle;
    }

    public void setRecenttitle(String recenttitle) {
        this.recenttitle = recenttitle;
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
