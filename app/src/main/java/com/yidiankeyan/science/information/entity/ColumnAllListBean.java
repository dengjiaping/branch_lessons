package com.yidiankeyan.science.information.entity;

/**
 * //  ◥█▄▃▁   ▁▂▂▃▃▂▂▂▂▂▂▂▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▅
 * //.......◥█☆█▅▄▃▁▁▁▁▁▃▄▅▅▅▅▅▅▅▅▅▅▅▅▅▅▄▁█▅●    ...Created by zn on 2017/8/18 0018.
 * //〓▇█████ ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓███▅▄▃███●
 * // 〓〓.๑۩۞۩๑.█████████████████████◤
 * //   ◥█████████◤ ◥        ◢◤
 * //     ◥██████◤        ◥  ◢◤
 * //       ████◤██████◤
 * //       █▓▓▓▓██◤            专栏
 * //      █▓▓▓██◆                  -全部专栏bean
 * //     █▓▓▓██◆
 * //    █▓▓▓██◆
 * //   █▓▓▓██◆
 * //  █▓▓▓██◆
 * // █▓▓ ██◆
 * //█▓▓ ██◆
 */
public class ColumnAllListBean {

    /**
     * id :
     * columnName :
     * columnTitle :
     * columnPrice :
     * createTime :
     * columnWriter :
     * columnWriterIntro :
     * columnPicture :
     * release :
     * type :
     */

    private String id;
    private String columnName;
    private String columnTitle;
    private String columnPrice;
    private String createTime;
    private String columnWriter;
    private String columnWriterIntro;
    private String columnPicture;
    private String rankUpdate;

    public String getColumnActivityPrice() {
        return columnActivityPrice;
    }

    public String getRankUpdate() {
        return rankUpdate;
    }

    public void setRankUpdate(String rankUpdate) {
        this.rankUpdate = rankUpdate;
    }

    public void setColumnActivityPrice(String columnActivityPrice) {
        this.columnActivityPrice = columnActivityPrice;
    }

    public int getIshasactivityprice() {
        return ishasactivityprice;
    }

    public void setIshasactivityprice(int ishasactivityprice) {
        this.ishasactivityprice = ishasactivityprice;
    }

    private String release;
    private String type;
    private String columnActivityPrice;
    private int ishasactivityprice;   //1：有活动价  0：无活动价

    public String getHaveYouPurchased() {
        return haveYouPurchased;
    }

    public void setHaveYouPurchased(String haveYouPurchased) {
        this.haveYouPurchased = haveYouPurchased;
    }

    private String haveYouPurchased;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnTitle() {
        return columnTitle;
    }

    public void setColumnTitle(String columnTitle) {
        this.columnTitle = columnTitle;
    }

    public String getColumnPrice() {
        return columnPrice;
    }

    public void setColumnPrice(String columnPrice) {
        this.columnPrice = columnPrice;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getColumnWriter() {
        return columnWriter;
    }

    public void setColumnWriter(String columnWriter) {
        this.columnWriter = columnWriter;
    }

    public String getColumnWriterIntro() {
        return columnWriterIntro;
    }

    public void setColumnWriterIntro(String columnWriterIntro) {
        this.columnWriterIntro = columnWriterIntro;
    }

    public String getColumnPicture() {
        return columnPicture;
    }

    public void setColumnPicture(String columnPicture) {
        this.columnPicture = columnPicture;
    }

    public String getRelease() {
        return release;
    }

    public void setRelease(String release) {
        this.release = release;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
