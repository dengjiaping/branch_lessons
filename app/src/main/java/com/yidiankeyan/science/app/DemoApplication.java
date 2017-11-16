package com.yidiankeyan.science.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.Log;

//import com.squareup.leakcanary.LeakCanary;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.PlatformConfig;
import com.xiaomi.channel.commonutils.logger.LoggerInterface;
import com.xiaomi.mipush.sdk.Logger;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.yidiankeyan.science.information.entity.AnswerAlbumBean;
import com.yidiankeyan.science.utils.LogUtils;
import com.yidiankeyan.science.utils.MIUIUtils;
import com.yidiankeyan.science.utils.SpUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.Util;

import org.xutils.x;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import cn.jpush.android.api.JPushInterface;
import cn.magicwindow.Session;

import static com.yidiankeyan.science.utils.Util.getVersionCode;


/**
 * Created by nby on 2016/7/6.
 */
public class DemoApplication extends Application implements
        Thread.UncaughtExceptionHandler {

    public static Context applicationContext;
    private static DemoApplication instance;
    private static Stack<Activity> activityStack;
    public static final String TAG = "com.yidiankeyan.science";
    public static long startTime;
    // 小米key
    public static final String APP_ID = "2882303761517526596";
    public static final String APP_KEY = "5711752699596";

    public static boolean DEBUG = true;
//        public static boolean DEBUG = false;
    public static Map<Integer, AnswerAlbumBean> answerAlbumMap = new HashMap<>();
    public List<Long> annoyList = new ArrayList<>();
    public static boolean isBuy = false;  //是否能够分享的开关，true为允许分享  false为不允许分享

    public static boolean isPlay = false;  //用来判断是否有无手动关闭正在播放的音频 默认是false代表播放关闭状态 true代表正在播放状态

    public static List mList = new ArrayList();   //精品专栏
    public static List mListselect = new ArrayList();  //专栏列表
    public static List mListImageTextselect = new ArrayList();  //专栏详情图文列表
    public static boolean isBuyImagetext = false;
    public static List mListAudioselect = new ArrayList();  //专栏详情音频列表
    public static List mListArticleselect = new ArrayList();  //专栏详情未购买免费试听列表

    public Handler appHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Util.deleteFiles(Util.getSDCardPath() + "/temp_crop.jpg", Util.getSDCardPath() + "/temp.jpg"
                    , Util.getSDCardPath() + "/temp_document.jpg"
                    , Util.getSDCardPath() + "/temp_avatar.jpg"
                    , Util.getSDCardPath() + "/temp_avatar.png"
                    , Util.getSDCardPath() + "/temp_avatar_crop.jpg"
                    , Util.getSDCardPath() + "/temp_opinion.jpg"
                    , Util.getSDCardPath() + "/temp_opinion_crop.jpg"
                    , Util.getSDCardPath() + "/temp_authentication_avatar.jpg"
                    , Util.getSDCardPath() + "/temp_authentication_avatar_crop.jpg");
            SpUtils.clearSp(DemoApplication.applicationContext);
            SpUtils.putBooleanSp(DemoApplication.applicationContext, SystemConstant.APP_IS_FIRST_START, true);
            SpUtils.putStringSp(DemoApplication.applicationContext, "access_token", "-" + java.util.UUID.randomUUID().toString().trim().replace("-", ""));
            SpUtils.putIntSp(DemoApplication.applicationContext, "versionCode", getVersionCode(DemoApplication.applicationContext));
        }
    };

    @Override
    public void onCreate() {
        MultiDex.install(this);
        super.onCreate();
        //魔窗设置
        Session.setAutoSession(this);
//        LeakCanary.install(this);
        applicationContext = this;
        instance = this;
        if (TextUtils.isEmpty(SpUtils.getStringSp(DemoApplication.applicationContext, "access_token"))) {
            Util.deleteFiles(Util.getSDCardPath() + "/temp_crop.jpg", Util.getSDCardPath() + "/temp.jpg"
                    , Util.getSDCardPath() + "/temp_document.jpg"
                    , Util.getSDCardPath() + "/temp_avatar.jpg"
                    , Util.getSDCardPath() + "/temp_avatar.png"
                    , Util.getSDCardPath() + "/temp_avatar_crop.jpg"
                    , Util.getSDCardPath() + "/temp_opinion.jpg"
                    , Util.getSDCardPath() + "/temp_opinion_crop.jpg"
                    , Util.getSDCardPath() + "/temp_authentication_avatar.jpg"
                    , Util.getSDCardPath() + "/temp_authentication_avatar_crop.jpg");
            SpUtils.putStringSp(this, "access_token", "-" + java.util.UUID.randomUUID().toString().trim().replace("-", ""));
        }
        PlatformConfig.setWeixin("wx6cc0fdc476d91025", "3e13e06a75be59a54f236c3ead72c3fb");
        PlatformConfig.setSinaWeibo("2333903051", "b88f343c396b6a44d294ca4857e3eb9a");
        PlatformConfig.setQQZone("1105592976", "7hoT8FJUh7I5QGYM");
        x.Ext.init(this);
        x.Ext.setDebug(DEBUG);
        LogUtils.setDebug(DEBUG);
        MobclickAgent.setCatchUncaughtExceptions(false);
        MobclickAgent.setDebugMode(DEBUG);
        Thread.setDefaultUncaughtExceptionHandler(this);
        if (MIUIUtils.isMIUI()) {
            LogUtils.e("注册小米推送");
            LoggerInterface newLogger = new LoggerInterface() {

                @Override
                public void setTag(String tag) {
                    // ignore
                }

                @Override
                public void log(String content, Throwable t) {
                    Log.d(TAG, content, t);
                }

                @Override
                public void log(String content) {
                    Log.d(TAG, content);
                }
            };
            Logger.setLogger(this, newLogger);
            if (shouldInit()) {
                MiPushClient.registerPush(this, APP_ID, APP_KEY);
            }
            JPushInterface.stopPush(this);
        } else {
            LogUtils.e("注册极光推送");
            JPushInterface.setDebugMode(DEBUG);    // 设置开启日志,发布时请关闭日志
            JPushInterface.init(this);            // 初始化 JPush
            MiPushClient.unregisterPush(this);
            MiPushClient.disablePush(this);
        }
    }

    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

    public static DemoApplication getInstance() {
        return instance;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        System.out.println("uncaughtException");
        ex.printStackTrace();
        try {
            File file = new File(Util.getSDCardPath() + "/exception");
            if (!file.exists())
                file.mkdir();
            File file1 = new File(file, System.currentTimeMillis() + ".txt");
            file1.createNewFile();
            PrintWriter p = new PrintWriter(new FileOutputStream(file1));
            ex.printStackTrace(p);
            p.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        AppExit();
        System.exit(0);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    /**
     * add Activity 添加Activity到栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     * get current Activity 获取当前Activity（栈中最后一个压入的）
     */
    public Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 结束当前Activity（栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
                return;
            }
        }
    }

    /**
     * 获取指定类名的Activity
     */
    public Activity getActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                return activity;
            }
        }
        return null;
    }

    /**
     * 判断指定的activity是否存在
     *
     * @param cls
     * @return
     */
    public boolean activityExisted(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    public int getActivityCount() {
        return activityStack == null ? 0 : activityStack.size();
    }

    /**
     * 退出应用程序
     */
    public void AppExit() {
        try {
            finishAllActivity();
        } catch (Exception e) {
        }
    }

}
