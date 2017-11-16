package com.yidiankeyan.science.information.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * //  ◥█▄▃▁   ▁▂▂▃▃▂▂▂▂▂▂▂▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▅
 * //.......◥█☆█▅▄▃▁▁▁▁▁▃▄▅▅▅▅▅▅▅▅▅▅▅▅▅▅▄▁█▅●    ...Created by zn on 2017/7/5 0005.
 * //〓▇█████ ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓███▅▄▃███●
 * // 〓〓.๑۩۞۩๑.█████████████████████◤
 * //   ◥█████████◤ ◥        ◢◤
 * //     ◥██████◤        ◥  ◢◤
 * //       ████◤██████◤
 * //       █▓▓▓▓██◤            月刊详情
 * //      █▓▓▓██◆                  -节选列表bean
 * //     █▓▓▓██◆
 * //    █▓▓▓██◆
 * //   █▓▓▓██◆
 * //  █▓▓▓██◆
 * // █▓▓ ██◆
 * //█▓▓ ██◆
 */
public class MagazineExcerptBean implements Parcelable {

    /**
     * id : 2c34549c859144e29e15e84c0239d78d
     * monthlyId : 21575e70e8c44e0684aefb8207531f30
     * name : 节选二
     * space : 22
     * length : 443
     * createTime : 1499064040000
     * checkstatus : 2
     * author : 天外来客
     */

    private String id;
    private String monthlyId;
    private String monthlyName;
    private String name;
    private int space;
    private int length;
    private long createTime;
    private int checkstatus;
    private String author;
    private String audiourl;
    private int downloadState;
    private String filePath;
    private boolean checked;
    private boolean playing;
    /**
     * 是否观看，已下载的时候会用到
     */
    private int alreadyWatch;
    /**
     * 文件大小，已下载的时候会用到
     */
    private String fileSize;
    /**
     * 这篇内容所属的专辑有多少篇
     */
    private int contentNum;
    private String coverimg;

    public String getMonthlyName() {
        return monthlyName;
    }

    public void setMonthlyName(String monthlyName) {
        this.monthlyName = monthlyName;
    }

    public String getAudiourl() {
        return audiourl;
    }

    public void setAudiourl(String audiourl) {
        this.audiourl = audiourl;
    }

    public int getAlreadyWatch() {
        return alreadyWatch;
    }

    public void setAlreadyWatch(int alreadyWatch) {
        this.alreadyWatch = alreadyWatch;
    }

    public int getDownloadState() {
        return downloadState;
    }

    public void setDownloadState(int downloadState) {
        this.downloadState = downloadState;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public int getContentNum() {
        return contentNum;
    }

    public void setContentNum(int contentNum) {
        this.contentNum = contentNum;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMonthlyId() {
        return monthlyId;
    }

    public void setMonthlyId(String monthlyId) {
        this.monthlyId = monthlyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSpace() {
        return space;
    }

    public void setSpace(int space) {
        this.space = space;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getCheckstatus() {
        return checkstatus;
    }

    public void setCheckstatus(int checkstatus) {
        this.checkstatus = checkstatus;
    }

    public String getauthor() {
        return author;
    }

    public void setauthor(String author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "MagazineExcerptBean{" +
                "id='" + id + '\'' +
                ", monthlyId='" + monthlyId + '\'' +
                ", name='" + name + '\'' +
                ", space=" + space +
                ", length=" + length +
                ", createTime=" + createTime +
                ", checkstatus=" + checkstatus +
                ", author='" + author + '\'' +
                ", audiourl='" + audiourl + '\'' +
                ", downloadState=" + downloadState +
                ", filePath='" + filePath + '\'' +
                ", checked=" + checked +
                ", playing=" + playing +
                ", alreadyWatch=" + alreadyWatch +
                ", fileSize='" + fileSize + '\'' +
                ", contentNum=" + contentNum +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.monthlyId);
        dest.writeString(this.name);
        dest.writeLong(this.createTime);
        dest.writeInt(this.space);
        dest.writeInt(this.length);
        dest.writeInt(this.checkstatus);
        dest.writeString(this.author);
        dest.writeInt(this.downloadState);
        dest.writeString(this.filePath);
        dest.writeByte(this.checked ? (byte) 1 : (byte) 0);
        dest.writeByte(this.playing ? (byte) 1 : (byte) 0);
        dest.writeInt(this.alreadyWatch);
        dest.writeString(this.fileSize);
        dest.writeString(this.audiourl);
    }

    public MagazineExcerptBean(Parcel in) {
        this.id = in.readString();
        this.monthlyId = in.readString();
        this.name = in.readString();
        this.createTime = in.readLong();
        this.space = in.readInt();
        this.length = in.readInt();
        this.checkstatus = in.readInt();
        this.author = in.readString();
        this.downloadState = in.readInt();
        this.filePath = in.readString();
        this.checked = in.readByte() != 0;
        this.playing = in.readByte() != 0;
        this.alreadyWatch = in.readInt();
        this.fileSize = in.readString();
        this.audiourl = in.readString();
    }

    public MagazineExcerptBean() {
    }

    public static final Creator<MagazineExcerptBean> CREATOR = new Creator<MagazineExcerptBean>() {
        @Override
        public MagazineExcerptBean createFromParcel(Parcel source) {
            return new MagazineExcerptBean(source);
        }

        @Override
        public MagazineExcerptBean[] newArray(int size) {
            return new MagazineExcerptBean[size];
        }
    };

    public void setCoverimg(String coverimg) {
        this.coverimg = coverimg;
    }

    public String getCoverimg() {
        return coverimg;

    }
}
