package com.yidiankeyan.science.information.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nby on 2016/7/21.
 */
public class BannerBean implements Parcelable {
    /**
     * id : 3f999950e3fb4e20a41fd950efd72a55
     * title : 有趣、有料、有态度
     * desc :
     * imgurl : /static/upload/cmsweb/image/jpeg2016/11/2/5f2bdaa73a974d32a92bde80fc3762c3.jpg
     * linkurl : baed7e6767b040c5b7e2d6dd415977b8
     * linktype : 1
     * createtime : 1478052146000
     * uplinetime : 1478044800000
     * offlinetime : 1480464000000
     * isuse : 1
     * positionid : 1
     * menuid : null
     * belongid : 2
     * checkstatus : 2
     * text :
     */

    private String id;
    private String title;
    private String desc;
    private String imgurl;
    private String linkurl;
    private int linktype;
    private long createtime;
    private long uplinetime;
    private long offlinetime;
    private int isuse;
    private int positionid;
    private String menuid;
    private int belongid;
    private int checkstatus;

    public String getColumnActivityPrice() {
        return columnActivityPrice;
    }

    public void setColumnActivityPrice(String columnActivityPrice) {
        this.columnActivityPrice = columnActivityPrice;
    }

    public int getIshasactivityprice() {
        return ishasactivityprice;
    }

    public void setIshasactivityprice(int ishasactivityprice) {
        this.ishasactivityprice = ishasactivityprice;
    }

    private String text;
    private String haveYouPurchased;
    private String columnActivityPrice;
    private int ishasactivityprice;   //1：有活动价  0：无活动价

    private String columnWriterIntro;

    public String getColumnPrice() {
        return columnPrice;
    }

    public void setColumnPrice(String columnPrice) {
        this.columnPrice = columnPrice;
    }

    private String columnPrice;

    public String getColumnWriterIntro() {
        return columnWriterIntro;
    }

    public void setColumnWriterIntro(String columnWriterIntro) {
        this.columnWriterIntro = columnWriterIntro;
    }

    public String getHaveYouPurchased() {
        return haveYouPurchased;
    }

    public void setHaveYouPurchased(String haveYouPurchased) {
        this.haveYouPurchased = haveYouPurchased;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getLinkurl() {
        return linkurl;
    }

    public void setLinkurl(String linkurl) {
        this.linkurl = linkurl;
    }

    public int getLinktype() {
        return linktype;
    }

    public void setLinktype(int linktype) {
        this.linktype = linktype;
    }

    public long getCreatetime() {
        return createtime;
    }

    public void setCreatetime(long createtime) {
        this.createtime = createtime;
    }

    public long getUplinetime() {
        return uplinetime;
    }

    public void setUplinetime(long uplinetime) {
        this.uplinetime = uplinetime;
    }

    public long getOfflinetime() {
        return offlinetime;
    }

    public void setOfflinetime(long offlinetime) {
        this.offlinetime = offlinetime;
    }

    public int getIsuse() {
        return isuse;
    }

    public void setIsuse(int isuse) {
        this.isuse = isuse;
    }

    public int getPositionid() {
        return positionid;
    }

    public void setPositionid(int positionid) {
        this.positionid = positionid;
    }

    public String getMenuid() {
        return menuid;
    }

    public void setMenuid(String menuid) {
        this.menuid = menuid;
    }

    public int getBelongid() {
        return belongid;
    }

    public void setBelongid(int belongid) {
        this.belongid = belongid;
    }

    public int getCheckstatus() {
        return checkstatus;
    }

    public void setCheckstatus(int checkstatus) {
        this.checkstatus = checkstatus;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeString(this.desc);
        dest.writeString(this.imgurl);
        dest.writeString(this.linkurl);
        dest.writeInt(this.linktype);
        dest.writeLong(this.createtime);
        dest.writeLong(this.uplinetime);
        dest.writeLong(this.offlinetime);
        dest.writeInt(this.isuse);
        dest.writeInt(this.positionid);
        dest.writeString(this.menuid);
        dest.writeInt(this.belongid);
        dest.writeInt(this.checkstatus);
        dest.writeString(this.text);
    }

    public BannerBean() {
    }

    protected BannerBean(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.desc = in.readString();
        this.imgurl = in.readString();
        this.linkurl = in.readString();
        this.linktype = in.readInt();
        this.createtime = in.readLong();
        this.uplinetime = in.readLong();
        this.offlinetime = in.readLong();
        this.isuse = in.readInt();
        this.positionid = in.readInt();
        this.menuid = in.readString();
        this.belongid = in.readInt();
        this.checkstatus = in.readInt();
        this.text = in.readString();
    }

    public static final Creator<BannerBean> CREATOR = new Creator<BannerBean>() {
        @Override
        public BannerBean createFromParcel(Parcel source) {
            return new BannerBean(source);
        }

        @Override
        public BannerBean[] newArray(int size) {
            return new BannerBean[size];
        }
    };
}
