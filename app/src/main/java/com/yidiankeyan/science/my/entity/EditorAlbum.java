package com.yidiankeyan.science.my.entity;

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
 * Created by nby on 2016/9/24.
 * 作用：
 */
public class EditorAlbum {

    /**
     * albumid : b834b27747654283b23bf9934973c928
     * subjectname : 虚拟现实
     * albumname : VR技术前沿
     * albumtype : 1
     * recenttitle : 南昌积极抢占VR产业风口 多项政策扶持VR产业发展
     * albumauthor : 赵帅
     * authorid : 9d1461830f2d4a1092ab69291a5aa738
     * coverimgurl : /static/upload/cmsweb/image/png2016/8/16/7d85333b03db4359bb94d6f15b1156a2.png
     * recentupdatetime : 1471334227000
     * contentnum : 1
     * readnum : 3526
     */

    private String albumid;
    private String subjectname;
    private String albumname;
    private int albumtype;
    private String recenttitle;
    private String albumauthor;
    private String authorid;
    private String coverimgurl;
    private long recentupdatetime;
    private int contentnum;
    private int readnum;
    private int checkstatus;

    public int getCheckstatus() {
        return checkstatus;
    }

    public void setCheckstatus(int checkstatus) {
        this.checkstatus = checkstatus;
    }

    public String getAlbumid() {
        return albumid;
    }

    public void setAlbumid(String albumid) {
        this.albumid = albumid;
    }

    public String getSubjectname() {
        return subjectname;
    }

    public void setSubjectname(String subjectname) {
        this.subjectname = subjectname;
    }

    public String getAlbumname() {
        return albumname;
    }

    public void setAlbumname(String albumname) {
        this.albumname = albumname;
    }

    public int getAlbumtype() {
        return albumtype;
    }

    public void setAlbumtype(int albumtype) {
        this.albumtype = albumtype;
    }

    public String getRecenttitle() {
        return recenttitle;
    }

    public void setRecenttitle(String recenttitle) {
        this.recenttitle = recenttitle;
    }

    public String getAlbumauthor() {
        return albumauthor;
    }

    public void setAlbumauthor(String albumauthor) {
        this.albumauthor = albumauthor;
    }

    public String getAuthorid() {
        return authorid;
    }

    public void setAuthorid(String authorid) {
        this.authorid = authorid;
    }

    public String getCoverimgurl() {
        return coverimgurl;
    }

    public void setCoverimgurl(String coverimgurl) {
        this.coverimgurl = coverimgurl;
    }

    public long getRecentupdatetime() {
        return recentupdatetime;
    }

    public void setRecentupdatetime(long recentupdatetime) {
        this.recentupdatetime = recentupdatetime;
    }

    public int getContentnum() {
        return contentnum;
    }

    public void setContentnum(int contentnum) {
        this.contentnum = contentnum;
    }

    public int getReadnum() {
        return readnum;
    }

    public void setReadnum(int readnum) {
        this.readnum = readnum;
    }
}
