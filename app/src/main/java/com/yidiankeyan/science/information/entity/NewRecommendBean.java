package com.yidiankeyan.science.information.entity;

import java.util.List;

/**
 * Created by nby on 2016/7/29.
 * 新换的接口，所以新建了一个推荐下的专题
 */
public class NewRecommendBean {

    /**
     * subjectid : -1
     * name : 猜你喜欢
     * simpleAlbumModles : [{"albumid":"6d92cee057e04cfb9ca4050ced281ef1","coverimgurl":"/static/upload/cmsweb/image/png2016/7/28/1469755252593趣味数学416、416.png","lastupdatetitle":"为什么不能在加时赛中抛硬币","name":"趣味数学","authorname":"伟大的测试人员","albumtype":"1"},{"albumid":"427c0281efca480ba52c1ca5cab3c7cf","coverimgurl":"/static/upload/cmsweb/image/png2016/7/28/1469759882484数学理论222.222.png","lastupdatetitle":"PCA的数学原理","name":"数学理论","authorname":"伟大的测试人员","albumtype":"1"},{"albumid":"58da25a1931548acb325bdcd7b21f0b6","coverimgurl":"/static/upload/cmsweb/image/jpeg2016/7/28/14697510208331.jpg","lastupdatetitle":"望远镜的性能决定因素","name":"天文器材","authorname":"伟大的测试人员","albumtype":"1"}]
     */

    private int subjectid;
    private String name;
    /**
     * albumid : 6d92cee057e04cfb9ca4050ced281ef1
     * coverimgurl : /static/upload/cmsweb/image/png2016/7/28/1469755252593趣味数学416、416.png
     * lastupdatetitle : 为什么不能在加时赛中抛硬币
     * name : 趣味数学
     * authorname : 伟大的测试人员
     * albumtype : 1
     */

    private List<SimpleAlbumModlesBean> simpleAlbumModles;

    public int getSubjectid() {
        return subjectid;
    }

    public void setSubjectid(int subjectid) {
        this.subjectid = subjectid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SimpleAlbumModlesBean> getSimpleAlbumModles() {
        return simpleAlbumModles;
    }

    public void setSimpleAlbumModles(List<SimpleAlbumModlesBean> simpleAlbumModles) {
        this.simpleAlbumModles = simpleAlbumModles;
    }

    public static class SimpleAlbumModlesBean {
        private String albumid;
        private String coverimgurl;
        private String lastupdatetitle;
        private String name;
        private String authorname;
        private String albumtype;

        public String getAlbumid() {
            return albumid;
        }

        public void setAlbumid(String albumid) {
            this.albumid = albumid;
        }

        public String getCoverimgurl() {
            return coverimgurl;
        }

        public void setCoverimgurl(String coverimgurl) {
            this.coverimgurl = coverimgurl;
        }

        public String getLastupdatetitle() {
            return lastupdatetitle;
        }

        public void setLastupdatetitle(String lastupdatetitle) {
            this.lastupdatetitle = lastupdatetitle;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAuthorname() {
            return authorname;
        }

        public void setAuthorname(String authorname) {
            this.authorname = authorname;
        }

        public String getAlbumtype() {
            return albumtype;
        }

        public void setAlbumtype(String albumtype) {
            this.albumtype = albumtype;
        }
    }
}
