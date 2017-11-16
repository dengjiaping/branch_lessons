package com.yidiankeyan.science.information.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nby on 2016/8/9.
 * 答人实体类
 */
public class AnswerPeopleBean implements Parcelable {

    /**
     * id : 2
     * userimgurl : 11
     * kdname : 中和
     * profession : 艺术家/哲学家/数学家
     * eavesdropnum : 0
     * answersnum : 0
     * messageurl : 1233
     * authentication : 0
     * isfocus : 0
     */

    private String id;
    private String userimgurl;
    private String kdname;
    private String profession;
    private int eavesdropnum;
    private int answersnum;
    private String messageurl;
    private int authentication;
    private int isfocus;
    private String content;
    private String title;
    private int imgUrl;
    private String updateTime;
    private String hearNumber;
    private int likeNumber;
    private int problemAmount;
    /**
     * position : position
     */

    private String position;
    /**
     * followernum : 0
     */

    private int followernum;


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

    public int getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(int imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
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

    public int getProblemAmount() {
        return problemAmount;
    }

    public void setProblemAmount(int problemAmount) {
        this.problemAmount = problemAmount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserimgurl() {
        return userimgurl;
    }

    public void setUserimgurl(String userimgurl) {
        this.userimgurl = userimgurl;
    }

    public String getKdname() {
        return kdname;
    }

    public void setKdname(String kdname) {
        this.kdname = kdname;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public int getEavesdropnum() {
        return eavesdropnum;
    }

    public void setEavesdropnum(int eavesdropnum) {
        this.eavesdropnum = eavesdropnum;
    }

    public int getAnswersnum() {
        return answersnum;
    }

    public void setAnswersnum(int answersnum) {
        this.answersnum = answersnum;
    }

    public String getMessageurl() {
        return messageurl;
    }

    public void setMessageurl(String messageurl) {
        this.messageurl = messageurl;
    }

    public int getAuthentication() {
        return authentication;
    }

    public void setAuthentication(int authentication) {
        this.authentication = authentication;
    }

    public int getIsfocus() {
        return isfocus;
    }

    public void setIsfocus(int isfocus) {
        this.isfocus = isfocus;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.userimgurl);
        dest.writeString(this.kdname);
        dest.writeString(this.profession);
        dest.writeInt(this.eavesdropnum);
        dest.writeInt(this.answersnum);
        dest.writeString(this.messageurl);
        dest.writeInt(this.authentication);
        dest.writeInt(this.isfocus);
        dest.writeString(this.content);
        dest.writeString(this.title);
        dest.writeInt(this.imgUrl);
        dest.writeString(this.updateTime);
        dest.writeString(this.hearNumber);
        dest.writeInt(this.likeNumber);
        dest.writeInt(this.problemAmount);
        dest.writeString(this.position);
    }

    public AnswerPeopleBean() {
    }

    protected AnswerPeopleBean(Parcel in) {
        this.id = in.readString();
        this.userimgurl = in.readString();
        this.kdname = in.readString();
        this.profession = in.readString();
        this.eavesdropnum = in.readInt();
        this.answersnum = in.readInt();
        this.messageurl = in.readString();
        this.authentication = in.readInt();
        this.isfocus = in.readInt();
        this.content = in.readString();
        this.title = in.readString();
        this.imgUrl = in.readInt();
        this.updateTime = in.readString();
        this.hearNumber = in.readString();
        this.likeNumber = in.readInt();
        this.problemAmount = in.readInt();
        this.position = in.readString();
    }

    public static final Creator<AnswerPeopleBean> CREATOR = new Creator<AnswerPeopleBean>() {
        @Override
        public AnswerPeopleBean createFromParcel(Parcel source) {
            return new AnswerPeopleBean(source);
        }

        @Override
        public AnswerPeopleBean[] newArray(int size) {
            return new AnswerPeopleBean[size];
        }
    };

    public int getFollowernum() {
        return followernum;
    }

    public void setFollowernum(int followernum) {
        this.followernum = followernum;
    }
}
