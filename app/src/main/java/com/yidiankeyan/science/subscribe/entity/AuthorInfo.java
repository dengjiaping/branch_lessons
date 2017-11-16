package com.yidiankeyan.science.subscribe.entity;

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
 * Created by nby on 2016/9/11.
 * 作用：
 */
public class AuthorInfo {

    /**
     * imgurl : /panjianwei.jpg
     * userid : 1
     * username : 潘建伟
     * albumnum : 140
     * follower_num : 1
     * tip_total : 19.2
     * isfocus : 1
     */

    private String imgurl;
    private String userid;
    private String username;
    private int albumnum;
    private int follower_num;
    private double tip_total;
    private int isfocus;

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
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

    public int getAlbumnum() {
        return albumnum;
    }

    public void setAlbumnum(int albumnum) {
        this.albumnum = albumnum;
    }

    public int getFollower_num() {
        return follower_num;
    }

    public void setFollower_num(int follower_num) {
        this.follower_num = follower_num;
    }

    public double getTip_total() {
        return tip_total;
    }

    public void setTip_total(double tip_total) {
        this.tip_total = tip_total;
    }

    public int getIsfocus() {
        return isfocus;
    }

    public void setIsfocus(int isfocus) {
        this.isfocus = isfocus;
    }
}
