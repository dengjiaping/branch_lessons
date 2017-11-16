package com.yidiankeyan.science.information.acitivity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.bumptech.glide.Glide;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.yidiankeyan.science.R;
import com.yidiankeyan.science.alipay.AliPay;
import com.yidiankeyan.science.alipay.PayResult;
import com.yidiankeyan.science.app.DemoApplication;
import com.yidiankeyan.science.app.activity.MainActivity;
import com.yidiankeyan.science.app.base.BaseActivity;
import com.yidiankeyan.science.app.entity.EventMsg;
import com.yidiankeyan.science.db.DB;
import com.yidiankeyan.science.download.DownloadManager;
import com.yidiankeyan.science.information.entity.IssuesDetailBean;
import com.yidiankeyan.science.utils.AudioPlayManager;
import com.yidiankeyan.science.utils.GsonUtils;
import com.yidiankeyan.science.utils.LogUtils;
import com.yidiankeyan.science.utils.SpUtils;
import com.yidiankeyan.science.utils.SystemConstant;
import com.yidiankeyan.science.utils.TimeUtils;
import com.yidiankeyan.science.utils.Util;
import com.yidiankeyan.science.utils.net.HttpUtil;
import com.yidiankeyan.science.utils.net.ResultEntity;
import com.yidiankeyan.science.view.MarqueeTextView;
import com.yidiankeyan.science.wx.WXPay;
import com.zhy.autolayout.AutoFrameLayout;
import com.zhy.autolayout.AutoLinearLayout;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class PreviewColumnContentActivity extends BaseActivity {

    private TextView tvTitle;
    private ImageView imgAvatar;
    private ImageView imgAuthorAvatar;
    private TextView tvAuthorName;
    private TextView tvDate;
    private AutoFrameLayout flAudio;
    private ImageView imgContentAvatar;
    private ImageView imgMediaState;
    private TextView tvAudioTitleDesc;
    private ImageView imgDownload;
    private SeekBar seekBar;
    private TextView tvCurrTime;
    private TextView tvTotalTime;
    private TextView tvDescTitle;
    //    private TextView tvDescContent;
    private TextView tvSubscribe;
    private Timer mTimer;
    private TimerTask mTimerTask;
    private IssuesDetailBean issuesDetailBean;
    private MarqueeTextView maintitleTxt;
    private PopupWindow payPopupWindow;
    private TextView tvOrderInfo;
    private AutoLinearLayout llAll;
    private WebView webView;

    /**
     * 随着播放进度更新ui
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                try {
                    tvCurrTime.setText(TimeUtils.getTimeFromLong(AudioPlayManager.getInstance().getCurrentPosition()));
                    seekBar.setProgress((int) AudioPlayManager.getInstance().getCurrentPosition());
                    tvTotalTime.setText(TimeUtils.getTimeFromLong(AudioPlayManager.getInstance().getIntDuration()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

    /**
     * 000
     */
    private SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                if (AudioPlayManager.getInstance().CURRENT_STATE == SystemConstant.ON_PLAYING || AudioPlayManager.getInstance().CURRENT_STATE == SystemConstant.ON_PAUSE) {
                    if (progress < AudioPlayManager.getInstance().getIntDuration())
                        AudioPlayManager.getInstance().mIjkMediaPlayer.seekTo(progress);
                }
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            AudioPlayManager.getInstance().pause();
            DemoApplication.isPlay = false;
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            AudioPlayManager.getInstance().ijkStart();
            //释放时开始播放
            if (AudioPlayManager.getInstance().mIjkMediaPlayer == null) {
                AudioPlayManager.getInstance().release();
            }
            AudioPlayManager.getInstance().ijkStart();
            DemoApplication.isPlay = true;
        }
    };

    @Override
    protected int setContentView() {
        EventBus.getDefault().register(this);
        return R.layout.activity_preview_column_content;
    }

    @Override
    protected void initView() {
        tvTitle = (TextView) findViewById(R.id.tv_title);
        imgAvatar = (ImageView) findViewById(R.id.img_avatar);
        imgAuthorAvatar = (ImageView) findViewById(R.id.img_author_avatar);
        tvAuthorName = (TextView) findViewById(R.id.tv_author_name);
        tvDate = (TextView) findViewById(R.id.tv_date);
        webView = (WebView) findViewById(R.id.web_view);
        flAudio = (AutoFrameLayout) findViewById(R.id.fl_audio);
        imgContentAvatar = (ImageView) findViewById(R.id.img_content_avatar);
        imgMediaState = (ImageView) findViewById(R.id.img_media_state);
        tvAudioTitleDesc = (TextView) findViewById(R.id.tv_audio_title_desc);
        imgDownload = (ImageView) findViewById(R.id.img_download);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        tvCurrTime = (TextView) findViewById(R.id.tv_curr_time);
        tvTotalTime = (TextView) findViewById(R.id.tv_total_time);
        tvDescTitle = (TextView) findViewById(R.id.tv_desc_title);
        llAll = (AutoLinearLayout) findViewById(R.id.ll_all);
//        tvDescContent = (TextView) findViewById(R.id.tv_desc_content);
        tvSubscribe = (TextView) findViewById(R.id.tv_subscribe);
        maintitleTxt = (MarqueeTextView) findViewById(R.id.maintitle_txt);
    }

    @Override
    protected void initAction() {
        flAudio.setOnClickListener(this);
        tvSubscribe.setOnClickListener(this);
        findViewById(R.id.ll_return).setOnClickListener(this);
//        if (getIntent().getBooleanExtra("purchase", false)) {
        tvSubscribe.setVisibility(View.GONE);
        maintitleTxt.setText("");
//        } else {
//            maintitleTxt.setText("试看");
//        }
        toHttpGetDetail();
        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                //每隔0.1秒检查音频播放器是否处于播放中，如果是的话更新进度条和已播放时间
                if (AudioPlayManager.getInstance().isPlaying()) {
                    mHandler.sendEmptyMessage(0);
                }
            }
        };
        seekBar.setOnSeekBarChangeListener(onSeekBarChangeListener);
        mTimer.scheduleAtFixedRate(mTimerTask, 0, 100);
    }

    private void toHttpGetDetail() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", getIntent().getStringExtra("id"));
        HttpUtil.post(DemoApplication.applicationContext, SystemConstant.URL + SystemConstant.ISSUES_DETAIL, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    issuesDetailBean = (IssuesDetailBean) GsonUtils.json2Bean(GsonUtils.obj2Json(result.getData()), IssuesDetailBean.class);
                    initData();
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {

            }
        });
    }

    /**
     * 初始化音频，如果已下载播放本地，未下载播放网络音频
     */
    private void initAudio() {
        List<IssuesDetailBean> list = new ArrayList<>();
        list.add(issuesDetailBean);
        AudioPlayManager.getInstance().init(list, 0, AudioPlayManager.PlayModel.ORDER);
        AudioPlayManager.getInstance().ijkStart();
        DemoApplication.isPlay = true;
    }

    private void initData() {
        imgDownload.setOnClickListener(this);
        Glide.with(this).load(Util.getImgUrl(issuesDetailBean.getCoverimg())).into(imgAvatar);
        Glide.with(this).load(Util.getImgUrl(issuesDetailBean.getCoverimg())).bitmapTransform(new CropCircleTransformation(this)).error(R.drawable.icon_default_avatar)
                .placeholder(R.drawable.icon_default_avatar).into(imgContentAvatar);
        Glide.with(this).load(Util.getImgUrl(issuesDetailBean.getUserimgurl())).bitmapTransform(new CropCircleTransformation(this)).into(imgAuthorAvatar);
        tvAuthorName.setText(issuesDetailBean.getUsername());
        tvAudioTitleDesc.setText(issuesDetailBean.getName());
        tvTitle.setText(issuesDetailBean.getName());
        tvDescTitle.setText(issuesDetailBean.getName());
//        tvDescContent.setText(issRuesDetailBean.getContent());
//        webView.loadData(fmtString(issuesDetailBean.getContent()), "text/html", "utf-8");
//        webView.loadDataWithBaseURL(null, issuesDetailBean.getContent(), "text/html", "utf-8", null);
        webView.loadUrl(SystemConstant.MYURL + "issues/content/" + getIntent().getStringExtra("id") + "/" + SpUtils.getStringSp(DemoApplication.applicationContext, "access_token"));
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setLoadWithOverviewMode(true);
        if (issuesDetailBean.getPermission() == 1) {
            maintitleTxt.setText(issuesDetailBean.getColumnname());
            tvSubscribe.setVisibility(View.GONE);
        } else {
            tvSubscribe.setText("订购: ¥" + issuesDetailBean.getPrice() + "/年");
            maintitleTxt.setText("试看");
            tvSubscribe.setVisibility(View.VISIBLE);
        }
        initAudio();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fl_audio:
                switch (AudioPlayManager.getInstance().CURRENT_STATE) {
                    case SystemConstant.ON_PREPARE:
                        //音频播放器在准备过程中
                        Toast.makeText(mContext, "正在加载", Toast.LENGTH_SHORT).show();
                        break;
                    case SystemConstant.ON_PAUSE:
                        //音频播放器暂停中，开始播放
                        AudioPlayManager.getInstance().ijkStart();
                        DemoApplication.isPlay = true;
                        break;
                    case SystemConstant.ON_PLAYING:
                        //暂停
                        AudioPlayManager.getInstance().pause();
                        DemoApplication.isPlay = false;
                        break;
                    default:
                        //音频播放器处于停止中，重新开始播放
                        AudioPlayManager.getInstance().ijkStart();
                        DemoApplication.isPlay = true;
                        break;
                }
                break;
            case R.id.ll_return:
                if (!DemoApplication.getInstance().activityExisted(MainActivity.class)) {
                    //如果mainActivity不存在则跳转主页面
                    startActivity(new Intent(this, MainActivity.class));
                }
                finish();
                break;
            case R.id.tv_subscribe:
                if (!Util.hintLogin(this))
                    return;
                showPayWindow();
                break;
            case R.id.img_download:
                if (issuesDetailBean.getPermission() != 1) {
                    Toast.makeText(DemoApplication.applicationContext, "请先购买", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (issuesDetailBean.getAudiourl() == null || TextUtils.isEmpty(issuesDetailBean.getAudiourl()) || "null".equals(issuesDetailBean.getAudiourl())) {
                    Toast.makeText(DemoApplication.applicationContext, "该文件有误，无法下载", Toast.LENGTH_SHORT).show();
                    break;
                }
                //查询该内容是否下载过
                IssuesDetailBean albumContent = DB.getInstance(DemoApplication.applicationContext).queryIssuesDownloadFile(issuesDetailBean.getId());
                String suffixes = issuesDetailBean.getAudiourl().substring(issuesDetailBean.getAudiourl().lastIndexOf("."));
                //未下载过,开始下载
                if (albumContent == null) {
                    Toast.makeText(DemoApplication.applicationContext, "开始下载", Toast.LENGTH_SHORT).show();
                    try {
                        issuesDetailBean.setFilePath(Util.getSDCardPath() + "/MOZDownloads/" + issuesDetailBean.getName() + suffixes);
                        DB.getInstance(DemoApplication.applicationContext).insertDownloadFile(issuesDetailBean);
                        DownloadManager.getInstance().startDownload(
                                SystemConstant.ACCESS_IMG_HOST + issuesDetailBean.getAudiourl()
                                , issuesDetailBean.getName()
                                , Util.getSDCardPath() + "/MOZDownloads/" + issuesDetailBean.getName() + suffixes
                                , true
                                , false
                                , null
                                , issuesDetailBean.getId(), 2, 0);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (albumContent.getDownloadState() == 0) {
                        Toast.makeText(DemoApplication.applicationContext, "下载中...", Toast.LENGTH_SHORT).show();
                        //代表该内容已下载完成
                    } else if (albumContent.getDownloadState() == 1) {
                        //判断本地中是否存在该文件
                        if (Util.fileExisted(albumContent.getFilePath())) {
                            Toast.makeText(DemoApplication.applicationContext, "该文件已下载", Toast.LENGTH_SHORT).show();
                        } else {
                            issuesDetailBean.setFilePath(Util.getSDCardPath() + "/MOZDownloads/" + issuesDetailBean.getName() + suffixes);
                            try {
                                DB.getInstance(DemoApplication.applicationContext).updataDownloadFileState(issuesDetailBean.getId(), 0, 2);
                                DownloadManager.getInstance().startDownload(
                                        SystemConstant.ACCESS_IMG_HOST + issuesDetailBean.getAudiourl()
                                        , issuesDetailBean.getName()
                                        , Util.getSDCardPath() + "/MOZDownloads/" + issuesDetailBean.getName() + suffixes, true, false, null, issuesDetailBean.getId(), 2, 0);
                            } catch (DbException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                break;
        }
    }

    private void showPayWindow() {
        if (payPopupWindow == null) {
            View view = View.inflate(this, R.layout.popupwindow_pay, null);
            tvOrderInfo = (TextView) view.findViewById(R.id.tv_order_info);
            view.findViewById(R.id.tv_pay_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Util.finishPop(PreviewColumnContentActivity.this, payPopupWindow);
                }
            });
            view.findViewById(R.id.tv_wx_pay).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //微信支付
                    Util.finishPop(PreviewColumnContentActivity.this, payPopupWindow);
                    wxPay();
                }
            });
            view.findViewById(R.id.tv_ali_pay).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //支付宝支付
                    Util.finishPop(PreviewColumnContentActivity.this, payPopupWindow);
                    aliPay();
                }
            });
            view.findViewById(R.id.tv_balance_pay).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Util.finishPop(PreviewColumnContentActivity.this, payPopupWindow);
                    showWaringDialog("支付", "是否使用" + issuesDetailBean.getPrice() + "墨子币？", new OnDialogButtonClickListener() {
                        @Override
                        public void onPositiveButtonClick() {
                            toHttpBalancePay();
                        }

                        @Override
                        public void onNegativeButtonClick() {

                        }
                    });
                }
            });
            payPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            payPopupWindow.setContentView(view);
            payPopupWindow.setAnimationStyle(R.style.AnimBottom);
            WindowManager.LayoutParams lp = PreviewColumnContentActivity.this.getWindow().getAttributes();
            lp.alpha = 0.6f;
            PreviewColumnContentActivity.this.getWindow().setAttributes(lp);
            payPopupWindow.setFocusable(true);
            payPopupWindow.setOutsideTouchable(true);
            payPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            payPopupWindow.showAtLocation(llAll, Gravity.BOTTOM, 0, 0);
            payPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    Util.finishPop(PreviewColumnContentActivity.this, payPopupWindow);
                }
            });
        } else {
            WindowManager.LayoutParams lp = PreviewColumnContentActivity.this.getWindow().getAttributes();
            lp.alpha = 0.6f;
            PreviewColumnContentActivity.this.getWindow().setAttributes(lp);
            payPopupWindow.showAtLocation(llAll, Gravity.BOTTOM, 0, 0);
        }
        tvOrderInfo.setText("您将支付金额¥" + issuesDetailBean.getPrice() + ",请再次确认购买");
    }

    private void toHttpBalancePay() {
        if (issuesDetailBean == null)
            return;
        Map<String, Object> map = new HashMap<>();
        map.put("goodid", issuesDetailBean.getColumnid());
        map.put("payment", 4);
        HttpUtil.post(DemoApplication.applicationContext, SystemConstant.URL + SystemConstant.BUY_COLUMN, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    Toast.makeText(DemoApplication.applicationContext, "购买成功", Toast.LENGTH_SHORT).show();
                    toHttpGetDetail();
                } else if (result.getCode() == 417) {
                    Toast.makeText(DemoApplication.applicationContext, "余额不足", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DemoApplication.applicationContext, "支付失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {

            }
        });
    }

    private void aliPay() {
        if (issuesDetailBean == null)
            return;
        Map<String, Object> map = new HashMap<>();
        map.put("goodid", issuesDetailBean.getColumnid());
        map.put("payment", 2);
        HttpUtil.post(DemoApplication.applicationContext, SystemConstant.URL + SystemConstant.BUY_COLUMN, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    final AliPay aliPay = (AliPay) GsonUtils.json2Bean(GsonUtils.obj2Json(result.getData()), AliPay.class);
                    Runnable payRunnable = new Runnable() {

                        @Override
                        public void run() {
                            PayTask alipay = new PayTask(PreviewColumnContentActivity.this);
                            Map<String, String> result = alipay.payV2(aliPay.getSignedParams(), true);
                            PayResult payResult = new PayResult(result);
                            if (TextUtils.equals(payResult.getResultStatus(), "9000")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(DemoApplication.applicationContext, "购买成功", Toast.LENGTH_SHORT).show();
                                        toHttpGetDetail();
                                    }
                                });
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "支付失败", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
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

    private void wxPay() {
        if (issuesDetailBean == null)
            return;
        Map<String, Object> map = new HashMap<>();
        map.put("goodid", issuesDetailBean.getColumnid());
        map.put("payment", 1);
        HttpUtil.post(DemoApplication.applicationContext, SystemConstant.URL + SystemConstant.BUY_COLUMN, map, new HttpUtil.HttpCallBack() {
            @Override
            public void successResult(ResultEntity result) throws JSONException {
                if (result.getCode() == 200) {
                    PayReq req = new PayReq();
                    WXPay wxPay = (WXPay) GsonUtils.json2Bean(GsonUtils.obj2Json(result.getData()), WXPay.class);
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
                    IWXAPI api = WXAPIFactory.createWXAPI(PreviewColumnContentActivity.this, req.appId);
                    api.registerApp(req.appId);
                    api.sendReq(req);
                }
            }

            @Override
            public void errorResult(Throwable ex, boolean isOnCallback) {

            }
        });
    }

    @Subscribe
    public void onEvent(EventMsg msg) {
        switch (msg.getWhat()) {
            case SystemConstant.ON_PREPARE:
                //正在准备
                break;
            case SystemConstant.ON_PLAYING:
                Log.e("state", "ON_PLAYING");
                //开始播放
                imgMediaState.setImageResource(R.drawable.audio_click_stop);
                seekBar.setMax((int) AudioPlayManager.getInstance().getIntDuration());
                tvTotalTime.setText(TimeUtils.getAnswerLength(AudioPlayManager.getInstance().getIntDuration()));
                break;
            case SystemConstant.ON_BUFFERING:
                //缓冲区变化
                seekBar.setMax(msg.getArg1());
                break;
            case SystemConstant.ON_STOP:
                Log.e("state", "ON_STOP");
                //停止播放
                imgMediaState.setImageResource(R.drawable.audio_click_play);
                break;
            case SystemConstant.ON_PAUSE:
                Log.e("state", "ON_PAUSE");
                //暂停播放
                imgMediaState.setImageResource(R.drawable.audio_click_play);
                break;
            case SystemConstant.AUDIO_COMPLET:
                AudioPlayManager.getInstance().replay();
                seekBar.setProgress(0);
                tvCurrTime.setText("00:00");
                imgMediaState.setImageResource(R.drawable.audio_click_play);
                break;
            case SystemConstant.ON_USER_INFORMATION:
                toHttpGetDetail();
                break;
            case SystemConstant.ON_WEIXIN_PAY_FINISH:
                if (!(DemoApplication.getInstance().currentActivity() instanceof ColumnDetailsActivity))
                    return;
                int result = msg.getArg1();
                if (result == -100) {
                    Toast.makeText(mContext, "支付失败", Toast.LENGTH_SHORT).show();
                } else if (result == 0) {
                    Toast.makeText(mContext, "支付成功", Toast.LENGTH_SHORT).show();
                    Toast.makeText(DemoApplication.applicationContext, "购买成功", Toast.LENGTH_SHORT).show();
                    toHttpGetDetail();
                } else if (result == -2) {
                    Toast.makeText(mContext, "支付取消", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "支付失败", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!DemoApplication.getInstance().activityExisted(MainActivity.class)) {
                //如果mainActivity不存在则跳转主页面
                AudioPlayManager.getInstance().release();
                startActivity(new Intent(this, MainActivity.class));
                finish();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AudioPlayManager.getInstance().release();
        EventBus.getDefault().unregister(this);
        mTimer.cancel();
        mTimerTask.cancel();
        mTimer = null;
        mTimerTask = null;
    }
}
