package com.yidiankeyan.science.my.entity;

import java.util.List;

/**
 * Created zn nby on 2016/9/24.
 */
public class EditorTypeBean {
    private int albumType;
    private String title;
    private List<EditorAlbum> mDatas;

    public EditorTypeBean(int albumType, String title) {
        this.albumType = albumType;
        this.title = title;
    }

    public EditorTypeBean(int albumType, String title, List<EditorAlbum> mDatas) {
        this.albumType = albumType;
        this.title = title;
        this.mDatas = mDatas;
    }

    public List<EditorAlbum> getmDatas() {
        return mDatas;
    }

    public void setmDatas(List<EditorAlbum> mDatas) {
        this.mDatas = mDatas;
    }

    public int getAlbumType() {
        return albumType;
    }

    public void setAlbumType(int albumType) {
        this.albumType = albumType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
