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
 * Created by nby on 2016/11/9.
 * 作用：快报bean
 */
public class MozActivityBean {

    /**
     * totalNumber : 2
     * listData : [{"id":"531978787b8b4ecba5fd64c4e61889cf","title":"数学建模大赛","surfaceploturl":"/static/upload/cmsweb/image/png2016/9/3/3c197d20ec5c4b4da5c5e7b8e23d3525.png","starttime":1477980000000,"expiretime":1480485600000,"createtime":1478504182000,"description":"","creatorid":"6504704244b14e7393f8bbdb7b2982dd","checkstatus":2,"isavailable":1,"actstatus":1},{"id":"d300df2ff59940389e3a97b52f6a0f11","title":"sa","surfaceploturl":"/static/upload/cmsweb/image/jpeg2016/11/7/f5d158f7a05042b28644fe4bfbf1ec92.jpg","starttime":1477929600000,"expiretime":1478102400000,"createtime":1478487515000,"description":"cvbb","creatorid":"65814b06bdc94a50a3fb20365b696265","checkstatus":2,"isavailable":1,"actstatus":2}]
     */

    private int totalNumber;
    /**
     * id : 531978787b8b4ecba5fd64c4e61889cf
     * title : 数学建模大赛
     * surfaceploturl : /static/upload/cmsweb/image/png2016/9/3/3c197d20ec5c4b4da5c5e7b8e23d3525.png
     * starttime : 1477980000000
     * expiretime : 1480485600000
     * createtime : 1478504182000
     * description :
     * creatorid : 6504704244b14e7393f8bbdb7b2982dd
     * checkstatus : 2
     * isavailable : 1
     * actstatus : 1
     */

    private List<ListDataBean> listData;

    public int getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(int totalNumber) {
        this.totalNumber = totalNumber;
    }

    public List<ListDataBean> getListData() {
        return listData;
    }

    public void setListData(List<ListDataBean> listData) {
        this.listData = listData;
    }

    public static class ListDataBean {
        private String id;
        private String title;
        private String surfaceploturl;
        private long starttime;
        private long expiretime;
        private long createtime;
        private String description;
        private String creatorid;
        private int checkstatus;
        private int isavailable;
        private int actstatus;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSurfaceploturl() {
            return surfaceploturl;
        }

        public void setSurfaceploturl(String surfaceploturl) {
            this.surfaceploturl = surfaceploturl;
        }

        public long getStarttime() {
            return starttime;
        }

        public void setStarttime(long starttime) {
            this.starttime = starttime;
        }

        public long getExpiretime() {
            return expiretime;
        }

        public void setExpiretime(long expiretime) {
            this.expiretime = expiretime;
        }

        public long getCreatetime() {
            return createtime;
        }

        public void setCreatetime(long createtime) {
            this.createtime = createtime;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getCreatorid() {
            return creatorid;
        }

        public void setCreatorid(String creatorid) {
            this.creatorid = creatorid;
        }

        public int getCheckstatus() {
            return checkstatus;
        }

        public void setCheckstatus(int checkstatus) {
            this.checkstatus = checkstatus;
        }

        public int getIsavailable() {
            return isavailable;
        }

        public void setIsavailable(int isavailable) {
            this.isavailable = isavailable;
        }

        public int getActstatus() {
            return actstatus;
        }

        public void setActstatus(int actstatus) {
            this.actstatus = actstatus;
        }
    }
}
