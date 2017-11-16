package com.yidiankeyan.science.information.entity;

/**
 * //  ◥█▄▃▁   ▁▂▂▃▃▂▂▂▂▂▂▂▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▅
 * //.......◥█☆█▅▄▃▁▁▁▁▁▃▄▅▅▅▅▅▅▅▅▅▅▅▅▅▅▄▁█▅●    ...Created by zn on 2016/11/30 0030.
 * //〓▇█████ ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓███▅▄▃███●
 * // 〓〓.๑۩۞۩๑.█████████████████████◤
 * //   ◥█████████◤ ◥        ◢◤
 * //     ◥██████◤        ◥  ◢◤
 * //       ████◤██████◤                墨子读书
 * //       █▓▓▓▓██◤                        -详情
 * //      █▓▓▓██◆
 * //     █▓▓▓██◆
 * //    █▓▓▓██◆
 * //   █▓▓▓██◆
 * //  █▓▓▓██◆
 * // █▓▓ ██◆
 * //█▓▓ ██◆
 */
public class MozReadDetailsBean {

    /**
     * id : 003cd380f89440de8aa6a45a722e7cc2
     * name : 大学
     * price : 299.99
     * desc : 八股文，中国文萃
     * coverimgurl : /static/upload/cmsweb/image/jpeg2016/11/2/7d1b8d7f7ea04008a37637b98cc02ff2.jpg
     * author : 花花
     * casterid : 9d1461830f2d4a1092ab69291a5aa738
     * subjectid : 0000000010
     * mediaurl : /static/upload/cmsweb/audio/mp32016/9/25/33e76ae4fb9e4252a072138fdb9b17fe.mp3
     * readnum : 0
     * praisenum : 0
     * createtime : 1480300776000
     * lastupdatetime : 1480386262000
     * checkstatus : 2
     * castername : 大帅
     * subjectname : 地球科学
     * casterimgurl : /app/92400809/jpg/2016/11/17/d056cc0da5bb4fd0b9bf5ceb8c9a44b0.jpg
     */

    private String id;
    private String name;
    private double price;
    private String desc;
    private String coverimgurl;
    private String author;
    private String casterid;
    private String subjectid;
    private String mediaurl;
    private int readnum;
    private int praisenum;
    private long createtime;
    private long lastupdatetime;
    private int checkstatus;
    private String castername;
    private String subjectname;
    private String casterimgurl;
    private String filePath;
    private int alreadyWatch;
    private int downloadState;
    private String fileSize;
    private boolean needDelete;
    private String poster;
    private int medialength;

    public int getIsActPriceShow() {
        return isActPriceShow;
    }

    public void setIsActPriceShow(int isActPriceShow) {
        this.isActPriceShow = isActPriceShow;
    }

    private int isActPriceShow;  //1显示  0不显示
  public int getMedialength() {
        return medialength;
    }

    public void setMedialength(int medialength) {
        this.medialength = medialength;
    }

    /**
     * price : 10
     * discount : 0.2
     * originalprice : 50
     * activityprice : 0
     * actbegintime : 1489031878000
     * actendtime : 1490414284000
     * subjectid : 10
     * casterimgurl : null
     */

    private double discount;
    private double originalprice;
    private double activityprice;
    private long actbegintime;
    private long actendtime;


    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCoverimgurl() {
        return coverimgurl;
    }

    public void setCoverimgurl(String coverimgurl) {
        this.coverimgurl = coverimgurl;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCasterid() {
        return casterid;
    }

    public void setCasterid(String casterid) {
        this.casterid = casterid;
    }

    public String getSubjectid() {
        return subjectid;
    }

    public void setSubjectid(String subjectid) {
        this.subjectid = subjectid;
    }

    public String getMediaurl() {
        return mediaurl;
    }

    public void setMediaurl(String mediaurl) {
        this.mediaurl = mediaurl;
    }

    public int getReadnum() {
        return readnum;
    }

    public void setReadnum(int readnum) {
        this.readnum = readnum;
    }

    public int getPraisenum() {
        return praisenum;
    }

    public void setPraisenum(int praisenum) {
        this.praisenum = praisenum;
    }

    public long getCreatetime() {
        return createtime;
    }

    public void setCreatetime(long createtime) {
        this.createtime = createtime;
    }

    public long getLastupdatetime() {
        return lastupdatetime;
    }

    public void setLastupdatetime(long lastupdatetime) {
        this.lastupdatetime = lastupdatetime;
    }

    public int getCheckstatus() {
        return checkstatus;
    }

    public void setCheckstatus(int checkstatus) {
        this.checkstatus = checkstatus;
    }

    public String getCastername() {
        return castername;
    }

    public void setCastername(String castername) {
        this.castername = castername;
    }

    public String getSubjectname() {
        return subjectname;
    }

    public void setSubjectname(String subjectname) {
        this.subjectname = subjectname;
    }

    public String getCasterimgurl() {
        return casterimgurl;
    }

    public void setCasterimgurl(String casterimgurl) {
        this.casterimgurl = casterimgurl;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setAlreadyWatch(int alreadyWatch) {
        this.alreadyWatch = alreadyWatch;
    }

    public int getAlreadyWatch() {
        return alreadyWatch;
    }

    public void setDownloadState(int downloadState) {
        this.downloadState = downloadState;
    }

    public int getDownloadState() {
        return downloadState;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileSize() {
        return fileSize;
    }

    public boolean isNeedDelete() {
        return needDelete;
    }

    public void setNeedDelete(boolean needDelete) {
        this.needDelete = needDelete;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getOriginalprice() {
        return originalprice;
    }

    public void setOriginalprice(double originalprice) {
        this.originalprice = originalprice;
    }

    public double getActivityprice() {
        return activityprice;
    }

    @Override
    public String toString() {
        return "MozReadDetailsBean{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", desc='" + desc + '\'' +
                ", coverimgurl='" + coverimgurl + '\'' +
                ", author='" + author + '\'' +
                ", casterid='" + casterid + '\'' +
                ", subjectid='" + subjectid + '\'' +
                ", mediaurl='" + mediaurl + '\'' +
                ", readnum=" + readnum +
                ", praisenum=" + praisenum +
                ", createtime=" + createtime +
                ", lastupdatetime=" + lastupdatetime +
                ", checkstatus=" + checkstatus +
                ", castername='" + castername + '\'' +
                ", subjectname='" + subjectname + '\'' +
                ", casterimgurl='" + casterimgurl + '\'' +
                ", filePath='" + filePath + '\'' +
                ", alreadyWatch=" + alreadyWatch +
                ", downloadState=" + downloadState +
                ", fileSize='" + fileSize + '\'' +
                ", needDelete=" + needDelete +
                ", poster='" + poster + '\'' +
                ", discount=" + discount +
                ", originalprice=" + originalprice +
                ", activityprice=" + activityprice +
                ", actbegintime=" + actbegintime +
                ", actendtime=" + actendtime +
                '}';
    }

    public void setActivityprice(double activityprice) {
        this.activityprice = activityprice;
    }

    public long getActbegintime() {
        return actbegintime;
    }

    public void setActbegintime(long actbegintime) {
        this.actbegintime = actbegintime;
    }

    public long getActendtime() {
        return actendtime;
    }

    public void setActendtime(long actendtime) {
        this.actendtime = actendtime;
    }
}
