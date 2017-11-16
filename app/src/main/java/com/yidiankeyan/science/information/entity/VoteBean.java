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
 * Created by nby on 2016/11/15.
 * 作用：
 */
public class VoteBean {

    /**
     * id : e9823010906d11e69b0bf48e388ad43a
     * refid : 59112f11369e4309804bb40c83635f4b
     * lotteryid : 111
     * name : 北京赛区
     * coverimgurl : /static/upload/cmsweb/image/png2016/8/12/a8d450834a85438ca98c40d418b71384.png
     * description : 北京人不会
     * iscanvote : 1
     * actstatus : 1
     */

    private InfoBean info;
    /**
     * pages : 1
     * pagesize : 10
     * records : 3
     * pagetotal : 1
     */

    private PageInfoBean pageInfo;
    /**
     * voteid : e9823010906d11e69b0bf48e388ad43a
     * refid : 59112f11369e4309804bb40c83635f4b
     * enrolid : 06c2d634650348cb84cc9ebf63f2bdf6
     * name : 北京队
     * teamnumber : 一号队
     * coverimgurl : /static/upload/cmsweb/image/jpeg2016/11/3/ed9c29dc66c64c21989158db21bb943f.jpg
     * sort : 1
     * score : 666
     * description : 北京队的描述
     * totalnum : 17
     * ranknum : 1
     */

    private List<PartsBean> parts;

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public PageInfoBean getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfoBean pageInfo) {
        this.pageInfo = pageInfo;
    }

    public List<PartsBean> getParts() {
        return parts;
    }

    public void setParts(List<PartsBean> parts) {
        this.parts = parts;
    }

    public static class InfoBean {
        private String id;
        private String refid;
        private String lotteryid;
        private String name;
        private String coverimgurl;
        private String description;
        private int iscanvote;
        private int actstatus;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getRefid() {
            return refid;
        }

        public void setRefid(String refid) {
            this.refid = refid;
        }

        public String getLotteryid() {
            return lotteryid;
        }

        public void setLotteryid(String lotteryid) {
            this.lotteryid = lotteryid;
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

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
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
    }

    public static class PageInfoBean {
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

    public static class PartsBean {
        private String voteid;
        private String refid;
        private String enrolid;
        private String name;
        private String teamnumber;
        private String coverimgurl;
        private int sort;
        private int score;
        private String description;
        private int totalnum;
        private int ranknum;

        public String getVoteid() {
            return voteid;
        }

        public void setVoteid(String voteid) {
            this.voteid = voteid;
        }

        public String getRefid() {
            return refid;
        }

        public void setRefid(String refid) {
            this.refid = refid;
        }

        public String getEnrolid() {
            return enrolid;
        }

        public void setEnrolid(String enrolid) {
            this.enrolid = enrolid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTeamnumber() {
            return teamnumber;
        }

        public void setTeamnumber(String teamnumber) {
            this.teamnumber = teamnumber;
        }

        public String getCoverimgurl() {
            return coverimgurl;
        }

        public void setCoverimgurl(String coverimgurl) {
            this.coverimgurl = coverimgurl;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getTotalnum() {
            return totalnum;
        }

        public void setTotalnum(int totalnum) {
            this.totalnum = totalnum;
        }

        public int getRanknum() {
            return ranknum;
        }

        public void setRanknum(int ranknum) {
            this.ranknum = ranknum;
        }
    }
}
