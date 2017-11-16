package com.yidiankeyan.science.utils.net;

import com.yidiankeyan.science.app.entity.UserInforMation;
import com.yidiankeyan.science.my.entity.WXBindingPhoneBean;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

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
 * Created by nby on 2017/6/20.
 * 作用：
 */

public interface UserApiServer {
    /**
     * 登录
     */
    @GET("user/user/login/{userName}/{userPassword}/0")
    Call<RetrofitResult<UserInforMation>> login(@Path("userName") String userName, @Path("userPassword") String userPassword);

    /**
     * 微信登录
     */
    @GET("user/user/wx/login/{code}/1")
    Call<RetrofitResult<UserInforMation>> wxLogin(@Path("code") String code);

    /**
     * 上传推送id
     */
    @POST("user/user/pushInfo")
    Call<RetrofitResult<Object>> postPushId(@Body Map map);

    /**
     * 登出
     */
    @POST("user/user/logout")
    Call<RetrofitResult<Object>> logout();
    /**
     * 账号绑定
     *
     * @return
     */
    @POST("user/user/wx/binding")
    Call<RetrofitResult<WXBindingPhoneBean>> getBinddingPhone(@Body Map map);

    /**
     * 账号绑定
     * -密码设置
     *
     * @return
     */
    @POST("user/user/wx/edit/{phone}/{password}")
    Call<RetrofitResult<Object>> getPhonePassWord(@Path("phone") String phone, @Path("password") String password);

    /**
     * 账号解绑
     *
     * @return
     */
    @POST("user/user/wx/unbinding")
    Call<RetrofitResult<WXBindingPhoneBean>> getRemoveBindding(@Body Map map);
}
