package com.yidiankeyan.science.collection.entity;

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
 * Created by nby on 2016/10/20.
 * 作用：
 */
public class CollectArticle {

    /**
     * collectid : cf34bce64a06445994428c680ab11519
     * articleid : d3b134fe6d23406596cb60273bf853df
     * coverimgurl : /static/upload/cmsweb/image/jpeg2016/9/1/bf009dde83314d888ac9e0289f98187e.jpg
     * name : 从内蕴证明到陈示性类，他成为了现代几何学的新掌门人
     * albumname : 超级数学建模
     * authorname : 测试
     * readnum : 5
     */

    private String collectid;
    private String articleid;
    private String coverimgurl;
    private String name;
    private String albumname;
    private String authorname;
    private int readnum;

    private boolean isNeedDelete;
    /**
     * articletype : 3
     */

    private int articletype;
    /**
     * albumid : 测试视频压缩
     * praisenum : 920
     */

    private String albumid;
    private int praisenum;

    public boolean isNeedDelete() {
        return isNeedDelete;
    }

    public void setNeedDelete(boolean needDelete) {
        isNeedDelete = needDelete;
    }

    public String getCollectid() {
        return collectid;
    }

    public void setCollectid(String collectid) {
        this.collectid = collectid;
    }

    public String getArticleid() {
        return articleid;
    }

    public void setArticleid(String articleid) {
        this.articleid = articleid;
    }

    public String getCoverimgurl() {
        return coverimgurl;
    }

    public void setCoverimgurl(String coverimgurl) {
        this.coverimgurl = coverimgurl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlbumname() {
        return albumname;
    }

    public void setAlbumname(String albumname) {
        this.albumname = albumname;
    }

    public String getAuthorname() {
        return authorname;
    }

    public void setAuthorname(String authorname) {
        this.authorname = authorname;
    }

    public int getReadnum() {
        return readnum;
    }

    public void setReadnum(int readnum) {
        this.readnum = readnum;
    }

    public int getArticletype() {
        return articletype;
    }

    public void setArticletype(int articletype) {
        this.articletype = articletype;
    }

    public String getAlbumid() {
        return albumid;
    }

    public void setAlbumid(String albumid) {
        this.albumid = albumid;
    }

    public int getPraisenum() {
        return praisenum;
    }

    public void setPraisenum(int praisenum) {
        this.praisenum = praisenum;
    }
}
