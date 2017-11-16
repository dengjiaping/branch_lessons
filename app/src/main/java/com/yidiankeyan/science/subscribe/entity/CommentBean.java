package com.yidiankeyan.science.subscribe.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nby on 2016/8/11.
 * 评论实体类
 */
public class CommentBean implements Parcelable {

    /**
     * userid : 1
     * username : 沙师弟
     * imgurl : /999.jpg
     * commentid : a36f9e99d4254026a78e06ef26e2c07a
     * createtime : 1470896561000
     * ups : 0
     * commentnum : 0
     * content : 测试音频评论
     */

    private String userid;
    private String username;
    private String imgurl;
    private String commentid;
    private long createtime;
    private int ups;
    private int commentnum;
    private String content;
    /**
     * isreply : 1
     * replyid : cc06a5f6fc4e4932a4550f9efca3cd58
     * replyname : 18500560145
     * commentnnum : 0
     */

    private int isreply;
    private String replyid;
    private String replyname;
    private int commentnnum;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getCommentid() {
        return commentid;
    }

    public void setCommentid(String commentid) {
        this.commentid = commentid;
    }

    public long getCreatetime() {
        return createtime;
    }

    public void setCreatetime(long createtime) {
        this.createtime = createtime;
    }

    public int getUps() {
        return ups;
    }

    public void setUps(int ups) {
        this.ups = ups;
    }

    public int getCommentnum() {
        return commentnum;
    }

    public void setCommentnum(int commentnum) {
        this.commentnum = commentnum;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userid);
        dest.writeString(this.username);
        dest.writeString(this.imgurl);
        dest.writeString(this.commentid);
        dest.writeLong(this.createtime);
        dest.writeInt(this.ups);
        dest.writeInt(this.commentnum);
        dest.writeString(this.content);
    }

    public CommentBean() {
    }

    protected CommentBean(Parcel in) {
        this.userid = in.readString();
        this.username = in.readString();
        this.imgurl = in.readString();
        this.commentid = in.readString();
        this.createtime = in.readLong();
        this.ups = in.readInt();
        this.commentnum = in.readInt();
        this.content = in.readString();
    }

    public static final Creator<CommentBean> CREATOR = new Creator<CommentBean>() {
        @Override
        public CommentBean createFromParcel(Parcel source) {
            return new CommentBean(source);
        }

        @Override
        public CommentBean[] newArray(int size) {
            return new CommentBean[size];
        }
    };

    public int getIsreply() {
        return isreply;
    }

    public void setIsreply(int isreply) {
        this.isreply = isreply;
    }

    public String getReplyid() {
        return replyid;
    }

    public void setReplyid(String replyid) {
        this.replyid = replyid;
    }

    public String getReplyname() {
        return replyname;
    }

    public void setReplyname(String replyname) {
        this.replyname = replyname;
    }

    public int getCommentnnum() {
        return commentnnum;
    }

    public void setCommentnnum(int commentnnum) {
        this.commentnnum = commentnnum;
    }
}
