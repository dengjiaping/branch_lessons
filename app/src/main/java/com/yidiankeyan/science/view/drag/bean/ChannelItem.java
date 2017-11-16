package com.yidiankeyan.science.view.drag.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * ITEM的对应可序化队列属性
 */
public class ChannelItem implements Parcelable {
    /**
     * 栏目在整体中的排序顺序  rank
     */
    public Integer orderId;
    /**
     * 栏目是否选中
     */
    public Integer selected;
    /**
     * subjectid : 1
     * subjectname : 哲学
     */

    private int subjectid;
    private String subjectname;

    public ChannelItem() {
    }

    public ChannelItem(int id, String name, int orderId, int selected) {
        subjectid = id;
        this.subjectname = name;
        this.orderId = Integer.valueOf(orderId);
        this.selected = Integer.valueOf(selected);
    }

    public ChannelItem(Integer id, String name, Integer selected) {
        this.subjectid = id;
        this.subjectname = name;
        this.selected = selected;
        this.orderId = id;
    }

    public int getId() {
        return subjectid;
    }

    public String getName() {
        return this.subjectname;
    }

    public int getOrderId() {
        return this.orderId.intValue();
    }

    public int getSelected() {
        if (selected == null)
            return 0;
        return this.selected;
    }

    public void setId(int paramInt) {
        this.subjectid = Integer.valueOf(paramInt);
    }

    public void setName(String paramString) {
        this.subjectname = paramString;
    }

    public void setOrderId(int paramInt) {
        this.orderId = Integer.valueOf(paramInt);
    }

    public void setSelected(Integer paramInteger) {
        this.selected = paramInteger;
    }

    public String toString() {
        return "ChannelItem [id=" + this.subjectid + ", name=" + this.subjectname
                + ", selected=" + this.selected + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChannelItem that = (ChannelItem) o;

        if (subjectid != that.subjectid) return false;
        if (orderId != null ? !orderId.equals(that.orderId) : that.orderId != null) return false;
        if (selected != null ? !selected.equals(that.selected) : that.selected != null)
            return false;
        return subjectname != null ? subjectname.equals(that.subjectname) : that.subjectname == null;

    }

    @Override
    public int hashCode() {
        int result = orderId != null ? orderId.hashCode() : 0;
        result = 31 * result + (selected != null ? selected.hashCode() : 0);
        result = 31 * result + subjectid;
        result = 31 * result + (subjectname != null ? subjectname.hashCode() : 0);
        return result;
    }

    public int getSubjectid() {
        return subjectid;
    }

    public void setSubjectid(int subjectid) {
        this.subjectid = subjectid;
    }

    public String getSubjectname() {
        return subjectname;
    }

    public void setSubjectname(String subjectname) {
        this.subjectname = subjectname;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.orderId);
        dest.writeValue(this.selected);
        dest.writeInt(this.subjectid);
        dest.writeString(this.subjectname);
    }

    protected ChannelItem(Parcel in) {
        this.orderId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.selected = (Integer) in.readValue(Integer.class.getClassLoader());
        this.subjectid = in.readInt();
        this.subjectname = in.readString();
    }

    public static final Creator<ChannelItem> CREATOR = new Creator<ChannelItem>() {
        @Override
        public ChannelItem createFromParcel(Parcel source) {
            return new ChannelItem(source);
        }

        @Override
        public ChannelItem[] newArray(int size) {
            return new ChannelItem[size];
        }
    };
}