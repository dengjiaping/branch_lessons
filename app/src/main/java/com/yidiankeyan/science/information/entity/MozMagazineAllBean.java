package com.yidiankeyan.science.information.entity;

/**
 * //  ◥█▄▃▁   ▁▂▂▃▃▂▂▂▂▂▂▂▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▅
 * //.......◥█☆█▅▄▃▁▁▁▁▁▃▄▅▅▅▅▅▅▅▅▅▅▅▅▅▅▄▁█▅●    ...Created by Administrator on 2017/4/24 0024.
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
public class MozMagazineAllBean {

    /**
     * monthlyDB : {"id":"12121ssads12dd","magazineId":"2ea662e6f9e9477d9e6a5bc344c40abd","magazineName":null,"name":"科技视野一月刊","interpreter":"赵大帅","author":"杰·休克曼","shortimg":"/static/upload/cmsweb/image/png2016/8/12/b4194479105f4a51a15fbe5648ba6d8e.png","coverimg":"/static/upload/cmsweb/image/png2016/8/16/682818753b8548f7aadb0ebf91cfcaf1.png","userid":"-0012","price":99,"createtime":1493203629000,"month":"2","desc":"探索古代火药研制之谜","length":699,"audiourl":null,"checkstatus":2,"isuse":1}
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
         * id : 12121ssads12dd
         * magazineId : 2ea662e6f9e9477d9e6a5bc344c40abd
         * magazineName : null
         * name : 科技视野一月刊
         * interpreter : 赵大帅
         * author : 杰·休克曼
         * shortimg : /static/upload/cmsweb/image/png2016/8/12/b4194479105f4a51a15fbe5648ba6d8e.png
         * coverimg : /static/upload/cmsweb/image/png2016/8/16/682818753b8548f7aadb0ebf91cfcaf1.png
         * userid : -0012
         * price : 99
         * createtime : 1493203629000
         * month : 2
         * desc : 探索古代火药研制之谜
         * length : 699
         * audiourl : null
         * checkstatus : 2
         * isuse : 1
         */

        private String id;
        private String magazineId;
        private String magazineName;
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

        public String getMagazineName() {
            return magazineName;
        }

        public void setMagazineName(String magazineName) {
            this.magazineName = magazineName;
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
