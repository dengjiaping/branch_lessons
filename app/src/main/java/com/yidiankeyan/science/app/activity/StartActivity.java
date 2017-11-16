package com.yidiankeyan.science.app.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.information.acitivity.MozReadDetailsActivity;
import com.yidiankeyan.science.utils.BitmapUtils;
import com.yidiankeyan.science.utils.SpUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cn.magicwindow.MLink;
import cn.magicwindow.MWConfiguration;
import cn.magicwindow.MagicWindowSDK;
import cn.magicwindow.mlink.MLinkCallback;
import cn.magicwindow.mlink.MLinkIntentBuilder;

/**
 * 启动界面
 */
public class StartActivity extends FragmentActivity {

    private ImageView imgStart;
    private static final int START_MAIN_ACTIVITY = 12;
    private static final int START_WELCOME_ACTIVITY = 13;
    private static final int LOAD_BANNER_FINISHED = 14;
    private boolean loadFinished;
    private boolean loadBannerFinished;
    private int goPosition;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case START_MAIN_ACTIVITY:
                    loadFinished = true;
                    goPosition = START_MAIN_ACTIVITY;
                    SpUtils.putStringSp(StartActivity.this, "returnLogin", "false");
                    if (loadBannerFinished) {
                        SystemConstant.ScreenWidth = Util.getScreenWidth(StartActivity.this);
                        SystemConstant.ScreenHeight = Util.getScreenHeight(StartActivity.this);
                        if (TextUtils.isEmpty(SpUtils.getStringSp(DemoApplication.applicationContext, "userId"))) {
                            startActivity(new Intent(StartActivity.this, MainLoginActivity.class));
                        } else {
                            if (SpUtils.getIntSp(DemoApplication.applicationContext, "isBinding") == 0 && SpUtils.getIntSp(DemoApplication.applicationContext, "loginMode") == 1) {
                                startActivity(new Intent(StartActivity.this, WxBindPhoneActivity.class));
                            } else {
                                startActivity(new Intent(StartActivity.this, MainActivity.class));
                            }
                        }
                        finish();
                    }
                    break;
                case LOAD_BANNER_FINISHED:
                    loadBannerFinished = true;
                    if (loadFinished) {
                        mHandler.sendEmptyMessage(goPosition);
                    }
                    break;
            }
        }
    };
    private String TAG = "StartActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (Util.fileExisted(Util.getSDCardPath() + "/exception/exception.txt")) {
//            HttpUtil.ossUpload(Util.getSDCardPath() + "/exception/exception.txt");
//        }
        if (DemoApplication.getInstance().getActivityCount() > 0) {
            finish();
            return;
        }
        int currentCode = SpUtils.getIntSp(DemoApplication.applicationContext, "versionCode");
        if (!SpUtils.getBooleanSp(StartActivity.this, SystemConstant.APP_IS_FIRST_START) || Util.getVersionCode(DemoApplication.applicationContext) > currentCode) {
            Util.logout();
            SpUtils.putIntSp(DemoApplication.applicationContext, "versionCode", Util.getVersionCode(DemoApplication.applicationContext));
            startActivity(new Intent(StartActivity.this, WelcomeActivity.class));
            finish();
            return;
        }
        setContentView(R.layout.activity_start);
        initView();
        // 初始化SDK
        initSDK();

        //注册SDK
        registerLinks(this);
        Uri mLink = getIntent().getData();

        //如果从浏览器传来 则进行路由操作
        if (mLink != null) {

            MLink.getInstance(this).router(this, mLink);
            finish();
        } else {
            //否则执行原本操作
            initAction();
        }
    }

    protected void initView() {
        imgStart = (ImageView) findViewById(R.id.img_start);
        if (!TextUtils.isEmpty(SpUtils.getStringSp(this, SystemConstant.CUSTOM_AVATAR_URL))) {
            Glide.with(this).load(SpUtils.getStringSp(this, SystemConstant.CUSTOM_AVATAR_URL)).into(imgStart);
        }
        imgStart.setImageBitmap(BitmapUtils.readBitMap(this, R.drawable.img_start));
        Glide.with(this).load(R.drawable.img_start).dontAnimate().diskCacheStrategy(DiskCacheStrategy.NONE).into(imgStart);
    }

    protected void initAction() {
        mHandler.sendEmptyMessageDelayed(START_MAIN_ACTIVITY, 2000);
//        mHandler.sendEmptyMessage(LOAD_BANNER_FINISHED);
        toHttpGetBanner();
    }


    private void initSDK() {
        MWConfiguration config = new MWConfiguration(this);
        //设置渠道，非必须（渠道推荐在AndroidManifest.xml内填写）
        config
                //开启Debug模式，显示Log，release时注意关闭
                .setDebugModel(true)
                //带有Fragment的页面。具体查看2.2.2
                .setPageTrackWithFragment(true)
                //设置分享方式，如果之前有集成sharesdk，可在此开启
                .setSharePlatform(MWConfiguration.ORIGINAL);
        MagicWindowSDK.initSDK(config);
    }

    private void registerLinks(Context context) {
        MLink.getInstance(context).registerDefault(new MLinkCallback() {
            @Override
            public void execute(Map paramMap, Uri uri, Context context) {
                //HomeActivity 为你的首页
                MLinkIntentBuilder.buildIntent(paramMap, context, MainActivity.class);
            }
        });
        // mLinkKey:  mLink 的 key, mLink的唯一标识
        MLink.getInstance(context).register("MOZ_KEY", new MLinkCallback() {
            public void execute(Map paramMap, Uri uri, Context context) {
                Intent intent = new Intent(context, MozReadDetailsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                if (paramMap != null) {
                    Iterator iter = paramMap.entrySet().iterator();
                    while (iter.hasNext()) {
                        Map.Entry entry = (Map.Entry) iter.next();
                        String key = (String) entry.getKey();
                        String val = (String) entry.getValue();
                        if (TextUtils.equals("val", key)) {
                            intent.putExtra("id", val);
                            break;
                        }
                    }
                }
                context.startActivity(intent);
//                MLinkIntentBuilder.buildIntent(paramMap, context, MozReadDetailsActivity.class);
            }
        });
    }

    /**
     * 获取轮播图
     */
    private void toHttpGetBanner() {
        Map<String, Object> map = new HashMap<>();
        map.put("positionid", 1);
        map.put("pages", 1);
        map.put("pagesize", 6);
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.QUERY_BANNER, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) {
                mHandler.sendEmptyMessage(LOAD_BANNER_FINISHED);
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
                if (mHandler != null)
                    mHandler.sendEmptyMessage(LOAD_BANNER_FINISHED);
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.setContentView(R.layout.empty_view);
        imgStart = null;
        mHandler = null;
        System.gc();
    }
}
