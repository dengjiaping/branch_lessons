package com.yidiankeyan.science.subscribe.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

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
 * Created by nby on 2016/10/13.
 * 作用：内容评论实体类
 */
public class ContentCommentBean implements Parcelable {

    /**
     * userid : -09ca122f5b1c474e89013e997f5cf61f
     * username : null
     * imgurl : null
     * commentid : 216be6c4d1fc44ff866b49e7930503bd
     * createtime : 1474251694000
     * ups : 0
     * commentnum : 2
     * content : 同乐
     * isreply : 0
     * replyid : null
     * replyname : null
     * commentnnum : 2
     */

    private FatherBean father;
    /**
     * userid : -1
     * username : null
     * imgurl : null
     * commentid : 3a2aeacb9bda451eb4aa44da8c7e389b
     * createtime : 1476346242000
     * ups : 0
     * commentnum : 0
     * content : 最新
     * isreply : 1
     * replyid : c9e7f97396704d38b76909e29dba5828
     * replyname : null
     * commentnnum : 0
     */

    private List<SonsBean> sons;

    public FatherBean getFather() {
        return father;
    }

    public void setFather(FatherBean father) {
        this.father = father;
    }

    public List<SonsBean> getSons() {
        return sons;
    }

    public void setSons(List<SonsBean> sons) {
        this.sons = sons;
    }

    public static class FatherBean implements Parcelable {
        private String userid;
        private String username;
        private String imgurl;
        private String commentid;
        private long createtime;
        private int ups;
        private int commentnum;
        private String content;
        private int isreply;
        private String replyid;
        private String replyname;
        private int commentnnum;
        private int liked;

        public int getLiked() {
            return liked;
        }

        public void setLiked(int liked) {
            this.liked = liked;
        }

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
            dest.writeInt(this.isreply);
            dest.writeString(this.replyid);
            dest.writeString(this.replyname);
            dest.writeInt(this.commentnnum);
            dest.writeInt(this.liked);
        }

        public FatherBean() {
        }

        protected FatherBean(Parcel in) {
            this.userid = in.readString();
            this.username = in.readString();
            this.imgurl = in.readString();
            this.commentid = in.readString();
            this.createtime = in.readLong();
            this.ups = in.readInt();
            this.commentnum = in.readInt();
            this.content = in.readString();
            this.isreply = in.readInt();
            this.replyid = in.readString();
            this.replyname = in.readString();
            this.commentnnum = in.readInt();
            this.liked = in.readInt();
        }

        public static final Creator<FatherBean> CREATOR = new Creator<FatherBean>() {
            @Override
            public FatherBean createFromParcel(Parcel source) {
                return new FatherBean(source);
            }

            @Override
            public FatherBean[] newArray(int size) {
                return new FatherBean[size];
            }
        };
    }

    public static class SonsBean implements Parcelable {
        private String userid;
        private String username;
        private String imgurl;
        private String commentid;
        private long createtime;
        private int ups;
        private int commentnum;
        private String content;
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
            dest.writeInt(this.isreply);
            dest.writeString(this.replyid);
            dest.writeString(this.replyname);
            dest.writeInt(this.commentnnum);
        }

        public SonsBean() {
        }

        protected SonsBean(Parcel in) {
            this.userid = in.readString();
            this.username = in.readString();
            this.imgurl = in.readString();
            this.commentid = in.readString();
            this.createtime = in.readLong();
            this.ups = in.readInt();
            this.commentnum = in.readInt();
            this.content = in.readString();
            this.isreply = in.readInt();
            this.replyid = in.readString();
            this.replyname = in.readString();
            this.commentnnum = in.readInt();
        }

        public static final Creator<SonsBean> CREATOR = new Creator<SonsBean>() {
            @Override
            public SonsBean createFromParcel(Parcel source) {
                return new SonsBean(source);
            }

            @Override
            public SonsBean[] newArray(int size) {
                return new SonsBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.father, flags);
        dest.writeList(this.sons);
    }

    public ContentCommentBean() {
    }

    protected ContentCommentBean(Parcel in) {
        this.father = in.readParcelable(FatherBean.class.getClassLoader());
        this.sons = new ArrayList<SonsBean>();
        in.readList(this.sons, SonsBean.class.getClassLoader());
    }

    public static final Creator<ContentCommentBean> CREATOR = new Creator<ContentCommentBean>() {
        @Override
        public ContentCommentBean createFromParcel(Parcel source) {
            return new ContentCommentBean(source);
        }

        @Override
        public ContentCommentBean[] newArray(int size) {
            return new ContentCommentBean[size];
        }
    };
}
