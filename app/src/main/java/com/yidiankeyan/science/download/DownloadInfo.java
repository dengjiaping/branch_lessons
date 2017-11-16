package com.yidiankeyan.science.download;


import com.yidiankeyan.science.information.adapter.AudioAlbumAdapter;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Author: wyouflf
 * Date: 13-11-10
 * Time: 下午8:11
 */
@Table(name = "download", onCreated = "CREATE UNIQUE INDEX index_name ON download(label,fileSavePath)")
public class DownloadInfo {

    public DownloadInfo() {
    }


    @Column(name = "id", isId = true)
    private long id;

    @Column(name = "state")
    private DownloadState state = DownloadState.STOPPED;

    @Column(name = "url")
    private String url;

    @Column(name = "label")
    private String label;

    @Column(name = "fileSavePath")
    private String fileSavePath;

    @Column(name = "progress")
    private int progress;

    @Column(name = "fileLength")
    private long fileLength;

    @Column(name = "autoResume")
    private boolean autoResume;

    @Column(name = "autoRename")
    private boolean autoRename;

    @Column(name = "contentId")
    private String contentId;

    @Column(name = "fileType")
    private int fileType;

    @Column(name = "contentNum")
    private int contentNum;


    public int getContentNum() {
        return contentNum;
    }

    public void setContentNum(int contentNum) {
        this.contentNum = contentNum;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public DownloadState getState() {
        return state;
    }

    public void setState(DownloadState state) {
        if (state == DownloadState.FINISHED&&holder!=null) {
            holder.onSuccess();
        }
        this.state = state;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getFileSavePath() {
        return fileSavePath;
    }

    public void setFileSavePath(String fileSavePath) {
        this.fileSavePath = fileSavePath;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        if (holder != null) {
            holder.setProgress(progress);
        }
        this.progress = progress;
    }

    private AudioAlbumAdapter.ViewHolder holder;

    public void setHolder(AudioAlbumAdapter.ViewHolder holder) {
        this.holder = holder;
    }

    public long getFileLength() {
        return fileLength;
    }

    public void setFileLength(long fileLength) {
        this.fileLength = fileLength;
    }

    public boolean isAutoResume() {
        return autoResume;
    }

    public void setAutoResume(boolean autoResume) {
        this.autoResume = autoResume;
    }

    public boolean isAutoRename() {
        return autoRename;
    }

    public void setAutoRename(boolean autoRename) {
        this.autoRename = autoRename;
    }

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DownloadInfo)) return false;

        DownloadInfo that = (DownloadInfo) o;

        if (id != that.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
