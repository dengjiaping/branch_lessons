package com.yidiankeyan.science.information.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nby on 2016/8/7.
 * 专辑文章实体类
 */
public class AlbumContent implements Parcelable {

    /**
     * mediaurl : /2.mp3
     */

    private String mediaurl;
    /**
     * checkstatus : 2
     * imgs :
     * space : 0.0
     * length : 0
     */

    private String checkstatus;
    private String imgs;
    private double space;
    private int length;
    /**
     * isNew : 1
     */

    private int isNew;

    public AlbumContent(String articlename) {
        this.articlename = articlename;
    }



    /**
     * articleid : b1b0c0b2b3ed44e08e2bbf909860f50e
     * articlename : 为什么不能在加时赛中抛硬币
     * lastupdatetime : null
     * articletype : 1
     * praisenum : 0
     * readnum : 0
     * coverimgurl : /84.jpg
     */


    private String articleid;
    private String articlename;
    private long lastupdatetime;
    private int articletype;
    private int praisenum;
    private int readnum;
    private String coverimgurl;
    private String albumId;
    private String albumName;
    private String albumAvatar;
    private int downloadState;
    private String filePath;
    private boolean checked;
    private boolean playing;
    private String authorname;
    /**
     * 是否观看，已下载的时候会用到
     */
    private int alreadyWatch;
    /**
     * 文件大小，已下载的时候会用到
     */
    private String fileSize;
    /**
     * 这篇内容所属的专辑有多少篇（为了在下载里面显示，真他妈费劲，就这么个小玩意去升级数据库）
     */
    private int contentNum;

    public int getContentNum() {
        return contentNum;
    }
    private boolean isNeedDelete;

    public boolean isNeedDelete() {
        return isNeedDelete;
    }

    public void setNeedDelete(boolean needDelete) {
        isNeedDelete = needDelete;
    }

    public void setContentNum(int contentNum) {
        this.contentNum = contentNum;
    }

    public int getAlreadyWatch() {
        return alreadyWatch;
    }

    public void setAlreadyWatch(int alreadyWatch) {
        this.alreadyWatch = alreadyWatch;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getAuthorname() {
        return authorname;
    }

    public void setAuthorname(String authorname) {
        this.authorname = authorname;
    }

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getDownloadState() {
        return downloadState;
    }

    public void setDownloadState(int downloadState) {
        this.downloadState = downloadState;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getAlbumAvatar() {
        return albumAvatar;
    }

    public void setAlbumAvatar(String albumAvatar) {
        this.albumAvatar = albumAvatar;
    }

    public String getArticleid() {
        return articleid;
    }

    public void setArticleid(String articleid) {
        this.articleid = articleid;
    }

    public String getArticlename() {
        return articlename;
    }

    public void setArticlename(String articlename) {
        this.articlename = articlename;
    }

    public long getLastupdatetime() {
        return lastupdatetime;
    }

    public void setLastupdatetime(long lastupdatetime) {
        this.lastupdatetime = lastupdatetime;
    }

    public int getArticletype() {
        return articletype;
    }

    public void setArticletype(int articletype) {
        this.articletype = articletype;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AlbumContent content = (AlbumContent) o;

        return articleid != null ? articleid.equals(content.articleid) : content.articleid == null;

    }

    @Override
    public String toString() {
        return "AlbumContent{" +
                "mediaurl='" + mediaurl + '\'' +
                ", articleid='" + articleid + '\'' +
                ", articlename='" + articlename + '\'' +
                ", lastupdatetime=" + lastupdatetime +
                ", articletype=" + articletype +
                ", praisenum=" + praisenum +
                ", readnum=" + readnum +
                ", coverimgurl='" + coverimgurl + '\'' +
                ", albumId='" + albumId + '\'' +
                ", albumName='" + albumName + '\'' +
                ", albumAvatar='" + albumAvatar + '\'' +
                ", downloadState=" + downloadState +
                ", filePath='" + filePath + '\'' +
                ", checked=" + checked +
                ", playing=" + playing +
                ", authorname='" + authorname + '\'' +
                ", alreadyWatch=" + alreadyWatch +
                ", fileSize='" + fileSize + '\'' +
                ", contentNum=" + contentNum +
                '}';
    }

    @Override
    public int hashCode() {
        return articleid != null ? articleid.hashCode() : 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mediaurl);
        dest.writeString(this.articleid);
        dest.writeString(this.articlename);
        dest.writeLong(this.lastupdatetime);
        dest.writeInt(this.articletype);
        dest.writeInt(this.praisenum);
        dest.writeInt(this.readnum);
        dest.writeString(this.coverimgurl);
        dest.writeString(this.albumId);
        dest.writeString(this.albumName);
        dest.writeString(this.albumAvatar);
        dest.writeInt(this.downloadState);
        dest.writeString(this.filePath);
        dest.writeByte(this.checked ? (byte) 1 : (byte) 0);
        dest.writeByte(this.playing ? (byte) 1 : (byte) 0);
        dest.writeString(this.authorname);
        dest.writeInt(this.alreadyWatch);
        dest.writeString(this.fileSize);
    }

    protected AlbumContent(Parcel in) {
        this.mediaurl = in.readString();
        this.articleid = in.readString();
        this.articlename = in.readString();
        this.lastupdatetime = in.readLong();
        this.articletype = in.readInt();
        this.praisenum = in.readInt();
        this.readnum = in.readInt();
        this.coverimgurl = in.readString();
        this.albumId = in.readString();
        this.albumName = in.readString();
        this.albumAvatar = in.readString();
        this.downloadState = in.readInt();
        this.filePath = in.readString();
        this.checked = in.readByte() != 0;
        this.playing = in.readByte() != 0;
        this.authorname = in.readString();
        this.alreadyWatch = in.readInt();
        this.fileSize = in.readString();
    }

    public static final Creator<AlbumContent> CREATOR = new Creator<AlbumContent>() {
        @Override
        public AlbumContent createFromParcel(Parcel source) {
            return new AlbumContent(source);
        }

        @Override
        public AlbumContent[] newArray(int size) {
            return new AlbumContent[size];
        }
    };


    public String getCheckstatus() {
        return checkstatus;
    }

    public void setCheckstatus(String checkstatus) {
        this.checkstatus = checkstatus;
    }

    public String getImgs() {
        return imgs;
    }

    public void setImgs(String imgs) {
        this.imgs = imgs;
    }

    public double getSpace() {
        return space;
    }

    public void setSpace(double space) {
        this.space = space;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getIsNew() {
        return isNew;
    }

    public void setIsNew(int isNew) {
        this.isNew = isNew;
    }
}
