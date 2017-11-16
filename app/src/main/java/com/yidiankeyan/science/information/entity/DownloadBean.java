package com.yidiankeyan.science.information.entity;

/**
 * Created by nby on 2016/7/22.
 */
public class DownloadBean {
    private String title;
    private double fileSize;

    public DownloadBean(String title, double fileSize) {
        this.title = title;
        this.fileSize = fileSize;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getFileSize() {
        return fileSize;
    }

    public void setFileSize(double fileSize) {
        this.fileSize = fileSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DownloadBean that = (DownloadBean) o;

        if (Double.compare(that.fileSize, fileSize) != 0) return false;
        return title != null ? title.equals(that.title) : that.title == null;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = title != null ? title.hashCode() : 0;
        temp = Double.doubleToLongBits(fileSize);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
