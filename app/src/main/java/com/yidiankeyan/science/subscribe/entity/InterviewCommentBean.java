package com.yidiankeyan.science.subscribe.entity;


/**
 * //  ◥█▄▃▁   ▁▂▂▃▃▂▂▂▂▂▂▂▁▁▁▁▁▁▁▁▁▁▁▁▁▁▁▅
 * //.......◥█☆█▅▄▃▁▁▁▁▁▃▄▅▅▅▅▅▅▅▅▅▅▅▅▅▅▄▁█▅●    ...Created by zn on 2017/6/9 0009.
 * //〓▇█████ ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓███▅▄▃███●
 * // 〓〓.๑۩۞۩๑.█████████████████████◤
 * //   ◥█████████◤ ◥        ◢◤
 * //     ◥██████◤        ◥  ◢◤
 * //       ████◤██████◤                专访评论合并类
 * //       █▓▓▓▓██◤
 * //      █▓▓▓██◆
 * //     █▓▓▓██◆
 * //    █▓▓▓██◆
 * //   █▓▓▓██◆
 * //  █▓▓▓██◆
 * // █▓▓ ██◆
 * //█▓▓ ██◆
 */
public class InterviewCommentBean extends ContentCommentBean  {

    public static final int ITEM = 0;
    public static final int SECTION = 1;

    public final int type;

    public String text;

    public int sectionPosition;
    public int listPosition;

    public ContentCommentBean contentCommentBean;

    public int getSectionPosition() {
        return sectionPosition;
    }

    public void setSectionPosition(int sectionPosition) {
        this.sectionPosition = sectionPosition;
    }

    public int getListPosition() {
        return listPosition;
    }

    public void setListPosition(int listPosition) {
        this.listPosition = listPosition;
    }

    public ContentCommentBean getContentCommentBean() {
        return contentCommentBean;
    }

    public void setContentCommentBean(ContentCommentBean contentCommentBean) {
        this.contentCommentBean = contentCommentBean;
    }


    public InterviewCommentBean(int type, String text) {
        this.type = type;
        this.text = text;
    }

    public InterviewCommentBean(int type, ContentCommentBean contentCommentBean) {
        this.type = type;
        this.contentCommentBean = contentCommentBean;
    }


    public InterviewCommentBean(int type, String text, int sectionPosition, int listPosition) {
        super();
        this.type = type;
        this.text = text;
        this.sectionPosition = sectionPosition;
        this.listPosition = listPosition;
    }

    @Override
    public String toString() {
        return text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InterviewCommentBean that = (InterviewCommentBean) o;

        if (type != that.type) return false;
        if (text != null ? !text.equals(that.text) : that.text != null) return false;
        return contentCommentBean != null ? contentCommentBean.equals(that.contentCommentBean) : that.contentCommentBean == null;

    }

    @Override
    public int hashCode() {
        int result = type;
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + (contentCommentBean != null ? contentCommentBean.hashCode() : 0);
        return result;
    }
}
