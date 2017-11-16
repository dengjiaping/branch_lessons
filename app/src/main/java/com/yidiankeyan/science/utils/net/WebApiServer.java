package com.yidiankeyan.science.utils.net;

import com.yidiankeyan.science.my.entity.PhoneBindingBean;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

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
 * Created by nby on 2017/7/21.
 * 作用：
 */

public interface WebApiServer {
    /**
     * {
     * "id":"快讯id",
     * "comment":"评论",
     * "userimg":"用户头像",
     * "username":"用户名称"
     * }
     */
    @POST("flashnews/createShareImg")
    Call<RetrofitResult<String>> createNewsShareImg(@Body Map map);

    @POST("/u/mscode")
    Call<RetrofitResult<Object>> getCode(@Body Map map);
}
