package com.yidiankeyan.science.utils.net;

import android.content.Context;

import com.google.gson.Gson;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.utils.AESUtil;
import com.yidiankeyan.science.utils.GsonUtils;
import com.yidiankeyan.science.utils.LogUtils;
import com.yidiankeyan.science.utils.SpUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.ToastMaker;
import com.yidiankeyan.science.utils.Util;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * zn 2016/7/21.
 */
public class HttpUtil {

    public static void get(Context context, String url, String mobNum, HttpCallBack callBack) {
        HttpUtil http = new HttpUtil();
        http.doGet(context, url, mobNum, callBack);
    }

    public static void post(Context mContext, String url, final Map map, final HttpCallBack callBack) {
        HttpUtil http = new HttpUtil();
        http.doPost(mContext, url, map, callBack, false);
    }

    public static void post(Context mContext, String url, final Map map, final HttpCallBack callBack, boolean encryption) {
        HttpUtil http = new HttpUtil();
        http.doPost(mContext, url, map, callBack, encryption);
    }

    /**
     * 表单提交
     *
     * @param context
     * @param url
     * @param map
     * @param callBack
     */
    public static void formPost(Context context, String url, Map<String, String> map, HttpUserCallBack callBack) {
        HttpUtil http = new HttpUtil();
        http.doFormPost(context, url, map, callBack);
    }

    private void doFormPost(Context context, final String url, Map<String, String> map, final HttpUserCallBack callBack) {
        LogUtils.v("网络入参: url===" + url + ",参数===" + map);
        RequestParams params = new RequestParams(url);
        params.setConnectTimeout(10000);
        params.addHeader("token", SpUtils.getStringSp(context, "access_token"));
        params.setMultipart(true);
        Set<String> keys = map.keySet();
        if (keys != null) {
            Iterator<String> iterator = keys.iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                String value = map.get(key);
                params.addBodyParameter(key, value);
            }
        }
        Callback.Cancelable cancelable = x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtils.v("网络出参: url===" + url + " ,参数=== " + result);
                UserResultEntity entity = new Gson().fromJson(result, UserResultEntity.class);
                try {
                    callBack.successResult(entity);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtils.v("onError:" + ex.toString());
                callBack.errorResult(ex, isOnCallback);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtils.v("onCancelled:" + cex.toString());
            }

            @Override
            public void onFinished() {
            }
        });
    }

    /**
     * @param context  上下文
     * @param url      上传路径
     * @param callBack 回调
     * @param files    文件
     */
    public static void upload(Context context, String url, HttpUploadCallBack callBack, File... files) {
        HttpUtil http = new HttpUtil();
        http.doUpload(context, url, callBack, files);
    }

    private void doUpload(Context context, String url, final HttpUploadCallBack callBack, File... files) {
        RequestParams params = new RequestParams(url);
        params.addHeader("token", SpUtils.getStringSp(context, "access_token"));
        params.setConnectTimeout(30000);
        params.setMultipart(true);
        for (int i = 0; i < files.length; i++) {
            if (i == 0)
                params.addBodyParameter("file", files[i]);
            else
                params.addBodyParameter("file" + i, files[i]);
        }
        Callback.Cancelable cancelable = x.http().post(params, new Callback.ProgressCallback<String>() {

            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {

            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                LogUtils.v("total=" + total + ",current=" + current + ",isDownloading=" + isDownloading);
            }

            @Override
            public void onSuccess(String result) {
                LogUtils.v("文件上传结果" + result);
                UploadResultEntity entity = new Gson().fromJson(result, UploadResultEntity.class);
                try {
                    callBack.successResult(entity);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtils.v("onError:" + ex.toString());
                callBack.errorResult(ex, isOnCallback);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void doGet(Context context, final String url, String mobNum, final HttpCallBack callBack) {
        RequestParams params = new RequestParams(url + "/" + mobNum);
        params.setConnectTimeout(10000);
        params.addHeader("token", SpUtils.getStringSp(context, "access_token"));
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtils.v("网络出参:url===" + url + ",参数===" + result);
                ResultEntity entity = new Gson().fromJson(result, ResultEntity.class);
                try {
                    callBack.successResult(entity);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtils.v("onError:" + ex.toString());
                callBack.errorResult(ex, isOnCallback);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void doPost(Context mContext, final String url, Map map, final HttpCallBack callBack, final boolean encryption) {
        RequestParams params = new RequestParams(url);
        params.setConnectTimeout(20000);
        params.addHeader("token", SpUtils.getStringSp(null, "access_token"));
        params.setAsJsonContent(true);
        if (map != null) {
            String jsonData = GsonUtils.map2Json(map);
            params.setBodyContent(jsonData);
            LogUtils.i("网络入参: url===" + url + ",  head=" + params.getHeaders().toString() + ", 参数=== " + jsonData);
        } else {
            LogUtils.i("网络入参: url===" + url + ", 参数===");
        }
        params.setCharset("utf-8");
        Callback.Cancelable cancelable = x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                ResultEntity entity = null;
                if (encryption) {
                    try {
                        String DeString = AESUtil.decrypt(result);
                        LogUtils.e("网络出参: url===" + url + ", 参数=== " + DeString);
                        entity = new Gson().fromJson(DeString, ResultEntity.class);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    LogUtils.e("网络出参: url===" + url + ", 参数=== " + result);
                    entity = new Gson().fromJson(result, ResultEntity.class);
                }
                try {
                    if (110 == entity.getCode()) {
                        if (SpUtils.getStringSp(DemoApplication.applicationContext, "access_token") == null || !SpUtils.getStringSp(DemoApplication.applicationContext, "access_token").startsWith("-")) {
                            Util.logout();
                            EventBus.getDefault().post(EventMsg.obtain(SystemConstant.ON_USER_LOGOUT));
                        }
                        ToastMaker.showShortToast("请重新登录");
                    }
                    callBack.successResult(entity);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtils.v("onError:" + ex.getMessage());
                callBack.errorResult(ex, isOnCallback);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtils.v("onCancelled" + cex.getMessage());
            }

            @Override
            public void onFinished() {
            }
        });
    }

    public interface HttpCallBack {
        void successResult(ResultEntity result) throws JSONException;

        void errorResult(Throwable ex, boolean isOnCallback);
    }

    public interface HttpUploadCallBack {
        void successResult(UploadResultEntity result) throws JSONException;

        void errorResult(Throwable ex, boolean isOnCallback);
    }

    public interface HttpUserCallBack {
        void successResult(UserResultEntity result) throws JSONException;

        void errorResult(Throwable ex, boolean isOnCallback);
    }


}
