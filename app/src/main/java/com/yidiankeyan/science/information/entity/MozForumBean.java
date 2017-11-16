package com.yidiankeyan.science.information.entity;

/**
 * //  ◥█▄▃▁   ▁▂▂▃▃▂▂▂▂▂▂▂▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▅
 * //.......◥█☆█▅▄▃▁▁▁▁▁▃▄▅▅▅▅▅▅▅▅▅▅▅▅▅▅▄▁█▅●    ...Created by zn on 2017/7/11 0011.
 * //〓▇█████ ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓███▅▄▃███●
 * // 〓〓.๑۩۞۩๑.█████████████████████◤
 * //   ◥█████████◤ ◥        ◢◤
 * //     ◥██████◤        ◥  ◢◤
 * //       ████◤██████◤
 * //       █▓▓▓▓██◤                        --墨子论坛bean
 * //      █▓▓▓██◆
 * //     █▓▓▓██◆
 * //    █▓▓▓██◆
 * //   █▓▓▓██◆
 * //  █▓▓▓██◆
 * // █▓▓ ██◆
 * //█▓▓ ██◆
 */
public class MozForumBean {

    /**
     * id : 1
     * forumName : 测试
     * forumImgUrl : /ksyun/2017/06/08/886f49a7c243442c8e2ba13a0e33a80a.jpg
     * forumvideoUrl : /ksyun/2017/06/08/9ad84f21133f4f5aa423d0bf067eb5f6.mp4
     * videoLength : 323
     * creatTime : 2017-07-10 10:56:43.0
     * isUse : 1
     * commentnum : 12
     * likeNum : 32
     * startTime : null
     * endTime : null
     * pageNum : null
     * pageSize : null
     */

    private String id;
    private String forumName;
    private String forumImgUrl;
    private String forumvideoUrl;
    private String videoLength;
    private String creatTime;
    private String isUse;
    private String commentnum;
    private String likeNum;
    private String startTime;
    private String endTime;
    private String pageNum;
    private String pageSize;
    private String forumIntro;
    private int liked;

    public int getLiked() {
        return liked;
    }

    public void setLiked(int liked) {
        this.liked = liked;
    }

    public String getForumIntro() {
        return forumIntro;
    }

    public void setForumIntro(String forumIntro) {
        this.forumIntro = forumIntro;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getForumName() {
        return forumName;
    }

    public void setForumName(String forumName) {
        this.forumName = forumName;
    }

    public String getForumImgUrl() {
        return forumImgUrl;
    }

    public void setForumImgUrl(String forumImgUrl) {
        this.forumImgUrl = forumImgUrl;
    }

    public String getForumvideoUrl() {
        return forumvideoUrl;
    }

    public void setForumvideoUrl(String forumvideoUrl) {
        this.forumvideoUrl = forumvideoUrl;
    }

    public String getVideoLength() {
        return videoLength;
    }

    public void setVideoLength(String videoLength) {
        this.videoLength = videoLength;
    }

    public String getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(String creatTime) {
        this.creatTime = creatTime;
    }

    public String getIsUse() {
        return isUse;
    }

    public void setIsUse(String isUse) {
        this.isUse = isUse;
    }

    public String getCommentnum() {
        return commentnum;
    }

    public void setCommentnum(String commentnum) {
        this.commentnum = commentnum;
    }

    public String getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(String likeNum) {
        this.likeNum = likeNum;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getPageNum() {
        return pageNum;
    }

    public void setPageNum(String pageNum) {
        this.pageNum = pageNum;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }
}
