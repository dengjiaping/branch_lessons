package com.yidiankeyan.science.my.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * //  ◥█▄▃▁   ▁▂▂▃▃▂▂▂▂▂▂▂▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▅
 * //.......◥█☆█▅▄▃▁▁▁▁▁▃▄▅▅▅▅▅▅▅▅▅▅▅▅▅▅▄▁█▅●    ...Created by zn on 2017/3/13 0013.
 * //〓▇█████ ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓███▅▄▃███●
 * // 〓〓.๑۩۞۩๑.█████████████████████◤
 * //   ◥█████████◤ ◥        ◢◤
 * //     ◥██████◤        ◥  ◢◤
 * //       ████◤██████◤                主页
 * //       █▓▓▓▓██◤                        -单篇文章
 * //      █▓▓▓██◆
 * //     █▓▓▓██◆
 * //    █▓▓▓██◆
 * //   █▓▓▓██◆
 * //  █▓▓▓██◆
 * // █▓▓ ██◆
 * //█▓▓ ██◆
 */
public class SinglesArticleBean implements Parcelable {

    /**
     * id : afa0297106aa4bf6af455a3f4b3abbbc
     * checkstatus : null
     * name : 测试热点大图视频
     * abstracts : 2016年法国欧洲杯将是历史上首次扩军到24支球队，普拉蒂尼的小国惠顾政策让冰岛、北爱尔兰、威尔士这样以前难以登上国际舞台的小球队也有了扬名立万的机会，当然对于球迷来说可以欣赏到的高水平比赛场次则从31场增加到了51场。但是对于彩民来说，所增加的不仅仅是20场竞猜场次，而是小组赛期间更为扑朔迷离的出线形势，将使竞猜的难度进一步增加。本文解读一番新赛制下24支球队的法国欧洲杯，到底几分才能出线？
     * summary : 这是一篇有关欧洲杯的文章
     * reference : 《天下足球》
     * userid : -0012
     * size : null
     * createtime : 1487666174000
     * author : null
     * publishtime : 1487666174000
     * isuse : null
     * type : 3
     * sourcetype : 1
     * commentnum : null
     * praisenum : null
     * reprintnum : null
     * readnum : null
     * coverimgurl : http://f.hiphotos.baidu.com/image/h%3D200/sign=9d661b069dcad1c8cfbbfb274f3e67c4/5bafa40f4bfbfbed064c3d5670f0f736afc31f85.jpg
     * shortimgurl : null
     * mediaurl : xx.mp3
     * albumid : -1
     * subjectid : null
     * isfrozen : null
     * belongtype : null
     * lastupdatetime : 1487666174000
     * content : null
     * covertype : null
     * imgs : null
     * uptime : 1579307803000
     */

    private String id;
    private Integer checkstatus;
    private String name;
    private String abstracts;
    private String summary;
    private String reference;
    private String userid;
    private Integer size;
    private long createtime;
    private String author;
    private long publishtime;
    private Integer isuse;
    private int type;
    private int sourcetype;
    private int commentnum;
    private int praisenum;
    private int reprintnum;
    private int readnum;
    private String coverimgurl;
    private String shortimgurl;
    private String mediaurl;
    private String albumid;
    private String subjectid;
    private Integer isfrozen;
    private Integer belongtype;
    private long lastupdatetime;
    private String content;
    private Integer covertype;//文字内容
    private String imgs;
    private long uptime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getCheckstatus() {
        return checkstatus;
    }

    public void setCheckstatus(Integer checkstatus) {
        this.checkstatus = checkstatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbstracts() {
        return abstracts;
    }

    public void setAbstracts(String abstracts) {
        this.abstracts = abstracts;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public long getCreatetime() {
        return createtime;
    }

    public void setCreatetime(long createtime) {
        this.createtime = createtime;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public long getPublishtime() {
        return publishtime;
    }

    public void setPublishtime(long publishtime) {
        this.publishtime = publishtime;
    }

    public Integer getIsuse() {
        return isuse;
    }

    public void setIsuse(Integer isuse) {
        this.isuse = isuse;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSourcetype() {
        return sourcetype;
    }

    public void setSourcetype(int sourcetype) {
        this.sourcetype = sourcetype;
    }

    public int getCommentnum() {
        return commentnum;
    }

    public void setCommentnum(int commentnum) {
        this.commentnum = commentnum;
    }

    public int getPraisenum() {
        return praisenum;
    }

    public void setPraisenum(int praisenum) {
        this.praisenum = praisenum;
    }

    public int getReprintnum() {
        return reprintnum;
    }

    public void setReprintnum(int reprintnum) {
        this.reprintnum = reprintnum;
    }

    public int getReadnum() {
        return readnum;
    }

    public void setReadnum(int readnum) {
        this.readnum = readnum;
    }

    public String getCoverimgurl() {
        return coverimgurl;
    }

    public void setCoverimgurl(String coverimgurl) {
        this.coverimgurl = coverimgurl;
    }

    public String getShortimgurl() {
        return shortimgurl;
    }

    public void setShortimgurl(String shortimgurl) {
        this.shortimgurl = shortimgurl;
    }

    public String getMediaurl() {
        return mediaurl;
    }

    public void setMediaurl(String mediaurl) {
        this.mediaurl = mediaurl;
    }

    public String getAlbumid() {
        return albumid;
    }

    public void setAlbumid(String albumid) {
        this.albumid = albumid;
    }

    public String getSubjectid() {
        return subjectid;
    }

    public void setSubjectid(String subjectid) {
        this.subjectid = subjectid;
    }

    public Integer getIsfrozen() {
        return isfrozen;
    }

    public void setIsfrozen(Integer isfrozen) {
        this.isfrozen = isfrozen;
    }

    public Integer getBelongtype() {
        return belongtype;
    }

    public void setBelongtype(Integer belongtype) {
        this.belongtype = belongtype;
    }

    public long getLastupdatetime() {
        return lastupdatetime;
    }

    public void setLastupdatetime(long lastupdatetime) {
        this.lastupdatetime = lastupdatetime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getCovertype() {
        return covertype;
    }

    public void setCovertype(Integer covertype) {
        this.covertype = covertype;
    }

    public String getImgs() {
        return imgs;
    }

    public void setImgs(String imgs) {
        this.imgs = imgs;
    }

    public long getUptime() {
        return uptime;
    }

    public void setUptime(long uptime) {
        this.uptime = uptime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeValue(this.checkstatus);
        dest.writeString(this.name);
        dest.writeString(this.abstracts);
        dest.writeString(this.summary);
        dest.writeString(this.reference);
        dest.writeString(this.userid);
        dest.writeValue(this.size);
        dest.writeLong(this.createtime);
        dest.writeString(this.author);
        dest.writeLong(this.publishtime);
        dest.writeValue(this.isuse);
        dest.writeInt(this.type);
        dest.writeInt(this.sourcetype);
        dest.writeInt(this.commentnum);
        dest.writeInt(this.praisenum);
        dest.writeInt(this.reprintnum);
        dest.writeInt(this.readnum);
        dest.writeString(this.coverimgurl);
        dest.writeString(this.shortimgurl);
        dest.writeString(this.mediaurl);
        dest.writeString(this.albumid);
        dest.writeString(this.subjectid);
        dest.writeValue(this.isfrozen);
        dest.writeValue(this.belongtype);
        dest.writeLong(this.lastupdatetime);
        dest.writeString(this.content);
        dest.writeValue(this.covertype);
        dest.writeString(this.imgs);
        dest.writeLong(this.uptime);
    }

    public SinglesArticleBean() {
    }

    protected SinglesArticleBean(Parcel in) {
        this.id = in.readString();
        this.checkstatus = (Integer) in.readValue(Integer.class.getClassLoader());
        this.name = in.readString();
        this.abstracts = in.readString();
        this.summary = in.readString();
        this.reference = in.readString();
        this.userid = in.readString();
        this.size = (Integer) in.readValue(Integer.class.getClassLoader());
        this.createtime = in.readLong();
        this.author = in.readString();
        this.publishtime = in.readLong();
        this.isuse = (Integer) in.readValue(Integer.class.getClassLoader());
        this.type = in.readInt();
        this.sourcetype = in.readInt();
        this.commentnum = in.readInt();
        this.praisenum = in.readInt();
        this.reprintnum = in.readInt();
        this.readnum = in.readInt();
        this.coverimgurl = in.readString();
        this.shortimgurl = in.readString();
        this.mediaurl = in.readString();
        this.albumid = in.readString();
        this.subjectid = in.readString();
        this.isfrozen = (Integer) in.readValue(Integer.class.getClassLoader());
        this.belongtype = (Integer) in.readValue(Integer.class.getClassLoader());
        this.lastupdatetime = in.readLong();
        this.content = in.readString();
        this.covertype = (Integer) in.readValue(Integer.class.getClassLoader());
        this.imgs = in.readString();
        this.uptime = in.readLong();
    }

    public static final Creator<SinglesArticleBean> CREATOR = new Creator<SinglesArticleBean>() {
        @Override
        public SinglesArticleBean createFromParcel(Parcel source) {
            return new SinglesArticleBean(source);
        }

        @Override
        public SinglesArticleBean[] newArray(int size) {
            return new SinglesArticleBean[size];
        }
    };
}
