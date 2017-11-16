package com.yidiankeyan.science.information.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nby on 2016/7/13.
 */
public class RecentContentBean implements Parcelable {

    private int type;//1:图片,2:音频,3:视频
    private String duration;//视频，音频的播放时长
    private String title;
    private String content;
    private String albumName;
    private String readCount;
    private String clickCount;
    private String time;
    private int imgUrl;
    private int voiceRes;
    private int playState;
    /**
     * id : test_article_001
     * name : 测试小说文章
     * abstracts : 短篇小说
     * createtime : 1470379077000
     * praisenum : 0
     * readnum : 0
     * coverimgurl : /static/44.jpg
     * mediaurl : null
     * albumname : 测试小说专辑
     * belongtype : 0
     */
    private String id;
    private String name;
    private String abstracts;
    private long createtime;
    private int praisenum;
    private int readnum;
    private String coverimgurl;
    private String mediaurl;
    private String albumname;
    private int belongtype;
    private boolean buffered;
    private String authername;

    public String getAuthername() {
        return authername;
    }

    public void setAuthername(String authername) {
        this.authername = authername;
    }

    public boolean isBuffered() {
        return buffered;
    }

    public void setBuffered(boolean buffered) {
        this.buffered = buffered;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RecentContentBean bean = (RecentContentBean) o;

        if (type != bean.type) return false;
        if (imgUrl != bean.imgUrl) return false;
        if (voiceRes != bean.voiceRes) return false;
        if (playState != bean.playState) return false;
        if (createtime != bean.createtime) return false;
        if (praisenum != bean.praisenum) return false;
        if (readnum != bean.readnum) return false;
        if (belongtype != bean.belongtype) return false;
        if (duration != null ? !duration.equals(bean.duration) : bean.duration != null)
            return false;
        if (title != null ? !title.equals(bean.title) : bean.title != null) return false;
        if (content != null ? !content.equals(bean.content) : bean.content != null) return false;
        if (albumName != null ? !albumName.equals(bean.albumName) : bean.albumName != null)
            return false;
        if (readCount != null ? !readCount.equals(bean.readCount) : bean.readCount != null)
            return false;
        if (clickCount != null ? !clickCount.equals(bean.clickCount) : bean.clickCount != null)
            return false;
        if (time != null ? !time.equals(bean.time) : bean.time != null) return false;
        if (id != null ? !id.equals(bean.id) : bean.id != null) return false;
        if (name != null ? !name.equals(bean.name) : bean.name != null) return false;
        if (abstracts != null ? !abstracts.equals(bean.abstracts) : bean.abstracts != null)
            return false;
        if (coverimgurl != null ? !coverimgurl.equals(bean.coverimgurl) : bean.coverimgurl != null)
            return false;
        if (mediaurl != null ? !mediaurl.equals(bean.mediaurl) : bean.mediaurl != null)
            return false;
        return albumname != null ? albumname.equals(bean.albumname) : bean.albumname == null;

    }

    @Override
    public int hashCode() {
        int result = type;
        result = 31 * result + (duration != null ? duration.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (albumName != null ? albumName.hashCode() : 0);
        result = 31 * result + (readCount != null ? readCount.hashCode() : 0);
        result = 31 * result + (clickCount != null ? clickCount.hashCode() : 0);
        result = 31 * result + (time != null ? time.hashCode() : 0);
        result = 31 * result + imgUrl;
        result = 31 * result + voiceRes;
        result = 31 * result + playState;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (abstracts != null ? abstracts.hashCode() : 0);
        result = 31 * result + (int) (createtime ^ (createtime >>> 32));
        result = 31 * result + praisenum;
        result = 31 * result + readnum;
        result = 31 * result + (coverimgurl != null ? coverimgurl.hashCode() : 0);
        result = 31 * result + (mediaurl != null ? mediaurl.hashCode() : 0);
        result = 31 * result + (albumname != null ? albumname.hashCode() : 0);
        result = 31 * result + belongtype;
        return result;
    }

    public int getPlayState() {
        return playState;
    }

    public void setPlayState(int playState) {
        this.playState = playState;
    }

    public int getVoiceRes() {
        return voiceRes;
    }

    public void setVoiceRes(int voiceRes) {
        this.voiceRes = voiceRes;
    }

    public int getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(int imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getReadCount() {
        return readCount;
    }

    public void setReadCount(String readCount) {
        this.readCount = readCount;
    }

    public String getClickCount() {
        return clickCount;
    }

    public void setClickCount(String clickCount) {
        this.clickCount = clickCount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public long getCreatetime() {
        return createtime;
    }

    public void setCreatetime(long createtime) {
        this.createtime = createtime;
    }

    public int getPraisenum() {
        return praisenum;
    }

    public void setPraisenum(int praisenum) {
        this.praisenum = praisenum;
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

    public String getMediaurl() {
        return mediaurl;
    }

    public void setMediaurl(String mediaurl) {
        this.mediaurl = mediaurl;
    }

    public String getAlbumname() {
        return albumname;
    }

    public void setAlbumname(String albumname) {
        this.albumname = albumname;
    }

    public int getBelongtype() {
        return belongtype;
    }

    public void setBelongtype(int belongtype) {
        this.belongtype = belongtype;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type);
        dest.writeString(this.duration);
        dest.writeString(this.title);
        dest.writeString(this.content);
        dest.writeString(this.albumName);
        dest.writeString(this.readCount);
        dest.writeString(this.clickCount);
        dest.writeString(this.time);
        dest.writeInt(this.imgUrl);
        dest.writeInt(this.voiceRes);
        dest.writeInt(this.playState);
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.abstracts);
        dest.writeLong(this.createtime);
        dest.writeInt(this.praisenum);
        dest.writeInt(this.readnum);
        dest.writeString(this.coverimgurl);
        dest.writeString(this.mediaurl);
        dest.writeString(this.albumname);
        dest.writeInt(this.belongtype);
        dest.writeByte(this.buffered ? (byte) 1 : (byte) 0);
    }

    public RecentContentBean() {
    }

    protected RecentContentBean(Parcel in) {
        this.type = in.readInt();
        this.duration = in.readString();
        this.title = in.readString();
        this.content = in.readString();
        this.albumName = in.readString();
        this.readCount = in.readString();
        this.clickCount = in.readString();
        this.time = in.readString();
        this.imgUrl = in.readInt();
        this.voiceRes = in.readInt();
        this.playState = in.readInt();
        this.id = in.readString();
        this.name = in.readString();
        this.abstracts = in.readString();
        this.createtime = in.readLong();
        this.praisenum = in.readInt();
        this.readnum = in.readInt();
        this.coverimgurl = in.readString();
        this.mediaurl = in.readString();
        this.albumname = in.readString();
        this.belongtype = in.readInt();
        this.buffered = in.readByte() != 0;
    }

    public static final Creator<RecentContentBean> CREATOR = new Creator<RecentContentBean>() {
        @Override
        public RecentContentBean createFromParcel(Parcel source) {
            return new RecentContentBean(source);
        }

        @Override
        public RecentContentBean[] newArray(int size) {
            return new RecentContentBean[size];
        }
    };
}
