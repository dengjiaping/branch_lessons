package com.yidiankeyan.science.information.entity;

import java.util.List;

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
 * Created by nby on 2016/11/24.
 * 作用：热点新闻bean
 */
public class HotNewsBean {

    /**
     * id : 7496c7389a5e4013936446c270de197b
     * name : 测试热点大图视频
     * author : null
     * type : 3
     * coverimgurl : http://f.hiphotos.baidu.com/image/h%3D200/sign=9d661b069dcad1c8cfbbfb274f3e67c4/5bafa40f4bfbfbed064c3d5670f0f736afc31f85.jpg
     * covertype : 3
     * imgs : http://f.hiphotos.baidu.com/image/h%3D200/sign=9d661b069dcad1c8cfbbfb274f3e67c4/5bafa40f4bfbfbed064c3d5670f0f736afc31f85.jpg,http://f.hiphotos.baidu.com/image/h%3D200/sign=9d661b069dcad1c8cfbbfb274f3e67c4/5bafa40f4bfbfbed064c3d5670f0f736afc31f85.jpg,http://f.hiphotos.baidu.com/image/h%3D200/sign=9d661b069dcad1c8cfbbfb274f3e67c4/5bafa40f4bfbfbed064c3d5670f0f736afc31f85.jpg
     * praisenum : 65
     * readnum : 1067
     * createtime : 1479969707000
     * commentnum : 0
     * mediaurl : null
     */

    private String id;
    private String name;
    private String author;
    private int type;
    private String coverimgurl;
    private int covertype;
    private String imgs;
    private int praisenum;
    private int readnum;
    private long createtime;
    private int commentnum;
    private String mediaurl;

    /**
     * albumid : f7106c2a0cbd406cb42714084f9410fc
     * albumname : 军事科技
     * tipnum : null
     */


    private String albumid;
    private String albumname;
    private Object tipnum;
    private int subjectid;
    private String subjectname;

    private String userid;
    /**
     * isFocus : 0
     * mozid : 4
     * mozname : 精彩视频观
     * focusModel : {"id":"3cfa5ec132fa4d86af8652223815b6f6","name":"数码","type":2,"updates":0}
     */

    private int isFocus;
    private String mozid;
    private String mozname;
    /**
     * id : 3cfa5ec132fa4d86af8652223815b6f6
     * name : 数码
     * type : 2
     * updates : 0
     */

    /**
     * id : 2160ebe6fabc455f97af8887339ffd4d
     * name : 手机
     * coverimgurl : /ksyun/2017/07/16/9237ed12c70c4cefadbc317d9ba0d8df.jpg
     * synopsis : null
     * followernum : 0
     * contentnum : 0
     * isFocus : null
     */

    private List<TagModelsBean> tagModels;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    //用户头像
    private String userCoverImg;
    //视频时长
    private int length;
    //是否收藏  1:收藏    0/null:未收藏
    private int isCollected;

    public int getIsCollected() {
        return isCollected;
    }

    public void setIsCollected(int isCollected) {
        this.isCollected = isCollected;
    }

    public String getUserCoverImg() {
        return userCoverImg;
    }

    public void setUserCoverImg(String userCoverImg) {
        this.userCoverImg = userCoverImg;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getSubjectid() {
        return subjectid;
    }

    public void setSubjectid(int subjectid) {
        this.subjectid = subjectid;
    }

    public String getSubjectname() {
        return subjectname;
    }

    public void setSubjectname(String subjectname) {
        this.subjectname = subjectname;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCoverimgurl() {
        return coverimgurl;
    }

    public void setCoverimgurl(String coverimgurl) {
        this.coverimgurl = coverimgurl;
    }

    public int getCovertype() {
        return covertype;
    }

    public void setCovertype(int covertype) {
        this.covertype = covertype;
    }

    public String getImgs() {
        return imgs;
    }

    public void setImgs(String imgs) {
        this.imgs = imgs;
    }

    public int getPraisenum() {
        return praisenum;
    }

    public void setPraisenum(int praisenum) {
        this.praisenum = praisenum;
    }

    public int getReadnum() {
        return readnum;
    }

    public void setReadnum(int readnum) {
        this.readnum = readnum;
    }

    public long getCreatetime() {
        return createtime;
    }

    public void setCreatetime(long createtime) {
        this.createtime = createtime;
    }

    public int getCommentnum() {
        return commentnum;
    }

    public void setCommentnum(int commentnum) {
        this.commentnum = commentnum;
    }

    public String getMediaurl() {
        return mediaurl;
    }

    public void setMediaurl(String mediaurl) {
        this.mediaurl = mediaurl;
    }

    public String getAlbumid() {
        return albumid;
    }

    public void setAlbumid(String albumid) {
        this.albumid = albumid;
    }

    public String getAlbumname() {
        return albumname;
    }

    public void setAlbumname(String albumname) {
        this.albumname = albumname;
    }

    public Object getTipnum() {
        return tipnum;
    }

    @Override
    public String toString() {
        return "HotNewsBean{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", type=" + type +
                ", coverimgurl='" + coverimgurl + '\'' +
                ", covertype=" + covertype +
                ", imgs='" + imgs + '\'' +
                ", praisenum=" + praisenum +
                ", readnum=" + readnum +
                ", createtime=" + createtime +
                ", commentnum=" + commentnum +
                ", mediaurl='" + mediaurl + '\'' +
                ", albumid='" + albumid + '\'' +
                ", albumname='" + albumname + '\'' +
                ", tipnum=" + tipnum +
                ", subjectid=" + subjectid +
                ", subjectname='" + subjectname + '\'' +
                ", userid='" + userid + '\'' +
                ", userCoverImg='" + userCoverImg + '\'' +
                ", length=" + length +
                ", isCollected=" + isCollected +
                '}';
    }

    public void setTipnum(Object tipnum) {
        this.tipnum = tipnum;
    }

    public int getIsFocus() {
        return isFocus;
    }

    public void setIsFocus(int isFocus) {
        this.isFocus = isFocus;
    }

    public String getMozid() {
        return mozid;
    }

    public void setMozid(String mozid) {
        this.mozid = mozid;
    }

    public String getMozname() {
        return mozname;
    }

    public void setMozname(String mozname) {
        this.mozname = mozname;
    }


    public List<TagModelsBean> getTagModels() {
        return tagModels;
    }

    public void setTagModels(List<TagModelsBean> tagModels) {
        this.tagModels = tagModels;
    }

    public static class TagModelsBean {
        private String id;
        private String name;
        private String coverimgurl;
        private Object synopsis;
        private int followernum;
        private int contentnum;
        private Object isFocus;

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

        public String getCoverimgurl() {
            return coverimgurl;
        }

        public void setCoverimgurl(String coverimgurl) {
            this.coverimgurl = coverimgurl;
        }

        public Object getSynopsis() {
            return synopsis;
        }

        public void setSynopsis(Object synopsis) {
            this.synopsis = synopsis;
        }

        public int getFollowernum() {
            return followernum;
        }

        public void setFollowernum(int followernum) {
            this.followernum = followernum;
        }

        public int getContentnum() {
            return contentnum;
        }

        public void setContentnum(int contentnum) {
            this.contentnum = contentnum;
        }

        public Object getIsFocus() {
            return isFocus;
        }

        public void setIsFocus(Object isFocus) {
            this.isFocus = isFocus;
        }
    }
}
