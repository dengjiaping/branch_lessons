package com.yidiankeyan.science.information.entity;

/**
 * //  ◥█▄▃▁   ▁▂▂▃▃▂▂▂▂▂▂▂▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▅
 * //.......◥█☆█▅▄▃▁▁▁▁▁▃▄▅▅▅▅▅▅▅▅▅▅▅▅▅▅▄▁█▅●    ...Created by Administrator on 2017/4/27 0027.
 * //〓▇█████ ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓███▅▄▃███●
 * // 〓〓.๑۩۞۩๑.█████████████████████◤
 * //   ◥█████████◤ ◥        ◢◤
 * //     ◥██████◤        ◥  ◢◤
 * //       ████◤██████◤
 * //       █▓▓▓▓██◤                    墨子杂志详情
 * //      █▓▓▓██◆
 * //     █▓▓▓██◆
 * //    █▓▓▓██◆
 * //   █▓▓▓██◆
 * //  █▓▓▓██◆
 * // █▓▓ ██◆
 * //█▓▓ ██◆
 */
public class MagazineDetailsBean {

    /**
     * id : 1f775b04cac84ef394eb50dd728703e4
     * name : 环球地理杂志
     * year : 2017
     * desc : it describes seen all over the world sign
     * createtime : 1492569050000
     * userid : -0021
     * shortimg : /static/upload/cmsweb/image/png2016/9/8/48566679f8444f1bbaa28280a9c77a7a.png
     * coverimg : /static/upload/cmsweb/image/png2016/9/8/48566679f8444f1bbaa28280a9c77a7a.png
     * sequence : 2
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
