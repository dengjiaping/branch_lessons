package com.yidiankeyan.science.information.entity;

import java.util.List;

/**
 * //  ◥█▄▃▁   ▁▂▂▃▃▂▂▂▂▂▂▂▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▅
 * //.......◥█☆█▅▄▃▁▁▁▁▁▃▄▅▅▅▅▅▅▅▅▅▅▅▅▅▅▄▁█▅●    ...Created by Administrator on 2016/11/29 0029.
 * //〓▇█████ ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓███▅▄▃███●
 * // 〓〓.๑۩۞۩๑.█████████████████████◤
 * //   ◥█████████◤ ◥        ◢◤
 * //     ◥██████◤        ◥  ◢◤
 * //       ████◤██████◤                推荐-墨子读书-bean
 * //       █▓▓▓▓██◤
 * //      █▓▓▓██◆
 * //     █▓▓▓██◆
 * //    █▓▓▓██◆
 * //   █▓▓▓██◆
 * //  █▓▓▓██◆
 * // █▓▓ ██◆
 * //█▓▓ ██◆
 */
public class MozReadBean {

    /**
     * pageInfo : {"pages":1,"pagesize":20,"records":36,"pagetotal":2}
     * list : [{"id":"8a007f811b604a229c738947e7ae23b2","name":"比特币开发者指南","price":10,"discount":0.2,"originalprice":50,"activityprice":0,"actbegintime":1489042973000,"actendtime":1489044005000,"desc":"希望提名书籍是推荐者自己看过的且确实觉得很好的，如果已阅书库里没有特别匹配的书籍，能介绍相关原理或者技术的也可以。\r\n多谢大家指教！","coverimgurl":"/ksyun/2017/03/09/416dfe84084249d8a75d3aa408c61b4b.png","author":"婷婷","mediaurl":"/ksyun/2017/03/09/cb157a72a9c84e0f8901ba47569c996c.mp3","subjectid":17,"subjectname":"宇宙探索","casterid":"a44fb075dbdc4ccb9f33081d40714065","castername":"0392","casterimgurl":"/ksyun/2017/02/14/6c640fdde1ee4a24b02cae10d786db2d.jpg","readnum":8,"praisenum":0,"checkstatus":2,"isbuy":0}]
     */

    private PageInfoBean pageInfo;
    private List<ListBean> list;

    public PageInfoBean getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfoBean pageInfo) {
        this.pageInfo = pageInfo;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class PageInfoBean {
        /**
         * pages : 1
         * pagesize : 20
         * records : 36
         * pagetotal : 2
         */

        private int pages;
        private int pagesize;
        private int records;
        private int pagetotal;

        public int getPages() {
            return pages;
        }

        public void setPages(int pages) {
            this.pages = pages;
        }

        public int getPagesize() {
            return pagesize;
        }

        public void setPagesize(int pagesize) {
            this.pagesize = pagesize;
        }

        public int getRecords() {
            return records;
        }

        public void setRecords(int records) {
            this.records = records;
        }

        public int getPagetotal() {
            return pagetotal;
        }

        public void setPagetotal(int pagetotal) {
            this.pagetotal = pagetotal;
        }
    }

    public static class ListBean {
        /**
         * id : 9ced1a1def434514bae3b6356f982b1f
         * name : 我的焦虑岁月
         * price : 1.99
         * discount : 1.0
         * originalprice : 1.99
         * activityprice : 0.0
         * actbegintime : 1497940800000
         * actendtime : 1498186800000
         * desc : 直到三十几年前，焦虑症在成为正式的诊断名称，可是今天，焦虑症已经成了最普遍的精神疾病。对于焦虑症，我们到底了解多少呢？在《我的焦虑岁月这本书里，作者以自己从小当大对抗焦虑的亲身体验为基础，加上医学、文化和社会等方面对焦虑症的研究，带我们了解这个常被人误解的领域。
         * coverimgurl : /ksyun/2017/07/22/1f64624ed3d943dea06dfdb535c282b4.jpg
         * author : 斯科特•斯多塞尔
         * mediaurl : /ksyun/2017/06/06/12b168d756d04ac6b6ab52ff87123a3d.mp3
         * medialength : 1140
         * subjectid : 41
         * subjectname : 综合
         * casterid : f1d3859b3da24075bf1d72732bd70ae7
         * castername : 北方
         * casterimgurl : /static/upload/cmsweb/image/jpeg2017/1/5/8876ac02fa264311b9641c6baed8d816.jpg
         * readnum : 63
         * praisenum : 0
         * checkstatus : 2
         * isbuy : 0
         * createtime : 1496726444000
         * lastupdatetime : 1502358181000
         */

        private String id;
        private String name;
        private double price;
        private double discount;
        private double originalprice;
        private double activityprice;
        private long actbegintime;
        private long actendtime;
        private String desc;
        private String coverimgurl;
        private String author;
        private String mediaurl;
        private Integer medialength;
        private int subjectid;
        private String subjectname;
        private String casterid;
        private String castername;
        private String casterimgurl;
        private int readnum;
        private int praisenum;
        private int checkstatus;
        private int isbuy;
        private long createtime;
        private long lastupdatetime;
        private int isActPriceShow;

        //liuchao add

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

        public int getIsActPriceShow() {
            return isActPriceShow;
        }

        public void setIsActPriceShow(int isActPriceShow) {
            this.isActPriceShow = isActPriceShow;
        }

        public void setPrice(double price) {
            this.price = price;

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

        public String getMediaurl() {
            return mediaurl;
        }

        public void setMediaurl(String mediaurl) {
            this.mediaurl = mediaurl;
        }

        public Integer getMedialength() {
            return medialength;
        }

        public void setMedialength(Integer medialength) {
            this.medialength = medialength;
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

        public String getCasterid() {
            return casterid;
        }

        public void setCasterid(String casterid) {
            this.casterid = casterid;
        }

        public String getCastername() {
            return castername;
        }

        public void setCastername(String castername) {
            this.castername = castername;
        }

        public String getCasterimgurl() {
            return casterimgurl;
        }

        public void setCasterimgurl(String casterimgurl) {
            this.casterimgurl = casterimgurl;
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

        public int getCheckstatus() {
            return checkstatus;
        }

        public void setCheckstatus(int checkstatus) {
            this.checkstatus = checkstatus;
        }

        public int getIsbuy() {
            return isbuy;
        }

        public void setIsbuy(int isbuy) {
            this.isbuy = isbuy;
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
    }
}
