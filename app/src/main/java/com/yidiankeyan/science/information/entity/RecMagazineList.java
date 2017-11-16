package com.yidiankeyan.science.information.entity;

/**
 * //  ◥█▄▃▁   ▁▂▂▃▃▂▂▂▂▂▂▂▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▅
 * //.......◥█☆█▅▄▃▁▁▁▁▁▃▄▅▅▅▅▅▅▅▅▅▅▅▅▅▅▄▁█▅●    ...Created by zn on 2017/7/4 0004.
 * //〓▇█████ ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓███▅▄▃███●
 * // 〓〓.๑۩۞۩๑.█████████████████████◤
 * //   ◥█████████◤ ◥        ◢◤
 * //     ◥██████◤        ◥  ◢◤
 * //       ████◤██████◤
 * //       █▓▓▓▓██◤                -推荐
 * //      █▓▓▓██◆                      -杂志列表bean
 * //     █▓▓▓██◆
 * //    █▓▓▓██◆
 * //   █▓▓▓██◆
 * //  █▓▓▓██◆
 * // █▓▓ ██◆
 * //█▓▓ ██◆
 */
public class RecMagazineList {

    /**
     * id : 343041c6247049b2987a31272914004e
     * name : 欢乐颂第二季
     * year : 2017
     * desc : 近期热播大剧《欢乐颂》由东阳正午阳光影视有限公司、山东影视制作有限公司联合出品的都市职场女性剧，由孔笙、简川訸执导，袁子弹编剧，侯鸿亮制片，刘涛、蒋欣、王子文、杨紫、乔欣、王凯、靳东、祖峰等领衔出演。该剧上映以来一直受到广大网友们的热评于关注，转眼间《欢乐颂》已经迎来了大结局，相信很多追剧的朋友们跟我一样的疑问，这样就结局了么？应该还有然后的呀？和小说相比还有好多情节都没演呢
     * createtime : 1496397045000
     * userid : a44fb075dbdc4ccb9f33081d40714065
     * shortimg : /ksyun/2017/06/02/166b48af47274fd2a9534d2e3a0d180e.jpg
     * coverimg : /ksyun/2017/06/02/37722d8c81e549c49717e532681999bb.jpg
     * sequence : 0
     * checkstatus : 2
     * isuse : 1
     */

    private String id;
    private String name;
    private String year;
    private String desc;
    private long createtime;
    private String userid;
    private String shortimg;
    private String coverimg;
    private int sequence;
    private int checkstatus;
    private int isuse;
    private String updatemonthname;

    public String getUpdatemonthname() {
        return updatemonthname;
    }

    public void setUpdatemonthname(String updatemonthname) {
        this.updatemonthname = updatemonthname;
    }

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

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
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
