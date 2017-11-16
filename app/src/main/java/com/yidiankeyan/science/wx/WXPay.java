package com.yidiankeyan.science.wx;

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
 * Created by nby on 2016/9/6.
 * 作用：
 */
public class WXPay {

    /**
     * 新版
     */
//    /**
//     * timeStamp : 1507880670
//     * paySign : A2D5824DC0EA51C547DD380785FA743F
//     * appId : wx6cc0fdc476d91025
//     * outTradeNo : wx20171013154530fbf7a977310403083745
//     * sign : 443954821E48B043CEFF99A5432B8B44
//     * signType : MD5
//     * partnerId : 1371127102
//     * prepayId : wx20171013154530fbf7a977310403083745
//     * pkg : Sign=WXPay
//     * nonceStr : NqWo9pTu4rmuqryg
//     */
//
//    private String timeStamp;
//    private String paySign;
//    private String appId;
//    private String outTradeNo;
//    private String sign;
//    private String signType;
//    private String partnerId;
//    private String prepayId;
//    private String pkg;
//    private String nonceStr;
//
//
//    public String getTimeStamp() {
//        return timeStamp;
//    }
//
//    public void setTimeStamp(String timeStamp) {
//        this.timeStamp = timeStamp;
//    }
//
//    public String getPaySign() {
//        return paySign;
//    }
//
//    public void setPaySign(String paySign) {
//        this.paySign = paySign;
//    }
//
//    public String getAppId() {
//        return appId;
//    }
//
//    public void setAppId(String appId) {
//        this.appId = appId;
//    }
//
//    public String getOutTradeNo() {
//        return outTradeNo;
//    }
//
//    public void setOutTradeNo(String outTradeNo) {
//        this.outTradeNo = outTradeNo;
//    }
//
//    public String getSign() {
//        return sign;
//    }
//
//    public void setSign(String sign) {
//        this.sign = sign;
//    }
//
//    public String getSignType() {
//        return signType;
//    }
//
//    public void setSignType(String signType) {
//        this.signType = signType;
//    }
//
//    public String getPartnerId() {
//        return partnerId;
//    }
//
//    public void setPartnerId(String partnerId) {
//        this.partnerId = partnerId;
//    }
//
//    public String getPrepayId() {
//        return prepayId;
//    }
//
//    public void setPrepayId(String prepayId) {
//        this.prepayId = prepayId;
//    }
//
//    public String getPkg() {
//        return pkg;
//    }
//
//    public void setPkg(String pkg) {
//        this.pkg = pkg;
//    }
//
//    public String getNonceStr() {
//        return nonceStr;
//    }
//
//    public void setNonceStr(String nonceStr) {
//        this.nonceStr = nonceStr;
//    }


    /**
     * 旧版
     */
//
//    /**
//     * id : 41f17d7f78ea4538845fe7e9ce85adce
//     * appid : wx9b2c43e0b50d3e49
//     * partnerid : 1302901001
//     * prepayid : wx20160906204414214e48c5e20347053624
//     * pkg : Sign=WXPay
//     * noncestr : 92b70a527191ca64ca2df1cc32142646
//     * timestamp : 1473165927
//     * sign : B740598EFD15BC1E077E172128074426
//     */

    private String id;
    private String appid;
    private String partnerid;
    private String prepayid;
    private String pkg;
    private String noncestr;
    private String timestamp;
    private String sign;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getPkg() {
        return pkg;
    }

    public void setPkg(String pkg) {
        this.pkg = pkg;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }


}
