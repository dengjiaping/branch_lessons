package com.yidiankeyan.science.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.ta.utdid2.android.utils.StringUtils;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.yidiankeyan.science.alipay.AliPay;
import com.yidiankeyan.science.alipay.PayResult;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.activity.EmptyActivity;
import com.yidiankeyan.science.app.activity.MainLoginActivity;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.information.acitivity.ColumnDetailActivity;
import com.yidiankeyan.science.information.acitivity.ColumnIntroductionActivity;
import com.yidiankeyan.science.information.acitivity.MozActivityDetailActivity;
import com.yidiankeyan.science.information.acitivity.MozInterviewDetailsActivity;
import com.yidiankeyan.science.information.acitivity.MozReadDetailsActivity;
import com.yidiankeyan.science.information.entity.BannerBean;
import com.yidiankeyan.science.information.entity.ColumnDetailsBean;
import com.yidiankeyan.science.subscribe.activity.AlbumDetailsActivity;
import com.yidiankeyan.science.utils.net.ApiServerManager;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;
import com.yidiankeyan.science.utils.net.RetrofitCallBack;
import com.yidiankeyan.science.utils.net.RetrofitResult;
import com.yidiankeyan.science.wx.WXPay;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.jpush.android.api.JPushInterface;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by www64 on 2016/6/7.
 * 功能描述：常量工具类
 */
public class Util {
    /**
     * 得到设备屏幕的宽度
     */
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 得到设备屏幕的高度
     */
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 得到设备的密度
     */
    public static float getScreenDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    /**
     * 把密度转换为像素
     */
    public static int dip2px(Context context, float px) {
        final float scale = getScreenDensity(context);
        return (int) (px * scale + 0.5);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * @param value  传入的值
     * @param length 精确度
     * @return
     */
    public static double round(double value, double length) {
        int jg = (int) (value * Math.pow(10, length + 1));
        int gw = jg % 10;
        if (gw >= 5) {
            jg = jg / 10 + 1;
        } else {
            jg = jg / 10;
        }
        return jg / (Math.pow(10, length));
    }

    /**
     * 销毁PopupWindow
     *
     * @param activity
     * @param popupWindow
     */
    public static void finishPop(Activity activity, PopupWindow popupWindow) {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = 1.0f;
        activity.getWindow().setAttributes(lp);
    }

    /**
     * 打卡软键盘
     */
    public static void openKeybord(EditText mEditText, Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    public static boolean isMobPhone(String number) {
        String regExp = "^[1]([3][0-9]{1}|[5][0-9]{1}|[8][0-9]{1}|47|45|49|[7][0-9]{1})[0-9]{8}$";
        return number.matches(regExp);//boolean
    }

    public static boolean fileExisted(File file) {
        if (file == null)
            return false;
        try {
            if (file.exists()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean fileExisted(String path) {
        if (path == null || TextUtils.isEmpty(path))
            return false;
        try {
            File file = new File(path);
            if (file.exists()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 关闭软键盘
     */
    public static void closeKeybord(EditText mEditText, Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }

    /**
     * 设置状态栏颜色
     *
     * @param setColor 要设置的颜色
     * @param activity
     */
    public static void setBarTintTransparency(String setColor, Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true, activity);
        }
        // create our manager instance after the content view is set
        SystemBarTintManager tintManager = new SystemBarTintManager(activity);
        // enable status bar tint
        tintManager.setStatusBarTintEnabled(true);
        // enable navigation bar tint
        tintManager.setNavigationBarTintEnabled(true);
        // set a custom tint color for all system bars
        tintManager.setTintColor(Color.parseColor(setColor));
    }

    private static void setTranslucentStatus(boolean on, Activity activity) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }


    public static final boolean isChineseCharacter(String chineseStr) {
        char[] charArray = chineseStr.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            // 是否是Unicode编码,除了"�"这个字符.这个字符要另外处理
            if ((charArray[i] >= '\u0000' && charArray[i] < '\uFFFD')
                    || ((charArray[i] > '\uFFFD' && charArray[i] < '\uFFFF'))) {
                continue;
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri     The Uri to query.
     * @author paulburke
     */
    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/"
                            + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection,
                        selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri,
                                       String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri
                .getAuthority());
    }

    public static int getResId(String variableName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(variableName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static Uri getUriForFile(Context context, File file) {
        if (context == null || file == null) {
            throw new NullPointerException();
        }
        Uri uri;
        if (Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(context.getApplicationContext(), "com.yidiankeyan.science.fileprovider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }

    public static String getSDCardPath() {
        String cmd = "cat /proc/mounts";
        Runtime run = Runtime.getRuntime();// 返回与当前 Java 应用程序相关的运行时对象
        try {
            Process p = run.exec(cmd);// 启动另一个进程来执行命令
            BufferedInputStream in = new BufferedInputStream(p.getInputStream());
            BufferedReader inBr = new BufferedReader(new InputStreamReader(in));

            String lineStr;
            while ((lineStr = inBr.readLine()) != null) {
                // 获得命令执行后在控制台的输出信息
                if (lineStr.contains("sdcard")
                        && lineStr.contains(".android_secure")) {
                    String[] strArray = lineStr.split(" ");
                    if (strArray != null && strArray.length >= 5) {
                        String result = strArray[1].replace("/.android_secure",
                                "");
                        return result;
                    }
                }
                // 检查命令是否执行失败。
                if (p.waitFor() != 0 && p.exitValue() == 1) {
                    // p.exitValue()==0表示正常结束，1：非正常结束
                }
            }
            inBr.close();
            in.close();
        } catch (Exception e) {

            return Environment.getExternalStorageDirectory().getPath();
        }

        return Environment.getExternalStorageDirectory().getPath();
    }

    @SuppressLint("NewApi")
    public static String parsePicturePath(Context context, Uri uri) {

        if (null == context || uri == null)
            return null;

        boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentUri
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageDocumentsUri
            if (isExternalStorageDocumentsUri(uri)) {
                String docId = DocumentsContract.getDocumentId(uri);
                String[] splits = docId.split(":");
                String type = splits[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + File.separator + splits[1];
                }
            }
            // DownloadsDocumentsUri
            else if (isDownloadsDocumentsUri(uri)) {
                String docId = DocumentsContract.getDocumentId(uri);
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaDocumentsUri
            else if (isMediaDocumentsUri(uri)) {
                String docId = DocumentsContract.getDocumentId(uri);
                String[] split = docId.split(":");
                String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selection = "_id=?";
                String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            if (isGooglePhotosContentUri(uri))
                return uri.getLastPathSegment();
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;

    }

    private static boolean isExternalStorageDocumentsUri(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private static boolean isDownloadsDocumentsUri(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private static boolean isMediaDocumentsUri(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private static boolean isGooglePhotosContentUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static void kedaAliPay(final Activity activity, String sellId, String goodsType, String goodsId, String price, final AliPayCallBack aliPayCallBack) {
        Map<String, Object> map = new HashMap<>();
        map.put("goods_type", goodsType);
        map.put("goods_id", goodsId);
        map.put("seller_id", sellId);
        map.put("price", price);
        HttpUtil.post(activity, SystemConstant.URL + "size/transactions/alipay", map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    final AliPay aliPay = (AliPay) GsonUtils.json2Bean(GsonUtils.obj2Json(result.getData()), AliPay.class);
                    Runnable payRunnable = new Runnable() {

                        @Override
                        public void run() {
                            PayTask alipay = new PayTask(activity);
                            Map<String, String> result = alipay.payV2(aliPay.getSignedParams(), true);
                            PayResult payResult = new PayResult(result);
                            aliPayCallBack.result(payResult, aliPay.getId());
                        }
                    };

                    Thread payThread = new Thread(payRunnable);
                    payThread.start();
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {

            }
        });
    }

    public static void kedaWeiXinPay(final Activity activity, String sellId, String goodsType, String goodsId, String price, final AliPayCallBack aliPayCallBack) {
        Map<String, Object> map = new HashMap<>();
        map.put("goods_type", goodsType);
        map.put("goods_id", goodsId);
        map.put("seller_id", sellId);
        map.put("price", price);
        HttpUtil.post(activity, SystemConstant.URL + "size/transactions/wechatPay", map, new HttpUtil.HttpCallBack() {

            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    PayReq req = new PayReq();
                    WXPay wxPay = (WXPay) GsonUtils.json2Bean(GsonUtils.obj2Json(result.getData()), WXPay.class);
                    aliPayCallBack.result(null, wxPay.getId());
                    req.appId = wxPay.getAppid();
                    req.partnerId = wxPay.getPartnerid();
                    req.prepayId = wxPay.getPrepayid();
                    req.packageValue = "Sign=WXPay";
                    req.nonceStr = wxPay.getNoncestr();
                    req.timeStamp = wxPay.getTimestamp();
                    List<NameValuePair> signParams = new LinkedList<NameValuePair>();
                    signParams.add(new BasicNameValuePair("appid", req.appId));
                    signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
                    signParams.add(new BasicNameValuePair("package", req.packageValue));
                    signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
                    signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
                    signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));
                    req.sign = wxPay.getSign();
                    LogUtils.e("orion=" + signParams.toString());
                    IWXAPI api = WXAPIFactory.createWXAPI(activity, req.appId);
                    api.registerApp(req.appId);
                    api.sendReq(req);
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {

            }
        });
    }

    public interface AliPayCallBack {
        void result(PayResult payResult, String id);
    }

    public static final String PREFS_NAME = "JPUSH_EXAMPLE";
    public static final String PREFS_DAYS = "JPUSH_EXAMPLE_DAYS";
    public static final String PREFS_START_TIME = "PREFS_START_TIME";
    public static final String PREFS_END_TIME = "PREFS_END_TIME";
    public static final String KEY_APP_KEY = "JPUSH_APPKEY";

    public static boolean isEmpty(String s) {
        if (null == s)
            return true;
        if (s.length() == 0)
            return true;
        if (s.trim().length() == 0)
            return true;
        return false;
    }

    // 校验Tag Alias 只能是数字,英文字母和中文
    public static boolean isValidTagAndAlias(String s) {
        Pattern p = Pattern.compile("^[\u4E00-\u9FA50-9a-zA-Z_!@#$&*+=.|￥¥]+$");
        Matcher m = p.matcher(s);
        return m.matches();
    }

    // 取得AppKey
    public static String getAppKey(Context context) {
        Bundle metaData = null;
        String appKey = null;
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            if (null != ai)
                metaData = ai.metaData;
            if (null != metaData) {
                appKey = metaData.getString(KEY_APP_KEY);
                if ((null == appKey) || appKey.length() != 24) {
                    appKey = null;
                }
            }
        } catch (PackageManager.NameNotFoundException e) {

        }
        return appKey;
    }

    // 取得版本名
    public static String getVersionName(Context context) {
        try {
            PackageInfo manager = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            return manager.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "Unknown";
        }
    }

    //取得版本号
    public static int getVersionCode(Context context) {
        try {
            PackageInfo manager = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            return manager.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return 0;
        }
    }

    public static String getDeviceId(Context context) {
        String deviceId = JPushInterface.getUdid(context);

        return deviceId;
    }

    public static void showToast(final String toast, final Context context) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(context, toast, Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }).start();
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = conn.getActiveNetworkInfo();
        return (info != null && info.isConnected());
    }

    public static String getImei(Context context, String imei) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            imei = telephonyManager.getDeviceId();
        } catch (Exception e) {
            Log.e(Util.class.getSimpleName(), e.getMessage());
        }
        return imei;
    }

    public static void deleteFiles(String... files) {
        for (int i = 0; i < files.length; i++) {
            if (fileExisted(files[i])) {
                new File(files[i]).delete();
            }
        }
    }

    /**
     * 是否处于登录状态
     *
     * @param activity
     * @return true:已登录;false:未登录
     */
    public static boolean hintLogin(final BaseActivity activity) {
        if (TextUtils.isEmpty(SpUtils.getStringSp(activity, "userId"))) {
            activity.showWaringDialog("提示", "您还没有登录，是否立即登录?", new BaseActivity.OnDialogButtonClickListener() {
                @Override
                public void onPositiveButtonClick() {
                    activity.startActivity(new Intent(activity, MainLoginActivity.class));
                    SpUtils.putStringSp(activity, "returnLogin", "true");
                }

                @Override
                public void onNegativeButtonClick() {

                }
            });
            return false;
        } else
            return true;
    }

    public static int getRelativeWidth(int width) {
        double x = width / 750f;
        return (int) (x * SystemConstant.ScreenWidth);
    }

    public static int getRelativeHeight(int height) {
        double x = height / 1334f;
        return (int) (x * SystemConstant.ScreenHeight);
    }

    public static void bannerJump(BannerBean banner, final Context context,int position) {
        Intent intent;
        switch (banner.getLinktype()) {
            case 0:
                intent = new Intent(context, MozActivityDetailActivity.class);
                intent.putExtra("url", banner.getLinkurl());
                intent.putExtra("isBanner", true);
                intent.putExtra("title", banner.getTitle());
                intent.putExtra("imgUrl", banner.getImgurl());
                context.startActivity(intent);
                break;
            case 1:
                //专辑
                intent = new Intent(context, AlbumDetailsActivity.class);
                intent.putExtra("albumId", banner.getLinkurl());
                context.startActivity(intent);
                break;
            case 2:
                //内容
                intent = new Intent(context, EmptyActivity.class);
                intent.putExtra("id", banner.getLinkurl());
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                context.startActivity(intent);
                break;
            case 3:
                //网页
                intent = new Intent(context, MozActivityDetailActivity.class);
                intent.putExtra("id", banner.getId());
                intent.putExtra("isBanner", true);
                intent.putExtra("title", banner.getTitle());
                intent.putExtra("imgUrl", banner.getImgurl());
                context.startActivity(intent);
                break;
            case 4:
                //墨子读书
                intent = new Intent(context, MozReadDetailsActivity.class);
                intent.putExtra("id", banner.getLinkurl());
                intent.putExtra("name", banner.getTitle());
                context.startActivity(intent);
                break;
            case 5:
                Intent intentInterview = new Intent(context, MozInterviewDetailsActivity.class);
                intentInterview.putExtra("id", banner.getId());
                intentInterview.putExtra("position", position);
                context.startActivity(intentInterview);
                break;
            case 6:
                //专栏详情
                if (!StringUtils.isEmpty(banner.getHaveYouPurchased()) &&
                        "0".equals(banner.getHaveYouPurchased())) {
                    intent = new Intent(context, ColumnIntroductionActivity.class);
                    intent.putExtra("id", banner.getId());
                    intent.putExtra("name", banner.getTitle());
                    intent.putExtra("haveyoupurchased", banner.getHaveYouPurchased());
                    intent.putExtra("ishasactivityprice", banner.getIshasactivityprice());
                    intent.putExtra("columnActivityPrice", banner.getColumnActivityPrice());
                    intent.putExtra("columnWriterIntro", banner.getColumnWriterIntro());
                    intent.putExtra("price", banner.getColumnPrice());
                    intent.putExtra("linkurl", banner.getLinkurl());
                    context.startActivity(intent);
                } else if (!StringUtils.isEmpty(banner.getHaveYouPurchased()) &&
                        "1".equals(banner.getHaveYouPurchased())) {
                    intent = new Intent(context, ColumnDetailActivity.class);
                    intent.putExtra("id", banner.getId());
                    intent.putExtra("name", banner.getTitle());
                    intent.putExtra("haveyoupurchased", banner.getHaveYouPurchased());
                    intent.putExtra("ishasactivityprice", banner.getIshasactivityprice());
                    intent.putExtra("columnActivityPrice", banner.getColumnActivityPrice());
                    intent.putExtra("columnWriterIntro", banner.getColumnWriterIntro());
                    intent.putExtra("price", banner.getColumnPrice());
                    intent.putExtra("linkurl", banner.getLinkurl());
                    context.startActivity(intent);
                }
                break;
        }
    }

    public static void logout() {
        DemoApplication.getInstance().appHandler.removeMessages(0);
        DemoApplication.getInstance().appHandler.sendEmptyMessageDelayed(0, 100);
    }

    public interface OnILiveLoginListener {
        void onResult(boolean status);
    }

    /**
     * 获取application中指定的meta-data
     *
     * @return 如果没有获取成功(没有对应值，或者异常)，则返回值为空
     */
    public static String getAppMetaData(Context ctx, String key) {
        if (ctx == null || TextUtils.isEmpty(key)) {
            return null;
        }
        String resultData = null;
        try {
            PackageManager packageManager = ctx.getPackageManager();
            if (packageManager != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        resultData = applicationInfo.metaData.getString(key);
                    }
                }

            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return resultData;
    }

    public static String getImgUrl(String url) {
        if (url == null) {
            return "";
        } else if (url.startsWith("http")) {
            return url;
        } else if (url.startsWith("/")) {
            return SystemConstant.ACCESS_IMG_HOST + url;
        } else {
            return SystemConstant.ACCESS_IMG_HOST + "/" + url;
        }
    }

    public static View getRootView(Activity context) {
        return ((ViewGroup) context.findViewById(android.R.id.content)).getChildAt(0);
    }
}
