package com.yidiankeyan.science.my.entity;

/**
 * //  ◥█▄▃▁   ▁▂▂▃▃▂▂▂▂▂▂▂▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▅
 * //.......◥█☆█▅▄▃▁▁▁▁▁▃▄▅▅▅▅▅▅▅▅▅▅▅▅▅▅▄▁█▅●    ...Created by zn on 2017/7/18 0018.
 * //〓▇█████ ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓███▅▄▃███●
 * // 〓〓.๑۩۞۩๑.█████████████████████◤
 * //   ◥█████████◤ ◥        ◢◤
 * //     ◥██████◤        ◥  ◢◤
 * //       ████◤██████◤
 * //       █▓▓▓▓██◤
 * //      █▓▓▓██◆          墨水明细bean
 * //     █▓▓▓██◆
 * //    █▓▓▓██◆
 * //   █▓▓▓██◆
 * //  █▓▓▓██◆
 * // █▓▓ ██◆
 * //█▓▓ ██◆
 */
public class InKRecordBean {

    /**
     * id : 10
     * inkId : 3
     * type : PAY_INK_RECHARGE
     * typeName : 墨子充值
     * num : 10
     * moreOrLess : 1
     * createTime : 2017-07-17 19:56:35.0
     * startTime : null
     * endTime : null
     * pageNum : null
     * pageSize : null
     */

    private String id;
    private String inkId;
    private String type;
    private String typeName;
    private String num;
    private String moreOrLess;
    private String createTime;
    private Object startTime;
    private Object endTime;
    private Object pageNum;
    private Object pageSize;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInkId() {
        return inkId;
    }

    public void setInkId(String inkId) {
        this.inkId = inkId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getMoreOrLess() {
        return moreOrLess;
    }

    public void setMoreOrLess(String moreOrLess) {
        this.moreOrLess = moreOrLess;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Object getStartTime() {
        return startTime;
    }

    public void setStartTime(Object startTime) {
        this.startTime = startTime;
    }

    public Object getEndTime() {
        return endTime;
    }

    public void setEndTime(Object endTime) {
        this.endTime = endTime;
    }

    public Object getPageNum() {
        return pageNum;
    }

    public void setPageNum(Object pageNum) {
        this.pageNum = pageNum;
    }

    public Object getPageSize() {
        return pageSize;
    }

    public void setPageSize(Object pageSize) {
        this.pageSize = pageSize;
    }
}
