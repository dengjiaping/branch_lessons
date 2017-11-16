package com.yidiankeyan.science.subscribe.entity;

import java.util.List;

/**
 * //  ◥█▄▃▁   ▁▂▂▃▃▂▂▂▂▂▂▂▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▅
 * //.......◥█☆█▅▄▃▁▁▁▁▁▃▄▅▅▅▅▅▅▅▅▅▅▅▅▅▅▄▁█▅●    ...Created by Administrator on 2016/9/2 0002.
 * //〓▇█████ ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓███▅▄▃███●
 * // 〓〓.๑۩۞۩๑.█████████████████████◤
 * //   ◥█████████◤ ◥        ◢◤
 * //     ◥██████◤        ◥  ◢◤
 * //       ████◤██████◤
 * //       █▓▓▓▓██◤
 * //      █▓▓▓██◆
 * //     █▓▓▓██◆
 * //    █▓▓▓██◆
 * //   █▓▓▓██◆
 * //  █▓▓▓██◆
 * // █▓▓ ██◆
 * //█▓▓ ██◆
 */
public class SearchAll {

    /**
     * albumid : 02970b25ce03447590246b376c072590
     * subjectname : 军事科技
     * albumname : 军事仿真科技
     * albumtype : 1
     * recenttitle : 庞大的战神家族：122毫米系列火炮
     * albumauthor : 潘建伟
     * authorid : 1
     * coverimgurl : /static/upload/cmsweb/image/png2016/8/19/6de568c29c494ed4b8f9291f0509a559.png
     * recentupdatetime : 1471598613000
     * contentnum : 2
     * readnum : 0
     */

    private List<AlbumlistBean> albumlist;
    /**
     * id : 074c5fd291ea41b3ae17ded26f48f367
     * checkstatus : 2
     * name : 经典物理与量子物理不再泾渭分明，科学家最新发现混沌现象和量子纠缠之间的联系
     * abstracts : <p>一个从树上掉下来的苹果造就了一个传奇的科学家。虽然苹果只是传说，但是这位科学家确实真真正正一手托起了经典力学，在量子力学诞生前，许多物理学家都认为，物理学的大厦已然建成，需要的只有修修补补了。然而由于经典力学对微观世界的解释越来越乏力，许多科学家们转向新的思考方式，最终的结果是现代物理的两大支柱之一——量子力学诞生了。于是对所有的非专业人士来说，物理学描述出来的世界已经从反常识走向了反人类了。经典力学和量子力学，在几乎所有人的理解里，都是自称一体的两个体系，如同波粒二象性理论出现之前的粒子和波。而于7月11日发表在《自然·物理》杂志上的一篇论文，带来了一点全新的契机，<strong>他们发现了的量子纠缠态和经典混乱度之间存在联系。而经典力学和量子力学的最终融合，说不定就会从这么一点契机开始。</strong></p><p><br/></p>
     * summary : <p>该研究成果对量子计算机的研究有重大影响。3个量子比特组成的量子计算机的功能还比较弱，但是科学家正不断制造拥有更多量子比特的计算机，它们的功能极其强大，可以解决传统计算机无力解决的问题，诸如机器学习、人工智能、流体力学和计算化学等。</p>
     * reference : <p>原文转自“宇宙天文”。<br/></p>
     * userid : 1
     * size : 0
     * createtime : 1470891102000
     * author : null
     * publishtime : null
     * isuse : 1
     * type : 1
     * sourcetype : 0
     * commentnum : 1
     * praisenum : 0
     * reprintnum : 0
     * readnum : 0
     * coverimgurl : /static/upload/cmsweb/image/png2016/8/11/9d1cb76cd7f040f393225d91c055a2e8.png
     * shortimgurl : /static/upload/cmsweb/image/png2016/8/11/9d1cb76cd7f040f393225d91c055a2e8.png
     * mediaurl : null
     * albumid : 8569004a5a9b464d9d9b0adbab832e89
     * subjectid : 9
     * isfrozen : 0
     * content : null
     * ispaid : null
     * isliked : null
     */

    private List<ArticlelistBean> articlelist;
    private List<AlbumEditorBean> authorlist;
    private List<AlbumAnswerBean> answerorlist;

    public List<AlbumlistBean> getAlbumlist() {
        return albumlist;
    }

    public void setAlbumlist(List<AlbumlistBean> albumlist) {
        this.albumlist = albumlist;
    }

    public List<ArticlelistBean> getArticlelist() {
        return articlelist;
    }

    public void setArticlelist(List<ArticlelistBean> articlelist) {
        this.articlelist = articlelist;
    }

    public List<AlbumEditorBean> getAuthorlist() {
        return authorlist;
    }

    public void setAuthorlist(List<AlbumEditorBean> authorlist) {
        this.authorlist = authorlist;
    }

    public List<AlbumAnswerBean> getAnswerorlist() {
        return answerorlist;
    }

    public void setAnswerorlist(List<AlbumAnswerBean> answerorlist) {
        this.answerorlist = answerorlist;
    }

    public static class AlbumlistBean {
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

    public static class ArticlelistBean {
        private String id;
        private int checkstatus;
        private String name;
        private String abstracts;
        private String summary;
        private String reference;
        private String userid;
        private int size;
        private long createtime;
        private Object author;
        private Object publishtime;
        private int isuse;
        private int type;
        private int sourcetype;
        private int commentnum;
        private int praisenum;
        private int reprintnum;
        private int readnum;
        private String coverimgurl;
        private String shortimgurl;
        private String mediaurl;
        private String albumid;
        private int subjectid;
        private int isfrozen;
        private Object content;
        private Object ispaid;
        private Object isliked;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getCheckstatus() {
            return checkstatus;
        }

        public void setCheckstatus(int checkstatus) {
            this.checkstatus = checkstatus;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAbstracts() {
            return abstracts;
        }

        public void setAbstracts(String abstracts) {
            this.abstracts = abstracts;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public String getReference() {
            return reference;
        }

        public void setReference(String reference) {
            this.reference = reference;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public long getCreatetime() {
            return createtime;
        }

        public void setCreatetime(long createtime) {
            this.createtime = createtime;
        }

        public Object getAuthor() {
            return author;
        }

        public void setAuthor(Object author) {
            this.author = author;
        }

        public Object getPublishtime() {
            return publishtime;
        }

        public void setPublishtime(Object publishtime) {
            this.publishtime = publishtime;
        }

        public int getIsuse() {
            return isuse;
        }

        public void setIsuse(int isuse) {
            this.isuse = isuse;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getSourcetype() {
            return sourcetype;
        }

        public void setSourcetype(int sourcetype) {
            this.sourcetype = sourcetype;
        }

        public int getCommentnum() {
            return commentnum;
        }

        public void setCommentnum(int commentnum) {
            this.commentnum = commentnum;
        }

        public int getPraisenum() {
            return praisenum;
        }

        public void setPraisenum(int praisenum) {
            this.praisenum = praisenum;
        }

        public int getReprintnum() {
            return reprintnum;
        }

        public void setReprintnum(int reprintnum) {
            this.reprintnum = reprintnum;
        }

        public int getReadnum() {
            return readnum;
        }

        public void setReadnum(int readnum) {
            this.readnum = readnum;
        }

        public String getCoverimgurl() {
            return coverimgurl;
        }

        public void setCoverimgurl(String coverimgurl) {
            this.coverimgurl = coverimgurl;
        }

        public String getShortimgurl() {
            return shortimgurl;
        }

        public void setShortimgurl(String shortimgurl) {
            this.shortimgurl = shortimgurl;
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

        public int getSubjectid() {
            return subjectid;
        }

        public void setSubjectid(int subjectid) {
            this.subjectid = subjectid;
        }

        public int getIsfrozen() {
            return isfrozen;
        }

        public void setIsfrozen(int isfrozen) {
            this.isfrozen = isfrozen;
        }

        public Object getContent() {
            return content;
        }

        public void setContent(Object content) {
            this.content = content;
        }

        public Object getIspaid() {
            return ispaid;
        }

        public void setIspaid(Object ispaid) {
            this.ispaid = ispaid;
        }

        public Object getIsliked() {
            return isliked;
        }

        public void setIsliked(Object isliked) {
            this.isliked = isliked;
        }
    }

    public static class AlbumEditorBean {


        /**
         * authorid : 2
         * name : 杨振宁
         * nick : 杨振宁
         * imgurl : /yangzhenning.jpg
         * albumname : null
         * fansnum : 4
         */

        private String authorid;
        private String name;
        private String nick;
        private String imgurl;
        private Object albumname;
        private int fansnum;

        public String getAuthorid() {
            return authorid;
        }

        public void setAuthorid(String authorid) {
            this.authorid = authorid;
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

        public Object getAlbumname() {
            return albumname;
        }

        public void setAlbumname(Object albumname) {
            this.albumname = albumname;
        }

        public int getFansnum() {
            return fansnum;
        }

        public void setFansnum(int fansnum) {
            this.fansnum = fansnum;
        }
    }

    public static class AlbumAnswerBean {

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
    }

}
