package com.yidiankeyan.science.information.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
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
 * Created by nby on 2016/9/2.
 * 作用：日课实体
 */
public class OneDayArticles implements Parcelable {

    /**
     * id : 3590d335a5174a53b8db5a365ddbed85
     * audiourl : /static/upload/cmsweb/image/jpeg2016/8/24/c11b2b532fbc4f05bc171c8c7ce9b41a.jpg
     * title : rike
     * content : null
     * contentauthor : null
     * contentauthorid : null
     * createtime : null
     * publishtime : null
     * timelength : null
     * size : null
     * praisenum : 0
     * readnum : 0
     * reprintnum : 0
     * totaldonated : null
     * casterid : null
     * source : null
     * subjectids : null
     * subjects : [{"id":1,"name":"哲学"},{"id":2,"name":"数学"},{"id":3,"name":"逻辑学"},{"id":4,"name":"科技史"}]
     * iscollected : null
     */

    private String id;
    private String audiourl;
    private String title;
    private String content;
    private String contentauthor;
    private String contentauthorid;
    private String createtime;
    private String publishtime;
    private long timelength;
    private long size;
    private int praisenum;
    private int readnum;
    private int reprintnum;
    private String totaldonated;
    private String casterid;
    private String source;
    private String subjectids;
    private String iscollected;
    /**
     * id : 1
     * name : 哲学
     */

    private int ispraised;
    private String coverimgurl;
    /**
     * tagids : null
     * tagList : [{"id":1,"name":"地球科学"}]
     */

    private Object tagids;
    /**
     * id : 1
     * name : 地球科学
     */

    private List<TagListBean> tagList;

    public String getCoverimgurl() {
        return coverimgurl;
    }

    public void setCoverimgurl(String coverimgurl) {
        this.coverimgurl = coverimgurl;
    }

    public int getIspraised() {
        return ispraised;
    }

    public void setIspraised(int ispraised) {
        this.ispraised = ispraised;
    }

    private List<SubjectsBean> subjects;

    @Override
    public String toString() {
        return "OneDayArticles{" +
                "id='" + id + '\'' +
                '}';
    }

    public Object getTagids() {
        return tagids;
    }

    public void setTagids(Object tagids) {
        this.tagids = tagids;
    }

    public List<TagListBean> getTagList() {
        return tagList;
    }

    public void setTagList(List<TagListBean> tagList) {
        this.tagList = tagList;
    }

    public static class SubjectsBean implements Parcelable {
        private int id;
        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeString(this.name);
        }

        public SubjectsBean() {
        }

        protected SubjectsBean(Parcel in) {
            this.id = in.readInt();
            this.name = in.readString();
        }

        public static final Creator<SubjectsBean> CREATOR = new Creator<SubjectsBean>() {
            @Override
            public SubjectsBean createFromParcel(Parcel source) {
                return new SubjectsBean(source);
            }

            @Override
            public SubjectsBean[] newArray(int size) {
                return new SubjectsBean[size];
            }
        };
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAudiourl() {
        return audiourl;
    }

    public void setAudiourl(String audiourl) {
        this.audiourl = audiourl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentauthor() {
        return contentauthor;
    }

    public void setContentauthor(String contentauthor) {
        this.contentauthor = contentauthor;
    }

    public String getContentauthorid() {
        return contentauthorid;
    }

    public void setContentauthorid(String contentauthorid) {
        this.contentauthorid = contentauthorid;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getPublishtime() {
        return publishtime;
    }

    public void setPublishtime(String publishtime) {
        this.publishtime = publishtime;
    }

    public long getTimelength() {
        return timelength;
    }

    public void setTimelength(long timelength) {
        this.timelength = timelength;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public int getPraisenum() {
        return praisenum;
    }

    public void setPraisenum(int praisenum) {
        this.praisenum = praisenum;
    }

    public int getReadnum() {
        return readnum;
    }

    public void setReadnum(int readnum) {
        this.readnum = readnum;
    }

    public int getReprintnum() {
        return reprintnum;
    }

    public void setReprintnum(int reprintnum) {
        this.reprintnum = reprintnum;
    }

    public String getTotaldonated() {
        return totaldonated;
    }

    public void setTotaldonated(String totaldonated) {
        this.totaldonated = totaldonated;
    }

    public String getCasterid() {
        return casterid;
    }

    public void setCasterid(String casterid) {
        this.casterid = casterid;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSubjectids() {
        return subjectids;
    }

    public void setSubjectids(String subjectids) {
        this.subjectids = subjectids;
    }

    public String getIscollected() {
        return iscollected;
    }

    public void setIscollected(String iscollected) {
        this.iscollected = iscollected;
    }

    public List<SubjectsBean> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<SubjectsBean> subjects) {
        this.subjects = subjects;
    }

    public OneDayArticles() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.audiourl);
        dest.writeString(this.title);
        dest.writeString(this.content);
        dest.writeString(this.contentauthor);
        dest.writeString(this.contentauthorid);
        dest.writeString(this.createtime);
        dest.writeString(this.publishtime);
        dest.writeLong(this.timelength);
        dest.writeLong(this.size);
        dest.writeInt(this.praisenum);
        dest.writeInt(this.readnum);
        dest.writeInt(this.reprintnum);
        dest.writeString(this.totaldonated);
        dest.writeString(this.casterid);
        dest.writeString(this.source);
        dest.writeString(this.subjectids);
        dest.writeString(this.iscollected);
        dest.writeString(this.coverimgurl);
        dest.writeList(this.subjects);
    }

    protected OneDayArticles(Parcel in) {
        this.id = in.readString();
        this.audiourl = in.readString();
        this.title = in.readString();
        this.content = in.readString();
        this.contentauthor = in.readString();
        this.contentauthorid = in.readString();
        this.createtime = in.readString();
        this.publishtime = in.readString();
        this.timelength = in.readLong();
        this.size = in.readLong();
        this.praisenum = in.readInt();
        this.readnum = in.readInt();
        this.reprintnum = in.readInt();
        this.totaldonated = in.readString();
        this.casterid = in.readString();
        this.source = in.readString();
        this.subjectids = in.readString();
        this.iscollected = in.readString();
        this.coverimgurl = in.readString();
        this.subjects = new ArrayList<SubjectsBean>();
        in.readList(this.subjects, SubjectsBean.class.getClassLoader());
    }

    public static final Creator<OneDayArticles> CREATOR = new Creator<OneDayArticles>() {
        @Override
        public OneDayArticles createFromParcel(Parcel source) {
            return new OneDayArticles(source);
        }

        @Override
        public OneDayArticles[] newArray(int size) {
            return new OneDayArticles[size];
        }
    };

    public static class TagListBean {
        private int id;
        private String name;

        public TagListBean(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
