package com.yidiankeyan.science.information.entity;

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
 * Created by nby on 2016/12/14.
 * 作用：
 */

public class PayTopBean {

    /**
     * id : d448d43011ab43ebafeb4ce26b4566c1
     * title : 读者（测试不要删除）
     * subtitle : 婷婷
     * coverimg : /static/upload/cmsweb/image/jpeg2016/12/10/b1454b65e37442c0bc266b35e66a3c8a.jpg
     * price : 8
     * salesnum : 4
     * entitytype : 1
     * userimg : http://static.poinetech.com/cms/static/upload/cmsweb/image/png2016/12/8/cb14bae6a9fe4ad099b065e726610c61.png
     * latesttitle : 及反馈的反馈道具科答进口道具考多少分v很多事
     */

    private String id;
    private String title;
    private String subtitle;
    private String coverimg;
    private double price;
    private int salesnum;
    private int entitytype;
    private String userimg;
    private String latesttitle;
    /**
     * ispaid : 0
     * vol : null
     */

    private int ispaid;
    private Object vol;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getCoverimg() {
        return coverimg;
    }

    public void setCoverimg(String coverimg) {
        this.coverimg = coverimg;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getSalesnum() {
        return salesnum;
    }

    public void setSalesnum(int salesnum) {
        this.salesnum = salesnum;
    }

    public int getEntitytype() {
        return entitytype;
    }

    public void setEntitytype(int entitytype) {
        this.entitytype = entitytype;
    }

    public String getUserimg() {
        return userimg;
    }

    public void setUserimg(String userimg) {
        this.userimg = userimg;
    }

    public String getLatesttitle() {
        return latesttitle;
    }

    public void setLatesttitle(String latesttitle) {
        this.latesttitle = latesttitle;
    }

    public int getIspaid() {
        return ispaid;
    }

    public void setIspaid(int ispaid) {
        this.ispaid = ispaid;
    }

    public Object getVol() {
        return vol;
    }

    public void setVol(Object vol) {
        this.vol = vol;
    }
}
