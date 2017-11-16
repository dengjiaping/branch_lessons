package com.yidiankeyan.science.information.entity;

import java.util.List;

/**
 * ////////////////////////////////////////////////////////////////////
 * //                          _ooOoo_                               //
 * //                         o8888888o                              //
 * //                         88" . "88                              //
 * //                         (| ^_^ |)                              //
 * //                         O\  =  /O                              //
 * //                      ____/`---'\____                           //
 * //                    .'  \\|     |//  `.                         //
 * //                   /  \\|||  :  |||//  \                        //
 * //                  /  _||||| -:- |||||-  \                       //
 * //                  |   | \\\  -  /// |   |                       //
 * //                  | \_|  ''\---/''  |   |                       //
 * //                  \  .-\__  `-`  ___/-. /                       //
 * //                ___`. .'  /--.--\  `. . ___                     //
 * //              ."" '<  `.___\_<|>_/___.'  >'"".                  //
 * //            | | :  `- \`.;`\ _ /`;.`/ - ` : | |                 //
 * //            \  \ `-.   \_ __\ /__ _/   .-` /  /                 //
 * //      ========`-.____`-.___\_____/___.-`____.-'========         //
 * //                           `=---='                              //
 * //      ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^        //
 * //             佛祖保佑      永无BUG     永不修改                  //
 * ////////////////////////////////////////////////////////////////////
 * Created by nby on 2016/12/1.
 * 作用：专栏下期刊列表
 */

public class IssuesForColumnBean {

    /**
     * id : 81dcdb9634104822b9da9919cc9adccf
     * coverimg : 1.PNG
     * name : 测试1
     * s2CIssueModels : [{"vol":2,"name":"测试","title":"title","createtime":1480488209000,"price":0,"readnum":0}]
     * vols : 1
     */
    private String poster;
    private String id;
    private String coverimg;
    private String name;
    private int vols;

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    /**
     * vol : 2
     * name : 测试
     * title : title
     * createtime : 1480488209000
     * price : 0
     * readnum : 0
     */

    private List<S2CIssueModelsBean> s2CIssueModels;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public int getVols() {
        return vols;
    }

    public void setVols(int vols) {
        this.vols = vols;
    }

    public List<S2CIssueModelsBean> getS2CIssueModels() {
        return s2CIssueModels;
    }

    public void setS2CIssueModels(List<S2CIssueModelsBean> s2CIssueModels) {
        this.s2CIssueModels = s2CIssueModels;
    }

    public static class S2CIssueModelsBean {
        private int vol;
        private String name;
        private String title;
        private long createtime;
        private int price;
        private int readnum;
        /**
         * id : 3a39408ad8ca4d23bb5a6268b6dc57a8
         * coverimg : coverimg
         * permission : 1
         */

        private String id;
        private String coverimg;
        private int permission;
        /**
         * checkstatus : 2
         * everRead : 1
         */

        private int checkstatus;
        private int everRead;
        /**
         * updateitem : 0
         */

        private int updateitem;

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

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public long getCreatetime() {
            return createtime;
        }

        public void setCreatetime(long createtime) {
            this.createtime = createtime;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public int getReadnum() {
            return readnum;
        }

        public void setReadnum(int readnum) {
            this.readnum = readnum;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCoverimg() {
            return coverimg;
        }

        public void setCoverimg(String coverimg) {
            this.coverimg = coverimg;
        }

        public int getPermission() {
            return permission;
        }

        public void setPermission(int permission) {
            this.permission = permission;
        }

        public int getCheckstatus() {
            return checkstatus;
        }

        public void setCheckstatus(int checkstatus) {
            this.checkstatus = checkstatus;
        }

        public int getEverRead() {
            return everRead;
        }

        public void setEverRead(int everRead) {
            this.everRead = everRead;
        }

        public int getUpdateitem() {
            return updateitem;
        }

        public void setUpdateitem(int updateitem) {
            this.updateitem = updateitem;
        }
    }
}
