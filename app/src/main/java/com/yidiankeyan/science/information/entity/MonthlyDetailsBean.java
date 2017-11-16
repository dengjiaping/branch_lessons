package com.yidiankeyan.science.information.entity;

import java.util.List;

/**
 * //  ◥█▄▃▁   ▁▂▂▃▃▂▂▂▂▂▂▂▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▅
 * //.......◥█☆█▅▄▃▁▁▁▁▁▃▄▅▅▅▅▅▅▅▅▅▅▅▅▅▅▄▁█▅●    ...Created by zn on 2017/4/26 0026.
 * //〓▇█████ ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓███▅▄▃███●
 * // 〓〓.๑۩۞۩๑.█████████████████████◤
 * //   ◥█████████◤ ◥        ◢◤
 * //     ◥██████◤        ◥  ◢◤
 * //       ████◤██████◤                月刊详情
 * //       █▓▓▓▓██◤
 * //      █▓▓▓██◆
 * //     █▓▓▓██◆
 * //    █▓▓▓██◆
 * //   █▓▓▓██◆
 * //  █▓▓▓██◆
 * // █▓▓ ██◆
 * //█▓▓ ██◆
 */
public class MonthlyDetailsBean {


    /**
     * monthlyDB : {"id":"d887a61a90af4247b6be9d256e4a8583","magazineId":"1f775b04cac84ef394eb50dd728703e4","name":"环球地理五月刊","interpreter":"Jams","author":"Tomthon","shortimg":"/static/upload/cmsweb/image/png2016/9/8/48566679f8444f1bbaa28280a9c77a7a.png","coverimg":"/static/upload/cmsweb/image/jpeg2016/11/2/7d1b8d7f7ea04008a37637b98cc02ff2.jpg","userid":"-0012","price":29,"createtime":1493024972000,"month":"5","desc":"This is a fantastic Monthly","length":222,"audiourl":null,"checkstatus":0,"isuse":1}
     * others : [{"id":"4530255ef04c4d1a8e7ea4d753ba8330","magazineId":"1f775b04cac84ef394eb50dd728703e4","name":"环球地理四月刊","interpreter":"Jams","author":"Tomthon","shortimg":"/static/upload/cmsweb/image/png2016/9/8/48566679f8444f1bbaa28280a9c77a7a.png","coverimg":"/static/upload/cmsweb/image/png2016/9/27/cbef89f04859474fa4169dc58faffb89.png","userid":"-0012","price":29,"createtime":1493024971000,"month":"4","desc":"This is a fantastic Monthly","length":222,"audiourl":"/static/upload/cmsweb/audio/mpeg2016/11/29/dc4370e2969d40f3a3677804a95c5e8e.mp3","checkstatus":0,"isuse":1},{"id":"81cdaa0c8e864026a4710ecccba6f885","magazineId":"1f775b04cac84ef394eb50dd728703e4","name":"环球地理一月刊","interpreter":"Jams","author":"Tomthon","shortimg":"/static/upload/cmsweb/image/png2016/9/8/48566679f8444f1bbaa28280a9c77a7a.png","coverimg":"/static/upload/cmsweb/image/jpeg2016/10/29/77d164a6ced046d582c61aa2e19a2a90.jpg","userid":"-0012","price":29,"createtime":1493024142000,"month":"1","desc":"This is a fantastic Monthly","length":222,"audiourl":"/static/upload/cmsweb/audio/mpeg2016/11/29/dc4370e2969d40f3a3677804a95c5e8e.mp3","checkstatus":0,"isuse":1},{"id":"a08505b020124276a645713b35a30c4c","magazineId":"1f775b04cac84ef394eb50dd728703e4","name":"环球地理三月刊","interpreter":"Jams","author":"Tomthon","shortimg":"/static/upload/cmsweb/image/png2016/9/8/48566679f8444f1bbaa28280a9c77a7a.png","coverimg":"/static/upload/cmsweb/image/png2016/8/16/65355572fae146f2ba1ea0045c384848.png","userid":"-0012","price":29,"createtime":1493024801000,"month":"3","desc":"This is a fantastic Monthly","length":222,"audiourl":"/static/upload/cmsweb/audio/mpeg2016/11/29/dc4370e2969d40f3a3677804a95c5e8e.mp3","checkstatus":0,"isuse":1}]
     * permission : 0
     */

    private MonthlyDBBean monthlyDB;
    private int permission;
    private List<OthersBean> others;

    public MonthlyDBBean getMonthlyDB() {
        return monthlyDB;
    }

    public void setMonthlyDB(MonthlyDBBean monthlyDB) {
        this.monthlyDB = monthlyDB;
    }

    public int getPermission() {
        return permission;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }

    public List<OthersBean> getOthers() {
        return others;
    }

    public void setOthers(List<OthersBean> others) {
        this.others = others;
    }

    public static class MonthlyDBBean {
        /**
         * id : d887a61a90af4247b6be9d256e4a8583
         * magazineId : 1f775b04cac84ef394eb50dd728703e4
         * name : 环球地理五月刊
         * interpreter : Jams
         * author : Tomthon
         * shortimg : /static/upload/cmsweb/image/png2016/9/8/48566679f8444f1bbaa28280a9c77a7a.png
         * coverimg : /static/upload/cmsweb/image/jpeg2016/11/2/7d1b8d7f7ea04008a37637b98cc02ff2.jpg
         * userid : -0012
         * price : 29
         * createtime : 1493024972000
         * month : 5
         * desc : This is a fantastic Monthly
         * length : 222
         * audiourl : null
         * checkstatus : 0
         * isuse : 1
         */

        private String id;
        private String magazineId;
        private String name;
        private String magazineName;
        private String interpreter;
        private String author;
        private String shortimg;
        private String coverimg;
        private String userid;
        private double price;
        private long createtime;
        private String month;
        private String desc;
        private int length;
        private String audiourl;
        private int checkstatus;
        private int isuse;
        private String filePath;
        private int downloadState;
        private String content;
        private double space;

        public double getSpace() {
            return space;
        }

        public void setSpace(double space) {
            this.space = space;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getMagazineName() {
            return magazineName;
        }

        public void setMagazineName(String magazineName) {
            this.magazineName = magazineName;
        }

        public int getDownloadState() {
            return downloadState;
        }

        public void setDownloadState(int downloadState) {
            this.downloadState = downloadState;
        }

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMagazineId() {
            return magazineId;
        }

        public void setMagazineId(String magazineId) {
            this.magazineId = magazineId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getInterpreter() {
            return interpreter;
        }

        public void setInterpreter(String interpreter) {
            this.interpreter = interpreter;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getShortimg() {
            return shortimg;
        }

        public void setShortimg(String shortimg) {
            this.shortimg = shortimg;
        }

        public String getCoverimg() {
            return coverimg;
        }

        public void setCoverimg(String coverimg) {
            this.coverimg = coverimg;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
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

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public int getLength() {
            return length;
        }

        public void setLength(int length) {
            this.length = length;
        }

        public String getAudiourl() {
            return audiourl;
        }

        public void setAudiourl(String audiourl) {
            this.audiourl = audiourl;
        }

        public int getCheckstatus() {
            return checkstatus;
        }

        public void setCheckstatus(int checkstatus) {
            this.checkstatus = checkstatus;
        }

        public int getIsuse() {
            return isuse;
        }

        public void setIsuse(int isuse) {
            this.isuse = isuse;
        }
    }

    public static class OthersBean {
        /**
         * id : 4530255ef04c4d1a8e7ea4d753ba8330
         * magazineId : 1f775b04cac84ef394eb50dd728703e4
         * name : 环球地理四月刊
         * interpreter : Jams
         * author : Tomthon
         * shortimg : /static/upload/cmsweb/image/png2016/9/8/48566679f8444f1bbaa28280a9c77a7a.png
         * coverimg : /static/upload/cmsweb/image/png2016/9/27/cbef89f04859474fa4169dc58faffb89.png
         * userid : -0012
         * price : 29
         * createtime : 1493024971000
         * month : 4
         * desc : This is a fantastic Monthly
         * length : 222
         * audiourl : /static/upload/cmsweb/audio/mpeg2016/11/29/dc4370e2969d40f3a3677804a95c5e8e.mp3
         * checkstatus : 0
         * isuse : 1
         */

        private String id;
        private String magazineId;
        private String name;
        private String interpreter;
        private String author;
        private String shortimg;
        private String coverimg;
        private String userid;
        private double price;
        private long createtime;
        private String month;
        private String desc;
        private int length;
        private String audiourl;
        private int checkstatus;
        private int isuse;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMagazineId() {
            return magazineId;
        }

        public void setMagazineId(String magazineId) {
            this.magazineId = magazineId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getInterpreter() {
            return interpreter;
        }

        public void setInterpreter(String interpreter) {
            this.interpreter = interpreter;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getShortimg() {
            return shortimg;
        }

        public void setShortimg(String shortimg) {
            this.shortimg = shortimg;
        }

        public String getCoverimg() {
            return coverimg;
        }

        public void setCoverimg(String coverimg) {
            this.coverimg = coverimg;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
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

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public int getLength() {
            return length;
        }

        public void setLength(int length) {
            this.length = length;
        }

        public String getAudiourl() {
            return audiourl;
        }

        public void setAudiourl(String audiourl) {
            this.audiourl = audiourl;
        }

        public int getCheckstatus() {
            return checkstatus;
        }

        public void setCheckstatus(int checkstatus) {
            this.checkstatus = checkstatus;
        }

        public int getIsuse() {
            return isuse;
        }

        public void setIsuse(int isuse) {
            this.isuse = isuse;
        }
    }
}
