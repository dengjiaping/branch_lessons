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
 * //      █▓▓▓██◆                  最新专栏
 * //     █▓▓▓██◆
 * //    █▓▓▓██◆
 * //   █▓▓▓██◆
 * //  █▓▓▓██◆
 * // █▓▓ ██◆
 * //█▓▓ ██◆
 */
public class RecentColumn {
    /**
     * id : 22
     * userid : e8c8438701c445dba427628e2652e195
     * coverimg : http://img.zcool.cn/community/015d1d56d3ba296ac7252ce6d98e21.jpg
     * name : 隔壁老王的专栏扩大版
     * price : 0.01
     * createtime : 1480556512000
     * summary : 隔壁老王的专栏扩大版
     * learnersinfo : 隔壁村寡妇
     * description : 隔壁老王的专栏扩大版隔壁老王的专栏扩大版
     * isuse : 1
     * checkstatus : 2
     * username : 弓长中和
     * lastupdatetime : 1481183637000
     * lastupdatetitle : 名字
     * purcharsings : 3
     * permission : 1
     * userImgUrl : /app/71689296/jpg/2016/11/04/0cc7eded5b094fb787a72b7ed4185776.jpg
     * s2CIssueModels : [{"id":"333","vol":6,"name":"名字","coverimg":"/static/upload/cmsweb/image/jpeg2016/11/3/a1dc5af5012448f0a06d484647519f86.jpg","title":"标题","freeable":0,"audiourl":"/static/upload/cmsweb/audio/mp32016/9/22/f354626858994f8c8f48c2795aaaa7f0.mp3","audiolenth":99,"createtime":1481183637000,"columnid":"22","isuse":1,"checkstatus":2,"price":0,"userid":"e8c8438701c445dba427628e2652e195","username":"弓长中和","userimgurl":"/app/71689296/jpg/2016/11/04/0cc7eded5b094fb787a72b7ed4185776.jpg","readnum":0}]
     * updates : 1
     */

    private String id;
    private String userid;
    private String coverimg;
    private String name;
    private double price;
    private long createtime;
    private String summary;
    private String learnersinfo;
    private String description;
    private int isuse;
    private int checkstatus;
    private String username;
    private long lastupdatetime;
    private String lastupdatetitle;
    private int purcharsings;
    private int permission;
    private String userImgUrl;
    private int updates;
    /**
     * id : 333
     * vol : 6
     * name : 名字
     * coverimg : /static/upload/cmsweb/image/jpeg2016/11/3/a1dc5af5012448f0a06d484647519f86.jpg
     * title : 标题
     * freeable : 0
     * audiourl : /static/upload/cmsweb/audio/mp32016/9/22/f354626858994f8c8f48c2795aaaa7f0.mp3
     * audiolenth : 99
     * createtime : 1481183637000
     * columnid : 22
     * isuse : 1
     * checkstatus : 2
     * price : 0
     * userid : e8c8438701c445dba427628e2652e195
     * username : 弓长中和
     * userimgurl : /app/71689296/jpg/2016/11/04/0cc7eded5b094fb787a72b7ed4185776.jpg
     * readnum : 0
     */

    private List<S2CIssueModelsBean> s2CIssueModels;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getCoverimg() {
        return coverimg;
    }

    public void setCoverimg(String coverimg) {
        this.coverimg = coverimg;
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

    public void setPrice(double price) {
        this.price = price;
    }

    public long getCreatetime() {
        return createtime;
    }

    public void setCreatetime(long createtime) {
        this.createtime = createtime;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getLearnersinfo() {
        return learnersinfo;
    }

    public void setLearnersinfo(String learnersinfo) {
        this.learnersinfo = learnersinfo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIsuse() {
        return isuse;
    }

    public void setIsuse(int isuse) {
        this.isuse = isuse;
    }

    public int getCheckstatus() {
        return checkstatus;
    }

    public void setCheckstatus(int checkstatus) {
        this.checkstatus = checkstatus;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public int getPurcharsings() {
        return purcharsings;
    }

    public void setPurcharsings(int purcharsings) {
        this.purcharsings = purcharsings;
    }

    public int getPermission() {
        return permission;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }

    public String getUserImgUrl() {
        return userImgUrl;
    }

    public void setUserImgUrl(String userImgUrl) {
        this.userImgUrl = userImgUrl;
    }

    public int getUpdates() {
        return updates;
    }

    public void setUpdates(int updates) {
        this.updates = updates;
    }

    public List<S2CIssueModelsBean> getS2CIssueModels() {
        return s2CIssueModels;
    }

    public void setS2CIssueModels(List<S2CIssueModelsBean> s2CIssueModels) {
        this.s2CIssueModels = s2CIssueModels;
    }

    public static class S2CIssueModelsBean {
        private String id;
        private int vol;
        private String name;
        private String coverimg;
        private String title;
        private int freeable;
        private String audiourl;
        private int audiolenth;
        private long createtime;
        private String columnid;
        private int isuse;
        private int checkstatus;
        private int price;
        private String userid;
        private String username;
        private String userimgurl;
        private int readnum;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getVol() {
            return vol;
        }

        public void setVol(int vol) {
            this.vol = vol;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCoverimg() {
            return coverimg;
        }

        public void setCoverimg(String coverimg) {
            this.coverimg = coverimg;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getFreeable() {
            return freeable;
        }

        public void setFreeable(int freeable) {
            this.freeable = freeable;
        }

        public String getAudiourl() {
            return audiourl;
        }

        public void setAudiourl(String audiourl) {
            this.audiourl = audiourl;
        }

        public int getAudiolenth() {
            return audiolenth;
        }

        public void setAudiolenth(int audiolenth) {
            this.audiolenth = audiolenth;
        }

        public long getCreatetime() {
            return createtime;
        }

        public void setCreatetime(long createtime) {
            this.createtime = createtime;
        }

        public String getColumnid() {
            return columnid;
        }

        public void setColumnid(String columnid) {
            this.columnid = columnid;
        }

        public int getIsuse() {
            return isuse;
        }

        public void setIsuse(int isuse) {
            this.isuse = isuse;
        }

        public int getCheckstatus() {
            return checkstatus;
        }

        public void setCheckstatus(int checkstatus) {
            this.checkstatus = checkstatus;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
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

        public String getUserimgurl() {
            return userimgurl;
        }

        public void setUserimgurl(String userimgurl) {
            this.userimgurl = userimgurl;
        }

        public int getReadnum() {
            return readnum;
        }

        public void setReadnum(int readnum) {
            this.readnum = readnum;
        }
    }
}
