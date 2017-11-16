package com.yidiankeyan.science.utils.net;

import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.utils.SpUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.ToastMaker;
import com.yidiankeyan.science.utils.Util;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
 * Created by nby on 2016/12/28.
 * 作用：Retrofit回调接口，统一处理token验证失败
 */

public abstract class RetrofitCallBack<T> implements Callback<RetrofitResult<T>> {
    @Override
    public void onResponse(Call<RetrofitResult<T>> call, Response<RetrofitResult<T>> response) {
        if (response == null || response.body() == null) {
            onFailure(call, new NullPointerException());
        } else {
            if (response.body().getCode() == 110) {
                if (SpUtils.getStringSp(DemoApplication.applicationContext, "access_token") == null || !SpUtils.getStringSp(DemoApplication.applicationContext, "access_token").startsWith("-")) {
                    Util.logout();
                    EventBus.getDefault().post(EventMsg.obtain(SystemConstant.ON_USER_LOGOUT));
                }
                ToastMaker.showShortToast("请重新登录");
            }
            try {
                onSuccess(call, response);
            } catch (Exception e) {
                e.printStackTrace();
                onFailure(call, e);
            }
        }
    }

    public abstract void onSuccess(Call<RetrofitResult<T>> call, Response<RetrofitResult<T>> response);
}
