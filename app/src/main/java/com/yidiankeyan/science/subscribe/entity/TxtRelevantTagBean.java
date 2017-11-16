package com.yidiankeyan.science.subscribe.entity;

/**
 * //  ◥█▄▃▁   ▁▂▂▃▃▂▂▂▂▂▂▂▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▅
 * //.......◥█☆█▅▄▃▁▁▁▁▁▃▄▅▅▅▅▅▅▅▅▅▅▅▅▅▅▄▁█▅●    ...Created by zn on 2017/7/19 0019.
 * //〓▇█████ ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓███▅▄▃███●
 * // 〓〓.๑۩۞۩๑.█████████████████████◤
 * //   ◥█████████◤ ◥        ◢◤
 * //     ◥██████◤        ◥  ◢◤
 * //       ████◤██████◤        -文章
 * //       █▓▓▓▓██◤                -相关标签
 * //      █▓▓▓██◆
 * //     █▓▓▓██◆
 * //    █▓▓▓██◆
 * //   █▓▓▓██◆
 * //  █▓▓▓██◆
 * // █▓▓ ██◆
 * //█▓▓ ██◆
 */
public class TxtRelevantTagBean {

    /**
     * id : 32
     * name : 魅族
     * coverimgurl : /ksyun/2017/07/16/9237ed12c70c4cefadbc317d9ba0d8df.jpg
     * synopsis : null
     * followernum : 0
     * contentnum : 0
     * isFocus : null
     */

    private String id;
    private String name;
    private String coverimgurl;
    private Object synopsis;
    private int followernum;
    private int contentnum;
    private Object isFocus;

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

    public String getCoverimgurl() {
        return coverimgurl;
    }

    public void setCoverimgurl(String coverimgurl) {
        this.coverimgurl = coverimgurl;
    }

    public Object getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(Object synopsis) {
        this.synopsis = synopsis;
    }

    public int getFollowernum() {
        return followernum;
    }

    public void setFollowernum(int followernum) {
        this.followernum = followernum;
    }

    public int getContentnum() {
        return contentnum;
    }

    public void setContentnum(int contentnum) {
        this.contentnum = contentnum;
    }

    public Object getIsFocus() {
        return isFocus;
    }

    public void setIsFocus(Object isFocus) {
        this.isFocus = isFocus;
    }
}
