package com.yidiankeyan.science.information.entity;

/**
 * ////////////////////////////////////////////////////////////////////
 * //                          _ooOoo_                               //
 * //                         o8888888o                              //
 * //                         88" . "88                              //
 * //                         (| ^_^ |)                              //
 * //                         O\  =  /O                              //
 * //                      ____/`---'\____                           //
 * //                    .'  \\|     |//  `.                         //
 * //                   /  \\|||  :  |||//  \                        //
 * //                  /  _||||| -:- |||||-  \                       //
 * //                  |   | \\\  -  /// |   |                       //
 * //                  | \_|  ''\---/''  |   |                       //
 * //                  \  .-\__  `-`  ___/-. /                       //
 * //                ___`. .'  /--.--\  `. . ___                     //
 * //              ."" '<  `.___\_<|>_/___.'  >'"".                  //
 * //            | | :  `- \`.;`\ _ /`;.`/ - ` : | |                 //
 * //            \  \ `-.   \_ __\ /__ _/   .-` /  /                 //
 * //      ========`-.____`-.___\_____/___.-`____.-'========         //
 * //                           `=---='                              //
 * //      ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^        //
 * //             佛祖保佑      永无BUG     永不修改                  //
 * ////////////////////////////////////////////////////////////////////
 * Created by nby on 2016/12/7.
 * 作用：期刊详情
 */

public class IssuesDetailBean {

    /**
     * id : 1acce6c894d648dfa5810b785604c2c4
     * vol : 2
     * name : 测试
     * coverimg : coverimg
     * title : title
     * freeable : 1
     * audiourl : audiourl
     * audiolenth : 60
     * createtime : 1480488209000
     * columnid : 22
     * isuse : 1
     * checkstatus : 2
     * content : content
     * price : 0
     * userid : 9f1c6ba76f3d4dc583169e94e4687ea3
     * username : 112
     * readnum : 0
     * userimgurl :
     */

    private String id;
    private int vol;
    private String name;
    private String coverimg;
    private String title;
    private int freeable;
    private String audiourl;
    private int audiolenth;
    private long createtime;
    private String columnid;
    private int isuse;
    private int checkstatus;
    private String content;
    private double price;
    private String userid;
    private String username;
    private int readnum;
    private String userimgurl;
    private int permission;
    private int downloadState;
    private String filePath;
    /**
     * 是否观看，已下载的时候会用到
     */
    private int alreadyWatch;
    /**
     * 文件大小，已下载的时候会用到
     */
    private String fileSize;
    /**
     * columnname : 隔壁老王的专栏
     */

    private String columnname;
    private boolean needDelete;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
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

    public int getDownloadState() {
        return downloadState;
    }

    public void setDownloadState(int downloadState) {
        this.downloadState = downloadState;
    }

    public int getPermission() {
        return permission;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getVol() {
        return vol;
    }

    public void setVol(int vol) {
        this.vol = vol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCoverimg() {
        return coverimg;
    }

    public void setCoverimg(String coverimg) {
        this.coverimg = coverimg;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getFreeable() {
        return freeable;
    }

    public void setFreeable(int freeable) {
        this.freeable = freeable;
    }

    public String getAudiourl() {
        return audiourl;
    }

    public void setAudiourl(String audiourl) {
        this.audiourl = audiourl;
    }

    public int getAudiolenth() {
        return audiolenth;
    }

    public void setAudiolenth(int audiolenth) {
        this.audiolenth = audiolenth;
    }

    public long getCreatetime() {
        return createtime;
    }

    public void setCreatetime(long createtime) {
        this.createtime = createtime;
    }

    public String getColumnid() {
        return columnid;
    }

    public void setColumnid(String columnid) {
        this.columnid = columnid;
    }

    public int getIsuse() {
        return isuse;
    }

    public void setIsuse(int isuse) {
        this.isuse = isuse;
    }

    public int getCheckstatus() {
        return checkstatus;
    }

    public void setCheckstatus(int checkstatus) {
        this.checkstatus = checkstatus;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getReadnum() {
        return readnum;
    }

    public void setReadnum(int readnum) {
        this.readnum = readnum;
    }

    public String getUserimgurl() {
        return userimgurl;
    }

    public void setUserimgurl(String userimgurl) {
        this.userimgurl = userimgurl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IssuesDetailBean that = (IssuesDetailBean) o;

        return id != null ? id.equals(that.id) : that.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    public IssuesDetailBean() {
    }

    public String getColumnname() {
        return columnname;
    }

    public void setColumnname(String columnname) {
        this.columnname = columnname;
    }

    public boolean isNeedDelete() {
        return needDelete;
    }

    public void setNeedDelete(boolean needDelete) {
        this.needDelete = needDelete;
    }
}
