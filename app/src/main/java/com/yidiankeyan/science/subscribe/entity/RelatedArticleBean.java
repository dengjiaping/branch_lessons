package com.yidiankeyan.science.subscribe.entity;

/**
 * //  ◥█▄▃▁   ▁▂▂▃▃▂▂▂▂▂▂▂▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▅
 * //.......◥█☆█▅▄▃▁▁▁▁▁▃▄▅▅▅▅▅▅▅▅▅▅▅▅▅▅▄▁█▅●    ...Created by zn on 2017/7/19 0019.
 * //〓▇█████ ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓███▅▄▃███●
 * // 〓〓.๑۩۞۩๑.█████████████████████◤
 * //   ◥█████████◤ ◥        ◢◤
 * //     ◥██████◤        ◥  ◢◤
 * //       ████◤██████◤
 * //       █▓▓▓▓██◤           文章详情
 * //      █▓▓▓██◆              -相关阅读
 * //     █▓▓▓██◆
 * //    █▓▓▓██◆
 * //   █▓▓▓██◆
 * //  █▓▓▓██◆
 * // █▓▓ ██◆
 * //█▓▓ ██◆
 */
public class RelatedArticleBean {

    /**
     * id : 91e9a531538c405696fce31093f0b2e7
     * name : 贾跃亭为造车，拉来了宝马“i3电动车之父”；孙宏斌或想甩乐视
     * type : 1
     * coverimgurl : /ksyun/2017/07/19/dfa10c5a95ba46cab12540c357b38181.jpeg
     * covertype : 1
     * imgs : /ksyun/2017/07/19/dfa10c5a95ba46cab12540c357b38181.jpeg
     * createtime : 1500429177000
     * length : 0
     * mozid : 27
     * mozname : 创业邦
     */

    private String id;
    private String name;
    private int type;
    private String coverimgurl;
    private int covertype;
    private String imgs;
    private long createtime;
    private int length;
    private String mozid;
    private String mozname;

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCoverimgurl() {
        return coverimgurl;
    }

    public void setCoverimgurl(String coverimgurl) {
        this.coverimgurl = coverimgurl;
    }

    public int getCovertype() {
        return covertype;
    }

    public void setCovertype(int covertype) {
        this.covertype = covertype;
    }

    public String getImgs() {
        return imgs;
    }

    public void setImgs(String imgs) {
        this.imgs = imgs;
    }

    public long getCreatetime() {
        return createtime;
    }

    public void setCreatetime(long createtime) {
        this.createtime = createtime;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getMozid() {
        return mozid;
    }

    public void setMozid(String mozid) {
        this.mozid = mozid;
    }

    public String getMozname() {
        return mozname;
    }

    public void setMozname(String mozname) {
        this.mozname = mozname;
    }
}
