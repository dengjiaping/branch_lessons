package com.yidiankeyan.science.information.acitivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ta.utdid2.android.utils.StringUtils;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.utils.DownLoadImageService;
import com.yidiankeyan.science.utils.FileUtils;
import com.yidiankeyan.science.utils.GsonUtils;
import com.yidiankeyan.science.utils.SpUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.ToastMaker;
import com.yidiankeyan.science.utils.Util;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.HashMap;
import java.util.Map;


public class MozActivityDetailActivity extends BaseActivity {

    private TextView tvTitle;
    private WebView wvActivityDetail;
    private FrameLayout video_fullView;// 全屏时视频加载view
    private View xCustomView;
    //    private ProgressDialog waitdialog = null;
    private WebChromeClient.CustomViewCallback xCustomViewCallback;
    private MyWebChromeClient xwebchromeclient;
    private AutoLinearLayout llAudioMore;
    private PopupWindow sharePopupWindow;
    private PopupWindow sharesPopupWindow;
    private AutoLinearLayout imgShareWx;
    private AutoLinearLayout imgShareFriendCircle;
    private ImageView imgShareSina;
    private AutoLinearLayout imgShareQQ;
    private TextView btnCancel;
    private AutoLinearLayout llAll;
    private String shareUrl;
    private AutoRelativeLayout layoutTitle;

    private String shareWay;
    private String strJson;
    private String title = "";
    private String url = "";
    private String urlHttp = "";
    private double type = 0;
    private int types = 0;
    private String coverimg;
    private String imgCover;
    private ImageView picture;

    @Override
    protected int setContentView() {
        EventBus.getDefault().register(this);
        return R.layout.activity_moz_activity_detail;
    }

    @Override
    protected void initView() {
        wvActivityDetail = (WebView) findViewById(R.id.wv_activity_detail);
        llAudioMore = (AutoLinearLayout) findViewById(R.id.ll_audio_more);
        llAll = (AutoLinearLayout) findViewById(R.id.ll_all);
        layoutTitle = (AutoRelativeLayout) findViewById(R.id.layout_title);
        tvTitle = (TextView) findViewById(R.id.maintitle_txt);
        picture = (ImageView) findViewById(R.id.img_txt_follow);
    }

    @Override
    protected void initAction() {
//        ((TextView) findViewById(R.id.maintitle_txt)).setText("活动详情");
        video_fullView = (FrameLayout) findViewById(R.id.video_fullView);
        findViewById(R.id.ll_return).setOnClickListener(this);
        llAudioMore.setOnClickListener(this);
        llAudioMore.setVisibility(View.VISIBLE);
//        if (getIntent().getBooleanExtra("isBanner", false)) {
//            ((TextView) findViewById(R.id.maintitle_txt)).setText("墨子送书啦");
//        }
        WebSettings ws = wvActivityDetail.getSettings();
        wvActivityDetail.requestFocusFromTouch(); //支持获取手势焦点，输入用户名、密码或其他
        ws.setSupportZoom(true);          //支持缩放
        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);// 排版适应屏幕x
        //设置自适应屏幕，两者合用
        ws.setUseWideViewPort(true);  //将图片调整到适合webview的大小
        ws.setLoadsImagesAutomatically(true);  //支持自动加载图片
        ws.setRenderPriority(WebSettings.RenderPriority.HIGH);  //提高渲染的优先级
        ws.setLoadWithOverviewMode(true);// setUseWideViewPort方法设置webview推荐使用的窗口。setLoadWithOverviewMode方法是设置webview加载的页面的模式。
        ws.setSavePassword(true);
        ws.setSaveFormData(true);// 保存表单数据
        ws.setJavaScriptEnabled(true);
        ws.setGeolocationEnabled(true);// 启用地理定位
        ws.setGeolocationDatabasePath("/data/data/org.itri.html5webview/databases/");// 设置定位的数据库路径
        ws.setDomStorageEnabled(true);
        ws.setSupportMultipleWindows(true);
        xwebchromeclient = new MyWebChromeClient();
        wvActivityDetail.setWebChromeClient(xwebchromeclient);
        wvActivityDetail.setWebViewClient(new MyWebViewClient());
        wvActivityDetail.addJavascriptInterface(new JsLoginJump(), "appLogin");
        wvActivityDetail.addJavascriptInterface(new JsInterface(), "share");
        wvActivityDetail.addJavascriptInterface(new JsInviteImg(), "inviteimg");
        if (getIntent().getBooleanExtra("isBanner", false)) {
            if (TextUtils.isEmpty(getIntent().getStringExtra("url"))) {
                wvActivityDetail.loadUrl(SystemConstant.MYURL + "banner/getcustom/" + getIntent().getStringExtra("id"));
            } else {
                //先用get请求判断是否需要登录才能调页面
//                RequestParams params = new RequestParams("http://192.168.1.197/gift/index");
//                x.http().get(params, new Callback.CommonCallback<String>() {
//                    @Override
//                    public void onSuccess(String result) {
//                        //不需登录
//                        wvActivityDetail.loadUrl(getIntent().getStringExtra("url"));
//                    }
//
//                    @Override
//                    public void onError(Throwable ex, boolean isOnCallback) {
//                        if (ex instanceof HttpException) {
////                            //需要登录
////                            if (!Util.hintLogin(MozActivityDetailActivity.this)) {
////                                return;
////                            }
                Map<String, String> extraHeaders = new HashMap<String, String>();
                if(!StringUtils.isEmpty(SpUtils.getStringSp(DemoApplication.applicationContext, "userId"))){
                    extraHeaders.put("signtoken", SpUtils.getStringSp(DemoApplication.applicationContext, "userId"));
                }else extraHeaders.put("signtoken", "");
                wvActivityDetail.loadUrl(getIntent().getStringExtra("url"), extraHeaders);
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(CancelledException cex) {
//
//                    }
//
//                    @Override
//                    public void onFinished() {
//
//                    }
//                });
            }
        } else {
            wvActivityDetail.loadUrl(SystemConstant.MYURL + "/flashreport/iteminfo/" + getIntent().getStringExtra("id"));
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && wvActivityDetail.canGoBack()) {
            if (wvActivityDetail.getUrl().startsWith(getIntent().getStringExtra("url"))) {
                finish();
            } else {
                wvActivityDetail.goBack();// 返回前一个页面
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_return:
                if (wvActivityDetail.getUrl().startsWith(SystemConstant.MYURL + "banner/getcustom/" + getIntent().getStringExtra("id"))) {
                    finish();
                } else if (wvActivityDetail.getUrl().startsWith(getIntent().getStringExtra("url"))) {
                    finish();
                } else {
                    if (wvActivityDetail.canGoBack()) {
                        wvActivityDetail.goBack();// 返回前一个页面
                        return;
                    }
                }
                break;
            case R.id.ll_audio_more:
                if (!Util.hintLogin(this)) return;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    wvActivityDetail.evaluateJavascript("appAutoShare()", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                            strJson = value;
                        }
                    });
                }
                if (TextUtils.equals("1", shareWay)) {
                    wvActivityDetail.evaluateJavascript("setShareContent()", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                            String jsonTxt = value.replace("\\", "");
                            int lenght = jsonTxt.length();
                            jsonTxt = jsonTxt.substring(0, lenght - 1);
                            lenght = jsonTxt.length();
                            jsonTxt = jsonTxt.substring(1, lenght);
                            Map map = (Map) GsonUtils.json2Map(jsonTxt);
                            for (Object key : map.keySet()) {
                                if (TextUtils.equals("title", (CharSequence) key)) {
                                    title = (String) map.get(key);
                                }
                                if (TextUtils.equals("type", (CharSequence) key)) {
                                    type = (double) map.get(key);
                                    types = (int) type;
                                }
                                if (TextUtils.equals("url", (CharSequence) key)) {
                                    url = (String) map.get(key);
                                    if (url.contains("http")) {
                                        urlHttp = url;
                                    } else {
                                        if (type > 1) {
                                            urlHttp = SystemConstant.ACCESS_IMG_HOST + url;
                                        } else {
                                            urlHttp = SystemConstant.MYURL + url;
                                        }
                                    }
                                }
                                if (TextUtils.equals("coverimg", (CharSequence) key)) {
                                    coverimg = (String) map.get(key);
                                    if (coverimg.contains("http")) {
                                        imgCover = coverimg;
                                    } else {
                                        imgCover = SystemConstant.ACCESS_IMG_HOST + coverimg;
                                    }
                                }

                            }
                            if (types > 1) {
                                shareImage(SHARE_MEDIA.WEIXIN_CIRCLE,
                                        title,
                                        urlHttp, null, new OnShareResultListener() {

                                            @Override
                                            public void onShareSuccess() {
                                                wvActivityDetail.evaluateJavascript("shareCallback()", new ValueCallback<String>() {
                                                    @Override
                                                    public void onReceiveValue(String value) {
                                                        wvActivityDetail.loadUrl("javascript:shareOK('" + strJson.substring(1, strJson.length() - 1) + "')");
                                                        wvActivityDetail.reload();
                                                    }
                                                });
                                            }
                                        });
                            } else {
                                if (getIntent().getBooleanExtra("isBanner", false)) {
                                    shareWeb(
                                            SHARE_MEDIA.WEIXIN_CIRCLE,
                                            title,
                                            " ",
                                            imgCover,
                                            TextUtils.isEmpty(getIntent().getStringExtra("url")) ?
//                                    getIntent().getStringExtra("url")
                                                    SystemConstant.MYURL + "banner/getcustom/" + getIntent().getStringExtra("id")
                                                    :
                                                    urlHttp
//                                            SystemConstant.MYURL + url
                                            , null, new OnShareResultListener() {
                                                @Override
                                                public void onShareSuccess() {
                                                    wvActivityDetail.evaluateJavascript("shareCallback()", new ValueCallback<String>() {
                                                        @Override
                                                        public void onReceiveValue(String value) {
                                                            wvActivityDetail.loadUrl("javascript:shareOK('" + strJson.substring(1, strJson.length() - 1) + "')");
                                                            wvActivityDetail.reload();
                                                        }
                                                    });
                                                }
                                            });
                                } else {
                                    shareWeb(
                                            SHARE_MEDIA.WEIXIN_CIRCLE,
                                            title,
                                            " ",
                                            imgCover,
                                            urlHttp
                                            , null, new OnShareResultListener() {
                                                @Override
                                                public void onShareSuccess() {
                                                    wvActivityDetail.evaluateJavascript("shareCallback()", new ValueCallback<String>() {
                                                        @Override
                                                        public void onReceiveValue(String value) {
                                                            wvActivityDetail.loadUrl("javascript:shareOK('" + strJson.substring(1, strJson.length() - 1) + "')");
                                                            wvActivityDetail.reload();
                                                        }
                                                    });
                                                }
                                            });
                                }
                            }
//                String str1 = value;
//                shareUrl = str1.substring(2, str1.length() - 1);
                        }
                    });

                } else {
                    showSharePop();
                }
                break;
        }
    }

    @SuppressLint("NewApi")
    private void showSharePop() {
        if (sharePopupWindow == null) {
            View view = View.inflate(this, R.layout.popupwindow_news_flash_share, null);
            imgShareWx = (AutoLinearLayout) view.findViewById(R.id.ll_share_wx);
            imgShareFriendCircle = (AutoLinearLayout) view.findViewById(R.id.ll_friend_circle);
            imgShareQQ = (AutoLinearLayout) view.findViewById(R.id.ll_share_qq);
//            imgShareSina = (ImageView) view.findViewById(R.id.img_share_sina);
            btnCancel = (TextView) view.findViewById(R.id.btn_cancel);
            sharePopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            sharePopupWindow.setContentView(view);
            sharePopupWindow.setAnimationStyle(R.style.AnimBottom);
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.6f;
            getWindow().setAttributes(lp);
            sharePopupWindow.setFocusable(true);
            sharePopupWindow.setOutsideTouchable(true);
            sharePopupWindow.setBackgroundDrawable(new BitmapDrawable());
            sharePopupWindow.showAtLocation(llAll, Gravity.BOTTOM, 0, 0);
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Util.finishPop(MozActivityDetailActivity.this, sharePopupWindow);
                }
            });
            sharePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    Util.finishPop(MozActivityDetailActivity.this, sharePopupWindow);
                }
            });
        } else {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.6f;
            getWindow().setAttributes(lp);
            sharePopupWindow.showAtLocation(llAll, Gravity.BOTTOM, 0, 0);
        }
        wvActivityDetail.evaluateJavascript("setShareContent()", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                if (!TextUtils.equals("null", value) && !TextUtils.isEmpty(value)) {
                    String jsonTxt = value.replace("\\", "");
                    int lenght = jsonTxt.length();
                    jsonTxt = jsonTxt.substring(0, lenght - 1);
                    lenght = jsonTxt.length();
                    jsonTxt = jsonTxt.substring(1, lenght);
                    Map map = (Map) GsonUtils.json2Map(jsonTxt);
                    for (Object key : map.keySet()) {
                        if (TextUtils.equals("title", (CharSequence) key)) {
                            title = (String) map.get(key);
                        }
                        if (TextUtils.equals("type", (CharSequence) key)) {
                            type = (double) map.get(key);
                            types = (int) type;
                        }
                        if (TextUtils.equals("url", (CharSequence) key)) {
                            url = (String) map.get(key);
                            if (coverimg.contains("http")) {
                                urlHttp = url;
                            } else {
                                if (type > 1) {
                                    urlHttp = SystemConstant.ACCESS_IMG_HOST + url;
                                } else {
                                    urlHttp = SystemConstant.MYURL + url;
                                }
                            }
                        }
                        if (TextUtils.equals("coverimg", (CharSequence) key)) {
                            coverimg = (String) map.get(key);
                            if (coverimg.contains("http")) {
                                imgCover = coverimg;
                            } else {
                                imgCover = SystemConstant.ACCESS_IMG_HOST + coverimg;
                            }
                        }

                    }
                } else {
                    imgShareWx.setEnabled(false);
                    imgShareFriendCircle.setEnabled(false);
                    imgShareQQ.setEnabled(false);
                }
//                String str1 = value;
//                shareUrl = str1.substring(2, str1.length() - 1);
            }
        });
//        wvActivityDetail.evaluateJavascript("getShareTitle()", new ValueCallback<String>() {
//            @Override
//            public void onReceiveValue(String value) {
//                titleSearch = value;
//            }
//        });
        imgShareWx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (types > 1) {
                    shareImage(SHARE_MEDIA.WEIXIN,
                            title,
                            urlHttp, null, new OnShareResultListener() {

                                @Override
                                public void onShareSuccess() {
                                    wvActivityDetail.evaluateJavascript("shareCallback()", new ValueCallback<String>() {
                                        @Override
                                        public void onReceiveValue(String value) {
                                            wvActivityDetail.loadUrl("javascript:shareOK('" + strJson.substring(1, strJson.length() - 1) + "')");
                                            wvActivityDetail.reload();
                                        }
                                    });
                                }
                            });
                } else {
                    if (getIntent().getBooleanExtra("isBanner", false)) {
                        shareWeb(
                                SHARE_MEDIA.WEIXIN,
                                title,
                                " ",
                                imgCover,
                                TextUtils.isEmpty(getIntent().getStringExtra("url")) ?
//                                    getIntent().getStringExtra("url")
                                        SystemConstant.MYURL + "banner/getcustom/" + getIntent().getStringExtra("id")
                                        :
                                        urlHttp
                                , null, new OnShareResultListener() {
                                    @Override
                                    public void onShareSuccess() {
                                        wvActivityDetail.evaluateJavascript("shareCallback()", new ValueCallback<String>() {
                                            @Override
                                            public void onReceiveValue(String value) {
                                                wvActivityDetail.loadUrl("javascript:shareOK('" + strJson.substring(1, strJson.length() - 1) + "')");
                                                wvActivityDetail.reload();
                                            }
                                        });
                                    }
                                });
                    } else {
                        shareWeb(
                                SHARE_MEDIA.WEIXIN,
                                title,
                                " ",
                                imgCover,
                                urlHttp
                                , null, new OnShareResultListener() {
                                    @Override
                                    public void onShareSuccess() {
                                        wvActivityDetail.evaluateJavascript("shareCallback()", new ValueCallback<String>() {
                                            @Override
                                            public void onReceiveValue(String value) {
                                                wvActivityDetail.loadUrl("javascript:shareOK('" + strJson.substring(1, strJson.length() - 1) + "')");
                                                wvActivityDetail.reload();
                                            }
                                        });
                                    }
                                });
                    }
                }
            }
        });
        imgShareFriendCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (types > 1) {
                    shareImage(SHARE_MEDIA.WEIXIN_CIRCLE,
                            title,
                            urlHttp, null, new OnShareResultListener() {

                                @Override
                                public void onShareSuccess() {
                                    wvActivityDetail.evaluateJavascript("shareCallback()", new ValueCallback<String>() {
                                        @Override
                                        public void onReceiveValue(String value) {
                                            wvActivityDetail.loadUrl("javascript:shareOK('" + strJson.substring(1, strJson.length() - 1) + "')");
                                            wvActivityDetail.reload();
                                        }
                                    });
                                }
                            });
                } else {
                    if (getIntent().getBooleanExtra("isBanner", false)) {
                        shareWeb(
                                SHARE_MEDIA.WEIXIN_CIRCLE,
                                title,
                                " ",
                                imgCover,
                                TextUtils.isEmpty(getIntent().getStringExtra("url")) ?
//                                    getIntent().getStringExtra("url")
                                        SystemConstant.MYURL + "banner/getcustom/" + getIntent().getStringExtra("id")
                                        :
                                        urlHttp
                                , null, new OnShareResultListener() {
                                    @Override
                                    public void onShareSuccess() {
                                        wvActivityDetail.evaluateJavascript("shareCallback()", new ValueCallback<String>() {
                                            @Override
                                            public void onReceiveValue(String value) {
                                                wvActivityDetail.loadUrl("javascript:shareOK('" + strJson.substring(1, strJson.length() - 1) + "')");
                                                wvActivityDetail.reload();
                                            }
                                        });
                                    }
                                });
                    } else {
                        shareWeb(
                                SHARE_MEDIA.WEIXIN_CIRCLE,
                                title,
                                " ",
                                imgCover,
                                urlHttp
                                , null, new OnShareResultListener() {
                                    @Override
                                    public void onShareSuccess() {
                                        wvActivityDetail.evaluateJavascript("shareCallback()", new ValueCallback<String>() {
                                            @Override
                                            public void onReceiveValue(String value) {
                                                wvActivityDetail.loadUrl("javascript:shareOK('" + strJson.substring(1, strJson.length() - 1) + "')");
                                                wvActivityDetail.reload();
                                            }
                                        });
                                    }
                                });
                    }
                }
            }
        });
        imgShareQQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (types > 1) {
//                    shareWeb(
//                            SHARE_MEDIA.QQ,
//                            titleSearch,
//                            " ",
//                            SystemConstant.ACCESS_IMG_HOST + getIntent().getStringExtra("imgUrl"),
//                            TextUtils.isEmpty(getIntent().getStringExtra("url")) ?
//                                    //getIntent().getStringExtra("url")
//                                    SystemConstant.MYURL + "banner/getcustom/" + getIntent().getStringExtra("id")
//                                    :
//                                    SystemConstant.MYURL + url
//                            , null, new OnShareResultListener() {
//                                @Override
//                                public void onShareSuccess() {
//                                    wvActivityDetail.evaluateJavascript("shareCallback()", new ValueCallback<String>() {
//                                        @Override
//                                        public void onReceiveValue(String value) {
//                                            wvActivityDetail.reload();
//                                        }
//                                    });
//                                }
//                            });
                    shareImage(SHARE_MEDIA.QQ,
                            title,
                            urlHttp, null, new OnShareResultListener() {

                                @Override
                                public void onShareSuccess() {
                                    wvActivityDetail.evaluateJavascript("shareCallback()", new ValueCallback<String>() {
                                        @Override
                                        public void onReceiveValue(String value) {
                                            wvActivityDetail.loadUrl("javascript:shareOK('" + strJson.substring(1, strJson.length() - 1) + "')");
                                            wvActivityDetail.reload();
                                        }
                                    });
                                }
                            });
                } else {
                    if (getIntent().getBooleanExtra("isBanner", false)) {
                        shareWeb(
                                SHARE_MEDIA.QQ,
                                title,
                                " ",
                                imgCover,
                                TextUtils.isEmpty(getIntent().getStringExtra("url")) ?
//                                    getIntent().getStringExtra("url")
                                        SystemConstant.MYURL + "banner/getcustom/" + getIntent().getStringExtra("id")
                                        :
                                        urlHttp
                                , null, new OnShareResultListener() {
                                    @Override
                                    public void onShareSuccess() {
                                        wvActivityDetail.evaluateJavascript("shareCallback()", new ValueCallback<String>() {
                                            @Override
                                            public void onReceiveValue(String value) {
                                                wvActivityDetail.loadUrl("javascript:shareOK('" + strJson.substring(1, strJson.length() - 1) + "')");
                                                wvActivityDetail.reload();
                                            }
                                        });
                                    }
                                });
                    } else {
                        shareWeb(
                                SHARE_MEDIA.QQ,
                                title,
                                " ",
                                imgCover,
                                urlHttp
                                , null, new OnShareResultListener() {
                                    @Override
                                    public void onShareSuccess() {
                                        wvActivityDetail.evaluateJavascript("shareCallback()", new ValueCallback<String>() {
                                            @Override
                                            public void onReceiveValue(String value) {
                                                wvActivityDetail.loadUrl("javascript:shareOK('" + strJson.substring(1, strJson.length() - 1) + "')");
                                                wvActivityDetail.reload();
                                            }
                                        });
                                    }
                                });
                    }
                }

            }
        });
//        imgShareSina.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (getIntent().getBooleanExtra("isBanner", false)) {
//                    shareWeb(
//                            SHARE_MEDIA.SINA,
//                            getIntent().getStringExtra("title"),
//                            " ",
//                            SystemConstant.ACCESS_IMG_HOST + getIntent().getStringExtra("imgUrl"),
//                            TextUtils.isEmpty(getIntent().getStringExtra("url")) ?
//                                    //getIntent().getStringExtra("url")
//                                    SystemConstant.MYURL + "banner/getcustom/" + getIntent().getStringExtra("id")
//                                    :
//                                    SystemConstant.MYURL + shareUrl
//                            , null);
//                } else {
//                    shareWeb(
//                            SHARE_MEDIA.SINA,
//                            getIntent().getStringExtra("title"),
//                            " ",
//                            SystemConstant.ACCESS_IMG_HOST + getIntent().getStringExtra("imgUrl"),
//                            SystemConstant.MYURL + shareUrl
//                            , null);
//                }
//            }
//        });
    }


    public class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (TextUtils.equals("null", view.getTitle()) && TextUtils.isEmpty(view.getTitle())) {
                tvTitle.setText("活动详情");
            } else {
                tvTitle.setText(view.getTitle());
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                wvActivityDetail.evaluateJavascript("appAutoShare()", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        strJson = value;
                    }
                });
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                wvActivityDetail.evaluateJavascript("appAutoShareStyle()", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        if (!TextUtils.isEmpty(value)) {
                            shareWay = value.replace("\"", "");
                        } else {
                            shareWay = "2";
                        }

                    }
                });
            }
//            waitdialog.dismiss();
        }
    }

    public class MyWebChromeClient extends WebChromeClient {
        private View xprogressvideo;

        // 播放网络视频时全屏会被调用的方法
        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            wvActivityDetail.setVisibility(View.INVISIBLE);
            layoutTitle.setVisibility(View.GONE);
            // 如果一个视图已经存在，那么立刻终止并新建一个
            if (xCustomView != null) {
                callback.onCustomViewHidden();
                return;
            }
            video_fullView.addView(view);
            xCustomView = view;
            xCustomViewCallback = callback;
            video_fullView.setVisibility(View.VISIBLE);
        }

        // 视频播放退出全屏会被调用的
        @Override
        public void onHideCustomView() {
            if (xCustomView == null)// 不是全屏播放状态
                return;


            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            xCustomView.setVisibility(View.GONE);
            video_fullView.removeView(xCustomView);
            xCustomView = null;
            video_fullView.setVisibility(View.GONE);
            xCustomViewCallback.onCustomViewHidden();
            layoutTitle.setVisibility(View.VISIBLE);
            wvActivityDetail.setVisibility(View.VISIBLE);
        }

        // 视频加载时进程loading
        @Override
        public View getVideoLoadingProgressView() {
            if (xprogressvideo == null) {
                LayoutInflater inflater = LayoutInflater
                        .from(MozActivityDetailActivity.this);
                xprogressvideo = inflater.inflate(
                        R.layout.video_loading_progress, null);
            }
            return xprogressvideo;
        }

//        @Override
//        public void onProgressChanged(WebView view, int newProgress) {
//            if (newProgress == 100) {
//                //加载网页完成
//                wvActivityDetail.getProgressbar().setVisibility(View.GONE);
//            } else {
//                if (wvActivityDetail.getProgressbar().getVisibility() == View.GONE)
//                    wvActivityDetail.getProgressbar().setVisibility(View.VISIBLE);
//                wvActivityDetail.getProgressbar().setProgress(newProgress);
//            }
//            super.onProgressChanged(view, newProgress);
//        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            wvActivityDetail.reload();
        }

    }

    @Subscribe
    public void onEvent(EventMsg msg) {
        switch (msg.getWhat()) {
            case SystemConstant.ON_USER_INFORMATION:
                Map<String, String> extraHeaders = new HashMap<String, String>();
                if(!StringUtils.isEmpty(SpUtils.getStringSp(DemoApplication.applicationContext, "userId"))){
                    extraHeaders.put("signtoken", SpUtils.getStringSp(DemoApplication.applicationContext, "userId"));
                }else extraHeaders.put("signtoken", "");
                wvActivityDetail.loadUrl(getIntent().getStringExtra("url"), extraHeaders);
                break;
        }
    }

    private class JsLoginJump {

        public JsLoginJump() {
        }

        @JavascriptInterface
        public void showLoginFromJs() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //需要登录
                    if (!Util.hintLogin(MozActivityDetailActivity.this)) {
                        return;
                    }
                }
            });
        }
    }

    /**
     * 签到抽奖
     * 分享
     */
    private class JsInterface {
        public JsInterface() {
        }

        @JavascriptInterface
        public void showInfoFromShare() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        wvActivityDetail.evaluateJavascript("appAutoShare()", new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String value) {
                                strJson = value;
                            }
                        });
                    }
                    if (TextUtils.equals("1", shareWay)) {
                        wvActivityDetail.evaluateJavascript("setShareContent()", new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String value) {
                                String jsonTxt = value.replace("\\", "");
                                int lenght = jsonTxt.length();
                                jsonTxt = jsonTxt.substring(0, lenght - 1);
                                lenght = jsonTxt.length();
                                jsonTxt = jsonTxt.substring(1, lenght);
                                Map map = (Map) GsonUtils.json2Map(jsonTxt);
                                for (Object key : map.keySet()) {
                                    if (TextUtils.equals("title", (CharSequence) key)) {
                                        title = (String) map.get(key);
                                    }
                                    if (TextUtils.equals("type", (CharSequence) key)) {
                                        type = (double) map.get(key);
                                        types = (int) type;
                                    }
                                    if (TextUtils.equals("url", (CharSequence) key)) {
                                        url = (String) map.get(key);
                                        if (url.contains("http")) {
                                            urlHttp = url;
                                        } else {
                                            if (type > 1) {
                                                urlHttp = SystemConstant.ACCESS_IMG_HOST + url;
                                            } else {
                                                urlHttp = SystemConstant.MYURL + url;
                                            }
                                        }
                                    }
                                    if (TextUtils.equals("coverimg", (CharSequence) key)) {
                                        coverimg = (String) map.get(key);
                                        if (coverimg.contains("http")) {
                                            imgCover = coverimg;
                                        } else {
                                            imgCover = SystemConstant.ACCESS_IMG_HOST + coverimg;
                                        }
                                    }

                                }
                                if (types > 1) {
                                    shareImage(SHARE_MEDIA.WEIXIN_CIRCLE,
                                            title,
                                            urlHttp, null, new OnShareResultListener() {

                                                @Override
                                                public void onShareSuccess() {
                                                    wvActivityDetail.evaluateJavascript("shareCallback()", new ValueCallback<String>() {
                                                        @Override
                                                        public void onReceiveValue(String value) {
                                                            wvActivityDetail.loadUrl("javascript:shareOK('" + strJson.substring(1, strJson.length() - 1) + "')");
                                                            wvActivityDetail.reload();
                                                        }
                                                    });
                                                }
                                            });
                                } else {
                                    if (getIntent().getBooleanExtra("isBanner", false)) {
                                        shareWeb(
                                                SHARE_MEDIA.WEIXIN_CIRCLE,
                                                title,
                                                " ",
                                                imgCover,
                                                TextUtils.isEmpty(getIntent().getStringExtra("url")) ?
//                                    getIntent().getStringExtra("url")
                                                        SystemConstant.MYURL + "banner/getcustom/" + getIntent().getStringExtra("id")
                                                        :
                                                        urlHttp
//                                            SystemConstant.MYURL + url
                                                , null, new OnShareResultListener() {
                                                    @Override
                                                    public void onShareSuccess() {
                                                        wvActivityDetail.evaluateJavascript("shareCallback()", new ValueCallback<String>() {
                                                            @Override
                                                            public void onReceiveValue(String value) {
                                                                wvActivityDetail.loadUrl("javascript:shareOK('" + strJson.substring(1, strJson.length() - 1) + "')");
                                                                wvActivityDetail.reload();
                                                            }
                                                        });
                                                    }
                                                });
                                    } else {
                                        shareWeb(
                                                SHARE_MEDIA.WEIXIN_CIRCLE,
                                                title,
                                                " ",
                                                imgCover,
                                                urlHttp
                                                , null, new OnShareResultListener() {
                                                    @Override
                                                    public void onShareSuccess() {
                                                        wvActivityDetail.evaluateJavascript("shareCallback()", new ValueCallback<String>() {
                                                            @Override
                                                            public void onReceiveValue(String value) {
                                                                wvActivityDetail.loadUrl("javascript:shareOK('" + strJson.substring(1, strJson.length() - 1) + "')");
                                                                wvActivityDetail.reload();
                                                            }
                                                        });
                                                    }
                                                });
                                    }
                                }
//                String str1 = value;
//                shareUrl = str1.substring(2, str1.length() - 1);
                            }
                        });

                    } else {
                        showSharePop();
                    }
                }
            });
        }
    }

    private class JsInviteImg {
        public JsInviteImg() {
        }

        @JavascriptInterface
        public void showInviteImg(final String inviteImg) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!TextUtils.isEmpty(inviteImg)) {

                        DownLoadImageService service = new DownLoadImageService(MozActivityDetailActivity.this,
                                inviteImg,
                                new DownLoadImageService.ImageDownLoadCallBack() {

                                    @Override
                                    public void onDownLoadSuccess(final File file) {
                                        // 在这里执行图片保存方法
                                        MozActivityDetailActivity.this.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                String s = "";
                                                s = "/DCIM/invitation" + System.currentTimeMillis() + ".jpg";
                                                FileUtils.copyFile(file, Util.getSDCardPath() + s, true);
                                                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(Util.getSDCardPath() + s))));
                                                ToastMaker.showShortToast("保存成功");
                                            }
                                        });
                                    }

                                    @Override
                                    public void onDownLoadFailed() {
                                        // 图片保存失败
                                        ToastMaker.showShortToast("保存失败");
                                    }
                                });
                        //启动图片下载线程
                        new Thread(service).start();
                    }

                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        video_fullView.removeAllViews();
        wvActivityDetail.loadUrl("about:blank");
        wvActivityDetail.stopLoading();
        wvActivityDetail.setWebChromeClient(null);
        wvActivityDetail.setWebViewClient(null);
        wvActivityDetail.removeAllViews();
        wvActivityDetail.destroy();
        wvActivityDetail = null;
    }


}
