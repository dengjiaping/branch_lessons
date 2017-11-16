package com.yidiankeyan.science.information.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nby on 2016/7/15.
 */
public class FlashBean implements Parcelable {
    private String content;
    private int lookCount;
    private String date;
    private boolean isRead;
    private boolean isClick;
    /**
     * id : 430b17e8d5a94181b57eacaa9d7e240a
     * title : 北京首发空气重污染红色预警，将视情况建议中小学幼儿园停课
     * createtime : 1470713074000
     * readnum : 0
     * sharenum : 0
     */

    private String id;
    private String title;
    private long createtime;
    private int readnum;
    private int sharenum;
    private String time;
    private String createtimestr;

    private int praisenum;

    private int isPraised;

    public String getCreatetimestr() {
        return createtimestr;
    }

    public void setCreatetimestr(String createtimestr) {
        this.createtimestr = createtimestr;
    }

    public int getIsPraised() {
        return isPraised;
    }

    public void setIsPraised(int isPraised) {
        this.isPraised = isPraised;
    }

    public int getPraisenum() {
        return praisenum;
    }

    public void setPraisenum(int praisenum) {
        this.praisenum = praisenum;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isClick() {
        return isClick;
    }

    public void setClick(boolean click) {
        isClick = click;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public FlashBean(String content) {
        this.content = content;
    }

    public FlashBean(String content, int lookCount) {
        this.content = content;
        this.lookCount = lookCount;
    }

    public FlashBean() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLookCount() {
        return lookCount;
    }

    public void setLookCount(int lookCount) {
        this.lookCount = lookCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getCreatetime() {
        return createtime;
    }

    public void setCreatetime(long createtime) {
        this.createtime = createtime;
    }

    public int getReadnum() {
        return readnum;
    }

    public void setReadnum(int readnum) {
        this.readnum = readnum;
    }

    public int getSharenum() {
        return sharenum;
    }

    public void setSharenum(int sharenum) {
        this.sharenum = sharenum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FlashBean flashBean = (FlashBean) o;

        if (lookCount != flashBean.lookCount) return false;
        if (isRead != flashBean.isRead) return false;
        if (isClick != flashBean.isClick) return false;
        if (createtime != flashBean.createtime) return false;
        if (readnum != flashBean.readnum) return false;
        if (sharenum != flashBean.sharenum) return false;
        if (praisenum != flashBean.praisenum) return false;
        if (isPraised != flashBean.isPraised) return false;
        if (content != null ? !content.equals(flashBean.content) : flashBean.content != null)
            return false;
        if (date != null ? !date.equals(flashBean.date) : flashBean.date != null) return false;
        if (id != null ? !id.equals(flashBean.id) : flashBean.id != null) return false;
        if (title != null ? !title.equals(flashBean.title) : flashBean.title != null) return false;
        return time != null ? time.equals(flashBean.time) : flashBean.time == null;

    }

    @Override
    public int hashCode() {
        int result = content != null ? content.hashCode() : 0;
        result = 31 * result + lookCount;
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (isRead ? 1 : 0);
        result = 31 * result + (isClick ? 1 : 0);
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (int) (createtime ^ (createtime >>> 32));
        result = 31 * result + readnum;
        result = 31 * result + sharenum;
        result = 31 * result + (time != null ? time.hashCode() : 0);
        result = 31 * result + praisenum;
        result = 31 * result + isPraised;
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.content);
        dest.writeInt(this.lookCount);
        dest.writeString(this.date);
        dest.writeByte(this.isRead ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isClick ? (byte) 1 : (byte) 0);
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeLong(this.createtime);
        dest.writeInt(this.readnum);
        dest.writeInt(this.sharenum);
        dest.writeString(this.time);
        dest.writeInt(this.praisenum);
        dest.writeInt(this.isPraised);
    }

    protected FlashBean(Parcel in) {
        this.content = in.readString();
        this.lookCount = in.readInt();
        this.date = in.readString();
        this.isRead = in.readByte() != 0;
        this.isClick = in.readByte() != 0;
        this.id = in.readString();
        this.title = in.readString();
        this.createtime = in.readLong();
        this.readnum = in.readInt();
        this.sharenum = in.readInt();
        this.time = in.readString();
        this.praisenum = in.readInt();
        this.isPraised = in.readInt();
    }

    public static final Creator<FlashBean> CREATOR = new Creator<FlashBean>() {
        @Override
        public FlashBean createFromParcel(Parcel source) {
            return new FlashBean(source);
        }

        @Override
        public FlashBean[] newArray(int size) {
            return new FlashBean[size];
        }
    };
}
