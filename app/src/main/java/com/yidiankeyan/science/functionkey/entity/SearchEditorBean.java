package com.yidiankeyan.science.functionkey.entity;

/**
 * 主编搜索结果
 */
public class SearchEditorBean {

    /**
     * authorid : 1
     * name : 1
     * nick : 潘建伟
     * imgurl : /11.png
     * albumname : null
     * fansnum : 9
     */

    private String authorid;
    private String name;
    private String nick;
    private String imgurl;
    private Object albumname;
    private int fansnum;

    public String getAuthorid() {
        return authorid;
    }

    public void setAuthorid(String authorid) {
        this.authorid = authorid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public Object getAlbumname() {
        return albumname;
    }

    public void setAlbumname(Object albumname) {
        this.albumname = albumname;
    }

    public int getFansnum() {
        return fansnum;
    }

    public void setFansnum(int fansnum) {
        this.fansnum = fansnum;
    }
}
