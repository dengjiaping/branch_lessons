package com.yidiankeyan.science.information.entity;

/**
 * Created by nby on 2016/7/31.
 * 视频专辑，视频实体类
 */
public class VideoBean {
    private String avatatUrl;
    private String videoUrl;
    private String title;
    private boolean isPlaying;

    public VideoBean(String avatatUrl, String videoUrl, String title) {
        this.avatatUrl = avatatUrl;
        this.videoUrl = videoUrl;
        this.title = title;
    }

    public VideoBean(String avatatUrl) {
        this.avatatUrl = avatatUrl;
    }

    public String getAvatatUrl() {
        return avatatUrl;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public void setAvatatUrl(String avatatUrl) {
        this.avatatUrl = avatatUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
