package com.yidiankeyan.science.information.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nby on 2016/7/22.
 */
public class NoticeBean {

    private int type;
    private List<NotifyChildBean> notifyList;
    private String title;
    private boolean loadFinish;

    public boolean isLoadFinish() {
        return loadFinish;
    }

    public void setLoadFinish(boolean loadFinish) {
        this.loadFinish = loadFinish;
    }

    public void removeAll() {
        notifyList.removeAll(notifyList);
    }

    public NoticeBean(String title, int type) {
        this.title = title;
        this.type = type;
        notifyList = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<NotifyChildBean> getNotifyList() {
        return notifyList;
    }

    public void createChild(String avatar, String name) {
        NotifyChildBean childBean = new NotifyChildBean(avatar, name);
        notifyList.add(childBean);
    }

    public void createChild(String avatar, String name, String des) {
        NotifyChildBean childBean = new NotifyChildBean(avatar, name, des);
        notifyList.add(childBean);
    }

    public void createChild(String avatar, String name, String des, String date, String id) {
        NotifyChildBean childBean = new NotifyChildBean(avatar, name, des, date, id);
        notifyList.add(childBean);
    }
    public void createChild(String avatar, String name, String des, String date) {
        NotifyChildBean childBean = new NotifyChildBean(avatar, name, des, date);
        notifyList.add(childBean);
    }

    public void setNotifyList(List<NotifyChildBean> notifyList) {
        this.notifyList = notifyList;
    }

    public class NotifyChildBean {
        private String avatar;
        private String name;
        private String des;
        private String date;
        private String id;

        public NotifyChildBean(String avatar, String name) {
            this.avatar = avatar;
            this.name = name;
        }

        public NotifyChildBean(String avatar, String name, String des, String date) {
            this.avatar = avatar;
            this.name = name;
            this.des = des;
            this.date = date;
        }
        public NotifyChildBean(String avatar, String name, String des, String date, String id) {
            this.avatar = avatar;
            this.name = name;
            this.des = des;
            this.date = date;
            this.id = id;
        }

        public NotifyChildBean(String avatar, String name, String des) {
            this.avatar = avatar;
            this.name = name;
            this.des = des;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDes() {
            return des;
        }

        public void setDes(String des) {
            this.des = des;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }
}
