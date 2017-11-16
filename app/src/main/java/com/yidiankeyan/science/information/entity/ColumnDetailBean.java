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
 * Created by nby on 2016/12/1.
 * 作用：
 */

public class ColumnDetailBean {

    /**
     * id : 31266e03108b479888d240bebabc0eff
     * userid : 9f1c6ba76f3d4dc583169e94e4687ea3
     * coverimg : /static/upload/cmsweb/image/png2016/12/1/41bcd499feb048369b9f8a9d53d3ce77.png
     * name : 隔壁老王的专栏扩大版
     * price : 0
     * createtime : 1480556512000
     * summary : 隔壁老王的专栏扩大版
     * learnersinfo : 隔壁村寡妇
     * description : 隔壁老王的专栏扩大版隔壁老王的专栏扩大版
     * isuse : 1
     * checkstatus : 0
     * username : 56386925
     * lastupdatetime : 1480509527000
     * lastupdatetitle : 测试
     * purcharsings : 0
     * permission : 0
     * userImgUrl :
     */

    private String id;
    private String userid;
    private String coverimg;
    private String name;
    private double price;
    private long createtime;
    private String summary;
    private String learnersinfo;
    private String description;
    private int isuse;
    private int checkstatus;
    private String username;
    private long lastupdatetime;
    private String lastupdatetitle;
    private int purcharsings;
    private int permission;
    private String userImgUrl;
    /**
     * discount : 0.2
     */
    private String poster;

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    private double discount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getCoverimg() {
        return coverimg;
    }

    public void setCoverimg(String coverimg) {
        this.coverimg = coverimg;
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

    public long getCreatetime() {
        return createtime;
    }

    public void setCreatetime(long createtime) {
        this.createtime = createtime;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getLearnersinfo() {
        return learnersinfo;
    }

    public void setLearnersinfo(String learnersinfo) {
        this.learnersinfo = learnersinfo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getLastupdatetime() {
        return lastupdatetime;
    }

    public void setLastupdatetime(long lastupdatetime) {
        this.lastupdatetime = lastupdatetime;
    }

    public String getLastupdatetitle() {
        return lastupdatetitle;
    }

    public void setLastupdatetitle(String lastupdatetitle) {
        this.lastupdatetitle = lastupdatetitle;
    }

    public int getPurcharsings() {
        return purcharsings;
    }

    public void setPurcharsings(int purcharsings) {
        this.purcharsings = purcharsings;
    }

    public int getPermission() {
        return permission;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }

    public String getUserImgUrl() {
        return userImgUrl;
    }

    public void setUserImgUrl(String userImgUrl) {
        this.userImgUrl = userImgUrl;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }
}
