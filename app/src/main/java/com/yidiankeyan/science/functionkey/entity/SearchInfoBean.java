package com.yidiankeyan.science.functionkey.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索结果更多类型实体类
 */
public class SearchInfoBean {
    private int type;
    private List<SearchChildBean> searchList;
    private String title;

    public SearchInfoBean(String title, int type) {
        this.title = title;
        this.type = type;
        searchList = new ArrayList<>();
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void SearchChild(String authorid, String imgUrl, String name, String nick, int fansnum) {
        SearchChildBean childBean = new SearchChildBean(authorid, imgUrl, name, nick, fansnum);
        searchList.add(childBean);
    }

    public void SearchChild(String userimgurl, String kdname, String profession, int answersnum, int eavesdropnum, int followernum) {
        SearchChildBean childBean = new SearchChildBean(userimgurl, kdname, profession, answersnum, eavesdropnum, followernum);
        searchList.add(childBean);
    }

    public void SearchChild(String mediaurl, String articleId, int type, String imgUrl, String albumName, String belongAlbum, String contentAmount, String readAmount, String dateTime) {
        SearchChildBean childBean = new SearchChildBean(mediaurl, articleId, type, imgUrl, albumName, belongAlbum, contentAmount, readAmount, dateTime);
        searchList.add(childBean);
    }

    public void SearchChild(String albumId, String imgUrl, String albumName, String contentNumber, String contentName, String belongAlbum, String contentAmount, String readAmount, String dateTime) {
        SearchChildBean childBean = new SearchChildBean(albumId, imgUrl, albumName, contentNumber, contentName, belongAlbum, contentAmount, readAmount, dateTime);
        searchList.add(childBean);
    }

    public void SearchChild(String albumid, String subjectname, String albumname, int albumtype, String recenttitle, String albumauthor, String authorid, String coverimgurl, String recentupdatetime, int contentnum, int readnum) {
        SearchChildBean childBean = new SearchChildBean(albumid, subjectname, albumname, albumtype, recenttitle, albumauthor, authorid, coverimgurl, recentupdatetime, contentnum, readnum);
        searchList.add(childBean);
    }

    public List<SearchChildBean> getSearchList() {
        return searchList;
    }

    public void setSearchList(List<SearchChildBean> searchList) {
        this.searchList = searchList;
    }


    public class SearchChildBean {
        private String imgUrl;
        private String albumName;//专辑名称
        private String contentNumber;//内容编号
        private String contentName;//内容名称
        private String belongAlbum;//所属专题
        private String contentAmount;//内容量
        private String readAmount;//阅读量
        private String albumId;//专辑id
        private String dateTime;
        private int type;
        private String articleId;
        private String mediaurl;


        private String albumid;
        private Object subjectname;
        private String albumname;
        private int albumtype;
        private Object recenttitle;
        private String albumauthor;
        private String authorid;
        private String coverimgurl;
        private Object recentupdatetime;
        private int contentnum;
        private int readnum;
        /**
         * id : 2
         * userimgurl : /yangzhenning.jpg
         * kdname : 杨振宁
         * profession : 物理混沌现象
         * eavesdropnum : 0
         * answersnum : 0
         * messageurl : /1.mp3
         * authentication : 2
         * followernum : 2
         * isfocus : 0
         */

        private String id;
        private String userimgurl;
        private String kdname;
        private String profession;
        private int eavesdropnum;
        private int answersnum;
        private String messageurl;
        private int authentication;
        private int followernum;
        private int isfocus;
        /**
         * name : 杨振宁
         * nick : 杨振宁
         * imgurl : /yangzhenning.jpg
         * fansnum : 4
         */

        private String name;
        private String nick;
        private String imgurl;
        private int fansnum;


        public String getMediaurl() {
            return mediaurl;
        }

        public void setMediaurl(String mediaurl) {
            this.mediaurl = mediaurl;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public SearchChildBean(String mediaurl, String articleId, int type, String imgUrl, String albumName, String belongAlbum, String contentAmount, String readAmount, String dateTime) {
            this.albumId = albumId;
            this.imgUrl = imgUrl;
            this.albumName = albumName;
            this.contentNumber = contentNumber;
            this.contentName = contentName;
            this.belongAlbum = belongAlbum;
            this.contentAmount = contentAmount;
            this.readAmount = readAmount;
            this.dateTime = dateTime;
            this.type = type;
            this.articleId = articleId;
            this.mediaurl = mediaurl;
        }

        public String getArticleId() {
            return articleId;
        }

        public void setArticleId(String articleId) {
            this.articleId = articleId;
        }

        public String getAlbumId() {
            return albumId;
        }

        public void setAlbumId(String albumId) {
            this.albumId = albumId;
        }

        public String getDateTime() {
            return dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getAlbumName() {
            return albumName;
        }

        public void setAlbumName(String albumName) {
            this.albumName = albumName;
        }

        public String getContentNumber() {
            return contentNumber;
        }

        public void setContentNumber(String contentNumber) {
            this.contentNumber = contentNumber;
        }

        public String getContentName() {
            return contentName;
        }

        public void setContentName(String contentName) {
            this.contentName = contentName;
        }

        public String getBelongAlbum() {
            return belongAlbum;
        }

        public void setBelongAlbum(String belongAlbum) {
            this.belongAlbum = belongAlbum;
        }

        public String getContentAmount() {
            return contentAmount;
        }

        public void setContentAmount(String contentAmount) {
            this.contentAmount = contentAmount;
        }

        public String getReadAmount() {
            return readAmount;
        }

        public void setReadAmount(String readAmount) {
            this.readAmount = readAmount;
        }


        public SearchChildBean(String authorid, String imgurl, String name, String nick, int fansnum) {
            this.authorid = authorid;
            this.imgurl = imgurl;
            this.name = name;
            this.nick = nick;
            this.fansnum = fansnum;
        }

        public SearchChildBean(String userimgurl, String kdname, String profession, int answersnum, int eavesdropnum, int followernum) {
            this.userimgurl = userimgurl;
            this.kdname = kdname;
            this.profession = profession;
            this.answersnum = answersnum;
            this.eavesdropnum = eavesdropnum;
            this.followernum = followernum;
        }


        public SearchChildBean(String imgUrl, String albumName, String contentName, String belongAlbum, String contentAmount, String readAmount) {
            this.imgUrl = imgUrl;
            this.albumName = albumName;
            this.contentName = contentName;
            this.belongAlbum = belongAlbum;
            this.contentAmount = contentAmount;
            this.readAmount = readAmount;
        }

        public SearchChildBean(String albumId, String imgUrl, String albumName, String contentNumber, String contentName, String belongAlbum, String contentAmount, String readAmount, String dateTime) {
            this.albumId = albumId;
            this.imgUrl = imgUrl;
            this.albumName = albumName;
            this.contentNumber = contentNumber;
            this.contentName = contentName;
            this.belongAlbum = belongAlbum;
            this.contentAmount = contentAmount;
            this.readAmount = readAmount;
            this.dateTime = dateTime;
        }

        /**
         * albumid : 303560268a7341c2a4f147821c312896
         * subjectname : null
         * albumname : 村长棒棒哒
         * albumtype : 4
         * recenttitle : null
         * albumauthor : 嘿嘿
         * authorid : 26ebdad7512b46db9fac1ff9b2211248
         * coverimgurl :
         * recentupdatetime : null
         * contentnum : 0
         * readnum : 88
         */
        public String getAlbumid() {
            return albumid;
        }

        public void setAlbumid(String albumid) {
            this.albumid = albumid;
        }

        public Object getSubjectname() {
            return subjectname;
        }

        public void setSubjectname(Object subjectname) {
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

        public Object getRecenttitle() {
            return recenttitle;
        }

        public void setRecenttitle(Object recenttitle) {
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

        public Object getRecentupdatetime() {
            return recentupdatetime;
        }

        public void setRecentupdatetime(Object recentupdatetime) {
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

        @Override
        public String toString() {
            return "SearchEditor{" +
                    "albumid='" + albumid + '\'' +
                    ", subjectname=" + subjectname +
                    ", albumname='" + albumname + '\'' +
                    ", albumtype=" + albumtype +
                    ", recenttitle=" + recenttitle +
                    ", albumauthor='" + albumauthor + '\'' +
                    ", authorid='" + authorid + '\'' +
                    ", coverimgurl='" + coverimgurl + '\'' +
                    ", recentupdatetime=" + recentupdatetime +
                    ", contentnum=" + contentnum +
                    ", readnum=" + readnum +
                    '}';
        }

        public SearchChildBean(String albumid, String subjectname, String albumname, int albumtype, String recenttitle, String albumauthor, String authorid, String coverimgurl, String recentupdatetime, int contentnum, int readnum) {
            this.albumid = albumid;
            this.subjectname = subjectname;
            this.albumname = albumname;
            this.albumtype = albumtype;
            this.recenttitle = recenttitle;
            this.albumauthor = albumauthor;
            this.authorid = authorid;
            this.coverimgurl = coverimgurl;
            this.recentupdatetime = recentupdatetime;
            this.contentnum = contentnum;
            this.readnum = readnum;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserimgurl() {
            return userimgurl;
        }

        public void setUserimgurl(String userimgurl) {
            this.userimgurl = userimgurl;
        }

        public String getKdname() {
            return kdname;
        }

        public void setKdname(String kdname) {
            this.kdname = kdname;
        }

        public String getProfession() {
            return profession;
        }

        public void setProfession(String profession) {
            this.profession = profession;
        }

        public int getEavesdropnum() {
            return eavesdropnum;
        }

        public void setEavesdropnum(int eavesdropnum) {
            this.eavesdropnum = eavesdropnum;
        }

        public int getAnswersnum() {
            return answersnum;
        }

        public void setAnswersnum(int answersnum) {
            this.answersnum = answersnum;
        }

        public String getMessageurl() {
            return messageurl;
        }

        public void setMessageurl(String messageurl) {
            this.messageurl = messageurl;
        }

        public int getAuthentication() {
            return authentication;
        }

        public void setAuthentication(int authentication) {
            this.authentication = authentication;
        }

        public int getFollowernum() {
            return followernum;
        }

        public void setFollowernum(int followernum) {
            this.followernum = followernum;
        }

        public int getIsfocus() {
            return isfocus;
        }

        public void setIsfocus(int isfocus) {
            this.isfocus = isfocus;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNick() {
            return nick;
        }

        public void setNick(String nick) {
            this.nick = nick;
        }

        public String getImgurl() {
            return imgurl;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }

        public int getFansnum() {
            return fansnum;
        }

        public void setFansnum(int fansnum) {
            this.fansnum = fansnum;
        }
    }
}
