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
 * Created by nby on 2016/11/17.
 * 作用：
 */
public class TeamDetailBean {

    /**
     * id : 06c2d634650348cb84cc9ebf63f2bdf6
     * name : 北京队
     * teamnumber : 一号队
     * vediourl : /static/upload/cmsweb/audio/mpeg2016/10/31/757b60e31e3a47f9a94821cca94c78e1.mp3
     * coverimgurl : /static/upload/cmsweb/image/jpeg2016/11/3/ed9c29dc66c64c21989158db21bb943f.jpg
     * description : 北京队的描述
     * content : 北京北京北京北京北京
     * sort : 1
     * score : 666
     * createtime : 1479428104000
     * creatorid : 6504704244b14e7393f8bbdb7b2982dd
     * checkstatus : 2
     * isavailable : 1
     */

    private String id;
    private String name;
    private String teamnumber;
    private String vediourl;
    private String coverimgurl;
    private String description;
    private String content;
    private int sort;
    private int score;
    private long createtime;
    private String creatorid;
    private int checkstatus;
    private int isavailable;

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

    public String getTeamnumber() {
        return teamnumber;
    }

    public void setTeamnumber(String teamnumber) {
        this.teamnumber = teamnumber;
    }

    public String getVediourl() {
        return vediourl;
    }

    public void setVediourl(String vediourl) {
        this.vediourl = vediourl;
    }

    public String getCoverimgurl() {
        return coverimgurl;
    }

    public void setCoverimgurl(String coverimgurl) {
        this.coverimgurl = coverimgurl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public long getCreatetime() {
        return createtime;
    }

    public void setCreatetime(long createtime) {
        this.createtime = createtime;
    }

    public String getCreatorid() {
        return creatorid;
    }

    public void setCreatorid(String creatorid) {
        this.creatorid = creatorid;
    }

    public int getCheckstatus() {
        return checkstatus;
    }

    public void setCheckstatus(int checkstatus) {
        this.checkstatus = checkstatus;
    }

    public int getIsavailable() {
        return isavailable;
    }

    public void setIsavailable(int isavailable) {
        this.isavailable = isavailable;
    }
}
