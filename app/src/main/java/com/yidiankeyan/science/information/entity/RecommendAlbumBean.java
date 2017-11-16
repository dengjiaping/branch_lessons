package com.yidiankeyan.science.information.entity;

/**
 * Created by nby on 2016/7/29.
 * 资讯-推荐专辑实体
 */
public class RecommendAlbumBean {

    /**
     * recommendid : 46d54d382c3f4c469fa41a34efffc740
     * albumid : fd52216c0db2498bb5ada13a7d0e7c98
     * name : 我的测试专辑
     * coverimgurl : null
     * shortimgurl : http://f.hiphotos.baidu.com/image/h%3D200/sign=9d661b069dcad1c8cfbbfb274f3e67c4/5bafa40f4bfbfbed064c3d5670f0f736afc31f85.jpg
     * price : null
     * priceunit : null
     * type : 1
     * createtime : 1469093583000
     * userid : 1
     * username : 姚航
     * isfrozen : 0
     * isuse : 0
     * describe : null
     * lastupdatetime : 1469352783000
     * lastupdatetitle : null
     * contentnum : 1
     * size : 2
     * praisenum : null
     * readnum : 0
     * belongtype : 1
     * belongid : 1
     * iteratornum : 1
     * isuserorder : 1
     * updates : 0
     */

    /**
     * 总编推荐ID
     */
    private String recommendid;
    /**
     * 专辑id
     */
    private String albumid;
    /**
     * 专辑名
     */
    private String name;
    /**
     * 封面图地址
     */
    private String coverimgurl;
    /**
     * 封面图缩略图地址
     */
    private String shortimgurl;
    /**
     * 价格
     */
    private Object price;
    /**
     * 购买期间
     */
    private Object priceunit;
    /**
     * 专辑类型
     */
    private int type;
    /**
     * 时间戳
     */
    private long createtime;
    /**
     * 用户id
     */
    private String userid;
    /**
     * 用户名
     */
    private String username;
    /**
     * 是否冻结专题
     */
    private int isfrozen;
    /**
     * 是否可用
     */
    private int isuse;
    /**
     * 专辑描述
     */
    private String describe;
    /**
     * 上次更新时间
     */
    private long lastupdatetime;
    /**
     * 新近更新文章名
     */
    private String lastupdatetitle;
    /**
     * 内容数量
     */
    private int contentnum;
    /**
     * 占用空间大小
     */
    private int size;
    /**
     * 点赞数
     */
    private int praisenum;
    /**
     * 阅读数
     */
    private int readnum;
    /**
     * 从属类型（1）专题 （2）板块
     */
    private int belongtype;
    /**
     * 从属id
     */
    private int belongid;
    /**
     * 更新数
     */
    private int iteratornum;
    /**
     * 用户是否订阅
     */
    private int isuserorder;
    /**
     * 相对用户的更新量（更新数显示用这个值）
     */
    private int updates;
    /**
     * orderid : c8d4ca9016704ae19a8a63ad8bb021a7
     * ordernum : 5
     * istop : 1
     * pushtoptime : 1469692795000
     */

    /**
     * 订阅ID（用户订阅该专辑之后才会有值）
     */
    private String orderid;
    private int ordernum;
    /**
     * 是否置顶
     */
    private int istop;
    /**
     * 置顶时间
     */
    private long pushtoptime;
    private int avatar;

    public String getRecommendid() {
        return recommendid;
    }

    public void setRecommendid(String recommendid) {
        this.recommendid = recommendid;
    }

    public String getAlbumid() {
        return albumid;
    }

    public void setAlbumid(String albumid) {
        this.albumid = albumid;
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

    public String getShortimgurl() {
        return shortimgurl;
    }

    public void setShortimgurl(String shortimgurl) {
        this.shortimgurl = shortimgurl;
    }

    public Object getPrice() {
        return price;
    }

    public void setPrice(Object price) {
        this.price = price;
    }

    public Object getPriceunit() {
        return priceunit;
    }

    public void setPriceunit(Object priceunit) {
        this.priceunit = priceunit;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getCreatetime() {
        return createtime;
    }

    public void setCreatetime(long createtime) {
        this.createtime = createtime;
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

    public int getIsfrozen() {
        return isfrozen;
    }

    public void setIsfrozen(int isfrozen) {
        this.isfrozen = isfrozen;
    }

    public int getIsuse() {
        return isuse;
    }

    public void setIsuse(int isuse) {
        this.isuse = isuse;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public long getLastupdatetime() {
        return lastupdatetime;
    }

    public void setLastupdatetime(long lastupdatetime) {
        this.lastupdatetime = lastupdatetime;
    }

    public String getLastupdatetitle() {
        return lastupdatetitle;
    }

    public void setLastupdatetitle(String lastupdatetitle) {
        this.lastupdatetitle = lastupdatetitle;
    }

    public int getContentnum() {
        return contentnum;
    }

    public void setContentnum(int contentnum) {
        this.contentnum = contentnum;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
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

    public int getBelongtype() {
        return belongtype;
    }

    public void setBelongtype(int belongtype) {
        this.belongtype = belongtype;
    }

    public int getBelongid() {
        return belongid;
    }

    public void setBelongid(int belongid) {
        this.belongid = belongid;
    }

    public int getIteratornum() {
        return iteratornum;
    }

    public void setIteratornum(int iteratornum) {
        this.iteratornum = iteratornum;
    }

    public int getIsuserorder() {
        return isuserorder;
    }

    public void setIsuserorder(int isuserorder) {
        this.isuserorder = isuserorder;
    }

    public int getUpdates() {
        return updates;
    }

    public void setUpdates(int updates) {
        this.updates = updates;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public int getOrdernum() {
        return ordernum;
    }

    public void setOrdernum(int ordernum) {
        this.ordernum = ordernum;
    }

    public int getIstop() {
        return istop;
    }

    public void setIstop(int istop) {
        this.istop = istop;
    }

    public long getPushtoptime() {
        return pushtoptime;
    }

    public void setPushtoptime(long pushtoptime) {
        this.pushtoptime = pushtoptime;
    }

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }

    public RecommendAlbumBean(int avatar, String lastupdatetitle, String name, int type) {
        this.name = name;
        this.avatar = avatar;
        this.type = type;
        this.lastupdatetitle = lastupdatetitle;
    }

    public RecommendAlbumBean() {
    }
}
