package com.yidiankeyan.science.information.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * //  ◥█▄▃▁   ▁▂▂▃▃▂▂▂▂▂▂▂▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▅
 * //.......◥█☆█▅▄▃▁▁▁▁▁▃▄▅▅▅▅▅▅▅▅▅▅▅▅▅▅▄▁█▅●    ...Created by zn on 2017/5/25 0025.
 * //〓▇█████ ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓███▅▄▃███●
 * // 〓〓.๑۩۞۩๑.█████████████████████◤
 * //   ◥█████████◤ ◥        ◢◤
 * //     ◥██████◤        ◥  ◢◤
 * //       ████◤██████◤
 * //       █▓▓▓▓██◤
 * //      █▓▓▓██◆
 * //     █▓▓▓██◆           墨子专访
 * //    █▓▓▓██◆
 * //   █▓▓▓██◆                 -即将上线
 * //  █▓▓▓██◆
 * // █▓▓ ██◆
 * //█▓▓ ██◆
 */
public class InterviewSoonBean implements Parcelable {

    public static final int CONTENT = 0;
    public static final int SECTION = 1;

    public int type = CONTENT;

    /**
     * id : 1
     * interviewIntro : 11111111111111111111
     * interviewName : 111111111111
     * interviewImgUrl : 1111111111111111111
     * interviewVideoUrl : 11111111111111111
     * videoUrlLength : 1344
     * likeNum : 1
     * browseNum : 0
     * onlineStatus : 1
     * top : 1
     * interviewType : 1
     * createTime : 2017-05-24 11:15:28.0
     * isUse : null
     * startTime : null
     * endTime : null
     * pageNum : null
     * pageSize : null
     */

    private String id;
    private String interviewIntro;
    private String interviewName;
    private String interviewImgUrl;
    private String interviewVideoUrl;
    private String videoUrlLength;
    private String likeNum;
    private String browseNum;
    private String onlineStatus;
    private String top;
    private String interviewType;
    private String createTime;
    private String isUse;
    private String startTime;
    private String endTime;
    private String pageNum;
    private String pageSize;
    private String isCollect;
    private String isRemind;
    private String commentnum;

    public String getCommentnum() {
        return commentnum;
    }

    public void setCommentnum(String commentnum) {
        this.commentnum = commentnum;
    }

    public InterviewSoonBean(int type, String interviewName) {
        this.type = type;
        this.interviewName = interviewName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInterviewIntro() {
        return interviewIntro;
    }

    public void setInterviewIntro(String interviewIntro) {
        this.interviewIntro = interviewIntro;
    }

    public String getInterviewName() {
        return interviewName;
    }

    public void setInterviewName(String interviewName) {
        this.interviewName = interviewName;
    }

    public String getInterviewImgUrl() {
        return interviewImgUrl;
    }

    public void setInterviewImgUrl(String interviewImgUrl) {
        this.interviewImgUrl = interviewImgUrl;
    }

    public String getInterviewVideoUrl() {
        return interviewVideoUrl;
    }

    public void setInterviewVideoUrl(String interviewVideoUrl) {
        this.interviewVideoUrl = interviewVideoUrl;
    }

    public String getVideoUrlLength() {
        return videoUrlLength;
    }

    public void setVideoUrlLength(String videoUrlLength) {
        this.videoUrlLength = videoUrlLength;
    }

    public String getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(String likeNum) {
        this.likeNum = likeNum;
    }

    public String getBrowseNum() {
        return browseNum;
    }

    public void setBrowseNum(String browseNum) {
        this.browseNum = browseNum;
    }

    public String getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(String onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public String getTop() {
        return top;
    }

    public void setTop(String top) {
        this.top = top;
    }

    public String getInterviewType() {
        return interviewType;
    }

    public void setInterviewType(String interviewType) {
        this.interviewType = interviewType;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getIsUse() {
        return isUse;
    }

    public void setIsUse(String isUse) {
        this.isUse = isUse;
    }

    public String getStartTime() {
        return startTime;
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

    public String getIsCollect() {
        return isCollect;
    }

    public void setIsCollect(String isCollect) {
        this.isCollect = isCollect;
    }

    public String getIsRemind() {
        return isRemind;
    }

    public void setIsRemind(String isRemind) {
        this.isRemind = isRemind;
    }

    @Override
    public String toString() {
        return "InterviewSoonBean{" +
                "type=" + type +
                ", id='" + id + '\'' +
                ", interviewIntro='" + interviewIntro + '\'' +
                ", interviewName='" + interviewName + '\'' +
                ", interviewImgUrl='" + interviewImgUrl + '\'' +
                ", interviewVideoUrl='" + interviewVideoUrl + '\'' +
                ", videoUrlLength='" + videoUrlLength + '\'' +
                ", likeNum='" + likeNum + '\'' +
                ", browseNum='" + browseNum + '\'' +
                ", onlineStatus='" + onlineStatus + '\'' +
                ", top='" + top + '\'' +
                ", interviewType='" + interviewType + '\'' +
                ", createTime='" + createTime + '\'' +
                ", isUse='" + isUse + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", pageNum='" + pageNum + '\'' +
                ", pageSize='" + pageSize + '\'' +
                ", isCollect='" + isCollect + '\'' +
                ", isRemind='" + isRemind + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.interviewIntro);
        dest.writeString(this.interviewName);
        dest.writeString(this.interviewImgUrl);
        dest.writeString(this.interviewVideoUrl);
        dest.writeString(this.videoUrlLength);
        dest.writeString(this.likeNum);
        dest.writeString(this.browseNum);
        dest.writeString(this.onlineStatus);
        dest.writeString(this.top);
        dest.writeString(this.interviewType);
        dest.writeString(this.createTime);
        dest.writeString(this.isUse);
        dest.writeString(this.startTime);
        dest.writeString(this.endTime);
        dest.writeString(this.pageNum);
        dest.writeString(this.pageSize);
        dest.writeString(this.isCollect);
        dest.writeString(this.isRemind);
    }

    public InterviewSoonBean() {
    }

    protected InterviewSoonBean(Parcel in) {
        this.id = in.readString();
        this.interviewIntro = in.readString();
        this.interviewName = in.readString();
        this.interviewImgUrl = in.readString();
        this.interviewVideoUrl = in.readString();
        this.videoUrlLength = in.readString();
        this.likeNum = in.readString();
        this.browseNum = in.readString();
        this.onlineStatus = in.readString();
        this.top = in.readString();
        this.interviewType = in.readString();
        this.createTime = in.readString();
        this.isUse = in.readString();
        this.startTime = in.readString();
        this.endTime = in.readString();
        this.pageNum = in.readString();
        this.pageSize = in.readString();
        this.isCollect = in.readString();
        this.isRemind = in.readString();
    }

    public static final Creator<InterviewSoonBean> CREATOR = new Creator<InterviewSoonBean>() {
        @Override
        public InterviewSoonBean createFromParcel(Parcel source) {
            return new InterviewSoonBean(source);
        }

        @Override
        public InterviewSoonBean[] newArray(int size) {
            return new InterviewSoonBean[size];
        }
    };
}
