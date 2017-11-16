package com.yidiankeyan.science.information.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zn on 2016/7/14 0014.
 */
public class AnswerAlbumBean implements Parcelable {
    private int imgUrl;//图片
    private String title;//标题
    private String content;//内容
    private String hearNumber;//偷听人数
    private int likeNumber;//点赞人数
    private String updateTime;//更新时间
    /**
     * id : 1
     * kdname : 学术研究
     * eavesdropnum : 0
     * praisenum : 0
     * kedernum : 0
     * kedausernum : 0
     * lastupdatetime : 1469878251000
     * lastmaker :
     * lastsolver :
     * coverimgurl :
     */

    private int id;
    private String kdname;
    private int eavesdropnum;
    private int praisenum;
    private int kedernum;
    private int kedausernum;
    private long lastupdatetime;
    private String lastmaker;
    private String lastsolver;
    private String coverimgurl;
    private boolean selected;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getProblemAmount() {
        return problemAmount;
    }

    public void setProblemAmount(int problemAmount) {
        this.problemAmount = problemAmount;
    }

    public int getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(int imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHearNumber() {
        return hearNumber;
    }

    public void setHearNumber(String hearNumber) {
        this.hearNumber = hearNumber;
    }

    public int getLikeNumber() {
        return likeNumber;
    }

    public void setLikeNumber(int likeNumber) {
        this.likeNumber = likeNumber;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    private int problemAmount;//问题量


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKdname() {
        return kdname;
    }

    public void setKdname(String kdname) {
        this.kdname = kdname;
    }

    public int getEavesdropnum() {
        return eavesdropnum;
    }

    public void setEavesdropnum(int eavesdropnum) {
        this.eavesdropnum = eavesdropnum;
    }

    public int getPraisenum() {
        return praisenum;
    }

    public void setPraisenum(int praisenum) {
        this.praisenum = praisenum;
    }

    public int getKedernum() {
        return kedernum;
    }

    public void setKedernum(int kedernum) {
        this.kedernum = kedernum;
    }

    public int getKedausernum() {
        return kedausernum;
    }

    public void setKedausernum(int kedausernum) {
        this.kedausernum = kedausernum;
    }

    public long getLastupdatetime() {
        return lastupdatetime;
    }

    public void setLastupdatetime(long lastupdatetime) {
        this.lastupdatetime = lastupdatetime;
    }

    public String getLastmaker() {
        return lastmaker;
    }

    public void setLastmaker(String lastmaker) {
        this.lastmaker = lastmaker;
    }

    public String getLastsolver() {
        return lastsolver;
    }

    public void setLastsolver(String lastsolver) {
        this.lastsolver = lastsolver;
    }

    public String getCoverimgurl() {
        return coverimgurl;
    }

    public void setCoverimgurl(String coverimgurl) {
        this.coverimgurl = coverimgurl;
    }

    public AnswerAlbumBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.imgUrl);
        dest.writeString(this.title);
        dest.writeString(this.content);
        dest.writeString(this.hearNumber);
        dest.writeInt(this.likeNumber);
        dest.writeString(this.updateTime);
        dest.writeInt(this.id);
        dest.writeString(this.kdname);
        dest.writeInt(this.eavesdropnum);
        dest.writeInt(this.praisenum);
        dest.writeInt(this.kedernum);
        dest.writeInt(this.kedausernum);
        dest.writeLong(this.lastupdatetime);
        dest.writeString(this.lastmaker);
        dest.writeString(this.lastsolver);
        dest.writeString(this.coverimgurl);
        dest.writeByte(this.selected ? (byte) 1 : (byte) 0);
        dest.writeInt(this.problemAmount);
    }

    protected AnswerAlbumBean(Parcel in) {
        this.imgUrl = in.readInt();
        this.title = in.readString();
        this.content = in.readString();
        this.hearNumber = in.readString();
        this.likeNumber = in.readInt();
        this.updateTime = in.readString();
        this.id = in.readInt();
        this.kdname = in.readString();
        this.eavesdropnum = in.readInt();
        this.praisenum = in.readInt();
        this.kedernum = in.readInt();
        this.kedausernum = in.readInt();
        this.lastupdatetime = in.readLong();
        this.lastmaker = in.readString();
        this.lastsolver = in.readString();
        this.coverimgurl = in.readString();
        this.selected = in.readByte() != 0;
        this.problemAmount = in.readInt();
    }

    public static final Creator<AnswerAlbumBean> CREATOR = new Creator<AnswerAlbumBean>() {
        @Override
        public AnswerAlbumBean createFromParcel(Parcel source) {
            return new AnswerAlbumBean(source);
        }

        @Override
        public AnswerAlbumBean[] newArray(int size) {
            return new AnswerAlbumBean[size];
        }
    };
}
