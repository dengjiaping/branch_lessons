package com.yidiankeyan.science.information.entity;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * //  ◥█▄▃▁   ▁▂▂▃▃▂▂▂▂▂▂▂▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▅
 * //.......◥█☆█▅▄▃▁▁▁▁▁▃▄▅▅▅▅▅▅▅▅▅▅▅▅▅▅▄▁█▅●    ...Created by zn on 2017/8/11 0011.
 * //〓▇█████ ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓███▅▄▃███●
 * // 〓〓.๑۩۞۩๑.█████████████████████◤
 * //   ◥█████████◤ ◥        ◢◤
 * //     ◥██████◤        ◥  ◢◤
 * //       ████◤██████◤
 * //       █▓▓▓▓██◤            专栏
 * //      █▓▓▓██◆                  音频bean
 * //     █▓▓▓██◆
 * //    █▓▓▓██◆
 * //   █▓▓▓██◆
 * //  █▓▓▓██◆
 * // █▓▓ ██◆
 * //█▓▓ ██◆
 */
public class ColumnAudioBean implements Parcelable {

    /**
     * startTime : null
     * endTime : null
     * pageNum : null
     * pageSize : null
     * id : 23
     * columnId : 15
     * audioName : 第46篇｜怎样让演讲和表达
     * audioUrl :
     * articleId : null
     * createTime : 2017-08-18 18:32:52.0
     * audioLength : 223
     * audioAh : 5
     * free : 0
     * sort : null
     */

    private String startTime;
    private String endTime;
    private String pageNum;
    private String pageSize;
    private String id;
    private String columnId;
    private String audioName;
    private String audioUrl;
    private String articleId;
    private String createTime;
    private String audioLength;
    private String audioAh;
    private String free;
    private String sort;
    private String audioImg;
    private int downloadState;
    private String filePath;
    private boolean checked;
    private boolean playing;
    private String authorname;
    private String columnName;

    protected ColumnAudioBean(Parcel in) {
        startTime = in.readString();
        endTime = in.readString();
        pageNum = in.readString();
        pageSize = in.readString();
        id = in.readString();
        columnId = in.readString();
        audioName = in.readString();
        audioUrl = in.readString();
        articleId = in.readString();
        createTime = in.readString();
        audioLength = in.readString();
        audioAh = in.readString();
        free = in.readString();
        sort = in.readString();
        audioImg = in.readString();
        downloadState = in.readInt();
        filePath = in.readString();
        checked = in.readByte() != 0;
        playing = in.readByte() != 0;
        authorname = in.readString();
        columnName = in.readString();
        haveYouPurchased = in.readString();
        alreadyWatch = in.readInt();
        fileSize = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(startTime);
        dest.writeString(endTime);
        dest.writeString(pageNum);
        dest.writeString(pageSize);
        dest.writeString(id);
        dest.writeString(columnId);
        dest.writeString(audioName);
        dest.writeString(audioUrl);
        dest.writeString(articleId);
        dest.writeString(createTime);
        dest.writeString(audioLength);
        dest.writeString(audioAh);
        dest.writeString(free);
        dest.writeString(sort);
        dest.writeString(audioImg);
        dest.writeInt(downloadState);
        dest.writeString(filePath);
        dest.writeByte((byte) (checked ? 1 : 0));
        dest.writeByte((byte) (playing ? 1 : 0));
        dest.writeString(authorname);
        dest.writeString(columnName);
        dest.writeString(haveYouPurchased);
        dest.writeInt(alreadyWatch);
        dest.writeString(fileSize);
    }
        public ColumnAudioBean() {

    }
    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ColumnAudioBean> CREATOR = new Creator<ColumnAudioBean>() {
        @Override
        public ColumnAudioBean createFromParcel(Parcel in) {
            return new ColumnAudioBean(in);
        }

        @Override
        public ColumnAudioBean[] newArray(int size) {
            return new ColumnAudioBean[size];
        }
    };

    public String getStartTime() {
        return startTime;
    }

    @Override
    public String toString() {
        return "ColumnAudioBean{" +
                "startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", pageNum='" + pageNum + '\'' +
                ", pageSize='" + pageSize + '\'' +
                ", id='" + id + '\'' +
                ", columnId='" + columnId + '\'' +
                ", audioName='" + audioName + '\'' +
                ", audioUrl='" + audioUrl + '\'' +
                ", articleId='" + articleId + '\'' +
                ", createTime='" + createTime + '\'' +
                ", audioLength='" + audioLength + '\'' +
                ", audioAh='" + audioAh + '\'' +
                ", free='" + free + '\'' +
                ", sort='" + sort + '\'' +
                ", audioImg='" + audioImg + '\'' +
                ", downloadState=" + downloadState +
                ", filePath='" + filePath + '\'' +
                ", checked=" + checked +
                ", playing=" + playing +
                ", authorname='" + authorname + '\'' +
                ", columnName='" + columnName + '\'' +
                ", haveYouPurchased='" + haveYouPurchased + '\'' +
                ", alreadyWatch=" + alreadyWatch +
                ", fileSize='" + fileSize + '\'' +
                '}';
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getPageNum() {
        return pageNum;
    }

    public void setPageNum(String pageNum) {
        this.pageNum = pageNum;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getColumnId() {
        return columnId;
    }

    public void setColumnId(String columnId) {
        this.columnId = columnId;
    }

    public String getAudioName() {
        return audioName;
    }

    public void setAudioName(String audioName) {
        this.audioName = audioName;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getAudioLength() {
        return audioLength;
    }

    public void setAudioLength(String audioLength) {
        this.audioLength = audioLength;
    }

    public String getAudioAh() {
        return audioAh;
    }

    public void setAudioAh(String audioAh) {
        this.audioAh = audioAh;
    }

    public String getFree() {
        return free;
    }

    public void setFree(String free) {
        this.free = free;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getAudioImg() {
        return audioImg;
    }

    public void setAudioImg(String audioImg) {
        this.audioImg = audioImg;
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

    public String getAuthorname() {
        return authorname;
    }

    public void setAuthorname(String authorname) {
        this.authorname = authorname;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getHaveYouPurchased() {
        return haveYouPurchased;
    }

    public void setHaveYouPurchased(String haveYouPurchased) {
        this.haveYouPurchased = haveYouPurchased;
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

    private String haveYouPurchased;
    /**
     * 是否观看，已下载的时候会用到
     */
    private int alreadyWatch;
    /**
     * 文件大小，已下载的时候会用到
     */
    private String fileSize;
}
