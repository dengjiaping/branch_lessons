package com.yidiankeyan.science.subscribe.entity;

/**
 * Created by nby on 2016/7/30.
 * 音频实体类
 */
public class AudioBean {
    private String name;
    private int audioRes;
    private boolean isPlaying;

    public AudioBean(String name, int audioRes) {
        this.name = name;
        this.audioRes = audioRes;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAudioRes() {
        return audioRes;
    }

    public void setAudioRes(int audioRes) {
        this.audioRes = audioRes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AudioBean audioBean = (AudioBean) o;

        if (audioRes != audioBean.audioRes) return false;
        return name != null ? name.equals(audioBean.name) : audioBean.name == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + audioRes;
        return result;
    }
}
