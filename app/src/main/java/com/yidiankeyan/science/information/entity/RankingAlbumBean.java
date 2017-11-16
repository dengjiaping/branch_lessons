package com.yidiankeyan.science.information.entity;

/**
 * Created by nby on 2016/8/10.
 * 排行
 * 专辑榜
 * 专辑实体类
 */
public class RankingAlbumBean {

    /**
     * albumid : 68ea3b1fcf2b4649a42b78385e118680
     * subjectname : 宇宙探索
     * albumname : 宇宙系列
     * albumtype : 2
     * recenttitle : 从神秘的脉冲星谈起
     * albumauthor : 手机用户
     * authorid : 17ab18c16efe442bbd265f55f81942c9
     * coverimgurl : /static/upload/cmsweb/image/png2016/8/23/93e9cf1f3da14e55a26bfc54f55b307c.png
     * recentupdatetime : 1473146456000
     * contentnum : 10
     * readnum : 1970
     */

    private String albumid;
    private String subjectname;
    private String albumname;
    private int albumtype;
    private String recenttitle;
    private String albumauthor;
    private String authorid;
    private String coverimgurl;
    private long recentupdatetime;
    private int contentnum;
    private int readnum;




    public String getAlbumid() {
        return albumid;
    }

    public void setAlbumid(String albumid) {
        this.albumid = albumid;
    }

    public String getSubjectname() {
        return subjectname;
    }

    public void setSubjectname(String subjectname) {
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
}
