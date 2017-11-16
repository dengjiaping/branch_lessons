package com.yidiankeyan.science.app.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.ta.utdid2.android.utils.StringUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.Config;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMVideo;
import com.umeng.socialize.media.UMusic;
import com.umeng.socialize.utils.Log;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.my.activity.MyDownloadActivity;
import com.yidiankeyan.science.utils.DisplayUtil;
import com.yidiankeyan.science.utils.SpUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.TimeUtils;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

import cn.magicwindow.Session;
import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by nby on 2016/7/2.
 */
public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    protected Context mContext;
    protected ProgressDialog progressDialog;
    protected InputMethodManager inputMethodManager;
    protected MaterialDialog mMaterialDialog;
    private OnDialogButtonClickListener onDialogButtonClickListener;
    private String shareId;
    private boolean isFull;
    protected View viewStatusBar;
    private View viewNavigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!(this instanceof MyDownloadActivity))
            DemoApplication.getInstance().addActivity(this);
        int layoutId = setContentView();
        isFull = isFullScreen();
        if (isFull)
            initialization();
        setContentView(layoutId);
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        Util.setBarTintTransparency("#ff6600", this);
        mContext = this;
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        mMaterialDialog = new MaterialDialog(this);
        initView();
        initAction();
        if (isFull) {
            viewStatusBar = findViewById(R.id.view_status_bar);
//            viewNavigationBar = findViewById(R.id.view_navigation_bar);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DisplayUtil.checkDeviceHasNavigationBar(this) && viewNavigationBar != null) {
//                ViewGroup.LayoutParams layoutParams = viewNavigationBar.getLayoutParams();
//                layoutParams.height = DisplayUtil.getNavigationBarHeight(this);
//                viewNavigationBar.setLayoutParams(layoutParams);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && viewStatusBar != null) {
                ViewGroup.LayoutParams layoutParams = viewStatusBar.getLayoutParams();//titlebarHolder为添加的顶部的（标题栏上方）占位控件
                layoutParams.height = DisplayUtil.getStatueBarHeight(this);
                viewStatusBar.setLayoutParams(layoutParams);
                viewStatusBar.setBackgroundColor(Color.TRANSPARENT);
            }
        }
    }

    protected boolean isFullScreen() {
        return false;
    }

    protected void initialization() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//此FLAG可使状态栏透明，且当前视图在绘制时，从屏幕顶端开始即top = 0开始绘制，这也是实现沉浸效果的基础
//            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);//可不加
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Session.onResume(this);
        MobclickAgent.onResume(this);
        if (!TextUtils.isEmpty(SpUtils.getStringSp(this, "userId"))) {
            long currentTime = System.currentTimeMillis();
            if (SpUtils.getLongSp(this, TimeUtils.formatDate(currentTime)) == 0) {
                //当前是已登录状态并且是这一天第一次启动
                android.util.Log.e("onResume", TimeUtils.formatDate(currentTime) + "," + currentTime);
                DemoApplication.startTime = currentTime;
                SpUtils.putLongSp(this, TimeUtils.formatDate(currentTime), currentTime);
                SpUtils.removeSpKey(this, TimeUtils.formatDate(currentTime - 1 * 24 * 60 * 60 * 1000));
            }
        }
    }

    protected abstract int setContentView();

    protected abstract void initView();

    protected abstract void initAction();

    protected void hideSoftKeyboard() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        startActivity(new Intent(this, DummyActivity.class));
//        finish();
//    }

//    @Override
//    public void onBackPressed() {
//        if (JCVideoPlayer.backPress()) {
//            return;
//        }
//        super.onBackPressed();
//    }


    @Override
    protected void onPause() {
        super.onPause();
        Session.onPause(this);
        MobclickAgent.onPause(this);
//        JCVideoPlayer.releaseAllVideos();
        if (!TextUtils.isEmpty(SpUtils.getStringSp(DemoApplication.applicationContext, "userId"))) {
            long currentTime = System.currentTimeMillis();
            long time = SpUtils.getLongSp(DemoApplication.applicationContext, TimeUtils.formatDate(currentTime));
//            String str = (currentTime - time) / 60 * 1000 + "";
            long min = (currentTime - time) / (60 * 1000);
//            android.util.Log.e("onPause", Util.formatDate(currentTime) + "," + ((currentTime - time) / 10 * 60 * 1000));
            if (time > 0 && min >= 10) {
                SpUtils.putLongSp(DemoApplication.applicationContext, TimeUtils.formatDate(currentTime), -1);
                toHttpGetIntegrate();
            }
        }
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!(this instanceof MyDownloadActivity))
            DemoApplication.getInstance().finishActivity(this);
//        inputMethodManager = null;
//        Util.fixFocusedViewLeak(getApplication());
    }

    private void toHttpGetIntegrate() {
        Map<String, Object> map = new HashMap<>();
        map.put("type", "DAILY_LOGIN");
        if(!StringUtils.isEmpty(SpUtils.getStringSp(DemoApplication.applicationContext, "userId"))){
            map.put("userid", SpUtils.getStringSp(DemoApplication.applicationContext, "userId"));
        }else map.put("userid", "");
        HttpUtil.post(DemoApplication.applicationContext, SystemConstant.URL + SystemConstant.GET_INTEGRATE, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    DemoApplication.startTime = -1;
                } else if (result.getCode() == 402) {
                    DemoApplication.startTime = -1;
                } else {
                    long currentTime = System.currentTimeMillis();
                    SpUtils.putLongSp(DemoApplication.applicationContext, TimeUtils.formatDate(currentTime), DemoApplication.startTime);
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {
                long currentTime = System.currentTimeMillis();
                SpUtils.putLongSp(DemoApplication.applicationContext, TimeUtils.formatDate(currentTime), DemoApplication.startTime);
            }
        });
    }

    public void showLoadingDialog(String message) {
        progressDialog.show();
        progressDialog.setMessage(message);
    }

    public void loadingDismiss() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    protected UMShareListener umShareListener;

    protected void onShareSuccess(String shareId) {

    }

    /**
     * 增加分享
     */
    private void toHttpAddShare() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", shareId);
        HttpUtil.post(this, SystemConstant.URL + SystemConstant.ADD_SHARE_NUMBER, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                onShareSuccess(shareId);
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {

            }
        });
    }

    public interface OnShareResultListener {
        void onShareSuccess();
    }

    public void shareImage(SHARE_MEDIA platform, String title, String mediaUrl, String shareId, final OnShareResultListener onShareResultListener) {
        if (umShareListener == null)
            umShareListener = new UMShareListener() {
                @Override
                public void onResult(SHARE_MEDIA platform) {
                    Log.d("plat", "platform" + platform);
                    if (platform.name().equals("WEIXIN_FAVORITE")) {
                        Toast.makeText(DemoApplication.applicationContext, platform + " 收藏成功啦", Toast.LENGTH_SHORT).show();
                    } else {
                        if (onShareResultListener != null) {
                            onShareResultListener.onShareSuccess();
                        }
                        Toast.makeText(DemoApplication.applicationContext, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
                        if (!TextUtils.isEmpty(BaseActivity.this.shareId))
                            toHttpAddShare();
                    }
                }

                @Override
                public void onError(SHARE_MEDIA platform, Throwable t) {
                    Toast.makeText(DemoApplication.applicationContext, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
                    if (t != null) {
                        Log.d("throw", "throw:" + t.getMessage());
                    }
                }

                @Override
                public void onCancel(SHARE_MEDIA platform) {
//                    Toast.makeText(DemoApplication.applicationContext, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
                }
            };
        this.shareId = shareId;
        progressDialog.setMessage("请稍后");
        Config.dialog = progressDialog;
        ShareAction shareAction = new ShareAction(this).setPlatform(platform).setCallback(umShareListener);
        UMImage image = new UMImage(this, mediaUrl);//网络图片
        shareAction.withMedia(image).share();
    }

    /**
     * 分享图文
     *
     * @param platform 分享平台
     * @param title    标题
     * @param text     内容
     * @param mediaUrl 封面
     * @param webUrl   网页
     * @param shareId  分享目标id , 如果需要分析后请求服务器让分享数增加则加上，不需要直接忽略
     */
    public void shareWeb(SHARE_MEDIA platform, String title, String text, String mediaUrl, String webUrl, String shareId, final OnShareResultListener onShareResultListener) {
        if (umShareListener == null)
            umShareListener = new UMShareListener() {
                @Override
                public void onResult(SHARE_MEDIA platform) {
                    Log.d("plat", "platform" + platform);
                    if (platform.name().equals("WEIXIN_FAVORITE")) {
                        Toast.makeText(DemoApplication.applicationContext, platform + " 收藏成功啦", Toast.LENGTH_SHORT).show();
                    } else {
                        if (onShareResultListener != null) {
                            onShareResultListener.onShareSuccess();
                        }
                        Toast.makeText(DemoApplication.applicationContext, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
                        if (!TextUtils.isEmpty(BaseActivity.this.shareId))
                            toHttpAddShare();
                    }
                }

                @Override
                public void onError(SHARE_MEDIA platform, Throwable t) {
                    Toast.makeText(DemoApplication.applicationContext, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
                    if (t != null) {
                        Log.d("throw", "throw:" + t.getMessage());
                    }
                }

                @Override
                public void onCancel(SHARE_MEDIA platform) {
//                    Toast.makeText(DemoApplication.applicationContext, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
                }
            };
        this.shareId = shareId;
        progressDialog.setMessage("请稍后");
        Config.dialog = progressDialog;
        ShareAction shareAction = new ShareAction(this).setPlatform(platform).setCallback(umShareListener);
        if (!TextUtils.isEmpty(title))
            shareAction.withTitle(title);
        if (!TextUtils.isEmpty(text))
            shareAction.withText(text);
        if (!TextUtils.isEmpty(mediaUrl)) {
            UMImage image = new UMImage(this, mediaUrl);
            shareAction.withMedia(image);
        } else {
            UMImage image = new UMImage(this, R.mipmap.ic_launcher);
            shareAction.withMedia(image);
        }
        if (!TextUtils.isEmpty(webUrl))
            shareAction.withTargetUrl(webUrl);
        shareAction.share();
    }

    /**
     * 分享图文
     *
     * @param platform 分享平台
     * @param title    标题
     * @param text     内容
     * @param mediaUrl 封面
     * @param webUrl   网页
     * @param shareId  分享目标id , 如果需要分析后请求服务器让分享数增加则加上，不需要直接忽略
     */
    public void shareWeb(SHARE_MEDIA platform, String title, String text, String mediaUrl, String webUrl, String shareId) {
        if (umShareListener == null)
            umShareListener = new UMShareListener() {
                @Override
                public void onResult(SHARE_MEDIA platform) {
                    Log.d("plat", "platform" + platform);
                    if (platform.name().equals("WEIXIN_FAVORITE")) {
                        Toast.makeText(DemoApplication.applicationContext, platform + " 收藏成功啦", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(DemoApplication.applicationContext, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
                        if (!TextUtils.isEmpty(BaseActivity.this.shareId))
                            toHttpAddShare();
                    }
                }

                @Override
                public void onError(SHARE_MEDIA platform, Throwable t) {
                    Toast.makeText(DemoApplication.applicationContext, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
                    if (t != null) {
                        Log.d("throw", "throw:" + t.getMessage());
                    }
                }

                @Override
                public void onCancel(SHARE_MEDIA platform) {
//                    Toast.makeText(DemoApplication.applicationContext, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
                }
            };
        this.shareId = shareId;
        progressDialog.setMessage("请稍后");
        Config.dialog = progressDialog;
        ShareAction shareAction = new ShareAction(this).setPlatform(platform).setCallback(umShareListener);
        if (!TextUtils.isEmpty(title))
            shareAction.withTitle(title);
        if (!TextUtils.isEmpty(text))
            shareAction.withText(text);
        if (!TextUtils.isEmpty(mediaUrl)) {
            UMImage image = new UMImage(this, mediaUrl);
            shareAction.withMedia(image);
        } else {
            UMImage image = new UMImage(this, R.mipmap.ic_launcher);
            shareAction.withMedia(image);
        }
        if (!TextUtils.isEmpty(webUrl))
            shareAction.withTargetUrl(webUrl);
        shareAction.share();
    }

    /**
     * 分享音频
     *
     * @param platform    分享平台
     * @param title       标题
     * @param text        内容
     * @param mediaUrl    图片
     * @param webUrl      音频地址
     * @param coverImgUrl 封面
     * @param shareId     分享目标id, 如果需要分析后请求服务器让分享数增加则加上，不需要直接忽略
     */
    public void shareAudio(SHARE_MEDIA platform, String title, String text, String mediaUrl, String webUrl, String coverImgUrl, String shareId) {
        if (umShareListener == null)
            umShareListener = new UMShareListener() {
                @Override
                public void onResult(SHARE_MEDIA platform) {
                    Log.d("plat", "platform" + platform);
                    if (platform.name().equals("WEIXIN_FAVORITE")) {
                        Toast.makeText(mContext, platform + " 收藏成功啦", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mContext, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
                        if (!TextUtils.isEmpty(BaseActivity.this.shareId))
                            toHttpAddShare();
                    }
                }

                @Override
                public void onError(SHARE_MEDIA platform, Throwable t) {
                    Toast.makeText(mContext, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
                    if (t != null) {
                        Log.d("throw", "throw:" + t.getMessage());
                    }
                }

                @Override
                public void onCancel(SHARE_MEDIA platform) {
//                    Toast.makeText(mContext, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
                }
            };
        this.shareId = shareId;
        progressDialog.setMessage("请稍后");
        Config.dialog = progressDialog;
        ShareAction shareAction = new ShareAction(this).setPlatform(platform).setCallback(umShareListener);
        if (!TextUtils.isEmpty(title))
            shareAction.withTitle(title);
        if (!TextUtils.isEmpty(text))
            shareAction.withText(text);
        if (!TextUtils.isEmpty(mediaUrl)) {
            UMusic music = new UMusic(mediaUrl);
            music.setTitle(title);//音乐的标题
            if (TextUtils.isEmpty(coverImgUrl))
                music.setThumb(new UMImage(mContext, R.mipmap.ic_launcher));
            else
                music.setThumb(new UMImage(mContext, coverImgUrl));//音乐的缩略图
            music.setDescription(text);//音乐的描述
            shareAction.withMedia(music);
        }
        if (!TextUtils.isEmpty(webUrl))
            shareAction.withTargetUrl(webUrl);
        else
            shareAction.withTargetUrl(mediaUrl);
        Log.e("mediaUrl:" + mediaUrl);
        shareAction.share();
    }

    /**
     * 分享视频
     *
     * @param platform    分享平台
     * @param title       标题
     * @param text        内容
     * @param mediaUrl    视频地址
     * @param webUrl      网页
     * @param coverImgUrl 封面
     * @param shareId     分享目标id, 如果需要分析后请求服务器让分享数增加则加上，不需要直接忽略
     */
    public void shareVideo(SHARE_MEDIA platform, String title, String text, String mediaUrl, String webUrl, String coverImgUrl, String shareId) {
        if (umShareListener == null)
            umShareListener = new UMShareListener() {
                @Override
                public void onResult(SHARE_MEDIA platform) {
                    Log.d("plat", "platform" + platform);
                    if (platform.name().equals("WEIXIN_FAVORITE")) {
                        Toast.makeText(mContext, platform + " 收藏成功啦", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mContext, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
                        if (!TextUtils.isEmpty(BaseActivity.this.shareId))
                            toHttpAddShare();
                    }
                }

                @Override
                public void onError(SHARE_MEDIA platform, Throwable t) {
                    Toast.makeText(mContext, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
                    if (t != null) {
                        Log.d("throw", "throw:" + t.getMessage());
                    }
                }

                @Override
                public void onCancel(SHARE_MEDIA platform) {
//                    Toast.makeText(mContext, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
                }
            };
        this.shareId = shareId;
        progressDialog.setMessage("请稍后");
        Config.dialog = progressDialog;
        ShareAction shareAction = new ShareAction(this).setPlatform(platform).setCallback(umShareListener);
        if (!TextUtils.isEmpty(title))
            shareAction.withTitle(title);
        if (!TextUtils.isEmpty(text))
            shareAction.withText(text);
        if (!TextUtils.isEmpty(mediaUrl)) {
            if (platform != SHARE_MEDIA.SINA) {
                UMVideo video = new UMVideo(mediaUrl);
                video.setTitle(title);
                video.setThumb(new UMImage(mContext, coverImgUrl));
                video.setDescription(text);
                shareAction.withMedia(video);
            }
        }
        if (!TextUtils.isEmpty(webUrl)) {
            shareAction.withTargetUrl(webUrl);
        } else {
            shareAction.withTargetUrl(mediaUrl);
        }
        if (platform == SHARE_MEDIA.SINA) {
            if (!TextUtils.isEmpty(mediaUrl)) {
                UMImage image = new UMImage(this, coverImgUrl);
                shareAction.withMedia(image);
            } else {
                UMImage image = new UMImage(this, R.mipmap.ic_launcher);
                shareAction.withMedia(image);
            }
        }
        shareAction.share();
    }

    public void showWaringDialog(String title, String message, OnDialogButtonClickListener onDialogButtonClickListener) {
        this.onDialogButtonClickListener = onDialogButtonClickListener;
        mMaterialDialog.setTitle(title);
        mMaterialDialog.setMessage(message);
        mMaterialDialog.setPositiveButton("确认", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMaterialDialog.dismiss();
                if (BaseActivity.this.onDialogButtonClickListener != null)
                    BaseActivity.this.onDialogButtonClickListener.onPositiveButtonClick();
            }
        });
        mMaterialDialog.setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMaterialDialog.dismiss();
                if (BaseActivity.this.onDialogButtonClickListener != null)
                    BaseActivity.this.onDialogButtonClickListener.onNegativeButtonClick();
            }
        });
        mMaterialDialog.show();
    }

    public interface OnDialogButtonClickListener {
        void onPositiveButtonClick();

        void onNegativeButtonClick();
    }

    /**
     * 防止fragment中SaveInstance数据保存后再次添加造成页面数据重叠
     *
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState, outPersistentState);
    }
}
