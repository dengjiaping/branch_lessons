package com.yidiankeyan.science.subscribe.entity;

/**
 * Created by nby on 2016/8/7.
 * 专辑详情里面的作者其他专辑
 */
public class AlbumDetailAuthorAlbum {

    /**
     * albumid : 58da25a1931548acb325bdcd7b21f0b6
     * subjectname : 天文学
     * albumname : 天文器材
     * albumtype : 1
     * recenttitle : 望远镜的性能决定因素
     * albumauthor : 沙师弟
     * authorid : 1
     * coverimgurl : /77.jpg
     * recentupdatetime : 1469690099000
     * contentnum : 3
     * readnum : 0
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

    public AlbumDetailAuthorAlbum(String albumname) {
        this.albumname = albumname;
    }

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

    @Override
    public String toString() {
        return "AlbumDetailAuthorAlbum{" +
                "albumid='" + albumid + '\'' +
                ", subjectname='" + subjectname + '\'' +
                ", albumname='" + albumname + '\'' +
                ", albumtype=" + albumtype +
                ", recenttitle='" + recenttitle + '\'' +
                ", albumauthor='" + albumauthor + '\'' +
                ", authorid='" + authorid + '\'' +
                ", coverimgurl='" + coverimgurl + '\'' +
                ", recentupdatetime=" + recentupdatetime +
                ", contentnum=" + contentnum +
                ", readnum=" + readnum +
                '}';
    }
}
