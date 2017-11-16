package com.yidiankeyan.science.information.entity;

import java.util.List;

/**
 * //  ◥█▄▃▁   ▁▂▂▃▃▂▂▂▂▂▂▂▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▅
 * //.......◥█☆█▅▄▃▁▁▁▁▁▃▄▅▅▅▅▅▅▅▅▅▅▅▅▅▅▄▁█▅●    ...Created by Administrator on 2016/11/29 0029.
 * //〓▇█████ ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓███▅▄▃███●
 * // 〓〓.๑۩۞۩๑.█████████████████████◤
 * //   ◥█████████◤ ◥        ◢◤
 * //     ◥██████◤        ◥  ◢◤
 * //       ████◤██████◤                推荐-精品专栏-bean
 * //       █▓▓▓▓██◤
 * //      █▓▓▓██◆
 * //     █▓▓▓██◆
 * //    █▓▓▓██◆
 * //   █▓▓▓██◆
 * //  █▓▓▓██◆
 * // █▓▓ ██◆
 * //█▓▓ ██◆
 */
public class SubscribeColumnBean {

    /**
     * pageNum : 1
     * pageSize : 3
     * size : 2
     * orderBy : null
     * startRow : 1
     * endRow : 2
     * total : 2
     * pages : 1
     * list : [{"startTime":null,"endTime":null,"pageNum":null,"pageSize":null,"id":"11","columnName":"芭芭拉·利斯科夫（Barbara Liskov）","columnTitle":"本名Barbara Jane Huberman。美国计算机科学家，2008年图灵奖得主，2004年约翰·冯诺依曼奖得主。现任麻省理工学院电子电气与计算机科学系教授。","columnImg":null,"columnPrice":"165.00","columnIntro":null,"createTime":"2017-08-18 15:38:49.0","columnUpdate":null,"columnCrowd":null,"columnBuyNotes":null,"columnWriter":"尼古拉斯·沃斯","columnWriterIntro":"尼古拉斯·沃斯（Niklaus Wirth，1934年2月15日\u2014），生於于瑞士温特图尔，是瑞士计算机科学家。少年时代的Niklaus Wirth与数学家Pascal一样喜欢动手动脑。1958年，Niklaus从苏黎世工学院取得学士学位后来到加拿大的莱维大学深造，之后进入美国加州大学伯克利分校获得博士学位。","columnPicture":"/ksyun/2017/07/11/70fe8d0702394921add7fb9d6838cb4d.jpeg","columnPoster":null,"release":"0","type":"1","effectiveTimeLength":null,"createUserid":null,"verifyUser":null,"haveYouPurchased":null},{"startTime":null,"endTime":null,"pageNum":null,"pageSize":null,"id":"10","columnName":"测试专栏----尕尕","columnTitle":null,"columnImg":null,"columnPrice":"112.00","columnIntro":null,"createTime":"2017-08-17 17:02:18.0","columnUpdate":null,"columnCrowd":null,"columnBuyNotes":null,"columnWriter":"尕尕","columnWriterIntro":"尕尕尕尕","columnPicture":"/ksyun/2017/08/17/8c5df64f9f9d4467bf44dcd05851444b.jpg","columnPoster":null,"release":"0","type":"1","effectiveTimeLength":null,"createUserid":null,"verifyUser":null,"haveYouPurchased":null}]
     * firstPage : 1
     * prePage : 0
     * nextPage : 0
     * lastPage : 1
     * isFirstPage : true
     * isLastPage : true
     * hasPreviousPage : false
     * hasNextPage : false
     * navigatePages : 8
     * navigatepageNums : [1]
     */

    private int pageNum;
    private int pageSize;
    private int size;
    private String orderBy;
    private int startRow;
    private int endRow;
    private long total;
    private int pages;
    private int firstPage;
    private int prePage;
    private int nextPage;
    private int lastPage;
    private boolean isFirstPage;
    private boolean isLastPage;
    private boolean hasPreviousPage;
    private boolean hasNextPage;
    private int navigatePages;
    private List<ListBean> list;
    private int[] navigatepageNums;
    private String haveYouPurchased; //1代表已购买 0代表未购买

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public int getStartRow() {
        return startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public int getEndRow() {
        return endRow;
    }

    public void setEndRow(int endRow) {
        this.endRow = endRow;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getFirstPage() {
        return firstPage;
    }

    public void setFirstPage(int firstPage) {
        this.firstPage = firstPage;
    }

    public int getPrePage() {
        return prePage;
    }

    public void setPrePage(int prePage) {
        this.prePage = prePage;
    }

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public int getLastPage() {
        return lastPage;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }

    public boolean isIsFirstPage() {
        return isFirstPage;
    }

    public void setIsFirstPage(boolean isFirstPage) {
        this.isFirstPage = isFirstPage;
    }

    public boolean isIsLastPage() {
        return isLastPage;
    }

    public void setIsLastPage(boolean isLastPage) {
        this.isLastPage = isLastPage;
    }

    public boolean isHasPreviousPage() {
        return hasPreviousPage;
    }

    public void setHasPreviousPage(boolean hasPreviousPage) {
        this.hasPreviousPage = hasPreviousPage;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public int getNavigatePages() {
        return navigatePages;
    }

    public void setNavigatePages(int navigatePages) {
        this.navigatePages = navigatePages;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public int[] getNavigatepageNums() {
        return navigatepageNums;
    }

    public void setNavigatepageNums(int[] navigatepageNums) {
        this.navigatepageNums = navigatepageNums;
    }

    public static class ListBean {
        /**
         * startTime : null
         * endTime : null
         * pageNum : null
         * pageSize : null
         * id : 11
         * columnName : 芭芭拉·利斯科夫（Barbara Liskov）
         * columnTitle : 本名Barbara Jane Huberman。美国计算机科学家，2008年图灵奖得主，2004年约翰·冯诺依曼奖得主。现任麻省理工学院电子电气与计算机科学系教授。
         * columnImg : null
         * columnPrice : 165.00
         * columnIntro : null
         * createTime : 2017-08-18 15:38:49.0
         * columnUpdate : null
         * columnCrowd : null
         * columnBuyNotes : null
         * columnWriter : 尼古拉斯·沃斯
         * columnWriterIntro : 尼古拉斯·沃斯（Niklaus Wirth，1934年2月15日—），生於于瑞士温特图尔，是瑞士计算机科学家。少年时代的Niklaus Wirth与数学家Pascal一样喜欢动手动脑。1958年，Niklaus从苏黎世工学院取得学士学位后来到加拿大的莱维大学深造，之后进入美国加州大学伯克利分校获得博士学位。
         * columnPicture : /ksyun/2017/07/11/70fe8d0702394921add7fb9d6838cb4d.jpeg
         * columnPoster : null
         * release : 0
         * type : 1
         * effectiveTimeLength : null
         * createUserid : null
         * verifyUser : null
         * haveYouPurchased : null
         */

        private String startTime;
        private String endTime;
        private String pageNum;
        private String pageSize;
        private String id;
        private String columnName;
        private String columnTitle;
        private String columnImg;
        private String columnPrice;
        private String columnIntro;
        private String createTime;
        private String columnUpdate;
        private String columnCrowd;
        private String columnBuyNotes;
        private String columnWriter;
        private String columnWriterIntro;
        private String columnPicture;
        private String columnPoster;
        private String release;
        private String type;
        private String effectiveTimeLength;
        private String createUserid;
        private String verifyUser;
        private String haveYouPurchased;

        public String getColumnActivityPrice() {
            return columnActivityPrice;
        }

        public void setColumnActivityPrice(String columnActivityPrice) {
            this.columnActivityPrice = columnActivityPrice;
        }

        private String rankUpdate;

        public int getIshasactivityprice() {
            return ishasactivityprice;
        }

        public void setIshasactivityprice(int ishasactivityprice) {
            this.ishasactivityprice = ishasactivityprice;
        }

        private String columnActivityPrice;
        private int ishasactivityprice;   //1：有活动价  0：无活动价


        public String getRankUpdate() {
            return rankUpdate;
        }

        public void setRankUpdate(String rankUpdate) {
            this.rankUpdate = rankUpdate;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getPageNum() {
            return pageNum;
        }

        public void setPageNum(String pageNum) {
            this.pageNum = pageNum;
        }

        public String getPageSize() {
            return pageSize;
        }

        public void setPageSize(String pageSize) {
            this.pageSize = pageSize;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getColumnName() {
            return columnName;
        }

        public void setColumnName(String columnName) {
            this.columnName = columnName;
        }

        public String getColumnTitle() {
            return columnTitle;
        }

        public void setColumnTitle(String columnTitle) {
            this.columnTitle = columnTitle;
        }

        public String getColumnImg() {
            return columnImg;
        }

        public void setColumnImg(String columnImg) {
            this.columnImg = columnImg;
        }

        public String getColumnPrice() {
            return columnPrice;
        }

        public void setColumnPrice(String columnPrice) {
            this.columnPrice = columnPrice;
        }

        public String getColumnIntro() {
            return columnIntro;
        }

        public void setColumnIntro(String columnIntro) {
            this.columnIntro = columnIntro;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getColumnUpdate() {
            return columnUpdate;
        }

        public void setColumnUpdate(String columnUpdate) {
            this.columnUpdate = columnUpdate;
        }

        public String getColumnCrowd() {
            return columnCrowd;
        }

        public void setColumnCrowd(String columnCrowd) {
            this.columnCrowd = columnCrowd;
        }

        public String getColumnBuyNotes() {
            return columnBuyNotes;
        }

        public void setColumnBuyNotes(String columnBuyNotes) {
            this.columnBuyNotes = columnBuyNotes;
        }

        public String getColumnWriter() {
            return columnWriter;
        }

        public void setColumnWriter(String columnWriter) {
            this.columnWriter = columnWriter;
        }

        public String getColumnWriterIntro() {
            return columnWriterIntro;
        }

        public void setColumnWriterIntro(String columnWriterIntro) {
            this.columnWriterIntro = columnWriterIntro;
        }

        public String getColumnPicture() {
            return columnPicture;
        }

        public void setColumnPicture(String columnPicture) {
            this.columnPicture = columnPicture;
        }

        public String getColumnPoster() {
            return columnPoster;
        }

        public void setColumnPoster(String columnPoster) {
            this.columnPoster = columnPoster;
        }

        public String getRelease() {
            return release;
        }

        public void setRelease(String release) {
            this.release = release;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getEffectiveTimeLength() {
            return effectiveTimeLength;
        }

        public void setEffectiveTimeLength(String effectiveTimeLength) {
            this.effectiveTimeLength = effectiveTimeLength;
        }

        public String getCreateUserid() {
            return createUserid;
        }

        public void setCreateUserid(String createUserid) {
            this.createUserid = createUserid;
        }

        public String getVerifyUser() {
            return verifyUser;
        }

        public void setVerifyUser(String verifyUser) {
            this.verifyUser = verifyUser;
        }

        public String getHaveYouPurchased() {
            return haveYouPurchased;
        }

        public void setHaveYouPurchased(String haveYouPurchased) {
            this.haveYouPurchased = haveYouPurchased;
        }
    }
}
