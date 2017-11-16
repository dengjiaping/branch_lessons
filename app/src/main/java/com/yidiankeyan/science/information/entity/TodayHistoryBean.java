package com.yidiankeyan.science.information.entity;

/**
 * Created by nby on 2016/7/7.
 */
public class TodayHistoryBean {
    private String title;
    private String time;
    private String category;
    private String categoryTwo;
    private String readCount;
    private String date;
    private boolean showControl;
    private int playState;//0:不在选中状态，1:播放状态，2:选中状态
    private String duration;//视频，音频的播放时长
    private int voiceRes;
    private int groupPosition;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TodayHistoryBean that = (TodayHistoryBean) o;

        if (voiceRes != that.voiceRes) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (date != null ? !date.equals(that.date) : that.date != null) return false;
        return duration != null ? duration.equals(that.duration) : that.duration == null;

    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (duration != null ? duration.hashCode() : 0);
        result = 31 * result + voiceRes;
        return result;
    }

    public String getCategoryTwo() {
        return categoryTwo;
    }

    public void setCategoryTwo(String categoryTwo) {
        this.categoryTwo = categoryTwo;
    }

    public int getGroupPosition() {
        return groupPosition;
    }

    public void setGroupPosition(int groupPosition) {
        this.groupPosition = groupPosition;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getVoiceRes() {
        return voiceRes;
    }

    public void setVoiceRes(int voiceRes) {
        this.voiceRes = voiceRes;
    }

    public int getPlayState() {
        return playState;
    }

    public void setPlayState(int playState) {
        this.playState = playState;
    }

    public boolean isShowControl() {
        return showControl;
    }

    public void setShowControl(boolean showControl) {
        this.showControl = showControl;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getReadCount() {
        return readCount;
    }

    public void setReadCount(String readCount) {
        this.readCount = readCount;
    }

}
