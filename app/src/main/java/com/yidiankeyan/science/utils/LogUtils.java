package com.yidiankeyan.science.utils;

import com.orhanobut.logger.Logger;

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
 * Created by nby on 2016/9/29.
 * 作用：
 */
public class LogUtils {

    private static boolean isDebug;

    public static void setDebug(boolean isDebug) {
        LogUtils.isDebug = isDebug;
    }

    public static void e(String message) {
        if (!isDebug)
            return;
        Logger.e(message);
    }

    public static void d(String message) {
        if (!isDebug)
            return;
        Logger.d(message);
    }

    public static void v(String message) {
        if (!isDebug)
            return;
        Logger.v(message);
    }

    public static void i(String message) {
        if (!isDebug)
            return;
        Logger.i(message);
    }

}
