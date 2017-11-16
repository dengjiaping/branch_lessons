package com.yidiankeyan.science.purchase.entity;

import com.yidiankeyan.science.information.entity.MozMagazinePurchaseBean;

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
 * Created by nby on 2016/12/13.
 * 作用：专栏已购个读书已购
 */

public class PurchaseBean {
    private ColumnPurchaseBean columnDetailBean;
    private PurchaseBook.ListBean bookBean;
    private MozMagazinePurchaseBean monthlyDBBean;

    private int isColumn;

    public int isColumn() {
        return isColumn;
    }

    public void setColumn(int column) {
        isColumn = column;
    }

    public PurchaseBean(ColumnPurchaseBean columnDetailBean, PurchaseBook.ListBean bookBean, MozMagazinePurchaseBean monthlyDBBean, int isColumn) {
        this.columnDetailBean = columnDetailBean;
        this.bookBean = bookBean;
        this.monthlyDBBean = monthlyDBBean;
        this.isColumn = isColumn;
    }

    public ColumnPurchaseBean getColumnDetailBean() {
        return columnDetailBean;
    }

    public void setColumnDetailBean(ColumnPurchaseBean columnDetailBean) {
        this.columnDetailBean = columnDetailBean;
    }

    public PurchaseBook.ListBean getBookBean() {
        return bookBean;
    }

    public void setBookBean(PurchaseBook.ListBean bookBean) {
        this.bookBean = bookBean;
    }

    public MozMagazinePurchaseBean getMonthlyDBBean() {
        return monthlyDBBean;
    }

    public void setMonthlyDBBean(MozMagazinePurchaseBean monthlyDBBean) {
        this.monthlyDBBean = monthlyDBBean;
    }
}
