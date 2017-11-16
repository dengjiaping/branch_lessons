package com.yidiankeyan.science.information.entity;

/**
 * //  ◥█▄▃▁   ▁▂▂▃▃▂▂▂▂▂▂▂▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▅
 * //.......◥█☆█▅▄▃▁▁▁▁▁▃▄▅▅▅▅▅▅▅▅▅▅▅▅▅▅▄▁█▅●    ...Created by Administrator on 2017/4/27 0027.
 * //〓▇█████ ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓███▅▄▃███●
 * // 〓〓.๑۩۞۩๑.█████████████████████◤
 * //   ◥█████████◤ ◥        ◢◤
 * //     ◥██████◤        ◥  ◢◤
 * //       ████◤██████◤                月刊系列列表
 * //       █▓▓▓▓██◤
 * //      █▓▓▓██◆
 * //     █▓▓▓██◆
 * //    █▓▓▓██◆
 * //   █▓▓▓██◆
 * //  █▓▓▓██◆
 * // █▓▓ ██◆
 * //█▓▓ ██◆
 */
public class MonthlySeriesBean {

    /**
     * monthlyDB : {"id":"81cdaa0c8e864026a4710ecccba6f885","magazineId":"1f775b04cac84ef394eb50dd728703e4","name":"环球地理一月刊","interpreter":"Jams","author":"Tomthon","shortimg":"/static/upload/cmsweb/image/png2016/9/8/48566679f8444f1bbaa28280a9c77a7a.png","coverimg":"/static/upload/cmsweb/image/jpeg2016/10/29/77d164a6ced046d582c61aa2e19a2a90.jpg","userid":"-0012","price":29,"createtime":1493024142000,"month":"1","desc":"This is a fantastic Monthly","length":222,"audiourl":null,"checkstatus":2,"isuse":1}
     * permission : 0
     */

    private MonthlyDBBean monthlyDB;
    private int permission;

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

    public static class MonthlyDBBean {
        /**
         * id : 81cdaa0c8e864026a4710ecccba6f885
         * magazineId : 1f775b04cac84ef394eb50dd728703e4
         * name : 环球地理一月刊
         * interpreter : Jams
         * author : Tomthon
         * shortimg : /static/upload/cmsweb/image/png2016/9/8/48566679f8444f1bbaa28280a9c77a7a.png
         * coverimg : /static/upload/cmsweb/image/jpeg2016/10/29/77d164a6ced046d582c61aa2e19a2a90.jpg
         * userid : -0012
         * price : 29
         * createtime : 1493024142000
         * month : 1
         * desc : This is a fantastic Monthly
         * length : 222
         * audiourl : null
         * checkstatus : 2
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
        private Object audiourl;
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

        public Object getAudiourl() {
            return audiourl;
        }

        public void setAudiourl(Object audiourl) {
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
