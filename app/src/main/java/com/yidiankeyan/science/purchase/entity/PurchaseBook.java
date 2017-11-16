package com.yidiankeyan.science.purchase.entity;

import java.util.List;

/**
 * //  ◥█▄▃▁   ▁▂▂▃▃▂▂▂▂▂▂▂▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▅
 * //.......◥█☆█▅▄▃▁▁▁▁▁▃▄▅▅▅▅▅▅▅▅▅▅▅▅▅▅▄▁█▅●    ...Created by zn on 2016/12/7 0007.
 * //〓▇█████ ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓███▅▄▃███●
 * // 〓〓.๑۩۞۩๑.█████████████████████◤
 * //   ◥█████████◤ ◥        ◢◤
 * //     ◥██████◤        ◥  ◢◤
 * //       ████◤██████◤
 * //       █▓▓▓▓██◤
 * //      █▓▓▓██◆              已购墨子书籍
 * //     █▓▓▓██◆
 * //    █▓▓▓██◆
 * //   █▓▓▓██◆
 * //  █▓▓▓██◆
 * // █▓▓ ██◆
 * //█▓▓ ██◆
 */
public class PurchaseBook {
    /**
     * pageInfo : {"pages":1,"pagesize":20,"records":10,"pagetotal":1}
     * list : [{"recordid":"0ad4b8cabcde49d696ace19a19a70c8a","bookid":"003cd380f89440de8aa6a45a722e7cc2","userid":"9d1461830f2d4a1092ab69291a5aa738","username":"大帅","orderid":"1612080413000038","orderstatus":2,"createtime":1481184780000,"bookname":"大学","price":1.99,"desc":"八股文，中国文萃","coverimgurl":"/static/upload/cmsweb/image/jpeg2016/11/2/7d1b8d7f7ea04008a37637b98cc02ff2.jpg","author":"花花","mediaurl":"/static/upload/cmsweb/audio/mp32016/9/25/33e76ae4fb9e4252a072138fdb9b17fe.mp3","casterid":"419f619d2cb64e869e1a90ae10b248e7","castername":"熬八马","subjectid":"10","subjectname":"地球科学","readnum":0},{"recordid":"208dcd78330c4e479ed7b84a08339e80","bookid":"e3e6dd8c0d2546d09531ef8659c0cd55","userid":"9d1461830f2d4a1092ab69291a5aa738","username":"大帅","orderid":"1612080526000041","orderstatus":2,"createtime":1481189198000,"bookname":"道德经","price":1.99,"desc":"道可道非常道，名可名非常名","coverimgurl":"/static/upload/cmsweb/image/gif2016/11/29/d3637ce0c3e947e0bf9e3ab22ad9356d.gif","author":"老子","mediaurl":"/static/upload/cmsweb/audio/mpeg2016/11/29/f2f2719b907e4e12af0026d8076a7c96.mp3","casterid":"419f619d2cb64e869e1a90ae10b248e7","castername":"熬八马","subjectid":"14","subjectname":null,"readnum":0},{"recordid":"4b4c0d884d8d4bf284f3fed3095f2847","bookid":"75eb1da9d1da421cbaa855e1e3f7daea","userid":"9d1461830f2d4a1092ab69291a5aa738","username":"大帅","orderid":"1612080230000025","orderstatus":2,"createtime":1481178641000,"bookname":"史记","price":1.09,"desc":"隔壁老王经验集续篇","coverimgurl":"/static/upload/cmsweb/image/gif2016/11/29/d3637ce0c3e947e0bf9e3ab22ad9356d.gif","author":"司马迁","mediaurl":"/static/upload/cmsweb/audio/mpeg2016/11/29/f803d12101f54345a28f087ebee7c5b0.mp3","casterid":"419f619d2cb64e869e1a90ae10b248e7","castername":"熬八马","subjectid":"12","subjectname":"超自然","readnum":0},{"recordid":"4bffe99b905a4777b4ff080eb1759e90","bookid":"003cd380f89440de8aa6a45a722e7cc3","userid":"9d1461830f2d4a1092ab69291a5aa738","username":"大帅","orderid":"1612080355000036","orderstatus":2,"createtime":1481183704000,"bookname":"中庸","price":1.09,"desc":"不错的文集","coverimgurl":"/static/upload/cmsweb/image/jpeg2016/11/2/7d1b8d7f7ea04008a37637b98cc02ff2.jpg","author":"青青","mediaurl":"/static/upload/cmsweb/audio/mp32016/9/25/33e76ae4fb9e4252a072138fdb9b17fe.mp3","casterid":"419f619d2cb64e869e1a90ae10b248e7","castername":"熬八马","subjectid":"10","subjectname":"地球科学","readnum":0},{"recordid":"54b3b151660a41b7abf77332230c5b57","bookid":"75eb1da9d1da421cbaa855e1e3f7daea","userid":"9d1461830f2d4a1092ab69291a5aa738","username":"大帅","orderid":"1612080230000019","orderstatus":2,"createtime":1481178631000,"bookname":"史记","price":1.09,"desc":"隔壁老王经验集续篇","coverimgurl":"/static/upload/cmsweb/image/gif2016/11/29/d3637ce0c3e947e0bf9e3ab22ad9356d.gif","author":"司马迁","mediaurl":"/static/upload/cmsweb/audio/mpeg2016/11/29/f803d12101f54345a28f087ebee7c5b0.mp3","casterid":"419f619d2cb64e869e1a90ae10b248e7","castername":"熬八马","subjectid":"12","subjectname":"超自然","readnum":0},{"recordid":"601bef79d1cf49758085d023bfeaaeaf","bookid":"75eb1da9d1da421cbaa855e1e3f7daea","userid":"9d1461830f2d4a1092ab69291a5aa738","username":"大帅","orderid":"1612080230000021","orderstatus":2,"createtime":1481178634000,"bookname":"史记","price":1.09,"desc":"隔壁老王经验集续篇","coverimgurl":"/static/upload/cmsweb/image/gif2016/11/29/d3637ce0c3e947e0bf9e3ab22ad9356d.gif","author":"司马迁","mediaurl":"/static/upload/cmsweb/audio/mpeg2016/11/29/f803d12101f54345a28f087ebee7c5b0.mp3","casterid":"419f619d2cb64e869e1a90ae10b248e7","castername":"熬八马","subjectid":"12","subjectname":"超自然","readnum":0},{"recordid":"83e5cb90508f4936821f91c079a7de47","bookid":"e3e6dd8c0d2546d09531ef8659c0cd56","userid":"9d1461830f2d4a1092ab69291a5aa738","username":"大帅","orderid":"1612080350000034","orderstatus":2,"createtime":1481183435000,"bookname":"孟子","price":1,"desc":"知之为知之，不知为不知，是知也","coverimgurl":"/static/upload/cmsweb/image/gif2016/11/29/d3637ce0c3e947e0bf9e3ab22ad9356d.gif","author":"孟子","mediaurl":"/static/upload/cmsweb/audio/mpeg2016/11/29/f2f2719b907e4e12af0026d8076a7c96.mp3","casterid":"419f619d2cb64e869e1a90ae10b248e7","castername":"熬八马","subjectid":"11","subjectname":null,"readnum":0},{"recordid":"86f1303f87274db3b970fc0a5fc5def9","bookid":"75eb1da9d1da421cbaa855e1e3f7daea","userid":"9d1461830f2d4a1092ab69291a5aa738","username":"大帅","orderid":"1612080230000017","orderstatus":2,"createtime":1481178626000,"bookname":"史记","price":1.09,"desc":"隔壁老王经验集续篇","coverimgurl":"/static/upload/cmsweb/image/gif2016/11/29/d3637ce0c3e947e0bf9e3ab22ad9356d.gif","author":"司马迁","mediaurl":"/static/upload/cmsweb/audio/mpeg2016/11/29/f803d12101f54345a28f087ebee7c5b0.mp3","casterid":"419f619d2cb64e869e1a90ae10b248e7","castername":"熬八马","subjectid":"12","subjectname":"超自然","readnum":0},{"recordid":"d8baebe961004b1fa788ec9118bdd9ce","bookid":"75eb1da9d1da421cbaa855e1e3f7daea","userid":"9d1461830f2d4a1092ab69291a5aa738","username":"大帅","orderid":"1612080230000023","orderstatus":2,"createtime":1481178637000,"bookname":"史记","price":1.09,"desc":"隔壁老王经验集续篇","coverimgurl":"/static/upload/cmsweb/image/gif2016/11/29/d3637ce0c3e947e0bf9e3ab22ad9356d.gif","author":"司马迁","mediaurl":"/static/upload/cmsweb/audio/mpeg2016/11/29/f803d12101f54345a28f087ebee7c5b0.mp3","casterid":"419f619d2cb64e869e1a90ae10b248e7","castername":"熬八马","subjectid":"12","subjectname":"超自然","readnum":0},{"recordid":"d8c88f7e36e142e8baffc053ff82bcd7","bookid":"75eb1da9d1da421cbaa855e1e3f7daea","userid":"9d1461830f2d4a1092ab69291a5aa738","username":"大帅","orderid":"1612080230000027","orderstatus":2,"createtime":1481178644000,"bookname":"史记","price":1.09,"desc":"隔壁老王经验集续篇","coverimgurl":"/static/upload/cmsweb/image/gif2016/11/29/d3637ce0c3e947e0bf9e3ab22ad9356d.gif","author":"司马迁","mediaurl":"/static/upload/cmsweb/audio/mpeg2016/11/29/f803d12101f54345a28f087ebee7c5b0.mp3","casterid":"419f619d2cb64e869e1a90ae10b248e7","castername":"熬八马","subjectid":"12","subjectname":"超自然","readnum":0}]
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
         * records : 10
         * pagetotal : 1
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
         * recordid : 0ad4b8cabcde49d696ace19a19a70c8a
         * bookid : 003cd380f89440de8aa6a45a722e7cc2
         * userid : 9d1461830f2d4a1092ab69291a5aa738
         * username : 大帅
         * orderid : 1612080413000038
         * orderstatus : 2
         * createtime : 1481184780000
         * bookname : 大学
         * price : 1.99
         * desc : 八股文，中国文萃
         * coverimgurl : /static/upload/cmsweb/image/jpeg2016/11/2/7d1b8d7f7ea04008a37637b98cc02ff2.jpg
         * author : 花花
         * mediaurl : /static/upload/cmsweb/audio/mp32016/9/25/33e76ae4fb9e4252a072138fdb9b17fe.mp3
         * casterid : 419f619d2cb64e869e1a90ae10b248e7
         * castername : 熬八马
         * subjectid : 10
         * subjectname : 地球科学
         * readnum : 0
         */

        private String recordid;
        private String bookid;
        private String userid;
        private String username;
        private String orderid;
        private int orderstatus;
        private long createtime;
        private String bookname;
        private double price;
        private String desc;
        private String coverimgurl;
        private String author;
        private String mediaurl;
        private String casterid;
        private String castername;
        private String subjectid;
        private String subjectname;
        private int readnum;

        public String getRecordid() {
            return recordid;
        }

        public void setRecordid(String recordid) {
            this.recordid = recordid;
        }

        public String getBookid() {
            return bookid;
        }

        public void setBookid(String bookid) {
            this.bookid = bookid;
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

        public String getOrderid() {
            return orderid;
        }

        public void setOrderid(String orderid) {
            this.orderid = orderid;
        }

        public int getOrderstatus() {
            return orderstatus;
        }

        public void setOrderstatus(int orderstatus) {
            this.orderstatus = orderstatus;
        }

        public long getCreatetime() {
            return createtime;
        }

        public void setCreatetime(long createtime) {
            this.createtime = createtime;
        }

        public String getBookname() {
            return bookname;
        }

        public void setBookname(String bookname) {
            this.bookname = bookname;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
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

        public String getSubjectid() {
            return subjectid;
        }

        public void setSubjectid(String subjectid) {
            this.subjectid = subjectid;
        }

        public String getSubjectname() {
            return subjectname;
        }

        public void setSubjectname(String subjectname) {
            this.subjectname = subjectname;
        }

        public int getReadnum() {
            return readnum;
        }

        public void setReadnum(int readnum) {
            this.readnum = readnum;
        }
    }
}
