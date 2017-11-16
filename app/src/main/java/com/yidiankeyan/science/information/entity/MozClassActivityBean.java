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
 * Created by nby on 2016/11/14.
 * 作用：
 */
public class MozClassActivityBean {

    /**
     * id : 59112f11369e4309804bb40c83635f4b
     * flashreportId : 531978787b8b4ecba5fd64c4e61889cf
     * name : 北京赛区
     * coverimgurl : /static/upload/cmsweb/image/png2016/8/12/a8d450834a85438ca98c40d418b71384.png
     * begintime : 1477805400000
     * endtime : 1477902600000
     * description : 北京人不会
     * sort : 1
     * checkstatus : 2
     * isavailable : 1
     * actstatus : 2
     */

    private List<ActivityListBean> activityList;
    /**
     * id : 234134wsdfw323442342
     * title : 关于竞赛的投票
     * description : 比赛描述
     * type : 2
     * refid : 59112f11369e4309804bb40c83635f4b
     * coverimgurl : /static/upload/cmsweb/image/png2016/8/15/1569ef15d6ea44269b838b401904ee30.png
     * starttime : 1477805400000
     * endtime : 1477805400000
     * iscanvote : 1
     * actstatus : 1
     * checkstatus : 2
     * isavailable : 1
     */

    private List<VoteListBean> voteList;
    /**
     * id : 59112f11369e4309804bb40c8363eeeb
     * name : 关于数学竞赛的投票后的一个抽奖活动
     * desc : 这是一个伟大的抽奖活动
     * type : 1
     * refid : 59112f11369e4309805bb40c83635f5b
     * starttime :
     * endtime :
     * isuse : 1
     */

    private List<LotteryListBean> lotteryList;

    public List<ActivityListBean> getActivityList() {
        return activityList;
    }

    public void setActivityList(List<ActivityListBean> activityList) {
        this.activityList = activityList;
    }

    public List<VoteListBean> getVoteList() {
        return voteList;
    }

    public void setVoteList(List<VoteListBean> voteList) {
        this.voteList = voteList;
    }

    public List<LotteryListBean> getLotteryList() {
        return lotteryList;
    }

    public void setLotteryList(List<LotteryListBean> lotteryList) {
        this.lotteryList = lotteryList;
    }

    public static class ActivityListBean {
        private String id;
        private String flashreportId;
        private String name;
        private String coverimgurl;
        private long begintime;
        private long endtime;
        private String description;
        private int sort;
        private int checkstatus;
        private int isavailable;
        private int actstatus;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getFlashreportId() {
            return flashreportId;
        }

        public void setFlashreportId(String flashreportId) {
            this.flashreportId = flashreportId;
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

        public long getBegintime() {
            return begintime;
        }

        public void setBegintime(long begintime) {
            this.begintime = begintime;
        }

        public long getEndtime() {
            return endtime;
        }

        public void setEndtime(long endtime) {
            this.endtime = endtime;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
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

    public static class VoteListBean {

        private String id;
        private String title;
        private String description;
        private int type;
        private String refid;
        private String coverimgurl;
        private long starttime;
        private long endtime;
        private int iscanvote;
        private int actstatus;
        private int checkstatus;
        private int isavailable;

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

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getRefid() {
            return refid;
        }

        public void setRefid(String refid) {
            this.refid = refid;
        }

        public String getCoverimgurl() {
            return coverimgurl;
        }

        public void setCoverimgurl(String coverimgurl) {
            this.coverimgurl = coverimgurl;
        }

        public long getStarttime() {
            return starttime;
        }

        public void setStarttime(long starttime) {
            this.starttime = starttime;
        }

        public long getEndtime() {
            return endtime;
        }

        public void setEndtime(long endtime) {
            this.endtime = endtime;
        }

        public int getIscanvote() {
            return iscanvote;
        }

        public void setIscanvote(int iscanvote) {
            this.iscanvote = iscanvote;
        }

        public int getActstatus() {
            return actstatus;
        }

        public void setActstatus(int actstatus) {
            this.actstatus = actstatus;
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
    }

    public static class LotteryListBean {

        /**
         * id : 00f29d0bb6ec467d82084559a51124d5
         * name : 活动抽奖
         * imgurl : /static/upload/cmsweb/image/jpeg2016/11/18/8217ffbda5284b76af32fd80b7d8d2b7.jpg
         * starttime : 1479457812000
         * endtime : 1480494614000
         * desc :
         * type : 1
         * qfctype : 1
         * qfcrefid : bd5e2735aad847f8ac98723ea0a84c28
         * reftype : 1
         * refid : 959d1559ac8f4bb6aff24e65065fce48
         */

        private String id;
        private String name;
        private String imgurl;
        private long starttime;
        private long endtime;
        private String desc;
        private int type;
        private int qfctype;
        private String qfcrefid;
        private int reftype;
        private String refid;

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

        public String getImgurl() {
            return imgurl;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }

        public long getStarttime() {
            return starttime;
        }

        public void setStarttime(long starttime) {
            this.starttime = starttime;
        }

        public long getEndtime() {
            return endtime;
        }

        public void setEndtime(long endtime) {
            this.endtime = endtime;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getQfctype() {
            return qfctype;
        }

        public void setQfctype(int qfctype) {
            this.qfctype = qfctype;
        }

        public String getQfcrefid() {
            return qfcrefid;
        }

        public void setQfcrefid(String qfcrefid) {
            this.qfcrefid = qfcrefid;
        }

        public int getReftype() {
            return reftype;
        }

        public void setReftype(int reftype) {
            this.reftype = reftype;
        }

        public String getRefid() {
            return refid;
        }

        public void setRefid(String refid) {
            this.refid = refid;
        }
    }
}
