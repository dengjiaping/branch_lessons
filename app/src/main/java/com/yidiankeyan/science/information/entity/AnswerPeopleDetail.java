package com.yidiankeyan.science.information.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * ////////////////////////////////////////////////////////////////////
 * //                          _ooOoo_                               //
 * //                         o8888888o                              //
 * //                         88" . "88                              //
 * //                         (| ^_^ |)                              //
 * //                         O\  =  /O                              //
 * //                      ____/`---'\____                           //
 * //                    .'  \\|     |//  `.                         //
 * //                   /  \\|||  :  |||//  \                        //
 * //                  /  _||||| -:- |||||-  \                       //
 * //                  |   | \\\  -  /// |   |                       //
 * //                  | \_|  ''\---/''  |   |                       //
 * //                  \  .-\__  `-`  ___/-. /                       //
 * //                ___`. .'  /--.--\  `. . ___                     //
 * //              ."" '<  `.___\_<|>_/___.'  >'"".                  //
 * //            | | :  `- \`.;`\ _ /`;.`/ - ` : | |                 //
 * //            \  \ `-.   \_ __\ /__ _/   .-` /  /                 //
 * //      ========`-.____`-.___\_____/___.-`____.-'========         //
 * //                           `=---='                              //
 * //      ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^        //
 * //             佛祖保佑      永无BUG     永不修改                  //
 * ////////////////////////////////////////////////////////////////////
 * Created by nby on 2016/9/3.
 * 作用：答人详情的具体信息
 */
public class AnswerPeopleDetail implements Parcelable {

    /**
     * id : -0012
     * mouthprice : 0.01
     * answersnum : 1
     * totalincome : 0.01
     * createtime : 1484101158000
     * domain : 1,4,11
     * focusnum : 6
     * listennum : 0
     * eavesdropnum : 0
     * messageurl : /2.mp3
     * messagetaketime : 23
     * name : 大和哥
     * coverimg : xxx.jpg
     * profession : 计算机博士
     * authenticated : 2
     * sign : u r the fancy gay to me
     * isFocus : 0
     * followers : 2
     */

    private String id;
    private double mouthprice;
    private int answersnum;
    private double totalincome;
    private long createtime;
    private String domain;
    private int focusnum;
    private int listennum;
    private int eavesdropnum;
    private String messageurl;
    private int messagetaketime;
    private String name;
    private String coverimg;
    private String profession;
    private int authenticated;
    private String sign;
    private int isFocus;
    private int followers;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getMouthprice() {
        return mouthprice;
    }

    public void setMouthprice(double mouthprice) {
        this.mouthprice = mouthprice;
    }

    public int getAnswersnum() {
        return answersnum;
    }

    public void setAnswersnum(int answersnum) {
        this.answersnum = answersnum;
    }

    public double getTotalincome() {
        return totalincome;
    }

    public void setTotalincome(double totalincome) {
        this.totalincome = totalincome;
    }

    public long getCreatetime() {
        return createtime;
    }

    public void setCreatetime(long createtime) {
        this.createtime = createtime;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public int getFocusnum() {
        return focusnum;
    }

    public void setFocusnum(int focusnum) {
        this.focusnum = focusnum;
    }

    public int getListennum() {
        return listennum;
    }

    public void setListennum(int listennum) {
        this.listennum = listennum;
    }

    public int getEavesdropnum() {
        return eavesdropnum;
    }

    public void setEavesdropnum(int eavesdropnum) {
        this.eavesdropnum = eavesdropnum;
    }

    public String getMessageurl() {
        return messageurl;
    }

    public void setMessageurl(String messageurl) {
        this.messageurl = messageurl;
    }

    public int getMessagetaketime() {
        return messagetaketime;
    }

    public void setMessagetaketime(int messagetaketime) {
        this.messagetaketime = messagetaketime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCoverimg() {
        return coverimg;
    }

    public void setCoverimg(String coverimg) {
        this.coverimg = coverimg;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public int getAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(int authenticated) {
        this.authenticated = authenticated;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public int getIsFocus() {
        return isFocus;
    }

    public void setIsFocus(int isFocus) {
        this.isFocus = isFocus;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeDouble(this.mouthprice);
        dest.writeInt(this.answersnum);
        dest.writeDouble(this.totalincome);
        dest.writeLong(this.createtime);
        dest.writeString(this.domain);
        dest.writeInt(this.focusnum);
        dest.writeInt(this.listennum);
        dest.writeInt(this.eavesdropnum);
        dest.writeString(this.messageurl);
        dest.writeInt(this.messagetaketime);
        dest.writeString(this.name);
        dest.writeString(this.coverimg);
        dest.writeString(this.profession);
        dest.writeInt(this.authenticated);
        dest.writeString(this.sign);
        dest.writeInt(this.isFocus);
        dest.writeInt(this.followers);
    }

    public AnswerPeopleDetail() {
    }

    protected AnswerPeopleDetail(Parcel in) {
        this.id = in.readString();
        this.mouthprice = in.readDouble();
        this.answersnum = in.readInt();
        this.totalincome = in.readDouble();
        this.createtime = in.readLong();
        this.domain = in.readString();
        this.focusnum = in.readInt();
        this.listennum = in.readInt();
        this.eavesdropnum = in.readInt();
        this.messageurl = in.readString();
        this.messagetaketime = in.readInt();
        this.name = in.readString();
        this.coverimg = in.readString();
        this.profession = in.readString();
        this.authenticated = in.readInt();
        this.sign = in.readString();
        this.isFocus = in.readInt();
        this.followers = in.readInt();
    }

    public static final Creator<AnswerPeopleDetail> CREATOR = new Creator<AnswerPeopleDetail>() {
        @Override
        public AnswerPeopleDetail createFromParcel(Parcel source) {
            return new AnswerPeopleDetail(source);
        }

        @Override
        public AnswerPeopleDetail[] newArray(int size) {
            return new AnswerPeopleDetail[size];
        }
    };
}
