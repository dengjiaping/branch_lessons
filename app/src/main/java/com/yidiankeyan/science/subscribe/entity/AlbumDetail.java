package com.yidiankeyan.science.subscribe.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 专辑详情实体类
 */
public class AlbumDetail implements Parcelable {

    /**
     * albumid : 6d92cee057e04cfb9ca4050ced281ef1
     * albumname : 趣味数学
     * imgurl : /78.jpg
     * readnum : 0
     * authorname : 沙师弟
     * subjectname : 哲学
     * lastupdatetime : 1469701503000
     * price : 0
     * type : 1
     * priceunit : 4
     * ispaid : 0
     * isorder : 1
     * describes : 测试describe
     * authorimgurl : xx
     * authordescribe : 一点科研测试人员
     * isfocus : 0
     */

    private String albumid;
    private String albumname;
    private String imgurl;
    private int readnum;
    private String authorname;
    private String subjectname;
    private long lastupdatetime;
    private double price;
    private int type;
    private int priceunit;
    private int ispaid;
    private int isorder;
    private String describes;
    private String authorimgurl;
    private String authordescribe;
    private int isfocus;
    /**
     * authorid : 1
     */

    private String authorid;
    /**
     * followersnum : 4
     * belongtype : 1
     * belongid : 2
     * contentnum : 6
     */

    private int followersnum;
    private int belongtype;
    private int belongid;
    private int contentnum;
    /**
     * lastupdatetitle : 从神秘的脉冲星谈起
     */
    private int permission;
    private String guidelines;

    public String getGuidelines() {
        return guidelines;
    }

    public void setGuidelines(String guidelines) {
        this.guidelines = guidelines;
    }

    public int getPermission() {
        return permission;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }

    private String lastupdatetitle;

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

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public int getReadnum() {
        return readnum;
    }

    public void setReadnum(int readnum) {
        this.readnum = readnum;
    }

    public String getAuthorname() {
        return authorname;
    }

    public void setAuthorname(String authorname) {
        this.authorname = authorname;
    }

    public String getSubjectname() {
        return subjectname;
    }

    public void setSubjectname(String subjectname) {
        this.subjectname = subjectname;
    }

    public long getLastupdatetime() {
        return lastupdatetime;
    }

    public void setLastupdatetime(long lastupdatetime) {
        this.lastupdatetime = lastupdatetime;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPriceunit() {
        return priceunit;
    }

    public void setPriceunit(int priceunit) {
        this.priceunit = priceunit;
    }

    public int getIspaid() {
        return ispaid;
    }

    public void setIspaid(int ispaid) {
        this.ispaid = ispaid;
    }

    public int getIsorder() {
        return isorder;
    }

    public void setIsorder(int isorder) {
        this.isorder = isorder;
    }

    public String getDescribes() {
        return describes;
    }

    public void setDescribes(String describes) {
        this.describes = describes;
    }

    public String getAuthorimgurl() {
        return authorimgurl;
    }

    public void setAuthorimgurl(String authorimgurl) {
        this.authorimgurl = authorimgurl;
    }

    public String getAuthordescribe() {
        return authordescribe;
    }

    public void setAuthordescribe(String authordescribe) {
        this.authordescribe = authordescribe;
    }

    public int getIsfocus() {
        return isfocus;
    }

    public void setIsfocus(int isfocus) {
        this.isfocus = isfocus;
    }

    public AlbumDetail() {
    }

    public String getAuthorid() {
        return authorid;
    }

    public void setAuthorid(String authorid) {
        this.authorid = authorid;
    }

    public int getFollowersnum() {
        return followersnum;
    }

    public void setFollowersnum(int followersnum) {
        this.followersnum = followersnum;
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

    public int getContentnum() {
        return contentnum;
    }

    public void setContentnum(int contentnum) {
        this.contentnum = contentnum;
    }

    public String getLastupdatetitle() {
        return lastupdatetitle;
    }

    public void setLastupdatetitle(String lastupdatetitle) {
        this.lastupdatetitle = lastupdatetitle;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.albumid);
        dest.writeString(this.albumname);
        dest.writeString(this.imgurl);
        dest.writeInt(this.readnum);
        dest.writeString(this.authorname);
        dest.writeString(this.subjectname);
        dest.writeLong(this.lastupdatetime);
        dest.writeDouble(this.price);
        dest.writeInt(this.type);
        dest.writeInt(this.priceunit);
        dest.writeInt(this.ispaid);
        dest.writeInt(this.isorder);
        dest.writeString(this.describes);
        dest.writeString(this.authorimgurl);
        dest.writeString(this.authordescribe);
        dest.writeInt(this.isfocus);
        dest.writeString(this.authorid);
        dest.writeInt(this.followersnum);
        dest.writeInt(this.belongtype);
        dest.writeInt(this.belongid);
        dest.writeInt(this.contentnum);
        dest.writeString(this.lastupdatetitle);
    }

    protected AlbumDetail(Parcel in) {
        this.albumid = in.readString();
        this.albumname = in.readString();
        this.imgurl = in.readString();
        this.readnum = in.readInt();
        this.authorname = in.readString();
        this.subjectname = in.readString();
        this.lastupdatetime = in.readLong();
        this.price = in.readDouble();
        this.type = in.readInt();
        this.priceunit = in.readInt();
        this.ispaid = in.readInt();
        this.isorder = in.readInt();
        this.describes = in.readString();
        this.authorimgurl = in.readString();
        this.authordescribe = in.readString();
        this.isfocus = in.readInt();
        this.authorid = in.readString();
        this.followersnum = in.readInt();
        this.belongtype = in.readInt();
        this.belongid = in.readInt();
        this.contentnum = in.readInt();
        this.lastupdatetitle = in.readString();
    }

    public static final Creator<AlbumDetail> CREATOR = new Creator<AlbumDetail>() {
        @Override
        public AlbumDetail createFromParcel(Parcel source) {
            return new AlbumDetail(source);
        }

        @Override
        public AlbumDetail[] newArray(int size) {
            return new AlbumDetail[size];
        }
    };
}
